package com.example.rewards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class YourProfileActivity extends AppCompatActivity {

    public TextView full_name;
    public TextView location;
    public TextView user_name;
    public TextView points_awarded;
    public TextView department;
    public TextView position;
    public TextView points_to_award;
    public TextView story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_login);

        full_name = findViewById(R.id.full_name);
        location = findViewById(R.id.location);
        user_name = findViewById(R.id.user_num);
        points_awarded = findViewById(R.id.points_awarded);
        department = findViewById(R.id.department);
        position = findViewById(R.id.position);
        points_to_award = findViewById(R.id.points_to);

        Intent intent = getIntent();
        String data = intent.getStringExtra("key");
        try {
            JSONObject json_obj = new JSONObject(data);
            full_name.setText(json_obj.getString("lastName")+ ", " +json_obj.getString("firstName") );
            location.setText(json_obj.getString("location"));
            user_name.setText(json_obj.getString("username"));
            points_awarded.setText(json_obj.getString("location"));
            department.setText(json_obj.getString("department"));
            position.setText(json_obj.getString("position"));
            //points_to_award.setText(json_obj.getInt("pointsToAward"));

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.example.rewards;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AwardActivity extends AppCompatActivity {

    public TextView full_name;
    public TextView department;
    public TextView position;
    public TextView story;
    public TextView points_awarded;
    public EditText comments;
    public EditText points_to_send;
    private ImageView imageView;
    public TextView comment_view;
    String imgString;
    Intent intent;
    public static int MAX_CHARS = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.award_activity);

        full_name = findViewById(R.id.full_name);
        department = findViewById(R.id.department);
        position = findViewById(R.id.position);
        story = findViewById(R.id.story);
        points_awarded = findViewById(R.id.points_awarded);
        comments = findViewById(R.id.comments);
        points_to_send = findViewById(R.id.points_to_send);
        imageView = findViewById(R.id.profile_pic);
        comment_view = findViewById(R.id.comment_view);

        comments.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CHARS)});
        addTextListener();

        intent = getIntent();
        full_name.setText(intent.getStringExtra("lastname") +", "+ intent.getStringExtra("firstname"));
        department.setText(intent.getStringExtra("department"));
        position.setText(intent.getStringExtra("position"));
        story.setText(intent.getStringExtra("story"));
        points_awarded.setText(intent.getStringExtra("pointsawarded"));
        imgString = intent.getStringExtra("imagestr");

        byte[] imageBytes = Base64.decode(imgString,  Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                savePoints();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addTextListener() {
        comments.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing to do here
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // Nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                int len = s.toString().length();
                String countText = "Comment: (" + len + " of " + MAX_CHARS + ")";
                comment_view.setText(countText);
            }
        });
    }

    public void savePoints(){
        final int points = Integer.parseInt(points_awarded.getText().toString());
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.save_changes, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Changes?");
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendPoints(points);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void sendPoints(Integer points){
        JSONObject target = new JSONObject();
        JSONObject source = new JSONObject();
        JSONObject json_to_send = new JSONObject();
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(currentDate);

        try{
            target.put("studentId", "A20349053");
            target.put("username", intent.getStringExtra("username"));
            target.put("name", intent.getStringExtra("firstname")+ " " + intent.getStringExtra("lastname"));
            target.put("date", "04/12/2019");
            target.put("notes", comments.getText().toString());
            target.put("value", Integer.parseInt(points_to_send.getText().toString()));

            json_to_send.put("target", target);

            source.put("studentId", "A20349053");
            source.put("username", intent.getStringExtra("sourceName"));
            source.put("password", intent.getStringExtra("sourcePsw"));

            json_to_send.put("source", source);

            new SendPointsAsync(this).execute(json_to_send);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendResults(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LeadboardActivity.class);
        startActivity(intent);
    }
}

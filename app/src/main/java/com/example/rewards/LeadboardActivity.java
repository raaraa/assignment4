package com.example.rewards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeadboardActivity extends AppCompatActivity {

    private RecyclerView recycler_view;
    private LeaderBoardAdapter leader_adapter;
    private List<User> user_list = new ArrayList<>();
    private String all_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_board);

        Intent intent = getIntent();
        String user_name = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        recycler_view = findViewById(R.id.recycler);
        leader_adapter = new LeaderBoardAdapter(this, user_list);
        recycler_view.setAdapter(leader_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        leader_adapter.notifyDataSetChanged();

        new AllProfilesAsync(this).execute("A20349053",user_name,password);
    }

    public void sendResults(String data) {
        //Toast.makeText(this, data, Toast.LENGTH_LONG).show();
        try{
            JSONArray json_arr = new JSONArray(data);
            for(int i=0; i<json_arr.length(); i++){
                JSONObject json_obj = json_arr.getJSONObject(i);
                User user = new User(json_obj.get("firstName").toString(),json_obj.get("lastName").toString(),
                                    json_obj.get("department").toString(),json_obj.get("position").toString());
                user_list.add(user);
                leader_adapter.notifyDataSetChanged();
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

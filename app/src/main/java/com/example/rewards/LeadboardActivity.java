package com.example.rewards;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeadboardActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recycler_view;
    private LeaderBoardAdapter leader_adapter;
    private List<User> user_list = new ArrayList<>();
    public String user_name;
    public String password;
    public String source_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_board);

        getSupportActionBar().setTitle("  Inspiration Leaderboard");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.arrow_with_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        user_name = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        source_name = intent.getStringExtra("name");

        recycler_view = findViewById(R.id.recycler);
        leader_adapter = new LeaderBoardAdapter(this, user_list);
        recycler_view.setAdapter(leader_adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        leader_adapter.notifyDataSetChanged();

        new AllProfilesAsync(this).execute("A20349053",user_name,password);
    }

    @Override
    public void onClick(View v) {
        int pos = recycler_view.getChildAdapterPosition(v);
        User user = user_list.get(pos);
        if(!user.getUser_name().equals(user_name)) {
            Intent intent = new Intent(this, AwardActivity.class);
            intent.putExtra("firstname", user.getFirst_name());
            intent.putExtra("lastname", user.getLast_name());
            intent.putExtra("department", user.getDeparment());
            intent.putExtra("position", user.getPosition());
            intent.putExtra("story", user.getStory());
            intent.putExtra("pointsawarded", user.getRewards_sent().toString());
            intent.putExtra("username", user.getUser_name());
            intent.putExtra("sourceUserName", user_name);
            intent.putExtra("sourcePsw", password);
            intent.putExtra("sourceName", source_name);
            intent.putExtra("imagestr", user.getImagestr());
            startActivity(intent);
        }
    }

    public void sendResults(String data) {
        //Toast.makeText(this, data, Toast.LENGTH_LONG).show();
        try{
            JSONArray json_arr = new JSONArray(data);
            Integer rewards ;
            for(int i=0; i<json_arr.length(); i++){
                rewards = 0;
                JSONObject json_obj = json_arr.getJSONObject(i);
                if(!json_obj.get("rewards").equals(null)){
                    for(int j=0; j <json_obj.getJSONArray("rewards").length(); j++){
                        JSONObject obj = (JSONObject) json_obj.getJSONArray("rewards").get(j);
                        rewards += (int) obj.get("value");
                    }
                }
                String imgString = json_obj.getString("imageBytes");
                byte[] imageBytes = Base64.decode(imgString,  Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                User user = new User(json_obj.get("firstName").toString(),json_obj.get("lastName").toString(),
                                    json_obj.get("department").toString(),json_obj.get("position").toString(),
                                    json_obj.get("story").toString(),json_obj.get("username").toString(),rewards,
                                    bitmap, imgString);
                user_list.add(user);
                Collections.sort(user_list, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return o2.getRewards_sent().compareTo(o1.getRewards_sent());
                    }
                });
                leader_adapter.notifyDataSetChanged();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

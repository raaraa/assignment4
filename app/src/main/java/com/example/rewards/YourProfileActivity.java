package com.example.rewards;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class YourProfileActivity extends AppCompatActivity {

    public TextView full_name;
    public TextView location;
    public TextView user_name;
    public TextView points_awarded;
    public TextView department;
    public TextView position;
    public TextView points_to_award;
    public TextView story;
    public String password;
    public String firstname;
    public String lastname;
    private ImageView imageView;
    public TextView reward_view;
    String imgString;

    public JSONArray json_reward = new JSONArray();

    private RecyclerView recycler_view;
    private ArrayList<Reward> rewardArrayList = new ArrayList<>();
    private RewardAdapter rAdapter;

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
        story = findViewById(R.id.story);
        imageView = findViewById(R.id.profile_pic);
        reward_view = findViewById(R.id.reward_view);

        Intent intent = getIntent();
        String data = intent.getStringExtra("key");
        try {
            JSONObject json_obj = new JSONObject(data);
            firstname =json_obj.getString("firstName");
            lastname = json_obj.getString("lastName");
            full_name.setText(lastname+ ", " +firstname);
            location.setText(json_obj.getString("location"));
            user_name.setText(json_obj.getString("username"));
            points_awarded.setText(json_obj.getString("location"));
            department.setText(json_obj.getString("department"));
            position.setText(json_obj.getString("position"));
            story.setText(json_obj.getString("story"));
            points_to_award.setText(json_obj.get("pointsToAward").toString());
            imgString = json_obj.getString("imageBytes");

            password = json_obj.getString("password");

            byte[] imageBytes = Base64.decode(imgString,  Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bitmap);

            json_reward = json_obj.getJSONArray("rewards");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        parseRewards(json_reward);

        recycler_view = findViewById(R.id.reward_history);
        rAdapter = new RewardAdapter();
        recycler_view.setAdapter(rAdapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        int points = 0;
        for(int i=0; i<rewardArrayList.size(); i++){
            points += Integer.parseInt(rewardArrayList.get(i).getValue());
        }
        points_awarded.setText(Integer.toString(points));
        reward_view.setText("Reward History: "+ "("+Integer.toString(rewardArrayList.size())+")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.leader_board:
                Intent intent = new Intent(this, LeadboardActivity.class);
                intent.putExtra("username", user_name.getText());
                intent.putExtra("password", password);
                intent.putExtra("sourceName", firstname + " " + lastname);
                startActivity(intent);
                return true;
            case R.id.edit_profile:
                Intent new_intent = new Intent(this, EditProfileActivity.class);
                new_intent.putExtra("username", user_name.getText());
                new_intent.putExtra("password", password);
                new_intent.putExtra("firstname", firstname);
                new_intent.putExtra("lastname", lastname);
                new_intent.putExtra("department", department.getText());
                new_intent.putExtra("position", position.getText());
                new_intent.putExtra("story", story.getText());
                new_intent.putExtra("image", imgString);
                startActivity(new_intent);
                //
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void parseRewards(JSONArray jsonArray){
        for(int i=0; i <jsonArray.length(); i++){
            try {
                JSONObject reward = jsonArray.getJSONObject(i);
                rewardArrayList.add(new Reward(reward.getString("date"),reward.getString("name"),
                                                reward.getString("notes"),reward.getString("value")));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //////////////////////////////////////////


    class RewardViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView name;
        TextView note;
        TextView value;

        RewardViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            name = view.findViewById(R.id.name);
            note = view.findViewById(R.id.note);
            value = view.findViewById(R.id.value);
        }

    }

//////////////////////////////////////////

    public class RewardAdapter extends RecyclerView.Adapter<RewardViewHolder> {

        @NonNull
        @Override
        public RewardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reward_entry, parent, false);

            //itemView.setOnClickListener(MainActivity.this);

            return new RewardViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
            Reward thisReward = rewardArrayList.get(position);

            holder.date.setText(thisReward.getDate());
            holder.name.setText(thisReward.getName());
            holder.note.setText(thisReward.getNote());
            holder.value.setText(thisReward.getValue());
        }

        @Override
        public int getItemCount() {
            return rewardArrayList.size();
        }
    }
}



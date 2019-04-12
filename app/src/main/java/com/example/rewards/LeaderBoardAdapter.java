package com.example.rewards;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardHolder> {
    private LeadboardActivity mainActivity;
    private List<User> user_list;

    public LeaderBoardAdapter(LeadboardActivity mainActivity, List<User> user_list) {
        this.mainActivity = mainActivity;
        this.user_list = user_list;
    }

    @Override
    public LeaderBoardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_board_row, parent, false);
        //view.setOnLongClickListener(mainActivity);
        view.setOnClickListener(mainActivity);
        return new LeaderBoardHolder(view);
    }

    @Override
    public void onBindViewHolder(LeaderBoardHolder holder, int position) {
        User user = user_list.get(position);
        holder.full_name.setText(user.getLast_name()+ ", "+ user.getFirst_name());
        holder.department_and_title.setText(user.getDeparment() + ", " + user.getPosition());
        holder.points.setText(user.getRewards_sent().toString());
    }

    @Override
    public int getItemCount() {
        return user_list.size();
    }
}

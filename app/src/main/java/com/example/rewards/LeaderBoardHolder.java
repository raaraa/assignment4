package com.example.rewards;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LeaderBoardHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public TextView full_name;
    public TextView department_and_title;

    public LeaderBoardHolder(View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.photo);
        full_name = itemView.findViewById(R.id.full_name);
        department_and_title = itemView.findViewById(R.id.department_and_title);
    }
}

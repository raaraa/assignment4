package com.example.rewards;

import android.content.Intent;

public class User {

    private String user_name;
    private String first_name;
    private String last_name;
    private String deparment;
    private String position;
    private String story;
    private Integer rewards_sent;

    public User(String first_name, String last_name, String department, String position, String story, String user_name,Integer rewards_sent) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.deparment = department;
        this.position = position;
        this.story = story;
        this.user_name = user_name;
        this.rewards_sent = rewards_sent;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getDeparment() { return deparment; }

    public String getPosition() { return position; }

    public String getStory() { return story; }

    public Integer getRewards_sent(){ return rewards_sent; }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setDeparment(String deparment) {
        this.deparment = deparment;
    }

    public void setStory(String story){ this.story = story;
    }
}

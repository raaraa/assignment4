package com.example.rewards;

public class User {

    private String user_name;
    private String first_name;
    private String last_name;
    private String deparment;
    private String position;

    public User(String first_name, String last_name, String department, String position) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.deparment = department;
        this.position = position;
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
}

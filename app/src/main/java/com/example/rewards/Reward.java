package com.example.rewards;

public class Reward {

    private String date;
    private String name;
    private String note;
    private String value;

    public Reward(String date, String name, String note, String value){
        this.date = date;
        this.name = name;
        this.note = note;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public String getValue() {
        return value;
    }
}

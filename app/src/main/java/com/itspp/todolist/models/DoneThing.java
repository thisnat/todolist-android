package com.itspp.todolist.models;

public class DoneThing {

    private int id,doneInTime;
    private String title,doneDate,doneTime;

    public DoneThing(int id, int doneInTime, String title, String doneDate, String doneTime) {
        this.id = id;
        this.doneInTime = doneInTime;
        this.title = title;
        this.doneDate = doneDate;
        this.doneTime = doneTime;
    }

    public int getId() {
        return id;
    }

    public int getDoneInTime() {
        return doneInTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public String getDoneTime() {
        return doneTime;
    }
}

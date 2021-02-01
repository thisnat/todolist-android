package com.itspp.todolist.models;

public class Thing {

    private int id;
    private double rating;
    private String title,desc,dueDate,dueDateTime,bullet;

    public Thing(int id, String title, String desc, String dueDate, String dueDateTime, String bullet, double rating) {
        this.id = id;
        this.rating = rating;
        this.title = title;
        this.desc = desc;
        this.dueDate = dueDate;
        this.dueDateTime = dueDateTime;
        this.bullet = bullet;
    }

    public int getId() {
        return id;
    }

    public double getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDueDateTime() {
        return dueDateTime;
    }

    public String getBullet() {
        return bullet;
    }

    @Override
    public String toString() {
        return "Thing{" +
                "id=" + id +
                ", rating=" + rating +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", dueDateTime='" + dueDateTime + '\'' +
                ", bullet='" + bullet + '\'' +
                '}';
    }
}

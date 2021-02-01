package com.itspp.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.itspp.todolist.models.DoneThing;
import com.itspp.todolist.models.Thing;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, "data.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE 'things' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,'title' TEXT,'desc' TEXT,'dueDate' TEXT,'dueDateTime' TEXT,'bullet' TEXT,'rating' DOUBLE)");
        sqLiteDatabase.execSQL("CREATE TABLE 'doneThings' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,'doneInTime' INT,'title' TEXT,'doneDate' TEXT,'doneTime' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addThing(String title,String desc,String dueDate,String dueDateTime,String bullet,Double rating){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("title",title);
        row.put("desc",desc);
        row.put("dueDate",dueDate);
        row.put("dueDateTime",dueDateTime);
        row.put("bullet",bullet);
        row.put("rating",rating);
        db.insert("things",null,row);
        db.close();
    }

    public ArrayList<Thing> getThings(){
        ArrayList<Thing> things = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int id;
        double rating;
        String title,desc,dueDate,dueDateTime,bullet;

        Cursor cs = db.rawQuery("SELECT * FROM things",null);
        while(cs.moveToNext()){
            id = cs.getInt(cs.getColumnIndex("id"));
            title = cs.getString(cs.getColumnIndex("title"));
            desc = cs.getString(cs.getColumnIndex("desc"));
            dueDate = cs.getString(cs.getColumnIndex("dueDate"));
            dueDateTime = cs.getString(cs.getColumnIndex("dueDateTime"));
            bullet = cs.getString(cs.getColumnIndex("bullet"));
            rating = cs.getDouble(cs.getColumnIndex("rating"));
            things.add(new Thing(id,title,desc,dueDate,dueDateTime,bullet,rating));
        }
        return things;
    }

    public ArrayList<Thing> getThingsByRating(){
        ArrayList<Thing> things = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int id;
        double rating;
        String title,desc,dueDate,dueDateTime,bullet;

        Cursor cs = db.rawQuery("SELECT * FROM things ORDER BY rating DESC",null);
        while(cs.moveToNext()){
            id = cs.getInt(cs.getColumnIndex("id"));
            title = cs.getString(cs.getColumnIndex("title"));
            desc = cs.getString(cs.getColumnIndex("desc"));
            dueDate = cs.getString(cs.getColumnIndex("dueDate"));
            dueDateTime = cs.getString(cs.getColumnIndex("dueDateTime"));
            bullet = cs.getString(cs.getColumnIndex("bullet"));
            rating = cs.getDouble(cs.getColumnIndex("rating"));
            things.add(new Thing(id,title,desc,dueDate,dueDateTime,bullet,rating));
        }
        return things;
    }

    public void addDoneThing(int doneInTime, String title, String doneDate, String doneTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("doneInTime",doneInTime);
        row.put("title",title);
        row.put("doneDate",doneDate);
        row.put("doneTime",doneTime);
        db.insert("doneThings",null,row);
        db.close();
    }

    public ArrayList<DoneThing> getDoneThings(){
        ArrayList<DoneThing> doneThings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int id,doneInTime;
        String title,doneDate,doneTime;

        Cursor cs = db.rawQuery("SELECT * FROM doneThings",null);
        while(cs.moveToNext()){
            id = cs.getInt(cs.getColumnIndex("id"));
            doneInTime = cs.getInt(cs.getColumnIndex("doneInTime"));
            title = cs.getString(cs.getColumnIndex("title"));
            doneDate = cs.getString(cs.getColumnIndex("doneDate"));
            doneTime = cs.getString(cs.getColumnIndex("doneTime"));
            doneThings.add(new DoneThing(id,doneInTime,title,doneDate,doneTime));
        }
        return doneThings;
    }
}

package com.itspp.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itspp.todolist.models.DoneThing;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SummaryActivity extends AppCompatActivity {

    int doneThingCount = 0;
    int thingCount;
    ArrayList<DoneThing> doneThingArrayList;
    TextView allText,inTimeText,doingText,recent1,recent2,recent3,weekPercent,monthPercent,allPercent;
    ProgressBar progressBarW,progressBarM,progressBarA;
    DecimalFormat df = new DecimalFormat(".0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent intent = getIntent();
        thingCount = intent.getIntExtra("things",0);

        doneThingArrayList = new ArrayList<>();
        this.allText = findViewById(R.id.allText);
        this.inTimeText = findViewById(R.id.inTimeText);
        this.doingText = findViewById(R.id.doingText);
        this.recent1 = findViewById(R.id.recent1);
        this.recent2 = findViewById(R.id.recent2);
        this.recent3 = findViewById(R.id.recent3);
        this.progressBarW = findViewById(R.id.progressBarW);
        this.progressBarM = findViewById(R.id.progressBarM);
        this.progressBarA = findViewById(R.id.progressBarA);
        this.weekPercent = findViewById(R.id.weekPercent);
        this.monthPercent = findViewById(R.id.monthPercent);
        this.allPercent = findViewById(R.id.allPercent);

        //database
        DbHelper dbHelper = new DbHelper(this);
        doneThingArrayList = dbHelper.getDoneThings();

        statsTextSet();
        recentTextSet();
        if(doneThingArrayList.size() != 0){
            try {
                progressBarSet();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void statsTextSet(){
        for(int i = 0;i < doneThingArrayList.size();i++){
            if(doneThingArrayList.get(i).getDoneInTime() == 1){
                doneThingCount++;
            }
        }
        allText.setText(doneThingArrayList.size()+"");
        inTimeText.setText(doneThingCount+"");
        doingText.setText(thingCount+"");
    }

    public void recentTextSet(){
        if(doneThingArrayList.size() == 0){
            //do nothing
        }
        else if(doneThingArrayList.size() == 1){
            DoneThing item1 = doneThingArrayList.get(doneThingArrayList.size()-1);
            recent1.setText(item1.getDoneDate()+" - "+item1.getDoneTime()+"  "+item1.getTitle());
        }
        else if(doneThingArrayList.size() == 2){
            DoneThing item1 = doneThingArrayList.get(doneThingArrayList.size()-1);
            DoneThing item2 = doneThingArrayList.get(doneThingArrayList.size()-2);
            recent1.setText(item1.getDoneDate()+" - "+item1.getDoneTime()+"  "+item1.getTitle());
            recent2.setText(item2.getDoneDate()+" - "+item2.getDoneTime()+"  "+item2.getTitle());
        }
        else if(doneThingArrayList.size() >= 3){
            DoneThing item1 = doneThingArrayList.get(doneThingArrayList.size()-1);
            DoneThing item2 = doneThingArrayList.get(doneThingArrayList.size()-2);
            DoneThing item3 = doneThingArrayList.get(doneThingArrayList.size()-3);
            recent1.setText(item1.getDoneDate()+" - "+item1.getDoneTime()+"  "+item1.getTitle());
            recent2.setText(item2.getDoneDate()+" - "+item2.getDoneTime()+"  "+item2.getTitle());
            recent3.setText(item3.getDoneDate()+" - "+item3.getDoneTime()+"  "+item3.getTitle());
        }
    }

    public void progressBarSet() throws ParseException {
        float all,month,week;
        float totalMonth = 0;
        float doneInMonth = 0;
        float totalWeek = 0;
        float doneInWeek = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar current = Calendar.getInstance();

        //all-time
        all = ((float)doneThingCount / (float)doneThingArrayList.size())*100;
        allPercent.setText(df.format(all)+"%");
        progressBarA.setProgress((int)all);

        //monthly
        for(int i = 0;i < doneThingArrayList.size();i++){
            String strdate = doneThingArrayList.get(i).getDoneDate()+" "+doneThingArrayList.get(i).getDoneTime();
            Date myDate = simpleDateFormat.parse(strdate);
            Calendar myCalendar = Calendar.getInstance();
            myCalendar.setTime(myDate);

            if(myCalendar.get(Calendar.MONTH) == current.get(Calendar.MONTH) && myCalendar.get(Calendar.YEAR) == current.get(Calendar.YEAR)){
                if(doneThingArrayList.get(i).getDoneInTime() == 1){
                    doneInMonth++;
                }
                totalMonth++;
            }
        }
        month = (doneInMonth / totalMonth)*100;
        if(Float.isNaN(month)){
            monthPercent.setText("ไม่มีรายการ");
            progressBarM.setProgress(100);
        }
        else{
            monthPercent.setText(df.format(month)+"%");
            progressBarM.setProgress((int) month);
        }

        //weekly
        for(int i = 0;i < doneThingArrayList.size();i++){
            String strdate = doneThingArrayList.get(i).getDoneDate()+" "+doneThingArrayList.get(i).getDoneTime();
            Date myDate = simpleDateFormat.parse(strdate);
            Calendar myCalendar = Calendar.getInstance();
            myCalendar.setTime(myDate);

            if(myCalendar.get(Calendar.WEEK_OF_YEAR) == current.get(Calendar.WEEK_OF_YEAR) && myCalendar.get(Calendar.YEAR) == current.get(Calendar.YEAR)){
                if(doneThingArrayList.get(i).getDoneInTime() == 1){
                    doneInWeek++;
                }
                totalWeek++;
            }
        }
        week = (doneInWeek / totalWeek)*100;
        if(Float.isNaN(week)){
            weekPercent.setText("ไม่มีรายการ");
            progressBarW.setProgress(100);
        }
        else{
            weekPercent.setText(df.format(week)+"%");
            progressBarW.setProgress((int)week);
        }
    }
}
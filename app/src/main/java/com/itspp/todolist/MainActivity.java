package com.itspp.todolist;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itspp.todolist.adapters.MainAdapter;
import com.itspp.todolist.models.Thing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView nothingToDo;
    ArrayList<Thing> thingArrayList;
    int things;
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thingArrayList = new ArrayList<>();
        //layout
        this.recyclerView = findViewById(R.id.recyclerView);
        this.toolbar = findViewById(R.id.toolbar);
        this.nothingToDo = findViewById(R.id.emptyText);

        //database
        DbHelper dbHelper = new DbHelper(this);
        thingArrayList = dbHelper.getThings();

        //toolbar
        Date date = new Date();
        CharSequence s  = DateFormat.format("MMMM dd, yyyy", date.getTime());
        things = thingArrayList.size();
        toolbar.setTitle(getTitleStatus(things));
        toolbar.setSubtitle(s);
        setSupportActionBar(toolbar);

        //nothing-to-do
        if(thingArrayList.size() == 0){
            nothingToDo.setVisibility(View.VISIBLE);
        }
        else {
            nothingToDo.setVisibility(View.INVISIBLE);
        }

        //view-setup
        mainAdapter = new MainAdapter(this,thingArrayList);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    public void onClickAdd(MenuItem menuItem){
        Intent intent = new Intent(this,AddActivity.class);
        startActivity(intent);
    }

    public void onClickSummary(MenuItem item) {
        Intent intent = new Intent(this,SummaryActivity.class);
        intent.putExtra("things",thingArrayList.size());
        startActivity(intent);
    }

    public void onClickStatClear(MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("กรุณายืนยัน");
        builder.setPositiveButton("ยืนยัน", (dialogInterface, i) -> {
            DbHelper dbHelper = new DbHelper(MainActivity.this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            db.execSQL("delete from doneThings");
            db.close();
            Toast.makeText(MainActivity.this,"ล้างสถิติแล้ว",Toast.LENGTH_LONG).show();
        });
        builder.setNegativeButton("ยกเลิก", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }

    public void onClickSortByRating(MenuItem item){
        DbHelper dbHelper = new DbHelper(this);
        thingArrayList.clear();
        thingArrayList.addAll(dbHelper.getThingsByRating());
        mainAdapter.notifyDataSetChanged();
    }

    public void onClickSortByNewest(MenuItem item){
        DbHelper dbHelper = new DbHelper(this);
        thingArrayList.clear();
        thingArrayList.addAll(dbHelper.getThings());
        Collections.reverse(thingArrayList);
        mainAdapter.notifyDataSetChanged();
    }

    public void onClickSortByOldest(MenuItem item){
        DbHelper dbHelper = new DbHelper(this);
        thingArrayList.clear();
        thingArrayList.addAll(dbHelper.getThings());
        mainAdapter.notifyDataSetChanged();
    }

    public void onClickAbout(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("ผู้จัดทำ");
        builder.setMessage("ณัฐวุติ อารยะกิตติพงศ์ \nรหัสนิสิต 6110450111");
        builder.setPositiveButton("ok", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }

    public String getTitleStatus(int things){
        if(things == 0){
            return "\uD83D\uDE2A nothing to do";
        }
        else if(things == 1){
            return "\uD83D\uDE00 1 thing to do!";
        }
        else if(things > 8 && things <= 16){
            return "\uD83D\uDE11 "+things+" things to do!";
        }
        else if(things > 16){
            return "\uD83D\uDD25 "+things+" things to do!";
        }
        else{
            //2-8 things
            return "\uD83D\uDE00 "+things+" things to do!";
        }
    }
}
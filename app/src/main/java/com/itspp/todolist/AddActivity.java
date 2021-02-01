package com.itspp.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.itspp.todolist.models.Thing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddActivity extends AppCompatActivity {

    Button dateButton,timeButton,bButton1,bButton2,bButton3,bButton4;
    Toolbar toolbar;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    RatingBar ratingBar;
    EditText edtTitle,edtDesc;

    String bulletSelected = "false";
    String sDate = null;
    String sTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        this.toolbar = findViewById(R.id.toolbar2);
        this.dateButton = findViewById(R.id.pickDateButton);
        this.timeButton = findViewById(R.id.pickTimeButton);
        this.bButton1 = findViewById(R.id.bButton1);
        this.bButton2 = findViewById(R.id.bButton2);
        this.bButton3 = findViewById(R.id.bButton3);
        this.bButton4 = findViewById(R.id.bButton4);
        this.ratingBar = findViewById(R.id.ratingBar);
        this.edtTitle = findViewById(R.id.edtTitleAdd);
        this.edtDesc = findViewById(R.id.edtDescAdd);

        toolbar.setTitle("เพิ่มรายการ");
        setSupportActionBar(toolbar);
        setBulletButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add,menu);
        return true;
    }

    public void save(MenuItem menuItem) throws ParseException {
        DbHelper dbHelper = new DbHelper(this);

        if(!saveCheck()){
            dbHelper.addThing(edtTitle.getText().toString(),edtDesc.getText().toString(),sDate,sTime,bulletSelected, (double) ratingBar.getRating());
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public boolean saveCheck() throws ParseException {
        boolean error = false;
        if(edtTitle.length() == 0 || edtDesc.length() == 0 || sDate == null || sTime == null){
            Toast.makeText(this, "กรุณาใส่ข้อมูลให้ครบ", Toast.LENGTH_LONG).show();
            error = true;
        }
        else {
            Date currentDate = new Date();
            String myDateStr = sDate+" "+sTime;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MM/yyyy HH:mm");
            Date myDate = simpleDateFormat.parse(myDateStr);
            if(currentDate.after(myDate)){
                Toast.makeText(this, "กรุณาใส่เวลาให้ถูกต้อง", Toast.LENGTH_LONG).show();
                error = true;
            }
        }
        return error;
    }

    public void datePick(View view){
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(AddActivity.this, (datePicker, i, i1, i2) -> {
            calendar.set(i,i1,i2);

            CharSequence s  = DateFormat.format("d/MM/yyyy", calendar.getTime());
            sDate = s.toString();
            CharSequence p  = DateFormat.format("d MMMM yyyy", calendar.getTime());
            dateButton.setText(p);
        },year ,month,day);
        datePickerDialog.show();
    }

    public void timePick(View view){
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(AddActivity.this, (timePicker, i, i1) -> {
            calendar.set(Calendar.HOUR_OF_DAY,i);
            calendar.set(Calendar.MINUTE,i1);

            CharSequence s  = DateFormat.format("HH:mm", calendar.getTime());
            sTime = s.toString();
            CharSequence p  = DateFormat.format("HH:mm", calendar.getTime())+" น.";
            timeButton.setText(p);
        },hour,minute,true);
        timePickerDialog.show();
    }

    public void setBulletButton(){
        bButton1.setBackgroundColor(0xFFcfd8dc);
        bButton2.setBackgroundColor(0xFFcfd8dc);
        bButton3.setBackgroundColor(0xFFcfd8dc);
        bButton4.setBackgroundColor(0xFF2196F3);

        bButton1.setOnClickListener(view -> {
            bButton1.setBackgroundColor(0xFF2196F3);
            bButton2.setBackgroundColor(0xFFcfd8dc);
            bButton3.setBackgroundColor(0xFFcfd8dc);
            bButton4.setBackgroundColor(0xFFcfd8dc);
            bulletSelected = "•";
        });

        bButton2.setOnClickListener(view -> {
            bButton1.setBackgroundColor(0xFFcfd8dc);
            bButton2.setBackgroundColor(0xFF2196F3);
            bButton3.setBackgroundColor(0xFFcfd8dc);
            bButton4.setBackgroundColor(0xFFcfd8dc);
            bulletSelected = "○";
        });

        bButton3.setOnClickListener(view -> {
            bButton1.setBackgroundColor(0xFFcfd8dc);
            bButton2.setBackgroundColor(0xFFcfd8dc);
            bButton3.setBackgroundColor(0xFF2196F3);
            bButton4.setBackgroundColor(0xFFcfd8dc);
            bulletSelected = "-";
        });

        bButton4.setOnClickListener(view -> {
            bButton1.setBackgroundColor(0xFFcfd8dc);
            bButton2.setBackgroundColor(0xFFcfd8dc);
            bButton3.setBackgroundColor(0xFFcfd8dc);
            bButton4.setBackgroundColor(0xFF2196F3);
            bulletSelected = "false";
        });
    }
}
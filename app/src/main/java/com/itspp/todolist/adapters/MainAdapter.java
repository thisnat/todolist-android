package com.itspp.todolist.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.itspp.todolist.DbHelper;
import com.itspp.todolist.MainActivity;
import com.itspp.todolist.R;
import com.itspp.todolist.models.Thing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder>{

    Context context;
    ArrayList<Thing> arrayList;

    public MainAdapter(Context context, ArrayList<Thing> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_main,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //bullet-check
        if(arrayList.get(position).getBullet().equals("false")){
            holder.titleText.setText(arrayList.get(position).getTitle());
        }
        else {
            holder.titleText.setText(arrayList.get(position).getBullet()+" "+arrayList.get(position).getTitle());
        }
        //rating-check
        if(arrayList.get(position).getRating() >= 4){
            holder.statusText.setText("\u2b50");
        }
        //on-fire-check
        String strDate = arrayList.get(position).getDueDate();
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        if(strDate.equals(currentDate)){
            holder.statusText.setText("\uD83D\uDD25");
        }
        //late-check
        String strDate2 = arrayList.get(position).getDueDate()+" - "+arrayList.get(position).getDueDateTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MM/yyyy - HH:mm");
        try {
            Date myDate = simpleDateFormat.parse(strDate2);
            Date currentDate2 = new Date();
            if (myDate.before(currentDate2)){
                holder.statusText.setText("\uD83D\uDD58");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.dateText.setText(strDate2);
        holder.descText.setText(arrayList.get(position).getDesc());
        holder.ratingText.setText(arrayList.get(position).getRating()+"");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titleText,dateText,descText,statusText,ratingText;
        Button doneButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.titleText);
            dateText = itemView.findViewById(R.id.dateText);
            descText = itemView.findViewById(R.id.descText);
            statusText = itemView.findViewById(R.id.statusText);
            doneButton = itemView.findViewById(R.id.doneButton);
            ratingText = itemView.findViewById(R.id.ratingText);

            doneButton.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("กรุณายืนยัน");
                builder.setCancelable(true);
                builder.setPositiveButton("ยืนยัน", (dialogInterface, i) -> {
                    DbHelper dbHelper = new DbHelper(context);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    db.execSQL("delete from things WHERE id="+arrayList.get(getAbsoluteAdapterPosition()).getId());

                    //done-in-time-check
                    Thing temp = arrayList.get(getAbsoluteAdapterPosition());
                    Date currentDate = new Date();
                    String myDateStr = temp.getDueDate()+" "+temp.getDueDateTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MM/yyyy HH:mm");
                    try {
                        Date myDate = simpleDateFormat.parse(myDateStr);
                        String sDate = new SimpleDateFormat("d/MM/yyyy").format(new Date());
                        String sTime = new SimpleDateFormat("HH:mm").format(new Date());
                        if(currentDate.before(myDate)){
                            dbHelper.addDoneThing(1,temp.getTitle(),sDate,sTime);
                        }
                        else {
                            dbHelper.addDoneThing(0,temp.getTitle(),sDate,sTime);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //start-MainActivity
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ((Activity)context).finish();
                    ((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    context.startActivity(intent);
                });
                builder.setNegativeButton("ยกเลิก", (dialogInterface, i) -> dialogInterface.cancel());
                builder.show();
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("กรุณายืนยันการลบ\nการลบรูปแบบนี้จะไม่นำไปคำนวณสถิติ");
                    builder.setCancelable(true);
                    builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DbHelper dbHelper = new DbHelper(context);
                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                            db.execSQL("delete from things WHERE id="+arrayList.get(getAbsoluteAdapterPosition()).getId());

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ((Activity)context).finish();
                            ((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            context.startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("ยกเลิก", (dialogInterface, i) -> dialogInterface.cancel());
                    builder.show();
                    return true;
                }
            });
        }
    }
}

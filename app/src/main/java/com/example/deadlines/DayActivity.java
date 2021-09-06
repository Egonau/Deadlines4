package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DayActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private TableLayout tableView;
    private ArrayList<HashMap<String, String>> dayDeadlines = new ArrayList<>();
    private TextView textViewDayName;
    private Button buttonAddEventDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        tableView = findViewById(R.id.tableView);
        auth = FirebaseAuth.getInstance();
        textViewDayName = findViewById(R.id.textViewDayName);
        buttonAddEventDay = findViewById(R.id.buttonAddEventDay);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(Integer.parseInt(Single.getInstance().chosenYear)+1900,Integer.parseInt(Single.getInstance().chosenMonth),Integer.parseInt(Single.getInstance().chosenDay)));
        Integer dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekString;
        parse();
        switch(dayOfWeek){
            case (1):{
                dayOfWeekString = "Суббота";
                break;
            }
            case (2):{
                dayOfWeekString = "Воскресенье";
                break;
            }
            case (3):{
                dayOfWeekString = "Понедельник";
                break;
            }
            case (4):{
                dayOfWeekString = "Вторник";
                break;
            }
            case (5):{
                dayOfWeekString = "Среда";
                break;
            }
            case (6):{
                dayOfWeekString = "Четверг";
                break;
            }
            case (7):{
                dayOfWeekString = "Пятница";
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + dayOfWeek);
        }
        textViewDayName.setText(dayOfWeekString);
        if (dayOfWeekString.equals("Воскресенье") || (Single.getInstance().schedule.size()==0)) {
        }
        else{
            HashMap<String, String> daySchedule = Single.getInstance().schedule.get(dayOfWeekString);
            Integer lessonsAmount = daySchedule.size();
            for (Integer i = 1;i<lessonsAmount;++i){
                if (daySchedule.get(String.valueOf(i)).equals("")) {
                    daySchedule.put(i.toString(),"Нет урока");
                }
            }
            for (Integer i = 1; i <= daySchedule.size(); ++i) {
                String resourse = null;
                String str = daySchedule.get(String.valueOf(i));
                Boolean infoPresser = false;
                if (Single.getInstance().dayDeadlines.containsKey(str) ){
                    resourse = "Red";
                    infoPresser = true;
                }
                if (str!=null){
                    setTableView(String.valueOf(i), daySchedule.get(String.valueOf(i)), resourse, true, infoPresser);
                }

            }

        }
        if (Single.getInstance().dayDeadlines.size()!=0 && Single.getInstance().dayDeadlines.containsKey("Not School")){
            for (Integer i = 0;i<Single.getInstance().dayDeadlines.get("Not School").size();++i){
                String resourse ="Red";
                setTableView("#",Single.getInstance().dayDeadlines.get("Not School").get(i).get("DeadlineName").toString(),resourse,false,true);
            }
        }

        
    }
    public void setTableView(String numberText, String infoText,String resourse,Boolean statusPressable, Boolean infoPressable){
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View row = (View) inflater.inflate(R.layout.standart_row,null);
        TextView numberTextView = (TextView) row.findViewById(R.id.numberTextView);
        TextView infoTextView = (TextView) row.findViewById(R.id.infoTextView);
        ImageView statusImageView = row.findViewById(R.id.statusImageView);
        ImageView infoImageView = row.findViewById(R.id.infoImageView);
        if (resourse!=null){
            statusImageView.setImageResource(R.drawable.red_button);//resourse
        }else
        {
            statusImageView.setImageResource(R.drawable.empty);
        }
        infoImageView.setImageResource(R.drawable.info);
        numberTextView.setText(numberText);
        infoTextView.setText(infoText);
        if (statusPressable){
            statusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Single.getInstance().tags.clear();
                    Single.getInstance().tags.put("Type","School");
                    Single.getInstance().tags.put("Lesson", infoText);
                    Intent intent=new Intent(DayActivity.this,DeadlineActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        if (infoPressable){
            infoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (numberText.equals("#")){
                        Single.getInstance().chosenLesson = "Not School";
                    }
                    else{
                        Single.getInstance().chosenLesson = infoText;
                    }
                    Intent intent=new Intent(DayActivity.this,DeadlineInfoActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        tableView.addView(row);
    }

    public void parse(){
        try {
            new Parser();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void AddEvent(View view) {
        Single.getInstance().tags.clear();
        Single.getInstance().tags.put("Type","NonSchool");
        Intent intent=new Intent(DayActivity.this,DeadlineActivity.class);
        startActivity(intent);
        finish();
    }
}
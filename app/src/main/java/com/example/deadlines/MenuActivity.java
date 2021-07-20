package com.example.deadlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity implements OnNavigationButtonClickedListener {

    private Button settingsButton;
    private Button deadlineButton;
    private CustomCalendar calendar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        settingsButton = findViewById(R.id.settingsButton);
        deadlineButton  = findViewById(R.id.deadlineButton);
        calendar = findViewById(R.id.custom_calendar);
        auth = FirebaseAuth.getInstance();
        HashMap<Object, Property> descriptionHashMap = new HashMap<>();

        Property defaultProperty = new Property();
        defaultProperty.layoutResource = R.layout.default_view;
        defaultProperty.dateTextViewResource = R.id.default_text;
        descriptionHashMap.put("default",defaultProperty);

        Property currentDate = new Property();
        currentDate.layoutResource = R.layout.current_view;
        currentDate.dateTextViewResource = R.id.current_text;
        descriptionHashMap.put("current",currentDate);

        Property schoolDeadline = new Property();
        schoolDeadline.layoutResource = R.layout.school_view;
        schoolDeadline.dateTextViewResource = R.id.school_text;
        descriptionHashMap.put("school", schoolDeadline);

        Property socialDeadline = new Property();
        socialDeadline.layoutResource  = R.layout.social_view;
        descriptionHashMap.put("social", socialDeadline);
        socialDeadline.dateTextViewResource = R.id.social_text;

        Property privateDeadline = new Property();
        privateDeadline.layoutResource  = R.layout.private_view;
        descriptionHashMap.put("private", privateDeadline);
        privateDeadline.dateTextViewResource = R.id.private_text;

        Property competitionDeadline = new Property();
        competitionDeadline.layoutResource  = R.layout.competition_view;
        descriptionHashMap.put("competition", competitionDeadline);
        competitionDeadline.dateTextViewResource = R.id.competition_text;

        calendar.setMapDescToProp(descriptionHashMap);
        Calendar defaultCalendar = Calendar.getInstance();
        HashMap<Integer,Object> dates = new HashMap<>();

        FirebaseDatabase.getInstance().getReference().child(auth.getUid()).child("Deadlines").child("Accepted").child(String.valueOf(Single.getInstance().chosenYear)).child(String.valueOf(Single.getInstance().chosenMonth)).child(String.valueOf(Single.getInstance().chosenDay)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String DeadlineDescription = ds.child("DeadlineDescription").getValue(String.class);
                    String DeadlineName = ds.child("DeadlineName").getValue(String.class);
                    String DeadlinePriority = ds.child("DeadlinePriority").getValue(String.class);
                    String DeadlineSharing = ds.child("DeadlineSharing").getValue(String.class);
                    String DeadlineTheme = ds.child("DeadlineTheme").getValue(String.class);
                    //dates.put()
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dates.put(defaultCalendar.get(defaultCalendar.DAY_OF_MONTH),"current");
        calendar.setDate(defaultCalendar,dates);
        calendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                Single.getInstance().chosenDay = String.valueOf(selectedDate.getTime().getDate());
                Single.getInstance().chosenMonth = String.valueOf(selectedDate.getTime().getMonth());
                Single.getInstance().chosenYear = String.valueOf(selectedDate.getTime().getYear());

                Intent intent=new Intent(MenuActivity.this,DeadlineActivity.class);
                startActivity(intent);
            }
        });
    }
    public void Settings(View view){
        Intent intent=new Intent(MenuActivity.this,SettingsActivity.class);
        startActivity(intent);
    }

    public void Deadline(View view){
        Intent intent=new Intent(MenuActivity.this,DeadlineActivity.class);
        startActivity(intent);
    }

    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[12];
        switch(newMonth.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                arr[0] = new HashMap<>(); //This is the map linking a date to its description
                break;
            case Calendar.FEBRUARY:
                arr[0] = new HashMap<>();
                break;
            case Calendar.MARCH:
                arr[0] = new HashMap<>();
                break;
            case Calendar.APRIL:
                arr[0] = new HashMap<>();
                break;
            case Calendar.MAY:
                arr[0] = new HashMap<>();
                break;
            case Calendar.JUNE:
                arr[0] = new HashMap<>();
                break;
            case Calendar.JULY:
                arr[0] = new HashMap<>();
                break;
            case Calendar.AUGUST:
                arr[0] = new HashMap<>();
                break;
            case Calendar.SEPTEMBER:
                arr[0] = new HashMap<>();
                break;
            case Calendar.OCTOBER:
                arr[0] = new HashMap<>();
                break;
            case Calendar.NOVEMBER:
                arr[0] = new HashMap<>();
                break;
            case Calendar.DECEMBER:
                arr[0] = new HashMap<>();
                break;

        }
        return arr;
    }


}
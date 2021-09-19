package com.example.deadlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnDayLongClickListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MenuActivity extends AppCompatActivity {

    private Button settingsButton;
    private Button deadlineButton;
    private Button olympsButton;
    private FirebaseAuth auth;
    private com.applandeo.materialcalendarview.CalendarView mCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        settingsButton = findViewById(R.id.settingsButton);
        deadlineButton  = findViewById(R.id.deadlineButton);
        olympsButton = findViewById(R.id.olympsButton);
        mCalendarView = (com.applandeo.materialcalendarview.CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setSwipeEnabled(false);
        auth = FirebaseAuth.getInstance();
        currentMonthEvents(mCalendarView);
        Single.getInstance().credentialsOfUser.put("Name","");
        Single.getInstance().credentialsOfUser.put("Group", "");
        Single.getInstance().credentialsOfUser.put("Olymps", "");
        Single.getInstance().credentialsOfUser.put("Building", "");
        Single.getInstance().credentialsOfUser.put("Occupation", "");
        getSuggestedDeadlines();
        getCredentials();
        getAllUsersInfo();
        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NotNull EventDay eventDay) {
                Single.getInstance().chosenDay = String.valueOf(eventDay.component1().getTime().getDate());
                Single.getInstance().chosenMonth = String.valueOf(eventDay.component1().getTime().getMonth());
                Single.getInstance().chosenYear = String.valueOf(eventDay.component1().getTime().getYear());
                getDeadlines();
                Intent intent=new Intent(MenuActivity.this,DayActivity.class);
                startActivity(intent);
            }
        });
        mCalendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                currentMonthEvents(mCalendarView);
            }
        });

        mCalendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                currentMonthEvents(mCalendarView);
            }
        });



    }
    public void currentMonthEvents(com.applandeo.materialcalendarview.CalendarView mCalendarView){
        mCalendarView.clearSelectedDays();
        getEvents();
    }
    public void getEvents(){
        Single.getInstance().calEvents.clear();
        mCalendarView.clearSelectedDays();
            FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(auth.getUid())).child("Deadlines").child("Accepted").child(String.valueOf(mCalendarView.getCurrentPageDate().getTime().getYear())).child(String.valueOf(mCalendarView.getCurrentPageDate().getTime().getMonth())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds:snapshot.getChildren()){
                        Integer day = Integer.parseInt(ds.getKey());
                        for (int i = (int) (ds.getChildrenCount()-1); i>=0; i--){
                            String sorter =ds.getValue(Single.getInstance().t).get(i).get("DeadlinePriority");
                            String str =ds.getValue(Single.getInstance().t).get(i).get("DeadlineTheme");
                            Calendar calendar = new GregorianCalendar(mCalendarView.getCurrentPageDate().getTime().getYear() +1900, mCalendarView.getCurrentPageDate().getTime().getMonth(),day);
                            Single.getInstance().calEvents.add(new EventDay(calendar, R.drawable.red_button));
                        }

                    }
                    mCalendarView.setEvents(Single.getInstance().calEvents);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    public void Settings(View view){
        Intent intent=new Intent(MenuActivity.this,SettingsActivity.class);
        startActivity(intent);
    }

    public void Deadline(View view){
        Intent intent=new Intent(MenuActivity.this,AcceptanceActivity.class);
        startActivity(intent);
    }
    public void getSuggestedDeadlines(){
        Single.getInstance().suggestedDeadlines.clear();
        FirebaseDatabase.getInstance().getReference().child(auth.getUid()).child("Deadlines").child("Suggested").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sn:snapshot.getChildren()){
                    GenericTypeIndicator<HashMap<String,Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    Single.getInstance().suggestedDeadlines.add(sn.getValue(t));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getDeadlines(){
            Single.getInstance().dayDeadlines.clear();
            FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(auth.getUid())).child("Deadlines").child("Accepted").child(Single.getInstance().chosenYear).child(Single.getInstance().chosenMonth).child(Single.getInstance().chosenDay).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds:snapshot.getChildren()){
                        GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>(){};
                        HashMap<String, Object> event = ds.getValue(t);
                        if (event.get("Type").equals("School")){
                            if (Single.getInstance().dayDeadlines.containsKey(event.get("Lesson").toString())){
                                Single.getInstance().dayDeadlines.get(event.get("Lesson").toString()).add(event);
                            }
                            else{
                                Single.getInstance().dayDeadlines.put(event.get("Lesson").toString(),new ArrayList<>());
                                Single.getInstance().dayDeadlines.get(event.get("Lesson").toString()).add(event);
                            }
                        }
                        else{
                            if (Single.getInstance().dayDeadlines.containsKey("Not School")){
                                Single.getInstance().dayDeadlines.get("Not School").add(event);
                            }
                            else{
                                Single.getInstance().dayDeadlines.put("Not School",new ArrayList<>());
                                Single.getInstance().dayDeadlines.get("Not School").add(event);
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    public void getCredentials(){
        FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(auth.getUid())).child("Credentials").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Single.getInstance().credentialsOfUser.put(ds.getKey().toString(),ds.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void Olymps(View view) {
        Intent intent=new Intent(MenuActivity.this,OlympiadsActivity.class);
        startActivity(intent);
    }
    public void getAllUsersInfo() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sn:snapshot.getChildren()){
                    GenericTypeIndicator<HashMap<String,Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    Single.getInstance().allUsersCredentials.put(sn.getKey().toString(),sn.child("Credentials").getValue(t));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
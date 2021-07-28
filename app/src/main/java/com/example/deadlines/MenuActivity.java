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

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity {

    private Button settingsButton;
    private Button deadlineButton;
    private FirebaseAuth auth;
    private com.applandeo.materialcalendarview.CalendarView mCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        settingsButton = findViewById(R.id.settingsButton);
        deadlineButton  = findViewById(R.id.deadlineButton);
        mCalendarView = (com.applandeo.materialcalendarview.CalendarView) findViewById(R.id.calendarView);
        auth = FirebaseAuth.getInstance();
        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NotNull EventDay eventDay) {
                Single.getInstance().chosenDay = String.valueOf(eventDay.component1().getTime().getDate());
                Single.getInstance().chosenMonth = String.valueOf(eventDay.component1().getTime().getMonth());
                Single.getInstance().chosenYear = String.valueOf(eventDay.component1().getTime().getYear());
                Intent intent=new Intent(MenuActivity.this,DeadlineActivity.class);
                startActivity(intent);
            }
        });
        FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(auth.getUid())).child("Deadlines").child("Accepted").child(String.valueOf(mCalendarView.getCurrentPageDate().getTime().getYear())).child(String.valueOf(mCalendarView.getCurrentPageDate().getTime().getMonth())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                Single.getInstance().events = snapshot.getValue(t);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(auth.getUid())).child("Deadlines").child("Accepted").child(String.valueOf(mCalendarView.getCurrentPageDate().getTime().getYear())).child(String.valueOf(mCalendarView.getCurrentPageDate().getTime().getMonth())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    Single.getInstance().events.add(ds.getValue(String.class));
                    /*
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR,Integer.valueOf(mCalendarView.getCurrentPageDate().getTime().getYear()) +1900);
                    calendar.set(Calendar.MONTH,Integer.valueOf(mCalendarView.getCurrentPageDate().getTime().getMonth()) );
                    calendar.set(Calendar.DATE, Integer.valueOf(ds.getKey()));
                    Log.e("Calendar Deadlines",calendar.toString());
                    Single.getInstance().events.add(new EventDay(calendar, R.drawable.red_button));
                    mCalendarView.setEvents(Single.getInstance().events);

                     */
                }
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
        Intent intent=new Intent(MenuActivity.this,DeadlineActivity.class);
        startActivity(intent);
    }
}
package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class OlympiadsActivity extends AppCompatActivity {
    private RecyclerView olympsRV;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olympiads);
        auth = FirebaseAuth.getInstance();
        parseOlymps();
        olympsRV = (RecyclerView)findViewById(R.id.olympsRV);
        olympsRV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        olympsRV.setLayoutManager(llm);
        OlympiadsAdapter adapter = new OlympiadsAdapter(OlympiadsActivity.this, Single.getInstance().allCurrentOlympiads);
        olympsRV.setAdapter(adapter);
    }
    public void parseOlymps(){
        try {
            new ParserOlymps();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
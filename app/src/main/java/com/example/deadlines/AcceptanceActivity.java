package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class AcceptanceActivity extends AppCompatActivity {
    private RecyclerView acceptanceRV;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptance);
        auth = FirebaseAuth.getInstance();
        acceptanceRV = (RecyclerView)findViewById(R.id.olympsRV);
        acceptanceRV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        acceptanceRV.setLayoutManager(llm);
        AcceptanceAdapter adapter = new AcceptanceAdapter(AcceptanceActivity.this, Single.getInstance().suggestedDeadlines);
        acceptanceRV.setAdapter(adapter);
    }
}
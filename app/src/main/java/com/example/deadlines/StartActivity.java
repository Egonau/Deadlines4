package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private Button loginStartButton;
    private Button registrationStartButton;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loginStartButton = findViewById(R.id.loginStartButton);
        registrationStartButton = findViewById(R.id.registrationStartButton);
        auth = FirebaseAuth.getInstance();
    }
    public void StartLogin(View view){
        if (auth.getCurrentUser() != null) {
            Intent intent=new Intent(StartActivity.this,MenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    public void StartRegistration(View view){
        Intent intent=new Intent(StartActivity.this,RegistrationActivity.class);
        startActivity(intent);
    }
}
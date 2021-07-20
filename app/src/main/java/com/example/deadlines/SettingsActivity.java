package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private Button saveSettingsButton;
    private Button logOutButton;
    private EditText nameEditText;
    private EditText groupEditText;
    private FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        logOutButton = findViewById(R.id.logOutButton);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);
        nameEditText = findViewById(R.id.nameEditText);
        groupEditText = findViewById(R.id.groupEditText);
        auth = FirebaseAuth.getInstance();
    }
    public void Save(View view){
        HashMap<String,Object> map = new HashMap<>();
        map.put("Name", nameEditText.getText().toString());
        map.put("Group", groupEditText.getText().toString());
        FirebaseDatabase.getInstance().getReference().child(auth.getUid()).child("Credentials").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SettingsActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void LogOut(View view){
        auth.signOut();
        Intent intent=new Intent(SettingsActivity.this,StartActivity.class);
        startActivity(intent);
        finish();
    }
}
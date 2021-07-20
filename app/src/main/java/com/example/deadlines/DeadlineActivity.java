package com.example.deadlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DeadlineActivity extends AppCompatActivity {

    private TextView deadlineText;
    private EditText deadlineNameTextEdit;
    private EditText deadlineThemeTextEdit;
    private EditText deadlinePriorityTextEdit;
    private EditText deadlineSharingTextEdit;
    private EditText deadlineDescriptionTextEdit;
    private Button deadlineSaveButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadline);
        deadlineNameTextEdit = findViewById(R.id.deadlineNameTextEdit);
        deadlineThemeTextEdit = findViewById(R.id.deadlineThemeTextEdit);
        deadlinePriorityTextEdit  = findViewById(R.id.deadlinePriorityTextEdit);
        deadlineSharingTextEdit = findViewById(R.id.deadlineSharingTextEdit);
        deadlineDescriptionTextEdit = findViewById(R.id.deadlineDescriptionTextEdit);
        deadlineSaveButton = findViewById(R.id.deadlineSaveButton);
        auth = FirebaseAuth.getInstance();
    }

    public void SaveDeadline(View view){
        HashMap<String,Object> map = new HashMap<>();
        map.put("DeadlineName",  deadlineNameTextEdit.getText().toString());
        map.put("DeadlineTheme", deadlineThemeTextEdit.getText().toString() );
        map.put("DeadlinePriority", deadlinePriorityTextEdit.getText().toString());
        map.put("DeadlineSharing", deadlineSharingTextEdit.getText().toString());
        map.put("DeadlineDescription", deadlineDescriptionTextEdit.getText().toString());
        FirebaseDatabase.getInstance().getReference().child(auth.getUid()).child("Deadlines").child("Accepted").child(String.valueOf(Single.getInstance().chosenYear)).child(String.valueOf(Single.getInstance().chosenMonth)).child(String.valueOf(Single.getInstance().chosenDay)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                FirebaseDatabase.getInstance().getReference().child(auth.getUid()).child("Deadlines").child("Accepted").child(String.valueOf(Single.getInstance().chosenYear)).child(String.valueOf(Single.getInstance().chosenMonth)).child(String.valueOf(Single.getInstance().chosenDay)).child(String.valueOf(snapshot.getChildrenCount())).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DeadlineActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        Intent intent=new Intent(DeadlineActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }

}
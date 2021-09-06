package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditingActivity extends AppCompatActivity {
    EditText editNameView;
    EditText editThemeView;
    EditText editPriorityView;
    EditText editSharingView;
    EditText editDescriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);
        editNameView = findViewById(R.id.editNameView);
        editThemeView = findViewById(R.id.editThemeView);
        editPriorityView = findViewById(R.id.editPriorityView);
        editSharingView = findViewById(R.id.editSharingView);
        editDescriptionView = findViewById(R.id.editDescriptionView);
        editNameView.setText(Single.getInstance().editedDeadline.get("DeadlineName").toString());
        editThemeView.setText(Single.getInstance().editedDeadline.get("DeadlineTheme").toString());
        editPriorityView.setText(Single.getInstance().editedDeadline.get("DeadlinePriority").toString());
        editSharingView.setText(Single.getInstance().editedDeadline.get("DeadlineSharing").toString());
        editDescriptionView.setText(Single.getInstance().editedDeadline.get("DeadlineDescription").toString());
    }

    public void Edit(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        String year = Single.getInstance().dateInfo[0];
        String month = Single.getInstance().dateInfo[1];
        String day = Single.getInstance().dateInfo[2];
        String number = Single.getInstance().dateInfo[3];
        Single.getInstance().editedDeadline.put("DeadlineName",editNameView.getText().toString());
        Single.getInstance().editedDeadline.put("DeadlineTheme",editThemeView.getText().toString());
        Single.getInstance().editedDeadline.put("DeadlinePriority",editPriorityView.getText().toString());
        Single.getInstance().editedDeadline.put("DeadlineSharing",editSharingView.getText().toString());
        Single.getInstance().editedDeadline.put("DeadlineDescription",editDescriptionView.getText().toString());
        rootRef.child(auth.getUid()).child("Deadlines").child("Accepted").child(year).child(month).child(day).child(number).updateChildren(Single.getInstance().editedDeadline);
        Intent intent=new Intent(this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
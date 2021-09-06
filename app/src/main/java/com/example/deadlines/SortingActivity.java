package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SortingActivity extends AppCompatActivity {

    private EditText textEditSorting;
    private Button sortingButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);
        textEditSorting = findViewById(R.id.textEditSorting);
        sortingButton = findViewById(R.id.sortingButton);
    }

    public void Sorting(View view) {
        Single.getInstance().sortingTag = String.valueOf(textEditSorting.getText());
        Intent intent=new Intent(SortingActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void Clearing(View view) {
        Single.getInstance().sortingTag = null;
        Intent intent=new Intent(SortingActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
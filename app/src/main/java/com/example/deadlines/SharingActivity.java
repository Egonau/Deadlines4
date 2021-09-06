package com.example.deadlines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SharingActivity extends AppCompatActivity {

    private EditText groupSharingEditText;
    private EditText buildingSharingEditText;
    private EditText occupationSharingEditText;
    private EditText nameSharingEditText;
    private Button sharingButton;
    private Button sharingAcceptButton;
    private ListView sharingListView;
    private FirebaseAuth auth;
    private List<String> users = new ArrayList<>();
    private SparseBooleanArray selected = new SparseBooleanArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        auth = FirebaseAuth.getInstance();
        groupSharingEditText = findViewById(R.id.groupSharingEditText);
        buildingSharingEditText = findViewById(R.id.buildingSharingEditText);
        occupationSharingEditText = findViewById(R.id.occupationSharingEditText);
        nameSharingEditText = findViewById(R.id.nameSharingEditText);
        sharingButton = findViewById(R.id.sharingButton);
        sharingAcceptButton = findViewById(R.id.sharingAcceptButton);
        sharingListView = findViewById(R.id.sharingListView);
        Single.getInstance().allUsersCredentials.remove(auth.getUid());
        for(Map.Entry entry:Single.getInstance().allUsersCredentials.entrySet()){
            HashMap<String,String> user  = (HashMap<String, String>) entry.getValue();
                users.add(user.get("Name"));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice, users);
        sharingListView.setAdapter(adapter);
        nameSharingEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                groupSharingEditText.getText().clear();
                occupationSharingEditText.getText().clear();
                buildingSharingEditText.getText().clear();
                users.clear();
                for(Map.Entry entry:Single.getInstance().allUsersCredentials.entrySet()){
                    HashMap<String,String> user  = (HashMap<String, String>) entry.getValue();
                    if (!user.get("Name").isEmpty()&&user.get("Name").contains(nameSharingEditText.getText().toString())){
                        users.add(user.get("Name"));
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(getBaseContext(),
                        android.R.layout.simple_list_item_multiple_choice, users);
                sharingListView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sharingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected=sharingListView.getCheckedItemPositions();
            }
        });
    }

    public void Accept(View view) {
        nameSharingEditText.getText().clear();
        users.clear();
        for(Map.Entry entry:Single.getInstance().allUsersCredentials.entrySet()){
            HashMap<String,String> user  = (HashMap<String, String>) entry.getValue();
            if (!user.get("Group").isEmpty()&&Objects.equals(user.get("Group"), groupSharingEditText.getText().toString())&&!user.get("Occupation").isEmpty()&&Objects.equals(user.get("Occupation"), occupationSharingEditText.getText().toString())&&!user.get("Building").isEmpty()&&Objects.equals(user.get("Building"), buildingSharingEditText.getText().toString())){//&&!user.get("Occupation").isEmpty()&&user.get("Occupation").equals(occupationSharingEditText.getText())&&!user.get("Building").isEmpty()&&user.get("Building").equals(buildingSharingEditText.getText())&&!user.get("Group").isEmpty()&&user.get("Group").equals(groupSharingEditText.getText())){
                users.add(user.get("Name"));
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice, users);
        sharingListView.setAdapter(adapter);
    }

    public void Share(View view) {
                for(Integer i=0;i < users.size();i++)
                {
                    if(selected.get(i)){
                        users.get(i);
                        for(Map.Entry entry:Single.getInstance().allUsersCredentials.entrySet()){
                            HashMap<String,String> user  = (HashMap<String, String>) entry.getValue();
                            if (Objects.equals(user.get("Name"),users.get(i))){
                                FirebaseDatabase.getInstance().getReference().child(entry.getKey().toString()).child("Deadlines").child("Suggested").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        Single.getInstance().sharingDeadline.put("DeadlineDateInfo",Single.getInstance().chosenYear+"_"+Single.getInstance().chosenMonth+"_"+Single.getInstance().chosenDay+"_"+String.valueOf(snapshot.getChildrenCount()));
                                        FirebaseDatabase.getInstance().getReference().child(entry.getKey().toString()).child("Deadlines").child("Suggested").child(String.valueOf(snapshot.getChildrenCount())).updateChildren(Single.getInstance().sharingDeadline).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(SharingActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(SharingActivity.this,MenuActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                    }
                                });
                            }
                        }
                    }
                }
    }
}
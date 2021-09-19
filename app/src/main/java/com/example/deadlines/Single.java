package com.example.deadlines;

import androidx.annotation.NonNull;

import com.applandeo.materialcalendarview.EventDay;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Single {
    private static final Single INSTANCE = new Single();
    public static String chosenDay = null;
    public static String chosenMonth = null;
    public static String chosenYear = null;
    public static List<EventDay> calEvents = new ArrayList<>();
    public static GenericTypeIndicator<ArrayList<HashMap<String, String>>> t = new
            GenericTypeIndicator<ArrayList<HashMap<String, String>>>() {
            };
    public static String sortingTag = null;
    public static HashMap<String, String> credentialsOfUser = new HashMap<>();
    public static HashMap<String, HashMap<String, String>> schedule = new HashMap<>();
    public static HashMap<String,String> tags = new HashMap<>();
    public static HashMap<String,ArrayList<HashMap<String,Object>>> dayDeadlines = new HashMap<>();
    public static String chosenLesson = null;
    public static List<HashMap<String,Object>> deletingDeadlines = new ArrayList<>();
    public static HashMap<String,Object> editedDeadline = new HashMap<>();
    public static HashMap<String,Object> sharingDeadline = new HashMap<>();
    public static String[] dateInfo;
    public static ArrayList<HashMap<String,Object>> suggestedDeadlines = new ArrayList<>();
    public static List<HashMap<String,Object>> deletingAcceptedDeadlines = new ArrayList<>();
    public static HashMap<String,HashMap<String,Object>> allUsersCredentials = new HashMap<>();
    public static ArrayList<HashMap<String,Object>> allCurrentOlympiads = new ArrayList<>();
    private Single() {
    }

    public static Single getInstance() {
        return INSTANCE;
    }
}

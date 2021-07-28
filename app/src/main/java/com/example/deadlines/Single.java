package com.example.deadlines;

import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.List;

public class Single {
    private static final Single INSTANCE = new Single();
    public static String chosenDay= null;
    public static String chosenMonth= null;
    public static String chosenYear= null;
    public static List<String> events = new ArrayList<>();
    private Single(){}


    public static Single getInstance(){
        return INSTANCE;
    }
}

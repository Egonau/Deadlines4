package com.example.deadlines;

public class Single {
    private static final Single INSTANCE = new Single();
    public static String chosenDay= null;
    public static String chosenMonth= null;
    public static String chosenYear= null;
    private Single(){}


    public static Single getInstance(){
        return INSTANCE;
    }
}

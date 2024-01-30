package com.example.schedulenow;

import java.util.ArrayList;

public class Class {
    private String cName;
    private String cTime;
    private String cInstructor;

    public Class(String name, String time, String instructor) {
        cName = name;
        cTime = time;
        cInstructor = instructor;
    }

    public String getName() {
        return cName;
    }

    public String getTime() {
        return cTime;
    }

    public String getInstructor() {
        return cInstructor;
    }

    private static int lastClassId = 0;
    public static ArrayList<Class> createClassesList(int numClasses) {
        // create ArrayList of type Class for each respective class
        ArrayList<Class> classes = new ArrayList<Class>();
        // loop and add each new class to the array
        for (int i = 1; i <= numClasses; i++) {
            classes.add(new Class("Class " + ++lastClassId, "Time " + i, "Instructor " + i));
        }
        // return the array of classes
        return classes;
    }
}

package com.example.schedulenow;

public class Class {
    private String cName;
    private String cTime;
    private String cInstructor;

    public Class(String name, String time, String instructor) {
        cName = name;
        cTime = time;
        cInstructor = instructor;
    }

    // methods for editing class details
    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public void setcInstructor(String cInstructor) {
        this.cInstructor = cInstructor;
    }

    // methods for getting class details
    public String getName() {
        return cName;
    }

    public String getTime() {
        return cTime;
    }

    public String getInstructor() {
        return cInstructor;
    }
}

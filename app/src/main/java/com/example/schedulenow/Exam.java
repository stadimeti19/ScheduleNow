package com.example.schedulenow;

import java.util.ArrayList;

public class Exam {
    private String examTitle;
    private String examDate;
    private String examTime;
    private String examLocation;

    public Exam(String title, String date, String time, String location) {
        this.examTitle = title;
        this.examDate = date;
        this.examTime = time;
        this.examLocation = location;
    }

    // define getters for exams
    public String getTitle() {
        return examTitle;
    }
    public String getDate() {
        return examDate;
    }
    public String getTime() {
        return examTime;
    }
    public String getLocation() {
        return examLocation;
    }

    // define setters for exams
    public void setTitle(String title) {
        this.examTitle = title;
    }
    public void setDate(String date) {
        this.examDate = date;
    }
    public void setTime(String time) {
        this.examTime = time;
    }
    public void setLocation(String location) {
        this.examLocation = location;
    }
}

package com.example.schedulenow;

import java.util.ArrayList;

public class ToDo {
    private String tName;
    private String tTime;

    public ToDo(String name, String time) {
        tName = name;
        tTime = time;
    }

    public String getName() {
        return tName;
    }

    public String getTime() {
        return tTime;
    }

    public void setTitle(String Title) {
        this.tName = Title;
    }

    public void setDueDate(String toDoDueDate) {
        this.tTime = toDoDueDate;
    }
}

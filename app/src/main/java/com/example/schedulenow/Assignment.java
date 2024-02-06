package com.example.schedulenow;

public class Assignment {
    private String assignmentTitle;
    private String assignmentDueDate;
    private String assignmentClass;

    public Assignment(String assignmentTitle, String assignmentDueDate, String assignmentClass) {
        this.assignmentTitle = assignmentTitle;
        this.assignmentDueDate = assignmentDueDate;
        this.assignmentClass = assignmentClass;
    }

    public String getTitle() {
        return assignmentTitle;
    }

    public String getDueDate() {
        return assignmentDueDate;
    }

    public String getAClass() {
        return assignmentClass;
    }

    // New methods for editing assignment details
    public void setTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public void setDueDate(String assignmentDueDate) {
        this.assignmentDueDate = assignmentDueDate;
    }

    public void setAClass(String assignmentClass) {
        this.assignmentClass = assignmentClass;
    }
}

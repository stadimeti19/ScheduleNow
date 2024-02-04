package com.example.schedulenow;

import java.util.ArrayList;

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

    private static int lastAssignmentId = 0;
    public static ArrayList<Assignment> createAssignmentsList(int numAssignments) {
        // create ArrayList of type Assignment for each respective assignments
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        // loop and add each new assignment to the array
        for (int i = 1; i <= numAssignments; i++) {
            assignments.add(new Assignment("Title " + (++lastAssignmentId), "DueDate " + i, "Asscociated Class " + i));
        }
        // return the array of assignments
        return assignments;
    }
}

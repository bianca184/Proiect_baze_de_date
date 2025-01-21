package com.example.schoolmanagementsystem.model;

public class Course {
    private String type;
    private String activityType;
    private String description;
    private int maxStudents;
    private String subject;

    public Course(String type, String activityType, String description, int maxStudents) {
        this.type = type;
        this.activityType = activityType;
        this.description = description;
        this.maxStudents = maxStudents;
    }

    public Course(String type, String activityType, String description, int maxStudents, String subject) {
        this.type = type;
        this.activityType = activityType;
        this.description = description;
        this.maxStudents = maxStudents;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }
}


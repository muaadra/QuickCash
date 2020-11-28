package com.softeng.quickcash;

import java.util.Date;
import java.util.HashMap;

public class TaskPost {
    private String author;
    private String postId;
    private String taskTitle;
    private String taskDescription;
    private float taskCost;
    private boolean postDeleted;
    private String assignedEmployee;
    private Date timeStamp;
    private Date expectedDate;
    private String latLonLocation;
    private float distance;
    private boolean isCompleted;
    private String totalPayed;
    private HashMap<String,Integer> Applicants;

    public TaskPost() {
    }

    public TaskPost(String author, String postId, String taskTitle,
                    String taskDescription, float taskCost, boolean postDeleted,
                    String assignedEmployee, Date timeStamp, Date expectedDate,
                    String latLonLocation) {
        this.author = author;
        this.postId = postId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskCost = taskCost;
        this.postDeleted = postDeleted;
        this.assignedEmployee = assignedEmployee;
        this.timeStamp = timeStamp;
        this.expectedDate = expectedDate;
        this.latLonLocation = latLonLocation;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public float getTaskCost() {
        return taskCost;
    }

    public void setTaskCost(float taskCost) {
        this.taskCost = taskCost;
    }

    public boolean isPostDeleted() {
        return postDeleted;
    }

    public void setPostDeleted(boolean postDeleted) {
        this.postDeleted = postDeleted;
    }

    public String getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(String assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getLatLonLocation() {
        return latLonLocation;
    }

    public void setLatLonLocation(String latLonLocation) {
        this.latLonLocation = latLonLocation;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public HashMap<String, Integer> getApplicants() {
        return Applicants;
    }

    public void setApplicants(HashMap<String, Integer> applicants) {
        this.Applicants = applicants;
    }

    public String getTotalPayed() {
        return totalPayed;
    }

    public void setTotalPayed(String totalPayed) {
        this.totalPayed = totalPayed;
    }
}

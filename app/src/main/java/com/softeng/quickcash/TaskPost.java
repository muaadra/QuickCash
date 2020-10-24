package com.softeng.quickcash;

import java.util.Date;

public class TaskPost {
    private String postId;
    private String taskTitle;
    private String taskDescription;
    private String taskCost;
    private boolean postDeleted;
    private boolean postAccepted;
    private Date expectedDate;
    private String latLonLocation;

    public TaskPost() {
    }

    public TaskPost(String postId, String taskTitle, String taskDescription,
                    String taskCost, boolean postDeleted,
                    boolean postAccepted, Date expectedDate,
                    String latLonLocation) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskCost = taskCost;
        this.postDeleted = postDeleted;
        this.postAccepted = postAccepted;
        this.expectedDate = expectedDate;
        this.latLonLocation = latLonLocation;
        this.postId = postId;
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

    public String getTaskCost() {
        return taskCost;
    }

    public void setTaskCost(String taskCost) {
        this.taskCost = taskCost;
    }

    public boolean isPostDeleted() {
        return postDeleted;
    }

    public void setPostDeleted(boolean postDeleted) {
        this.postDeleted = postDeleted;
    }

    public boolean isPostAccepted() {
        return postAccepted;
    }

    public void setPostAccepted(boolean postAccepted) {
        this.postAccepted = postAccepted;
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
}

package com.softeng.quickcash;

import java.util.Date;

public class TaskPost {
    private String taskTitle;
    private String taskDescription;
    private String taskCost;
    private boolean isPostClosed;
    private boolean isPostAccepted;
    private Date expectedDate;
    private String latLonLocation;

    public TaskPost() {
    }

    public TaskPost(String taskTitle, String taskDescription,
                    String taskCost, boolean isPostClosed,
                    boolean isPostAccepted, Date expectedDate,
                    String latLonLocation) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskCost = taskCost;
        this.isPostClosed = isPostClosed;
        this.isPostAccepted = isPostAccepted;
        this.expectedDate = expectedDate;
        this.latLonLocation = latLonLocation;
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

    public boolean isPostClosed() {
        return isPostClosed;
    }

    public void setPostClosed(boolean postClosed) {
        isPostClosed = postClosed;
    }

    public boolean isPostAccepted() {
        return isPostAccepted;
    }

    public void setPostAccepted(boolean postAccepted) {
        isPostAccepted = postAccepted;
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

package com.softeng.quickcash;

public class TaskApplication {
    private String taskId;
    private String taskAuthor;
    private long applicationDate;
    private int status;

    public TaskApplication() {
    }

    public TaskApplication(String taskId, String taskAuthor, long applicationDate) {
        this.taskId = taskId;
        this.taskAuthor = taskAuthor;
        this.applicationDate = applicationDate;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskAuthor() {
        return taskAuthor;
    }

    public void setTaskAuthor(String taskAuthor) {
        this.taskAuthor = taskAuthor;
    }

    public double getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(long applicationDate) {
        this.applicationDate = applicationDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

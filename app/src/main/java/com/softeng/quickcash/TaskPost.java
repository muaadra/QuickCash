package com.softeng.quickcash;

public class TaskPost {
    private String taskTitle;
    private String taskDescription;
    private String taskCost;

    public TaskPost() {
    }

    public TaskPost(String taskTitle, String taskDescription, String taskCost) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskCost = taskCost;
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
}

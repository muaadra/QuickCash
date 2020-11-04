package com.softeng.quickcash;

import java.util.ArrayList;

public class TaskList {
    ArrayList<TaskPost> TaskPosts = new ArrayList<>();

    public ArrayList<TaskPost> getTaskPosts() {
        return TaskPosts;
    }

    public void setTaskPosts(ArrayList<TaskPost> taskPosts) {
        TaskPosts = taskPosts;
    }


}

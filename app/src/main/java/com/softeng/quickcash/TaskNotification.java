package com.softeng.quickcash;

public class TaskNotification {
    private String postID;
    private boolean isNew;

    public TaskNotification() {
    }

    public TaskNotification(String postID, boolean isNew) {
        this.postID = postID;
        this.isNew = isNew;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        this.isNew = aNew;
    }
}

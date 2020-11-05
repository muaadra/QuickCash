package com.softeng.quickcash;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * use to ease reading data from firebase database
 * @param <T>
 */
public abstract class AllTaskList<T> implements ValueEventListener {
    public String path;
    TaskList taskLists = new TaskList();
    DatabaseReference ref;
    HashMap params;

    public AllTaskList(String path,  FirebaseDatabase db) {
        this.path = path;
        ref = db.getReference(path);
        ref.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        //get user sign up info as a UserSignUpData object
        TaskList taskLists = new TaskList();
        ArrayList<TaskPost> taskPostList = new ArrayList<>();
        if (dataSnapshot.exists()) {
            HashMap<String, HashMap<String, HashMap>> users = (HashMap<String,  HashMap<String,HashMap>>) dataSnapshot.getValue();
            for (HashMap<String, HashMap> user : users.values()) {
                if(user.get("TaskPosts") != null) {
                    HashMap<String, HashMap<String, Object>> taskPosts = (HashMap<String, HashMap<String, Object>>) user.get("TaskPosts");
                    for (HashMap<String, Object> taskPost : taskPosts.values()) {
                        TaskPost temp = new TaskPost();
                        temp.setTaskTitle(taskPost.get("taskTitle").toString());
                        temp.setTaskCost(Float.valueOf(taskPost.get("taskCost").toString()));
                        if(taskPost.get("postDeleted") == null || taskPost.get("postDeleted").toString().equals("true")) {
                            continue;
                        } else {
                            temp.setPostDeleted(taskPost.get("postDeleted").toString().equals("true"));
                        }
                        HashMap<String, Long> expectedDate  = (HashMap<String, Long>) taskPost.get("expectedDate");
                        Date date  = new Date();
                        date.setTime(expectedDate.get("time"));
                        temp.setExpectedDate(date);
                        temp.setLatLonLocation(taskPost.get("latLonLocation").toString());
                        temp.setPostId(taskPost.get("postId").toString());
                        temp.setTaskDescription(taskPost.get("taskDescription").toString());
                        taskPostList.add(temp);
                    }
                }
            }
            taskLists.setTaskPosts(taskPostList);
        }
        getReturnedDbData(taskLists);
    }

    public abstract void getReturnedDbData(TaskList tastList);


    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }


}

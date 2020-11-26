package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;

public class NewTasksNotifications extends AppCompatActivity {
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseStorage fbStorage;
    ArrayList<TaskPost> taskPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tasks_notifications);

        //create instance of FirebaseStorage
        fbStorage = FirebaseStorage.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotifications();
    }

    private void getNotifications() {
        final ArrayList<TaskNotification> tasks = new ArrayList<>();
        String path = "users/";
        final String userPath = UserStatusData.getUserID(this) +
                "/Notifications/NewTasks/";

        //read data from database
        new DbRead<DataSnapshot>(path,DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {
                DataSnapshot userShot = dataFromDb.child(userPath);
                for (DataSnapshot item : userShot.getChildren()) {
                    tasks.add(item.getValue(TaskNotification.class));
                }
                getPostsFromDBSnapShot(tasks, dataFromDb);
            }
        };
    }


    /**
     * creates a RecyclerView view for main task posts
     * @param posts list of posts
     */
    public void createRecyclerView(ArrayList<TaskPost> posts, ArrayList<String> newPostIds) {
        if(posts == null){
            return;
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.TaskPostsList);

        // using a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new TaskNotificationRVAdapter(posts, fbStorage,
                newPostIds);
        recyclerView.setAdapter(mAdapter);

    }

    private void getPostsFromDBSnapShot(ArrayList<TaskNotification> taskNotifications,
                                        DataSnapshot dataFromDb) {
        taskPosts = new ArrayList<>();
        final ArrayList<String> postIds = new ArrayList<>();
        final ArrayList<String> newPostIds = new ArrayList<>();
        for (TaskNotification t: taskNotifications) {
            if(t.isNew()){
                newPostIds.add(t.getPostID());
            }
            postIds.add(t.getPostID());
            t.setNew(false);
        }

        //loop through all children in path
        for (DataSnapshot userdata : dataFromDb.getChildren()) {

            for (DataSnapshot post : userdata.child("TaskPosts").getChildren()) {
                TaskPost taskPost = post.getValue(TaskPost.class);
                if(taskPost != null && postIds.contains(taskPost.getPostId())){
                    taskPosts.add(taskPost);
                    System.out.println(taskPost.getTaskTitle());
                }
            }
        }
        Collections.sort(taskPosts,new LatestDateSort(false));
        createRecyclerView(taskPosts, newPostIds);
        setNotificationStatusToViewed(taskNotifications);
    }

    private void setNotificationStatusToViewed(ArrayList<TaskNotification> taskNotifications){
        //path where you want to write data to


        for (TaskNotification t :taskNotifications) {
            String path = "users/" + UserStatusData.getUserID(this) +
                    "/Notifications/NewTasks/" + t.getPostID() ;
            new DbWrite<TaskNotification>(path,t,db) {
                @Override
                public void writeResult(TaskNotification userdata) {
                }
            };
        }

    }

}
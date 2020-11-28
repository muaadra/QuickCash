package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * this class represents the "My Application" activity, it displays user's
 * history of all applications submitted to tasks
 *
 * @author Muaad Alrawhani
 */
public class MyTasksApplications extends AppCompatActivity {
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    int emptyListTextViewOriginalHeight = -1; // to store original height of the TextView
    ArrayList<TaskPost> taskPosts;
    private FirebaseStorage fbStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks_applications);

        //create instance of FirebaseStorage
        fbStorage = FirebaseStorage.getInstance();
    }


    @Override
    protected void onResume() {
        super.onResume();

        getDataFromDbAndShowOnUI();
    }

    /**
     * creates the Recycler view for all posts I applied to
     * @param posts list of all my posts
     */
    public void createRecyclerView(ArrayList<TaskPost> posts) {

        //a message shows/expands in the recycleView if it's empty and collapses otherwise
        TextView emptyListTV = (TextView)findViewById(R.id.emptyStatusMyPosts);
        if(emptyListTextViewOriginalHeight == -1){
            emptyListTextViewOriginalHeight = emptyListTV.getHeight();
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.TaskPostsList);

        // using a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //sort list to show latest first
        Collections.sort(posts,new LatestDateSort(false));

        RecyclerView.Adapter mAdapter = new MyTasksApplicationsAdapter(posts, fbStorage);
        recyclerView.setAdapter(mAdapter);

        if(posts != null && posts.size() > 0){
            //hide message that says the list is empty
            emptyListTV.setHeight(0);
        }else {
            emptyListTV.setHeight(emptyListTextViewOriginalHeight);
        }

    }


    private void getDataFromDbAndShowOnUI() {

        final ArrayList<TaskPost> posts = new ArrayList<>();
        final String userId = UserStatusData.getEmail(this).replace(".", ";");

        //path to database object
        String path = "users/";

        new DbRead<DataSnapshot>(path, DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {
                //loop through all children in path
                DataSnapshot applicationsSnapShot = dataFromDb.child(userId + "/MyApplications");

                List<TaskApplication> applicationsList = DataSnapShotToArrayList.getArrayList(applicationsSnapShot,
                        TaskApplication.class);

                for (TaskApplication application : applicationsList) {
                    String taskPath = application.getTaskAuthor() + "/TaskPosts/" + application.getTaskId();
                    TaskPost tP = dataFromDb.child(taskPath).getValue(TaskPost.class);
                    if(tP != null){
                        posts.add(tP);
                    }
                }

                taskPosts = posts;
                createRecyclerView(taskPosts);
            }
        };
    }


}
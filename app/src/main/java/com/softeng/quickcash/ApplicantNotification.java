package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class represents the "My posts" activity, it displays all user's posts
 * (i.e active and deleted posts) into a toggleable Recycler view
 *
 * @author Muaad Alrawhani
 */
public class ApplicantNotification extends AppCompatActivity {
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    int emptyListTextViewOriginalHeight = -1; // to store original height of the TextView
    ArrayList<TaskPost> taskPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_notification);
    }

    /**
     * creates the Recycler view for all my task posts
     * @param posts list of all my posts
     */
    public void createRecyclerView(ArrayList<TaskPost> posts) {



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.TaskPostsList);

        // using a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        RecyclerView.Adapter mAdapter = new ApplicantNotificationAdapter(posts,db);
        recyclerView.setAdapter(mAdapter);


    }




}
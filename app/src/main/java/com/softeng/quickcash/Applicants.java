package com.softeng.quickcash;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Applicants extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Object TaskTypes;
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    ArrayList<String> applicantNames;
    ArrayList<userProfile> applicantProfiles;
    HashMap<String, Integer> flood;
    private String postID;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_applicants);


        //getApplicantsFromDB();


    }






    @Override
    protected void onResume() {
        super.onResume();
       // getApplicantsFromDB();
    }


    public void createRecyclerView(ArrayList<userProfile> userProfiles) {

        RecyclerView recyclerView = findViewById(R.id.applicantsRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ApplicantsAdapter(userProfiles, postID);
        recyclerView.setAdapter(mAdapter);
    }


}

package com.softeng.quickcash;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Applicants extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private ArrayList<String> applicantNames;
    private ArrayList<userProfile> applicantProfiles;
    private String postID;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_applicants);
    }

    /*
     * This method will compile the user Email ids to an arraylist of strings.
     * It will check the path of users > userID* > TaskPosts > postID* > Applicants
     */
    private void getApplicantsFromDB() {

        final ArrayList<String> applicantProfilesHere = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            postID = bundle.getString("postID");    //getting the postId from previous activity (employer tasks > task > taskID passed
        }

        userID = UserStatusData.getUserID(this);    //get the current userID of the user signed in

        String path = "users/" + userID + "/TaskPosts/" + postID + "/Applicants";

        new DbRead<DataSnapshot>(path, DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {

                for (DataSnapshot userdata : dataFromDb.getChildren()) {

                    applicantProfilesHere.add(userdata.getKey());

                }
                applicantNames = applicantProfilesHere;

                getDataFromDb();

            }

        };

    }

    private void getDataFromDb() {
        String path = "users/";
        new DbRead<DataSnapshot>(path, DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {
                extractUserProfile(dataFromDb);
            }
        };
    }

    /*
     * this method will take the Applicant entities and extract the userProfile objects and add them to profiles an arrayList of userProfiles
     */
    private void extractUserProfile(DataSnapshot dataFromDb) {
        final ArrayList<userProfile> profiles = new ArrayList<>();
        applicantProfiles = new ArrayList<>();
        for (DataSnapshot userdata : dataFromDb.getChildren()) {

            if (userdata.child("Profile").getValue(userProfile.class) != null) {
                userProfile up = userdata.child("Profile").getValue(userProfile.class); //create userProfile object and set to the individual userProfile at instance
                up.setUserId(userdata.getKey());   //set the userId for later referencing
                profiles.add(up);   //add the profile to the arraylist
            }

        }
        //loop through the profiles arrayList
        // if the user profile contains an id
        for (userProfile profile : profiles) {

            if (applicantNames.contains(profile.getUserId())) {

                applicantProfiles.add(profile);
            }
        }

        createRecyclerView(applicantProfiles);    //create the recycler view


    }


    @Override
    protected void onResume() {
        super.onResume();
        getApplicantsFromDB();
    }

    /*
     * This method will create the recycler view of user Profiles or Applicants to the job in which have profiles.
     */
    public void createRecyclerView(ArrayList<userProfile> userProfiles) {
        applicantProfiles = userProfiles;

        RecyclerView recyclerView = findViewById(R.id.applicantsRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ApplicantsAdapter(applicantProfiles, postID);
        recyclerView.setAdapter(mAdapter);


    }


}

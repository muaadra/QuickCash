package com.softeng.quickcash;

import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


public class Applicants extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private ArrayList<String> applicantNames;
    private ArrayList<userProfile> applicantProfiles;
    private int emptyListTextViewOriginalHeight = -1; // to store original height of the TextView
    private String postID;
    private String userID;
    private FirebaseStorage fbStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_applicants);

        //create instance of FirebaseStorage
        fbStorage = FirebaseStorage.getInstance();
    }


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
                userProfile profile = userdata.child("Profile").getValue(userProfile.class); //create userProfile object and set to the individual userProfile at instance
                profile.setUserId(userdata.getKey());   //set the userId for later referencing
                profiles.add(profile);   //add the profile to the arraylist
            }

        }

        //loop through the profiles arrayList
        // if the user profile contains an id
        for (userProfile profile : profiles) {

            if (applicantNames.contains(profile.getUserId())) {

                applicantProfiles.add(profile);
            }
        }

        createRecyclerView(applicantProfiles);
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
        //a message shows/expands in the recycleView if it's empty and collapses otherwise
        TextView emptyListTV = (TextView)findViewById(R.id.emptyStatusMyPosts);
        if(emptyListTextViewOriginalHeight == -1){
            emptyListTextViewOriginalHeight = emptyListTV.getHeight();
        }

        applicantProfiles = userProfiles;

        RecyclerView recyclerView = findViewById(R.id.applicantsRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ApplicantsAdapter(applicantProfiles, postID,fbStorage);
        recyclerView.setAdapter(mAdapter);


        if(applicantProfiles != null && applicantProfiles.size() > 0){
            //hide message that says the list is empty
            emptyListTV.setHeight(0);
        }else {
            emptyListTV.setHeight(emptyListTextViewOriginalHeight);
        }

    }


}

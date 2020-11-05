package com.softeng.quickcash;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseStorage fbStorage;

    ArrayList<TaskPost> taskPosts;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MyLocation myLocation;

    public String[] sortBy = {"latest Post", "test1",
            "Test2", "Test3"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create instance of FirebaseStorage
        fbStorage = FirebaseStorage.getInstance();

        spinnerSetup();

        //location setup
        myLocation = new MyLocation(this) {
            @Override
            public void LocationResult(Location location) {
                getDataFromDbAndShowOnUI();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDataFromDbAndShowOnUI();
    }


    private void getDataFromDbAndShowOnUI() {
        final ArrayList<TaskPost> posts = new ArrayList<>();

        //path to database object
        String path = "users/";

        new DbRead<DataSnapshot>(path, DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {
                //loop through all children in path
                for (DataSnapshot userdata : dataFromDb.getChildren()) {
                    String author = userdata.getKey();

                    for (DataSnapshot post : userdata.child("TaskPosts").getChildren()) {
                        TaskPost taskPost = post.getValue(TaskPost.class);
                        if(taskPost != null){
                            taskPost.setAuthor(author);
                            float distance = getDistance(taskPost.getLatLonLocation());
                            if(distance <= 50000){//local is defined as 50km max in this app
                                taskPost.setDistance(getDistance(taskPost.getLatLonLocation()));
                                posts.add(taskPost);
                            }

                        }
                    }

                }

                taskPosts = posts;
                createRecyclerView(taskPosts);
            }
        };
    }



    private float getDistance(String latitudeLongitude){
        Double lat = Double.parseDouble(latitudeLongitude.split(",")[0]);
        Double lon = Double.parseDouble(latitudeLongitude.split(",")[1]);

        LongLatLocation myLoc = new LongLatLocation(lat,lon);
        return myLocation.calcDistanceToLocation(myLoc);
    }


    /**
     * generates the spinner
     */
    public void spinnerSetup() {
        Spinner spinner = (Spinner) findViewById(R.id.sortBySpinner_PostATask);

        // Create an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, sortBy);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }



    /**
     * creates a RecyclerView view for main task posts
     * @param posts list of posts
     */
    public void createRecyclerView(ArrayList<TaskPost> posts) {
        recyclerView = (RecyclerView) findViewById(R.id.TaskPostsList);

        // using a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RVAdapterMainActivity(posts,fbStorage);
        recyclerView.setAdapter(mAdapter);

    }

    /**
     * runs when sigIn/out button is clicked
     */
    public void signInOrOutOnClickButton(View view) {
        if(UserStatusData.isUserSignIn(this)){
            //set user status to signed out
            UserStatusData.setUserSignInToFalse(this);
        }
        //go to next activity
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }


    /**
     * runs when go to profile button is clicked
     */
    public void goToProfileOnClickButton(View view) {
        //go to next activity
        if(UserStatusData.isUserSignIn(this)){
            goToEditProfileActivity();
        }else {
            goToSignInActivity();
        }

    }

    private void goToSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    /**
     * runs when post task button is clicked
     */
    public void gotToMyPostsOnButtonClick(View view) {
        if(!UserStatusData.isUserSignIn(this)){
            goToSignInActivity();
        }else {
            checkIfUserHasAProfile();
        }
    }

    private void checkIfUserHasAProfile(){
        String userId = UserStatusData.getEmail(this).replace(".", ";");
        //path to database object
        String path = "users/"+ userId +"/Profile";

        //read data from database
        DbRead<userProfile> dbRead = new DbRead<userProfile>(path,
                userProfile.class, db) {
            @Override
            public void getReturnedDbData(userProfile dataFromDb) {
                //after data is received from db call checkDbData
                if(dataFromDb!= null && dataFromDb.getfName() != null){
                    goToMyPostsActivity();
                }else {
                    goToEditProfileActivity();
                }
            }
        };

    }
    private void goToMyPostsActivity(){
        Intent intent = new Intent(this, MyPosts.class);
        startActivity(intent);
    }

    private void goToEditProfileActivity(){
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
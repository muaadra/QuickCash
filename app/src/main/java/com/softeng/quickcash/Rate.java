package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class Rate extends AppCompatActivity {
    private int rating;
    private ImageView stars[] = new ImageView[5];
    private String userIDToBeRated;
    private userProfile userToBeRatedProfile;
    private String postID;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        getPassedArgs();
        getUserInfoFromDB();
        findStarViews();
    }

    void getPassedArgs(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        postID = bundle.getString("postID");
        userIDToBeRated = bundle.getString("userIDToBeRated");
    }

    private void findStarViews() {
        stars[0] = findViewById(R.id.r1);
        stars[1] = findViewById(R.id.r2);
        stars[2] = findViewById(R.id.r3);
        stars[3] = findViewById(R.id.r4);
        stars[4] = findViewById(R.id.r5);
    }

    /**
     * runs when a start is clicked
     */
    public void starOnClick(View v){
        rating = Integer.parseInt(v.getTag().toString());
        showStars();
    }
    private void showStars(){
        ((TextView) findViewById(R.id.ratingLabel)).setText(rating + "");

        //reset stars
        for (int i = 0; i < stars.length ; i++) {
            stars[i].setImageResource(R.drawable.rating_star_grey);
        }

        //set new rating
        for (int i = 0; i < rating ; i++) {
            stars[i].setImageResource(R.drawable.rating_star);
        }
    }

    /**
     * runs when "submit rating" button is clicked
     */
    public void submitRating(View v){
        //delete application from employer side
        String path = "users/"+ userIDToBeRated + "/Ratings/" +
                postID;

        new DbWrite<Integer>(path,rating,db) {
            @Override
            public void writeResult(Integer userdata) {
                finishActivity();
            }
        };
    }

    private void finishActivity(){
        Toast.makeText(this,"Rating submitted",Toast.LENGTH_SHORT).show();
        finish();
    }

    private void getUserInfoFromDB(){
        //read data from database
        String path = "users/";

        new DbRead<DataSnapshot>(path, DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {
                userProfile profile = dataFromDb.child(userIDToBeRated +"/Profile").getValue(userProfile.class);
                //check if already rated
                Integer prevRating = dataFromDb.child(userIDToBeRated + "/Ratings/" + postID).getValue(Integer.class);
                if(prevRating != null){
                    rating = prevRating;
                    showStars();
                }

                if(profile != null){
                    showOnUI(profile);
                }
            }
        };
    }

    private void showOnUI(userProfile dataFromDb){
        userToBeRatedProfile = dataFromDb;
        ((TextView)findViewById(R.id.userToBeRated)).setText(userToBeRatedProfile.getfName());
    }

    /**
     * runs when author name button is clicked
     */
    public void goToViewProfile(View view) {
        Intent intent = new Intent(this, ViewProfile.class);
        intent.putExtra("userID",userIDToBeRated);
        startActivity(intent);
    }


}
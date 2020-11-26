package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class Rate extends AppCompatActivity {
    int rating;
    ImageView stars[] = new ImageView[1];
    String userIDToBeRated = "m@m;m";
    userProfile userToBeRatedProfile;
    String postID;
    final FirebaseDatabase db = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        getPassedArgs();
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

    }


    public void starOnClick(View v){

        rating = Integer.parseInt(v.getTag().toString());
        ((TextView) findViewById(R.id.ratingLabel)).setText(rating + "");

    }




}
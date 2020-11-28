package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewProfile extends AppCompatActivity {
    final FirebaseDatabase db = FirebaseDatabase.getInstance();

    private ImageView profileImage;
    private FirebaseStorage fbStorage;
    private String profileUserId;
    String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        getApplicationData();

        //create instance of FirebaseStorage
        fbStorage = FirebaseStorage.getInstance();

        profileImage = (ImageView)findViewById(R.id.profileImage_VP);

        getProfileData();

    }

    private void getApplicationData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            postId = bundle.getString("postId");
            if(postId != null){
                ((Button)findViewById(R.id.acceptApplicant)).setVisibility(View.VISIBLE);
            }
        }
    }


    private void getProfileData(){
        Bundle bundle = getIntent().getExtras();
        profileUserId = bundle.getString("userID");

        getProfileImage();

        //path to database object
        String path = "users/";

        //read data from database
        new DbRead<DataSnapshot>(path, DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {
                userProfile profile = dataFromDb.child(profileUserId +"/Profile").getValue(userProfile.class);
                //after data is received from db call checkDbData
                if(profile != null){
                    showPostDataOnUI(profile);
                }
                showRatings(dataFromDb);
            }
        };

    }


    private void showPostDataOnUI(userProfile userProfile){
        if(userProfile == null){
            return;
        }

        ((TextView)findViewById(R.id.profileName)).setText(userProfile.getfName());

        ((TextView)findViewById(R.id.aboutMe_Box)).setText(userProfile.getAboutMe());

    }

    private void showRatings(DataSnapshot dataFromDb){

        ArrayList<Integer> ratingsList = DataSnapShotToArrayList.getArrayList(
                dataFromDb.child(profileUserId + "/Ratings"),Integer.class);

        if(ratingsList.size() > 0){
            int rating = 0;
            for (int i = 0; i < ratingsList.size(); i++) {
                rating += ratingsList.get(i);
            }

            rating = (int)((float)rating/ratingsList.size());
            ((TextView)findViewById(R.id.profileRatings)).setText(rating + "/5");
        }

    }

    /**
     * get image to firebase
     */
    public void getProfileImage() {
        String imagePathAndName = "Images/user_ProfileImages/" + profileUserId;

        DbGetImage dbGetImage = new DbGetImage(fbStorage,imagePathAndName) {
            @Override
            public void imageGetResult(Bitmap dbProfileImage) {
                //set image on screen to image retrieved from db
                if(dbProfileImage != null){
                    profileImage.setImageBitmap(dbProfileImage);
                }else {
                    System.out.println("error getting image");
                }

            }
        };
        dbGetImage.getImage();
    }


    /**
     * runs when Accept Applicant button is pushed
     */
    public void acceptApplicant(View view){
        String myID = UserStatusData.getUserID(this);
        String path = "users/"+ myID +"/TaskPosts/"+ postId +"/assignedEmployee";

        new DbWrite<String>(path, profileUserId,db) {
            @Override
            public void writeResult(String userdata) {
                successPosted();
            }
        };
    }

    private void successPosted(){
        Toast.makeText(this,"Operation was successful",Toast.LENGTH_SHORT).show();
        finish();
    }


}
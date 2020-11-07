package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;

public class ViewProfile extends AppCompatActivity {
    final FirebaseDatabase db = FirebaseDatabase.getInstance();

    private ImageView profileImage;
    private FirebaseStorage fbStorage;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        //create instance of FirebaseStorage
        fbStorage = FirebaseStorage.getInstance();

        profileImage = (ImageView)findViewById(R.id.profileImage_VP);

        getProfileData();
    }


    private void getProfileData(){
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("userID");

        String imagePathAndName = "Images/user_ProfileImages/" + userId;

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

        //path to database object
        String path = "users/"+ userId +"/Profile";

        //read data from database
        new DbRead<userProfile>(path,
                userProfile.class, db) {
            @Override
            public void getReturnedDbData(userProfile dataFromDb) {
                //after data is received from db call checkDbData
                showPostDataOnUI(dataFromDb);
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



}
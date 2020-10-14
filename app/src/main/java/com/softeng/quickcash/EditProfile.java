/**
 * Jonathan Robichaud B00802259
 * QuickCash Group 17 Development
 */
package com.softeng.quickcash;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    final FirebaseDatabase db = FirebaseDatabase.getInstance();

    private Uri imageUri;
    private ImageView profileImage;
    private FirebaseStorage fbStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //create instance of FirebaseStorage
        fbStorage = FirebaseStorage.getInstance();

        profileImage = (ImageView)findViewById(R.id.profileImage_E);

        //get data from db if any
        onStartEditProfile();
    }


    /**
     * This method is called to retrieve the users profile information if previously stored.
     * It will take a datasnapshot and see if the users profile is stored, if yes display said info
     */
    public void onStartEditProfile(){

        String userId = UserStatusData.getEmail(this).replace(".", ";");
        //path to database object
        String path = "users/"+ userId +"/Profile";

        //read data from database
        DbRead<userProfile> dbRead = new DbRead<userProfile>(path,
                userProfile.class, db) {
            @Override
            public void getReturnedDbData(userProfile dataFromDb) {
                //after data is received from db call checkDbData
                checkDbData(dataFromDb);
            }
        };

        getProfileImage();
    }

    /**
     * this method runs after data is received from db
     */
    private void checkDbData(userProfile dataFromDb){
        //if result from db is null, means record does not exist
        if(dataFromDb != null){
            //show data
            ((TextView) findViewById(R.id.aboutMe_EditBox)).setText(dataFromDb.getAboutMe());
            ((TextView) findViewById(R.id.editProfileName)).setText(dataFromDb.getfName());
        }
    }

    /**
     * This method Will process the information if edit profile button is clicked.
     * Specific command implemented in order to update information.
     */
    public void saveProfileChanges(View view){
        writeProfileDataToDb(false);
        uploadProfileImage();

        ((TextView) findViewById(R.id.cancelProfile)).setText("Continue >>");
        ((TextView) findViewById(R.id.cancelProfile)).setTextColor(Color.parseColor("#09b52b"));
    }

    /**
     * This method will delete user profile if delete profile button is clicked
     * Set respective path under Profile to null
     */
    public void deleteProfile(View view){
        writeProfileDataToDb(true);
        //clear text fields
        ((TextView) findViewById(R.id.aboutMe_EditBox)).setText("");
        ((TextView) findViewById(R.id.editProfileName)).setText("");
    }

    /**
     * writes data to db
     * @param deleteProfile set true if you want to delete data
     */
    private void writeProfileDataToDb(boolean deleteProfile){
        String aboutMe = ((TextView) findViewById(R.id.aboutMe_EditBox)).getText().toString();
        String fName = ((TextView) findViewById(R.id.editProfileName)).getText().toString();

        userProfile profile = new userProfile(fName,aboutMe);

        // delete profile if view is null
        if(deleteProfile){
            profile = null;
            deleteImage();
        }

        String email = UserStatusData.getEmail(this);
        String userId = email.replace(".", ";");

        //path where you want to write data to
        String path = "users/"+ userId +"/Profile";

        DbWrite<userProfile> dataDbWrite = new DbWrite<userProfile>(path,profile,db) {
            @Override
            public void writeResult(userProfile userdata) {
                //acknowledge that user profile has been edited
                ((TextView) findViewById(R.id.textViewEditProfileConfirm))
                        .setText(R.string.profileEdited);
            }
        };
    }



    /**
     * when user clicks on profile image to edit, start browsing
     * local images
     */
    public void profileImageOnClick(View view){
        profileImage = (ImageView)view;

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    /**
     * result after user brows images
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();
            setProfileImage();
        }
    }

    /**
     * show image on device locally
     */
    private void setProfileImage() {
        if(imageUri == null){
            getProfileImage();
        }else {
            profileImage.setImageURI(imageUri);
        }

    }

    /**
     * upload image to firebase when clicked on save data
     */
    public void uploadProfileImage(){
        if(imageUri == null){
            return;
        }
        String id = UserStatusData.getEmail(this).replace(".",";");
        String imagePathAndName = "Images/user_ProfileImages/" + id;
        DbUploadImage dbUploadImage = new DbUploadImage(fbStorage,imagePathAndName,imageUri) {
            @Override
            public void uploadResult(boolean success, String uploadMessage) {
                //result whether upload was successful..
                if(success){
                    System.out.println(uploadMessage);
                }
            }
        };
    }

    /**
     * get image to firebase when clicked on save data
     */
    public void getProfileImage() {
        String id = UserStatusData.getEmail(this).replace(".",";");
        String imagePathAndName = "Images/user_ProfileImages/" + id;

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

    private void deleteImage(){
        String id = UserStatusData.getEmail(this).replace(".",";");
        String imagePathAndName = "Images/user_ProfileImages/" + id;

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
        dbGetImage.deleteImage();
        goBack();
        Toast.makeText(this,"Profile deleted",Toast.LENGTH_SHORT).show();
    }

    private void goBack(){
        finish();
    }

    /**
     * runs when a user dismisses create profile
     */
    public void cancelEditProfile(View view) {
        //go to next activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
/**
 * Jonathan Robichaud B00802259
 * QuickCash Group 17 Development
 */
package com.softeng.quickcash;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private static final String TAG = "";
    private DatabaseReference mDatabase;        //Reference to
    String idEmail = "";
    TextView textViewFName;     //Text view for users First name
    TextView textViewAboutMe;   //Text view for users About me section
    DatabaseReference dbProfile;    //Database reference

    private Uri imageUri;
    private ImageView profileImage;
    private FirebaseStorage fbStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //create instance of FirebaseStorage
        fbStorage = FirebaseStorage.getInstance();

        textViewFName = (TextView) findViewById(R.id.editProfileName); // Text view 'full name'
        textViewAboutMe = (TextView) findViewById(R.id.saveChanges); //Text view 'about me'

        Button editProfile = (Button) findViewById(R.id.createProfile);     // button 'create profile'
        Button deleteProfile = (Button) findViewById(R.id.deleteProfile);   //button 'delete profile'

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //initialize database reference to 'users'
        dbProfile = database.getReference("users");

        //If a user is editing their profile, it should preview the stored information
        try {
            onStartEditProfile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfile(idEmail);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //idEMAIL NEEDS TO BE PASSED IN FROM CURRENT SESSION
                // (logged in with account > edit profile ( pass in email logged in )
                editProfile("", textViewFName.getText().toString(),
                        textViewAboutMe.getText().toString());
            }
        });

        profileImage = (ImageView)findViewById(R.id.profileImage_E);
    }


    /**
     * This method is called to retrieve the users profile information if previously stored.
     * It will take a datasnapshot and see if the users profile is stored, if yes display said info
     */
    public void onStartEditProfile() throws IOException {
        final String userEmail = UserStatusData.getEmail(this);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                userProfile userDataFromDb = snapshot.child(userEmail).child("Profile").getValue(userProfile.class); //create profile object and set to according path (if null it is not there)
                if(userDataFromDb!=null) {  //if the datasnapshot does not contain the users profile
                    System.out.println("ABOUT ME : " +userDataFromDb.getAboutMe());
                    ((TextView) findViewById(R.id.saveChanges)).setText(userDataFromDb.getAboutMe());    //setting text views to previously stored information
                    ((TextView) findViewById(R.id.editProfileName)).setText(userDataFromDb.getfName());
                }else{
                    ((TextView) findViewById(R.id.saveChanges)).setText("");
                    ((TextView) findViewById(R.id.editProfileName)).setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "LoadPost:onCancelled", error.toException());
            }
        };
        dbProfile.addValueEventListener(postListener);

        setProfileImage();
    }

    /**
     * This method Will process the information if edit profile button is clicked.
     * Specific command implemented in order to update information.
     */
    public void editProfile(String idEmail, String fName, String aboutMe){
        userProfile profile = new userProfile(fName,aboutMe);   //create newly updated profile object
        Map<String,Object> updates = new HashMap<String,Object>();  //create Map in order to update children

        updates.put("aboutMe",profile.getAboutMe());    //update list of updates ( set to object path in db ) pass retrieved object information
        updates.put("fName",profile.getfName());

        idEmail = UserStatusData.getEmail(this);
        dbProfile.child(idEmail).child("Profile").updateChildren(updates);  //push the updates to the database
        ((TextView) findViewById(R.id.textViewEditProfileConfirm)).setText(R.string.profileEdited); //acknowledge that user profile has been edited

        uploadImage(profileImage);

    }
    /**
     * This method will delete user profile if delete profile button is clicked
     * Set respective path under Profile to null
     * @param idEmail
     */
    public void deleteProfile(String idEmail){
        idEmail = UserStatusData.getEmail(this);
        dbProfile.child(idEmail).child("Profile").setValue(null);   //set the Profile object to null to its respective account email
        ((TextView) findViewById(R.id.textViewDeleteProfileConfirm)).setText(R.string.profileDelete); //acknowledge that user profile has been deleted
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

            try {
                setProfileImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * show image on device locally
     */
    private void setProfileImage() throws IOException {
        System.out.println(">>>>>>>>" + (imageUri == null));
        if(imageUri == null){
            getImage(profileImage);
        }else {
            profileImage.setImageURI(imageUri);
        }

    }

    /**
     * upload image to firebase when clicked on save data
     */
    public void uploadImage(View view){
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
    public void getImage(final View view) throws IOException {
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
/**
 * Jonathan Robichaud B00802259
 * QuickCash Group 17 Development
 */
package com.softeng.quickcash;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    private static final String TAG = "";
    private DatabaseReference mDatabase;        //Reference to
    String idEmail = "";
    TextView textViewFName;     //Text view for users First name
    TextView textViewAboutMe;   //Text view for users About me section
    DatabaseReference dbProfile;    //Database reference


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        textViewFName = (TextView) findViewById(R.id.editProfileName); // Text view 'full name'
        textViewAboutMe = (TextView) findViewById(R.id.editProfileAboutMe); //Text view 'about me'

        Button editProfile = (Button) findViewById(R.id.createProfile);     // button 'create profile'
        Button deleteProfile = (Button) findViewById(R.id.deleteProfile);   //button 'delete profile'

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbProfile = database.getReference("users");     //initialize database reference to 'users'
        onStartEditProfile();   //If a user is editing their profile, it should preview the stored information

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfile(idEmail);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //idEMAIL NEEDS TO BE PASSED IN FROM CURRENT SESSION (logged in with account > edit profile ( pass in email logged in )
                editProfile("", textViewFName.getText().toString(), textViewAboutMe.getText().toString());
            }
        });

    }
    /**
     * This method Will process the information if edit profile button is clicked.
     * Specific command implemented in order to update information.
     * @param idEmail
     * @param fName
     * @param aboutMe
     */
    public void editProfile(String idEmail, String fName, String aboutMe){
        userProfile profile = new userProfile(fName,aboutMe);   //create newly updated profile object
        Map<String,Object> updates = new HashMap<String,Object>();  //create Map in order to update children

        updates.put("aboutMe",profile.getAboutMe());    //update list of updates ( set to object path in db ) pass retrieved object information
        updates.put("fName",profile.getfName());

        idEmail = "email7;com";
        dbProfile.child(idEmail).child("Profile").updateChildren(updates);  //push the updates to the database
        ((TextView) findViewById(R.id.textViewEditProfileConfirm)).setText(R.string.profileEdited); //acknowledge that user profile has been edited

    }



}
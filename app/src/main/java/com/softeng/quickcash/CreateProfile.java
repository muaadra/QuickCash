/**
 * Jonathan Robichaud B00802259
 * QuickCash Group 17 Development
 */
package com.softeng.quickcash;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateProfile extends AppCompatActivity {

    private static final String TAG = "";
    private DatabaseReference mDatabase;        //Reference to
    String idEmail = "";
    TextView textViewFName;     //Text view for users First name
    TextView textViewAboutMe;   //Text view for users About me section
    DatabaseReference dbProfile;    //Database reference
    DatabaseReference dbProfileIsThere;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        textViewFName = (TextView) findViewById(R.id.editProfileName);      //initialize text views
        textViewAboutMe = (TextView) findViewById(R.id.saveChanges);

        Button createProfile = (Button) findViewById(R.id.createProfile);   //initialize buttons
        Button cancelProfile = (Button) findViewById(R.id.cancelProfile);   //initialize buttons

        //initialize database reference with instance
        // (path; users -> email -> User account object and user profile object
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbProfile = database.getReference("users");

        createProfile.setOnClickListener(new View.OnClickListener() {   //button listener to actions by user
            @Override
            public void onClick(View v) {
                //idEMAIL NEEDS TO BE PASSED IN FROM CURRENT SESSION
                // (register account > create profile ( pass in email used to register )

                //send text from text views to method to create user object
                writeNewProfile("", textViewFName.getText().toString(), textViewAboutMe.getText().toString());
            }
        });

        cancelProfile.setOnClickListener(new View.OnClickListener() {   //button listener to actions by user
            @Override
            public void onClick(View v) {
                cancelCreateProfile(v);
            }
        });

    }

    /**
     * This method will take in the arguments in order to create userProfile object
     * args; String+ email(users email(to be retrieved from other methods)) String+ users First Name String+ users About Me
     */
    public void writeNewProfile(String idEmail, String fName, String aboutMe){

        userProfile profile = new userProfile(fName,aboutMe);   //pass information to create profile object
        idEmail = "email7;com"; //THIS NEEDS TO BE PASSED IN (global)
        dbProfile.child(idEmail).child("Profile").setValue(profile);    //store user profile into database using the email as a unique 'child' ID
        ((TextView)findViewById(R.id.textViewProfileConfirm)).setText(R.string.profileCreated); //display label acknowledging profile created

    }

    /**
     * runs when a user dismisses create profile
     */
    public void cancelCreateProfile(View view) {
        //go to next activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
/**
 * Jonathan Robichaud B00802259
 * QuickCash Group 17 Development
 */
package com.softeng.quickcash;
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
        textViewAboutMe = (TextView) findViewById(R.id.editProfileAboutMe);

        Button createProfile = (Button) findViewById(R.id.createProfile);   //initialize buttons

        FirebaseDatabase database = FirebaseDatabase.getInstance();     //initialize database reference with instance (path; users -> email -> User account object and user profile object
        dbProfile = database.getReference("users");

        createProfile.setOnClickListener(new View.OnClickListener() {   //button listener to actions by user
            @Override
            public void onClick(View v) {
                //idEMAIL NEEDS TO BE PASSED IN FROM CURRENT SESSION (register account > create profile ( pass in email used to register )
                writeNewProfile("", textViewFName.getText().toString(), textViewAboutMe.getText().toString());     //send text from text views to method to create user object
            }
        });

    }


}
package com.softeng.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    final FirebaseDatabase db = FirebaseDatabase.getInstance();

    public String[] sortBy = {"latest Post", "test1",
            "Test2", "Test3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
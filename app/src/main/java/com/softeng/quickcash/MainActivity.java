package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if user is signed in
        userSignedInCheck();

    }

    /**
     * checks if this is the first user run
     */
    private void userSignedInCheck(){
       if(!UserStatusData.isUserSignIn(this)){
            //go to next activity
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
       }
    }

    /**
     * runs when sig-out button is clicked
     */
    public void signOutOnClickButton(View view){
        //set user status to signed out
        UserStatusData.setUserSignInToFalse(this);

        //go to next activity
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}
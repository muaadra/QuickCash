package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if this is the first run
        checkUserFirstRun();

    }

    /**
     * checks if this is the first user run
     */
    private void checkUserFirstRun(){
       if(UserStatusData.getUserFirstRunStatus(this)
       && !UserStatusData.isUserSignIn(this)){
           //if it is the first run, then set status of first run to false
           UserStatusData.setUserFirstRun(this,false);

           //go to next activity
           Intent intent = new Intent(this, WelcomeActivity.class);
           startActivity(intent);
       }
    }
}
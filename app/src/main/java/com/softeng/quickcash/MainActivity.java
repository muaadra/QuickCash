package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;

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
       if(UserStatusData.getUserFirstRunStatus(this)){
           //if it is the first run, then set status of first run to false
           /*******  uncomment once MainActivity is ready  *********/
           //UserStatusData.setUserFirstRun(this,false);
           /*********                                        *******/
           NewActivity.goToActivity(this,WelcomeActivity.class);
       }
    }
}
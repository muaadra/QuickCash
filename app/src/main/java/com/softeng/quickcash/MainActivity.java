package com.softeng.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
     * runs when post task button is clicked
     */
    public void gotToMyPostsOnButtonClick(View view) {
        if(!UserStatusData.isUserSignIn(this)){
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, MyPosts.class);
            startActivity(intent);
        }
    }

    /**
     * runs when go to profile button is clicked
     */
    public void goToProfileOnClickButton(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }
}
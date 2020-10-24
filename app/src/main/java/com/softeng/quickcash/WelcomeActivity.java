package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        userSignedInCheckAndGoToMain();
    }


    /**
     * go to main activity if signed in
     */
    private void userSignedInCheckAndGoToMain() {
        if (UserStatusData.isUserSignIn(this)) {
            //go to next activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    public void signIn(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void continueAsGuest(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
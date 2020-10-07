package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    /**
     * this method is called when user clicks on the registration button
     */
    public void createAccountOnButtonClick(View view) {
        //reset warning labels/text views
        clearWarningLabels();

        InputValidation validation = new InputValidation();

        String email = ((EditText) findViewById(R.id.email_EditText))
                .getText().toString();

        //validate email input
        if(!validation.isEmailValid(email)){
            ((TextView) findViewById(R.id.emailStatus))
                    .setText(R.string.InvalidEmail);
        }

    }

    private void clearWarningLabels(){
        ((TextView) findViewById(R.id.emailStatus)).setText("");
        ((TextView) findViewById(R.id.passwordStatus)).setText("");
    }
}
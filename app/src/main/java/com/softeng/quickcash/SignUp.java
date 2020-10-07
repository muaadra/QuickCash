package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
/**
 * This class is the sign up activity; it takes email and password inputs from UI
 * then validates the input
 *
 * @author Muaad Alrawhani (B00538563)
 */

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

        String email = ((EditText) findViewById(R.id.email_EditText))
                .getText().toString();

        String password = ((EditText) findViewById(R.id.password_EditText))
                .getText().toString();

        //validate inputs
        InputValidation validation = new InputValidation();

        //validate email input
        if(!validation.isEmailValid(email)){
            ((TextView) findViewById(R.id.emailStatus))
                    .setText(R.string.InvalidEmail);
        }

        //validate password input
        if(!validation.isPasswordValid(password)){
            ((TextView) findViewById(R.id.passwordStatus))
                    .setText(R.string.InvalidPassword);
        }
    }

    private void clearWarningLabels(){
        ((TextView) findViewById(R.id.emailStatus)).setText("");
        ((TextView) findViewById(R.id.passwordStatus)).setText("");
    }
}
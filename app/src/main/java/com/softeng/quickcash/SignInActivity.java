package com.softeng.quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class is the sign in activity; it takes email and password inputs from UI
 * then validates the input
 *
 * @author Hana Park (B00875008)
 */

public class SignInActivity extends AppCompatActivity {
    EditText email, password;

    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference;
    UserSignUpData userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = (EditText) findViewById(R.id.input_email);
                password = (EditText) findViewById(R.id.input_password);
                userData = new UserSignUpData(email.getText().toString(),password.getText().toString());
                login_check(db, userData);
            }
        });
    }

    /**
     * clear err message
     */
    private void clearWarningLabels() {
        ((TextView) findViewById(R.id.err_msg_email)).setText("");
        ((TextView) findViewById(R.id.err_msg_pw)).setText("");
        ((TextView) findViewById(R.id.msg_confirm)).setText("");
    }

    /**
     * this method is called when user clicks on the login button
     */
    public void login_check(FirebaseDatabase db, UserSignUpData userData) {
        clearWarningLabels();
        //replace "." with ";" in user email, as for simplicity the email is used
        //as the user id, and path cannot contain "."
        String userId = userData.getEmail().replace(".", ";");
        //path to database object
        String path = "users/"+ userId +"/SignUpInfo";

        //read data from database
        DbRead<UserSignUpData> dbRead = new DbRead<UserSignUpData>(path,
                UserSignUpData.class, db) {
            @Override
            public void getReturnedDbData(UserSignUpData dataFromDb) {
                //after data is received from db call checkDbData
                checkDbData(dataFromDb);
            }
        };
    }

    /**
     * this method runs after data is received from db
     */
    private void checkDbData(UserSignUpData dataFromDb){
        try {
            if (userData.getPassword().equals(dataFromDb.getPassword())) {
                Intent intent = new Intent(this, ViewMasterTaskList.class);
                startActivity(intent);
                UserSignUpData signUpData = new UserSignUpData(userData.getEmail(),"password");
                UserStatusData.setUserSignInToTrue(this,signUpData);

            } else {
                ((TextView) findViewById(R.id.err_msg_pw))
                        .setText("Incorrect username and/or password.");
            }

        } catch (Exception e) {
            ((TextView) findViewById(R.id.err_msg_email))
                    .setText("User not found.");
        }
    }
}
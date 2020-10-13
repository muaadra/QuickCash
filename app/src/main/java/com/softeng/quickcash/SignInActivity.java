package com.softeng.quickcash;

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

    private void clearWarningLabels() {
        ((TextView) findViewById(R.id.err_msg_email)).setText("");
        ((TextView) findViewById(R.id.err_msg_pw)).setText("");
        ((TextView) findViewById(R.id.msg_confirm)).setText("");
    }

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
                ((TextView) findViewById(R.id.msg_confirm))
                        .setText("Login successful");
            } else {
                ((TextView) findViewById(R.id.err_msg_pw))
                        .setText("The password does not match.");
            }

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
            builder.setTitle("User not found.");
            builder.setMessage("User not found.");
            builder.setCancelable(true);
            builder.create().show();
        }
    }
}
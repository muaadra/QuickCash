package com.softeng.quickcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This class is the sign up activity; it takes email and password inputs from UI
 * then validates the input
 *
 * @author Muaad Alrawhani (B00538563)
 */

public class SignUpActivity extends AppCompatActivity {
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference;
    UserSignUpData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //get instance of DatabaseReference
        dbReference = FirebaseDatabase.getInstance().getReference();
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


        //create user sign-up data object
        userData = new UserSignUpData(email,password);

        if(isRegistrationInputValid(email,password)){
            addNewUserToDb();
        }
    }

    /**
     * checks if username and email inputs are valid
     * @return false if any input is invalid
     */
    private boolean isRegistrationInputValid(String email, String password) {
        //validate inputs
        InputValidation validation = new InputValidation();

        //if any input is invalid this will false
        //this is done this way (rather than return immediately if false),
        //in order to display 2 notification messages at the same time, one for
        //username invalid input and email invalid input
        boolean validInput = true;

        //validate username input
        if(!validation.isPasswordValid(password)){
            ((TextView) findViewById(R.id.passwordStatus))
                    .setText(R.string.InvalidPassword);
            validInput = false;
        }

        //validate email input
        if(!validation.isEmailValid(email)){
            ((TextView) findViewById(R.id.emailStatus))
                    .setText(R.string.InvalidEmail);
            validInput = false;
        }

        return validInput;
    }

    /**
     * send data to fireBase database and add new user if user doesn't exist
     * in database
     */
    private void addNewUserToDb(){
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
        //if result from db is null, means record does not exist
        if(dataFromDb == null){
            //user is not in db, write new user to db
            writeUserDataToDB();
        }else {
            ((TextView) findViewById(R.id.signUpStatus))
                    .setText(R.string.UserAlreadyExists);
        }
    }

    /**
     * this method writes the data to database
     */
    private void writeUserDataToDB() {
        //replace "." with ";" in user email, as for simplicity the email is used
        //as the user id, and path cannot contain "."
        String userId = userData.getEmail().replace(".", ";");

        //path where you want to write data to
        String path = "users/"+ userId +"/SignUpInfo";

        DbWrite<UserSignUpData> dataDbWrite = new DbWrite<UserSignUpData>(path,userData,db) {
            @Override
            public void writeResult(UserSignUpData userdata) {
                updateUIWithSignUpStatus(userdata);
            }
        };

    }

    /**
     *  let user know if sign-up was successful
     */
    private void updateUIWithSignUpStatus(UserSignUpData userdata){
        if(userdata != null){
            ((TextView) findViewById(R.id.signUpStatus))
                    .setText(R.string.SuccessfulSignUp);

            // go to main activity
            Intent intent = new Intent(this, CreateProfileActivity.class);
            startActivity(intent);

            //save user login status
            UserStatusData.setUserSignInToTrue(this,userdata);
        }else {
            ((TextView) findViewById(R.id.signUpStatus))
                    .setText(R.string.UnsuccessfulSignUp);
        }
    }

    private void clearWarningLabels(){
        ((TextView) findViewById(R.id.emailStatus)).setText("");
        ((TextView) findViewById(R.id.passwordStatus)).setText("");
    }
}
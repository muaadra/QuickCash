package com.softeng.quickcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    private DatabaseReference dbReference;

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
        UserSignUpData userData = new UserSignUpData(email,password);

        if(isRegistrationInputValid(email,password)){
            addNewUserToDb(userData);
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
    UserSignUpData userData;
    private void addNewUserToDb(UserSignUpData newUserData){
        userData = newUserData;

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userId = userData.getEmail().replace(".", ";");

                //get user sign up info as a UserSignUpData object
                UserSignUpData userDataFromDb = dataSnapshot.child("users")
                        .child(userId).child("SignUpInfo")
                        .getValue(UserSignUpData.class);

                if(userDataFromDb == null){
                    //user is not in db, write new user to db
                    writeUserDataToDB(userData);
                }else {
                    ((TextView) findViewById(R.id.signUpStatus))
                            .setText(R.string.UserAlreadyExists);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // error
                System.out.println("onCancelled error:" + error.getMessage());
            }

        };

        dbReference.addListenerForSingleValueEvent(postListener);
    }

    /**
     * this method writes the data to database
     */
    private void writeUserDataToDB(final UserSignUpData userData) {
        //using email address as user id and replacing "." with ";"
        final String id = userData.getEmail().replace(".", ";");

        //create a success listener to report back if db was committed successful
        OnSuccessListener<Void> onSuccessListener = new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                updateUIWithSignUpStatus(userData);
            }
        };

        //create a failure listener to report back if db was not committed successful
        OnFailureListener onFailureListener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateUIWithSignUpStatus(null);
            }
        };

        //write data to database
        dbReference.child("users").child(id).child("SignUpInfo").setValue(userData)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);

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
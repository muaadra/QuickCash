package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

public class ViewPost extends AppCompatActivity {

    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private String postID;
    private String authorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        getTaskPostFromDP();
    }


    private void getTaskPostFromDP(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            postID = bundle.getString("postID");
            authorID = bundle.getString("authorID");
        }

        //path to database object
        String path = "users/"+ authorID ;


        //read data from database
        new DbRead<DataSnapshot>(path,
                DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {
                //after data is received from db call checkDbData
                showPostDataOnUI(dataFromDb.child("/TaskPosts/" + postID).getValue(TaskPost.class),
                        dataFromDb);
            }
        };

    }

    private void showPostDataOnUI(TaskPost task, DataSnapshot dataSnapshot){
        if(task == null || dataSnapshot == null){
            return;
        }

        ((TextView)findViewById(R.id.taskTile)).setText(task.getTaskTitle().toUpperCase());

        ((TextView)findViewById(R.id.TaskLocation)).setText(
                getAddressFromLonLatOnUI(task.getLatLonLocation()));

        String date = DateFormat.getDateInstance().format(task.getExpectedDate().getTime());
        ((TextView)findViewById(R.id.expectedDateTView)).setText(date);

        ((TextView)findViewById(R.id.taskDescEditTxt)).setText(task.getTaskDescription());

        ((TextView)findViewById(R.id.payPerHourTV)).setText(task.getTaskCost() + "");

        ((TextView)findViewById(R.id.authorTV)).setText(dataSnapshot.child("/Profile/fName").getValue(String.class));
    }


    private String getAddressFromLonLatOnUI(String latLon){
        double lat = Double.parseDouble(latLon.split(",")[0]);
        double lon = Double.parseDouble(latLon.split(",")[1]);

        try {
            //get the address
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = geocoder.getFromLocation(lat,lon,1);
            String address = addresses.get(0).getAddressLine(0);

            return address;
        } catch (IOException e) {
            return "unable to get address";
        }
    }


    public void applyToTask(View v){
        String email = UserStatusData.getEmail(this);
        String userId = email.replace(".", ";");
        //path to database object
        String path = "users/"+ authorID +"/TaskPosts/" + postID + "/Applicants/" + userId;

        // 1 indicates new applicant (for notification purposes)
        new DbWrite<Integer>(path,1,db) {
            @Override
            public void writeResult(Integer userdata) {
                if(userdata != null){
                    goToMainAndShowStatus("application was successful");
                }else {
                    goToMainAndShowStatus("couldn't send application");
                }

            }
        };
    }

    private void goToMainAndShowStatus(String status) {
        Toast.makeText(this,status,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * runs when author name button is clicked
     */
    public void goToViewProfile(View view) {
        Intent intent = new Intent(this, ViewProfile.class);
        intent.putExtra("userID",authorID);
        startActivity(intent);
    }

}
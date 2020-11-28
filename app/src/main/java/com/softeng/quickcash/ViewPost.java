package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ViewPost extends AppCompatActivity {

    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private String postID;
    private String authorID;
    DataSnapshot root;
    boolean isRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getTaskPostFromDP();
    }

    private void getTaskPostFromDP(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null || bundle.getString("postID") ==null){
            return;
        }
        postID = bundle.getString("postID");
        authorID = bundle.getString("authorID");

        //path to database object
        String path = "users";


        //read data from database
        new DbRead<DataSnapshot>(path, DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {
                root = dataFromDb;
                //after data is received from db call checkDbData
                showPostDataOnUI(dataFromDb.child(authorID + "/TaskPosts/" + postID).getValue(TaskPost.class),
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

        ((TextView)findViewById(R.id.authorTV)).setText(dataSnapshot.child(task.getAuthor() + "/Profile/fName").getValue(String.class));


        if(task.isCompleted() && task.getAssignedEmployee().equals(UserStatusData.getUserID(this))){
            //employee is viewing the post
            //rate the employer
            showRating(task, task.getAuthor());
            isRating = true;
        }else if(task.isCompleted() && task.getAssignedEmployee().equals(task.getAuthor())){
            //employer is viewing the post
            //rate the employee
            showRating(task, task.getAssignedEmployee());
            isRating = true;
        }else if(haveIAppliedToThisTask(task)){
            ((Button)findViewById(R.id.applyToTask)).setText("Cancel My Application");
        }
    }

    String userIDToBeRated;
    private void showRating(TaskPost task, String targetUser) {
        userIDToBeRated = targetUser;
        ((TextView) findViewById(R.id.taskTile)).setText("TASK COMPLETED");
        ((TextView) findViewById(R.id.taskTile)).setTextColor(Color.RED);
        ((TextView) findViewById(R.id.textView10)).setText(" CAD");
        ((TextView) findViewById(R.id.payPerHourTV)).setText(task.getTotalPayed() + "");
        ((TextView) findViewById(R.id.textView8)).setText("Total Paid");
        ((Button)findViewById(R.id.applyToTask)).setText("Rate");
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

    /**
     * runs when apply button is clicked
     */
    public void applyToTask(View v){
        if(isRating){
            gotToRateActivity();
            return;
        }

        if(((Button)findViewById(R.id.applyToTask)).getText()
                .toString().equals("Cancel My Application")){
            cancelMyApplication();
            return;
        }

        String email = UserStatusData.getEmail(this);
        if(email.equals("")){
            return;
        }

        String userId = email.replace(".", ";");
        //path to database object
        String path = "users/"+ authorID +"/TaskPosts/" + postID + "/Applicants/" + userId;

        new DbWrite<Integer>(path,1,db) {
            @Override
            public void writeResult(Integer userdata) {
                if(userdata != null){
                    goToMyApplicationsAndShowStatus("application was successful");
                }else {
                    goToMyApplicationsAndShowStatus("couldn't send application");
                }

            }
        };

        addToMyApplications();
    }

    private void gotToRateActivity() {
        Intent intent = new Intent(this, Rate.class);
        intent.putExtra("userIDToBeRated", userIDToBeRated);
        intent.putExtra("postID",postID);
        startActivity(intent);
    }

    private void cancelMyApplication(){

        //delete application from employer side
        String path = "users/"+ authorID +"/TaskPosts/" + postID + "/Applicants/" +
                UserStatusData.getUserID(this);

        new DbWrite<Object>(path,null,db) {
            @Override
            public void writeResult(Object userdata) {

            }
        };

        //delete application from my side
        path = "users/"+ UserStatusData.getUserID(this) + "/MyApplications/"
                + postID;

        new DbWrite<Object>(path,null,db) {
            @Override
            public void writeResult(Object userdata) {
                endActivityWithToast("Application canceled");
            }
        };
    }

    private void endActivityWithToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        finish();
    }


    private void addToMyApplications() {
        String email = UserStatusData.getEmail(this);
        if(email.equals("")){
            return;
        }

        String userId = email.replace(".", ";");
        //path to database object
        String path = "users/"+ userId +"/MyApplications/" + postID;
        TaskApplication application = new TaskApplication(postID, authorID, (new Date()).getTime());

        new DbWrite<TaskApplication>(path,application,db) {
            @Override
            public void writeResult(TaskApplication userdata) {}
        };
    }

    private void goToMyApplicationsAndShowStatus(String status) {
        Toast.makeText(this,status,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MyTasksApplications.class);
        finish();
        startActivity(intent);
    }

    private boolean haveIAppliedToThisTask(TaskPost task){

        DataSnapshot applicationsSnapShot =  root.child(UserStatusData.getUserID(this)
                + "/MyApplications/");
        if(applicationsSnapShot.getValue() == null){
            return false ;
        }
        List<TaskApplication> applicationsList = DataSnapShotToArrayList.getArrayList(applicationsSnapShot,
                TaskApplication.class);

        for (TaskApplication application : applicationsList) {
            if(application.getTaskId().equals(task.getPostId())){
                return true;
            }
        }

        return false;
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
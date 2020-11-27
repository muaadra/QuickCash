package com.softeng.quickcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PostATaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    private MyLocation myLocation;
    final FirebaseDatabase db = FirebaseDatabase.getInstance();

    private String postID;
    private String newPostID;
    TaskPost taskPostFromDB;
    TaskPost writtenPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_a_task);

        spinnerSetup();

        showDataOnUI();

    }

    private void showDataOnUI() {
        //check if this is an existing task
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            //existing task, show data from db
            postID = bundle.getString("postID");
            if(postID != null){
                retrieveDataFromDbAndDisplay(postID);
                showApplicantsButton();
            }


        }else {
            //new task post
            getLocation();


            ((LinearLayout)findViewById(R.id.deleteButtonParent)).getLayoutParams().width = 0;
        }
    }

    private void showApplicantsButton() {
        ((Button) findViewById(R.id.applicantsButton)).setVisibility(View.VISIBLE);
    }

    private void retrieveDataFromDbAndDisplay(String postId) {

        String userId = UserStatusData.getEmail(this).replace(".", ";");
        String dBPath = "users/"+ userId +"/TaskPosts/" + postId ;

        new DbRead<TaskPost>(dBPath,TaskPost.class, db) {
            @Override
            public void getReturnedDbData(TaskPost dataFromDb) {
                //after data is received from db call checkDbData
                showDbDataOnUi(dataFromDb);
                taskPostFromDB = dataFromDb;
            }
        };
    }

    private void showDbDataOnUi(TaskPost dataFromDb){
        List<String> tempTypes = Arrays.asList(TaskTypes.getTaskTypes());
        int spinnerIndex = tempTypes.indexOf(dataFromDb.getTaskTitle());

        ((Spinner) findViewById(R.id.tasksTypeSpinner_PostATask)).setSelection(spinnerIndex);
        ((TextView) findViewById(R.id.taskDescEditTxt)).setText(dataFromDb.getTaskDescription());
        ((TextView) findViewById(R.id.costEditTxt)).setText(dataFromDb.getTaskCost() + "");
        expectedDate = Calendar.getInstance();
        expectedDate.setTime(dataFromDb.getExpectedDate());
        String date = DateFormat.getDateInstance().format(expectedDate.getTime());
        ((Button)findViewById(R.id.datePickerPostAtask)).setText(date);


        String address = latLongStringToAddress(dataFromDb);

        ((TextView)findViewById(R.id.GPSLocation)).setText(address);

        if(dataFromDb.getAssignedEmployee() != null
                && !dataFromDb.getAssignedEmployee().equals("")){
            ((Button) findViewById(R.id.payEmployee)).setVisibility(View.VISIBLE);
        }

        if(dataFromDb.isPostDeleted()){
            ((Button) findViewById(R.id.applyToTask)).setWidth(0);
            ((LinearLayout)findViewById(R.id.deleteButtonParent)).getLayoutParams().width = 0;
            ((LinearLayout)findViewById(R.id.postButtonParent)).getLayoutParams().width = 0;
            ((TextView) findViewById(R.id.postATaskStatus)).setText("can't post, view only");

        }
    }

    private String latLongStringToAddress(TaskPost dataFromDb) {
        //get the address from db
        List<Address> addresses = null;
        try {
            Geocoder geocoder = new Geocoder(this);
            Double lat = Double.parseDouble(dataFromDb.getLatLonLocation().split(",")[0]);
            Double lon = Double.parseDouble(dataFromDb.getLatLonLocation().split(",")[1]);
            addresses = geocoder.getFromLocation(lat, lon,1);
            myLocation = new MyLocation(null) {
                @Override
                public void LocationResult(Location location) {
                }
            };
            LongLatLocation latLocation = new LongLatLocation(lat,lon);
            myLocation.setLastLocation(latLocation);
        } catch (IOException e) {
            getLocation();
            return "";
        }

        return addresses.get(0).getAddressLine(0);
    }


    private void getLocation(){
        myLocation = new MyLocation(this) {
            @Override
            public void LocationResult(Location location) {
                showMyLocationOnUI(location);
            }
        };
    }

    private void showMyLocationOnUI(Location location){
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        try {
            //get the address
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = geocoder.getFromLocation(lat,lon,1);
            String address = addresses.get(0).getAddressLine(0);

            ((TextView)findViewById(R.id.GPSLocation)).setText(address);

        } catch (IOException e) {
            ((TextView)findViewById(R.id.GPSLocation)).setText("unable to get address");
            e.printStackTrace();
        }

    }

    /**
     * runs when calendar button is clicked
     */
    public void openCalenderOnClickButton(View view) {
        DatePicker datePicker = new DatePicker();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }

    /**
     * check if a sting is empty
     */
    private boolean isStringEmpty(String text){
        if(text == null || text.equals("")){
            return true;
        }
        return false;
    }

    /**
     * generates the spinner
     */
    public void spinnerSetup() {
        Spinner spinner = (Spinner) findViewById(R.id.tasksTypeSpinner_PostATask);

        // Create an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, TaskTypes.getTaskTypes());

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    Calendar expectedDate;

    /**
     * runs when a user cancels posting a task
     */
    public void deletePostATaskOnButtonClick(View view){
        taskPostFromDB.setPostDeleted(true);
        writeTaskToDB(taskPostFromDB);
    }

    /**
     * runs when a user clicks on the post button
     */
    public void postATaskOnButtonClick(View view){
        if(!areRequiredFieldsProvidedAndShowStatus()){
            return;
        }

        String userId = UserStatusData.getEmail(this).replace(".", ";");
        //path to database object
        String path = "users/"+ userId +"/TaskPosts";

        if(postID == null){// new item
            // Get a unique ID generated by a push()
            String postId =  db.getReference(path).push().getKey();
            newPostID = postId;
            writeTaskToDB(createTaskFromUI(postId));
        }else {
            //updating existing item
            writeTaskToDB(createTaskFromUI(postID));
        }

    }

    private boolean areRequiredFieldsProvidedAndShowStatus(){
        //getting value from ui
        String taskTitle =  ((Spinner) findViewById(R.id.tasksTypeSpinner_PostATask))
                .getSelectedItem().toString();
        String description =  ((TextView) findViewById(R.id.taskDescEditTxt)).getText().toString();
        String cost =  ((TextView) findViewById(R.id.costEditTxt)).getText().toString();

        if(taskTitle.equals(getResources().getString(R.string.selectATask)))
        {
            ((TextView)findViewById(R.id.postATaskStatus)).setText(R.string.selectATaskMissing);
            return false;

        }else if(description.equals(""))
        {
            ((TextView)findViewById(R.id.postATaskStatus)).setText(R.string.missingDescPostATask);
            return false;
        }else if(expectedDate == null)
        {
            ((TextView)findViewById(R.id.postATaskStatus)).setText(R.string.ExpectedDateError);
            return false;
        }else if(cost.equals(""))
        {
            ((TextView)findViewById(R.id.postATaskStatus)).setText(R.string.costMissingPostATask);
            return false;
        }

        return true;
    }

    private TaskPost createTaskFromUI(String postId){
        //getting values from ui
        String taskTitle =  ((Spinner) findViewById(R.id.tasksTypeSpinner_PostATask))
                .getSelectedItem().toString();
        String description =  ((TextView) findViewById(R.id.taskDescEditTxt)).getText().toString();
        Date date = expectedDate.getTime();
        float cost =  Float.parseFloat(((TextView) findViewById(R.id.costEditTxt)).getText().toString());
        String latLonLocation = myLocation.getLastLocation().getLatitude()+ "," +
                myLocation.getLastLocation().getLongitude();


        String userId = UserStatusData.getEmail(this).replace(".", ";");

        //creating new task object
        return new TaskPost(userId, postId, taskTitle, description, cost,false,
                "",new Date(),date, latLonLocation);
    }


    private void writeTaskToDB(TaskPost taskPost){
        //path where you want to write data to
        String userId = UserStatusData.getEmail(this).replace(".", ";");
        String path =  "users/"+ userId +"/TaskPosts/"  ;


        path = path + taskPost.getPostId();
        writtenPost = taskPost;
        //writing to db
        new DbWrite<TaskPost>(path,taskPost,db) {
            @Override
            public void writeResult(TaskPost userdata) {
                //task successful posted
                if(newPostID != null){
                    notifySubscribedEmployeesOfPost();
                }else {
                    finishActivity();
                }

            }
        };
    }

    public void goToApplicantsList(View view) {
        Intent intent = new Intent(this, Applicants.class);
        intent.putExtra("postID", postID);
        startActivity(intent);
    }

    public void goToPayEmployee(View view) {

    }

    public void taskCompleted(View v){
        if(postID == null){
            return;
        }
        TaskPost taskPost = createTaskFromUI(postID);
        taskPost.setCompleted(true);
        writeTaskToDB(taskPost);
    }

    private void notifySubscribedEmployeesOfPost(){
        String dBPath = "users/" ;

        new DbRead<DataSnapshot>(dBPath,DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {

                getSubScribedUsers(dataFromDb);
            }
        };
    }

    private void getSubScribedUsers(DataSnapshot dataFromDb) {
        ArrayList<String> subscribedUsers = new ArrayList<>();
        String myUserID = UserStatusData.getUserID(this);

        //get task index
        int taskIndex = 0;
        for (; taskIndex < TaskTypes.getTaskTypes().length ; taskIndex++) {
            if(TaskTypes.getTaskTypes()[taskIndex].equals(writtenPost.getTaskTitle())){
                break;
            }
        }

        //loop through all children in path
        for (DataSnapshot user : dataFromDb.getChildren()) {
            ArrayList<Boolean> tasksIndexes =
                    DataSnapShotToArrayList.getArrayList(user.child("TaskSubscriptions"),Boolean.class);
            if(tasksIndexes.size() > 0 && !myUserID.equals(user.getKey())){
                if(tasksIndexes.get(taskIndex)){
                    subscribedUsers.add(user.getKey());
                }
            }
        }

        if(subscribedUsers.size() > 0){
            sendNotifications(subscribedUsers);
        }else {
            finishActivity();
        }
    }

    private void sendNotifications(ArrayList<String> subscribedUsers) {
        //path to database object


        //add new notification
        for (int i = 0; i < subscribedUsers.size(); i++) {
            //raise new notification flag
            String path = "users/"+ subscribedUsers.get(i) +"/Notifications/NewTasksNotification";

            new DbWrite<Integer>(path,1,db) {
                @Override
                public void writeResult(Integer userdata) {
                }
            };

            path = "users/"+ subscribedUsers.get(i) +"/Notifications/NewTasks/"
                    + newPostID;

            new DbWrite<TaskNotification>(path,new TaskNotification(newPostID, true),db) {
                @Override
                public void writeResult(TaskNotification userdata) {
                    finishActivity();
                }
            };
        }
    }

    private void finishActivity(){
        Toast.makeText(this,"Task Posted",Toast.LENGTH_SHORT).show();
        finish();
    }



    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        expectedDate = Calendar.getInstance();
        expectedDate.set(expectedDate.YEAR,year);
        expectedDate.set(expectedDate.MONTH,month);
        expectedDate.set(expectedDate.DAY_OF_MONTH,dayOfMonth);

        String date = DateFormat.getDateInstance().format(expectedDate.getTime());
        ((Button)findViewById(R.id.datePickerPostAtask)).setText(date);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 99 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getLocation();
        }else {
            Toast.makeText(this,"This app requires permissions to get Location data"
                    ,Toast.LENGTH_SHORT).show();
        }
    }
}
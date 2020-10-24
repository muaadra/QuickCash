package com.softeng.quickcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    public String[] taskTypes = {"Select A Category"
            , "lawn mowing", "dog walking","baby sitting", "computer repairs"};

    MyLocation myLocation;
    final FirebaseDatabase db = FirebaseDatabase.getInstance();

    private String postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_a_task);

        String selectACategory = getResources().getString(R.string.selectATask);
        taskTypes[0] = selectACategory;


        spinnerSetup();

        //opening an existing task
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            postID = bundle.getString("postID");
            if(postID != null){
                retrieveDataFromDbAndDisplay(postID);
            }
            System.out.println(postID);
        }else {
            getLocation();
        }

    }

    private void retrieveDataFromDbAndDisplay(String postId) {

        String userId = UserStatusData.getEmail(this).replace(".", ";");
        String dBPath = "users/"+ userId +"/TaskPosts/" + postId ;

         new DbRead<TaskPost>(dBPath,TaskPost.class, db) {
            @Override
            public void getReturnedDbData(TaskPost dataFromDb) {
                //after data is received from db call checkDbData
                showDbDataOnUi(dataFromDb);
            }
        };
    }

    private void showDbDataOnUi(TaskPost dataFromDb){
        List<String> tempTypes = Arrays.asList(taskTypes);
       int spinnerIndex = tempTypes.indexOf(dataFromDb.getTaskTitle());

        ((Spinner) findViewById(R.id.tasksTypeSpinner_PostATask)).setSelection(spinnerIndex);
        ((TextView) findViewById(R.id.taskDescEditTxt)).setText(dataFromDb.getTaskDescription());
        ((TextView) findViewById(R.id.costEditTxt)).setText(dataFromDb.getTaskCost());
        expectedDate = Calendar.getInstance();
        expectedDate.setTime(dataFromDb.getExpectedDate());
        String date = DateFormat.getDateInstance().format(expectedDate.getTime());
        ((Button)findViewById(R.id.datePickerPostAtask)).setText(date);


        String address = latLongStringToAddress(dataFromDb);

        ((TextView)findViewById(R.id.GPSLocation)).setText(address);
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
    public boolean isStringEmpty(String text){
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
                (this, android.R.layout.simple_spinner_item, taskTypes);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    Calendar expectedDate;

    /**
     * runs when a user cancels posting a task
     */
    public void cancelPostATaskOnButtonClick(View view){
        finish();
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
            writeTaskToDB(postId);
        }else {
            //updating existing item
            writeTaskToDB(postID);
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

    private void writeTaskToDB(String postId){
        //getting values from ui
        String taskTitle =  ((Spinner) findViewById(R.id.tasksTypeSpinner_PostATask))
                .getSelectedItem().toString();
        String description =  ((TextView) findViewById(R.id.taskDescEditTxt)).getText().toString();
        Date date = expectedDate.getTime();
        String cost =  ((TextView) findViewById(R.id.costEditTxt)).getText().toString();
        String latLonLocation = myLocation.getLastLocation().getLatitude()+ "," +
                myLocation.getLastLocation().getLongitude();

        //path where you want to write data to
        String userId = UserStatusData.getEmail(this).replace(".", ";");
        String path =  "users/"+ userId +"/TaskPosts/"  ;


        path = path + postId;

        //creating new task object
        TaskPost taskPost = new TaskPost(postId, taskTitle,description,cost,false,false
                ,date, latLonLocation);

        //writing to db
        new DbWrite<TaskPost>(path,taskPost,db) {
            @Override
            public void writeResult(TaskPost userdata) {
                //task successful posted
                successPosted();
            }
        };
    }

    private void successPosted(){
        Toast.makeText(this,"Task successful posted!",Toast.LENGTH_SHORT).show();
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
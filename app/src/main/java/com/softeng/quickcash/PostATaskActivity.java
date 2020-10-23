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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PostATaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    MyLocation myLocation;

    public String[] taskTypes = {"Select A Category"
            , "Task1", "Task2","Task3","Task4"};
    final FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_a_task);

        String selectACategory = getResources().getString(R.string.selectATask);
        taskTypes[0] = selectACategory;

        getLocation();
        spinnerSetup();
    }


    private void getLocation(){
        myLocation = new MyLocation(this) {
            @Override
            public void LocationResult(Location location) {
                showMyLocationOnUI(location);
            }
        };
    }

    void showMyLocationOnUI(Location location){
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        Geocoder geocoder = new Geocoder(this);

        try {
            //get the address
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
        DialogFragment datePicker = new DatePicker();
        datePicker.show(getSupportFragmentManager(),"date picker");
    }

    public boolean isStringEmpty(String text){
        if(text == null || text.equals("")){
            return true;
        }
        return false;
    }

    public void spinnerSetup() {
        Spinner spinner = (Spinner) findViewById(R.id.tasksTypeSpinner_PostATask);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, taskTypes);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    Calendar expectedDate;
    public void postATaskOnButtonClick(View view){
        if(!areRequiredFieldsProvidedAndShowStatus()){
            return;
        }

        String userId = UserStatusData.getEmail(this).replace(".", ";");
        //path to database object
        String path = "users/"+ userId +"/TaskPosts";

        writeTaskToDB();
    }

    private boolean areRequiredFieldsProvidedAndShowStatus(){
        String taskTitle =  ((Spinner) findViewById(R.id.tasksTypeSpinner_PostATask))
                .getSelectedItem().toString();
        String description =  ((TextView) findViewById(R.id.taskDescEditTxt)).getText().toString();
        String cost =  ((TextView) findViewById(R.id.costEditTxt)).getText().toString();

        System.out.println(cost);

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

    private void writeTaskToDB(){
        String taskTitle =  ((Spinner) findViewById(R.id.tasksTypeSpinner_PostATask))
                .getSelectedItem().toString();
        String description =  ((TextView) findViewById(R.id.taskDescEditTxt)).getText().toString();
        Date date = expectedDate.getTime();
        String cost =  ((TextView) findViewById(R.id.costEditTxt)).getText().toString();

        if(date == null){
            ((TextView)findViewById(R.id.postATaskStatus)).setText(R.string.ExpectedDateError);
        }

        String userId = UserStatusData.getEmail(this).replace(".", ";");

        //path where you want to write data to
        String path =  "users/"+ userId +"/TaskPosts/"  ;

        // Get the unique ID generated by a push()
        String postId =  db.getReference(path).push().getKey();

        path = path + postId;
        TaskPost taskPost = new TaskPost(taskTitle,description,cost);

        new DbWrite<TaskPost>(path,taskPost,db) {
            @Override
            public void writeResult(TaskPost userdata) {
                System.out.println("task posted");
            }
        };
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
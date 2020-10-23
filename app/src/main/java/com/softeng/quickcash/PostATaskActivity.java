package com.softeng.quickcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class PostATaskActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    public String[] taskTypes = {"Select A Category"
            , "Task1", "Task2","Task3","Task4"};
    final FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_a_task);

        String selectACategory = getResources().getString(R.string.selectATask);
        taskTypes[0] = selectACategory;

        spinnerSetup();
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

    void spinnerSetup() {
        Spinner spinner = (Spinner) findViewById(R.id.tasksTypeSpinner_PostATask);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, taskTypes);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    int numberOfLastPostInDB = 0;
    Calendar expectedDate;
    public void postATaskOnButtonClick(View view){
        if(!areRequiredFieldsProvidedAndShowStatus()){
            return;
        }

        String userId = UserStatusData.getEmail(this).replace(".", ";");
        //path to database object
        String path = "users/"+ userId +"/TaskPosts";

        //read data from database
        DbRead<DataSnapshot> dbRead = new DbRead<DataSnapshot>(path,
                DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {
                numberOfLastPostInDB = (int) dataFromDb.getChildrenCount();

                //get last item number
                String taskId = "";
                for (DataSnapshot userdata : dataFromDb.getChildren()) {
                    taskId = userdata.getKey();
                }
                if(taskId != ""){
                    numberOfLastPostInDB = Integer.parseInt(taskId.split("_")[0]);
                }else {
                    numberOfLastPostInDB = 0;
                }

                writeTaskToDB();
            }
        };



    }

    private boolean areRequiredFieldsProvidedAndShowStatus(){
        String cost =  ((TextView) findViewById(R.id.costEditTxt)).getText().toString();
        String taskTitle =  ((Spinner) findViewById(R.id.tasksTypeSpinner_PostATask))
                .getSelectedItem().toString();

         if(expectedDate == null)
        {
            ((TextView)findViewById(R.id.postATaskStatus)).setText(R.string.ExpectedDateError);
            return false;
        }else if(cost.equals(""))
        {
            ((TextView)findViewById(R.id.postATaskStatus)).setText(R.string.costMissingPostATask);
            return false;
        }else if(taskTitle.equals(getResources().getString(R.string.selectATask)))
         {
             ((TextView)findViewById(R.id.postATaskStatus)).setText(R.string.selectATaskMissing);
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
        numberOfLastPostInDB++;
        String path =  "users/"+ userId +"/TaskPosts/" + numberOfLastPostInDB + "_" + taskTitle ;

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
}
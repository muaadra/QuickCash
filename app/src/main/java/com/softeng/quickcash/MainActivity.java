package com.softeng.quickcash;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final int MAX_LOCAL_DISTANCE = 50000; //local is defined as 50km max in this app
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseStorage fbStorage;

    ArrayList<TaskPost> taskPosts;
    private MyLocation myLocation;

    public String[] sortBy = {LatestDateSort.sortName, DistanceSort.sortName,
            CostSort.sortName, ExpectedDateSort.sortName};
    Spinner sortSpinner;

    boolean resumedActivity;
    Thread notificationThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create instance of FirebaseStorage
        fbStorage = FirebaseStorage.getInstance();

        spinnerSetup();

        //location setup
        setupLocation();

        showUserFirstLetterOnProfileIcon();
    }

    private void setupLocation() {
        myLocation = new MyLocation(this) {
            @Override
            public void LocationResult(Location location) {
                //update ui based on location data
                getDataFromDbAndShowOnUI();
            }
        };
    }

    /**
     * runs when Filter button is clicked
     */
    public void goToFilterActivity(View v){
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumedActivity = true;
        setFilterPrefsToDefaultsIfNeeded();

        //refresh UI after returning to activity
        if(myLocation.getLastLocation()!= null){
            getDataFromDbAndShowOnUI();
        }else {
            setupLocation();
        }

        startNotificationThread();

    }

    private void startNotificationThread(){
        if (notificationThread != null) return;

        notificationThread = new Thread(){
            @Override
            public void run() {
                while (getName().equals("run")){

                    checkNotifications();
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        notificationThread.setName("run");
        notificationThread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(notificationThread != null){
            notificationThread.setName("stop");
            notificationThread = null;
        }
    }

    private void checkNotifications() {
        String userID = UserStatusData.getUserID(this);
        final Button bell = ((Button)findViewById(R.id.bell));
        if(userID == null){
            bell.setVisibility(View.INVISIBLE);
            return;
        }
        String path = "users/" + userID + "/Notifications/NewTasksNotification";

        final Drawable bellOn = ContextCompat.getDrawable(this, R.drawable.bell_on);
        final Drawable bellOff = ContextCompat.getDrawable(this, R.drawable.bell);
        final Button newTasks = ((Button)findViewById(R.id.goToNewTasks));


        new DbRead<Integer>(path, Integer.class, db) {
            @Override
            public void getReturnedDbData(Integer dataFromDb) {
                if(dataFromDb != null && dataFromDb != 0){
                    bell.setBackground(bellOn);
                    newTasks.setTextColor(Color.parseColor("#02ad2d"));
                }else {
                    bell.setBackground(bellOff);
                    newTasks.setTextColor(Color.BLACK);
                }
            }
        };
    }

    private void SetNotificationsOff() {
        ((RelativeLayout)findViewById(R.id.notificationMenu)).setVisibility(View.INVISIBLE);

        String userID = UserStatusData.getUserID(this);
        if(userID == null){
            return;
        }
        String path = "users/" + userID + "/Notifications/NewTasksNotification";

        new DbWrite<Integer>(path,0,db) {
            @Override
            public void writeResult(Integer userdata) {}
        };
    }


    /**
     * generates the spinner
     */
    public void spinnerSetup() {
        sortSpinner = (Spinner) findViewById(R.id.sortBySpinner_PostATask);

        // Create an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, sortBy);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(this);
    }

    private void setFilterPrefsToDefaultsIfNeeded() {
        FilterPreferences filterPrefs = UserStatusData.getUserFilterPrefs(this);

        if(filterPrefs != null){
            if(filterPrefs.getCategories().size() == 0){
                filterPrefs.getCategories().addAll(Arrays.asList(TaskTypes.getTaskTypes()));
                UserStatusData.saveUserFilterPrefsData(filterPrefs,this);
            }
        }else {
            filterPrefs = new FilterPreferences();
            filterPrefs.getCategories().addAll(Arrays.asList(TaskTypes.getTaskTypes()));
            filterPrefs.setMaxDistance(MAX_LOCAL_DISTANCE/1000);
            UserStatusData.saveUserFilterPrefsData(filterPrefs,this);
        }
    }

    private void getDataFromDbAndShowOnUI() {
        //path to database object
        String path = "users/";

        new DbRead<DataSnapshot>(path, DataSnapshot.class, db) {
            @Override
            public void getReturnedDbData(DataSnapshot dataFromDb) {
                extractTaskPostsFromDBSnapShot(dataFromDb);
            }
        };
    }

    private void extractTaskPostsFromDBSnapShot(DataSnapshot dataFromDb) {
        final ArrayList<TaskPost> posts = new ArrayList<>();
        //loop through all children in path
        for (DataSnapshot userdata : dataFromDb.getChildren()) {

            for (DataSnapshot post : userdata.child("TaskPosts").getChildren()) {
                TaskPost taskPost = post.getValue(TaskPost.class);
                if(taskPost != null){
                    float distance = getDistance(taskPost.getLatLonLocation());
                    if(distance <= MAX_LOCAL_DISTANCE){
                        taskPost.setDistance(getDistance(taskPost.getLatLonLocation()));
                        posts.add(taskPost);
                    }
                }
            }
        }

        taskPosts = posts;
        sortAndRecreateRecyclerView(sortSpinner.getFirstVisiblePosition());

    }


    private float getDistance(String latitudeLongitude){
        if(latitudeLongitude == null){
            return -1;
        }

        Double lat = Double.parseDouble(latitudeLongitude.split(",")[0]);
        Double lon = Double.parseDouble(latitudeLongitude.split(",")[1]);

        LongLatLocation myLoc = new LongLatLocation(lat,lon);
        return myLocation.calcDistanceToLocation(myLoc);
    }


    /**
     * creates a RecyclerView view for main task posts
     * @param posts list of posts
     */
    public void createRecyclerView(ArrayList<TaskPost> posts) {
        if(posts == null){
            return;
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.TaskPostsList);

        // using a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new RVAdapterMainActivity(posts, fbStorage);
        recyclerView.setAdapter(mAdapter);

    }

    /**
     * runs when sigIn/out button is clicked
     */
    public void signInOrOutOnClickButton(View view) {
        if(UserStatusData.isUserSignIn(this)){
            //set user status to signed out
            UserStatusData.setUserSignInToFalse(this);
        }
        //go to next activity
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }


    /**
     * runs when go to profile button is clicked
     */
    public void goToProfileOnClickButton(View view) {
        //go to next activity
        if(UserStatusData.isUserSignIn(this)){
            goToEditProfileActivity();
        }else {
            goToSignInActivity();
        }

    }

    private void goToSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    /**
     * runs when post task button is clicked
     */
    public void gotToMyPostsOnButtonClick(View view) {
        if(!UserStatusData.isUserSignIn(this)){
            goToSignInActivity();
        }else {
            checkIfUserHasAProfile();
        }
    }

    private void checkIfUserHasAProfile(){
        String userId = UserStatusData.getEmail(this).replace(".", ";");
        //path to database object
        String path = "users/"+ userId +"/Profile";

        //read data from database
        DbRead<userProfile> dbRead = new DbRead<userProfile>(path,
                userProfile.class, db) {
            @Override
            public void getReturnedDbData(userProfile dataFromDb) {
                //after data is received from db call checkDbData
                if(dataFromDb!= null && dataFromDb.getfName() != null){
                    goToMyPostsActivity();
                }else {
                    goToEditProfileActivity();
                }
            }
        };

    }

    private void goToMyPostsActivity(){
        Intent intent = new Intent(this, MyPosts.class);
        startActivity(intent);
    }

    private void goToEditProfileActivity(){
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    private void sortAndRecreateRecyclerView(int sortByListPosition){
        if(taskPosts == null || taskPosts.size() == 0){
            return;
        }


        String selectedSort = sortBy[sortByListPosition];
        if(selectedSort.equals(DistanceSort.sortName)){

            Collections.sort(taskPosts,new DistanceSort(true));

        }else if(selectedSort.equals(CostSort.sortName)){

            Collections.sort(taskPosts,new CostSort(false));

        }else if(selectedSort.equals(ExpectedDateSort.sortName)){

            Collections.sort(taskPosts,new ExpectedDateSort(false));

        }else if(selectedSort.equals(LatestDateSort.sortName)){

            Collections.sort(taskPosts,new LatestDateSort(false));

        }

        PostsFilter postsFilter = new PostsFilter(this);
        createRecyclerView(postsFilter.applyFilters(taskPosts));
    }

    private void showUserFirstLetterOnProfileIcon(){
        String userId = UserStatusData.getEmail(this).replace(".", ";");
        //path to database object
        String path = "users/"+ userId +"/Profile";

        //read data from database
        DbRead<userProfile> dbRead = new DbRead<userProfile>(path,
                userProfile.class, db) {
            @Override
            public void getReturnedDbData(userProfile dataFromDb) {
                setUserFirstLetterOnProfileIcon(dataFromDb);
            }
        };
    }

    private void setUserFirstLetterOnProfileIcon(userProfile dataFromDb){
        if(dataFromDb == null){
            return;
        }
        String userFirstChar = "!";

        if(UserStatusData.isUserSignIn(this)){
            userFirstChar = dataFromDb.getfName();
            if(userFirstChar.length() > 0){
                userFirstChar = userFirstChar.substring(0,1);
            }
        }
        ((TextView)findViewById(R.id.goToProfile)).setText(userFirstChar);
    }

    /**
     * runs when bell notification button is clicked
     */
    public void bellOnClick(View v){
        RelativeLayout notificationMenu = (RelativeLayout) findViewById(R.id.notificationMenu);
        int isVisible = notificationMenu.getVisibility();
        if(isVisible == View.VISIBLE){
            notificationMenu.setVisibility(View.INVISIBLE);
        }else {
            notificationMenu.setVisibility(View.VISIBLE);
        }

    }

    /**
     * runs when new Task notification button is clicked
     */
    public void goToNewTaskNotificationActivity(View v){
        SetNotificationsOff();
        Intent intent = new Intent(this, NewTasksNotifications.class);
        startActivity(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        getAndSetSpinnerPrefs(position);

    }

    void getAndSetSpinnerPrefs(int position){
        FilterPreferences filterPrefs = UserStatusData.getUserFilterPrefs(this);

        if(resumedActivity){ //when activity first starts or resumes
            if(filterPrefs != null){
                if(filterPrefs.getSortMethodIndex() != -1){
                    position = filterPrefs.getSortMethodIndex();
                }
            }
            sortSpinner.setSelection(position);
            sortAndRecreateRecyclerView(position);
            resumedActivity = false;
        }else {
            if(filterPrefs == null){
                filterPrefs = new FilterPreferences();
            }

            filterPrefs.setSortMethodIndex(position);
            UserStatusData.saveUserFilterPrefsData(filterPrefs,this);
            sortAndRecreateRecyclerView(position);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
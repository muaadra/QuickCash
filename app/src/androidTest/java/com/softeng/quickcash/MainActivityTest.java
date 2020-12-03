package com.softeng.quickcash;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * tests that the main activity is showing if user is signed in
     */
    @Test
    public void mainActivityShowingSignedInUserTest(){
        //setup
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        UserStatusData.saveUserData("email","jojo@mo.com", activity);
                        UserSignUpData signUpData = new UserSignUpData("email","jojo@mo.com");
                        UserStatusData.setUserSignInToTrue(activity,signUpData);
                        //restart the activity
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                    }
                });

        //check sign-up screen is displayed
        onView(withId(R.id.mainActivityLayOut))
                .check(ViewAssertions.matches(isDisplayed()));

    }


    @Test
    public void userCantGoToPostATaskIfNotSignedIn()  {
        //setup, making sure user is signed in
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        UserStatusData.setUserSignInToFalse(activity);
                    }
                });

        onView(withId(R.id.postATask_main)).perform(click());

        //check screen is displayed
        onView(withId(R.id.SignInActivity_Layout)).check(matches(isDisplayed()));

    }

    @Test
    public void userGoesToSignInWhenClickOnProfileIfNotSignedIn()  {
        //setup, making sure user is signed in
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        UserStatusData.removeAllUserPreferences(activity);
                        UserStatusData.setUserSignInToFalse(activity);
                    }
                });

        onView(withId(R.id.goToProfile)).perform(click());

        //check screen is displayed
        onView(withId(R.id.SignInActivity_Layout)).check(matches(isDisplayed()));

    }

    @Test
    public void sortBySpinnerIsNotEmpty_Test() {
        final int[] count = {-1};

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        count[0] = ((Spinner) activity.findViewById(R.id.sortBySpinner_PostATask)).getCount();
                    }
                });

        assertNotEquals(0, count[0]);
    }

    @Test
    public void recyclerViewIsCreated_Test() {
        final boolean[] exists = {false};
        final ArrayList<TaskPost> posts = new ArrayList<>();
        TaskPost taskPost1 = new TaskPost("","1","t","d"
                ,5f,false,"", new Date(), Calendar.getInstance().getTime(),"hh");
        TaskPost taskPost2 = new TaskPost("","1","t","d"
                ,5f,false,"", new Date(), Calendar.getInstance().getTime(),"hh");
        posts.add(taskPost1);
        posts.add(taskPost2);

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        activity.createRecyclerView(posts);
                        exists[0] =
                                (((RecyclerView) activity.findViewById(R.id.TaskPostsList))
                                        .getAdapter() != null);
                    }
                });

        assertEquals(true, exists[0]);
    }


    @Test
    public void MyRecyclerViewCount_Test() {

        final ArrayList<TaskPost> posts = new ArrayList<>();
        TaskPost taskPost1 = new TaskPost("","1","t","d"
                ,5f,false,"", new Date(), Calendar.getInstance().getTime(),"hh");
        TaskPost taskPost2 = new TaskPost("","1","t","d"
                ,5f,false,"", new Date(), Calendar.getInstance().getTime(),"hh");
        posts.add(taskPost1);
        posts.add(taskPost2);

        final int[] count = {-1};

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        activity.createRecyclerView(posts);
                        count[0] =  ((RecyclerView) activity.findViewById(R.id.TaskPostsList))
                                .getAdapter().getItemCount();
                    }
                });

        assertEquals(2, count[0]);
    }

    @Test
    public void userGoesToMainActivityAfterApplyFilterTest() {
        onView(withId(R.id.goToFilters)).perform(click());
        onView(withId(R.id.filterLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void recyclerViewClickOnPostGoesToViewPostActivity() {

        final ArrayList<TaskPost> posts = new ArrayList<>();
        TaskPost taskPost1 = new TaskPost("","1","t","d"
                ,5f,false,"", new Date(), Calendar.getInstance().getTime(),"hh");
        TaskPost taskPost2 = new TaskPost("","1","t","d"
                ,5f,false,"", new Date(), Calendar.getInstance().getTime(),"hh");
        posts.add(taskPost1);
        posts.add(taskPost2);


        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        activity.createRecyclerView(posts);
                    }
                });

        onView(withId(R.id.TaskPostsList)).perform(click());
        onView(withId(R.id.ViewPostLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void sortPreferencesAreStoredTest() {
        final MainActivity[] activity = new MainActivity[1];
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity mainActivity) {
                        activity[0] = mainActivity;
                    }
                });

        onView(withId(R.id.sortBySpinner_PostATask)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(CostSort.sortName))).perform(click());


      assertEquals(activity[0].sortSpinner.getFirstVisiblePosition(),
              UserStatusData.getUserFilterPrefs(activity[0]).getSortMethodIndex());

    }

    @Test
    public void goToMyApplications()  {
        //setup, making sure user is signed in
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        ((Button)activity.findViewById(R.id.myApplicationsMainButton)).setVisibility(View.VISIBLE);
                    }
                });
        onView(withId(R.id.myApplicationsMainButton)).perform(click());

        //check screen is displayed
        onView(withId(R.id.myApplicationsLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void showNotificationTest(){
        //setup, making sure user is signed in
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        ((Button)activity.findViewById(R.id.bell)).setVisibility(View.VISIBLE);
                    }
                });
        onView(withId(R.id.bell)).perform(click());

        onView(withId(R.id.notificationMenu)).check(matches(isDisplayed()));
    }

    @Test
    public void goToNewTaskActivity(){
        //setup, making sure user is signed in
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                      ((Button)activity.findViewById(R.id.bell)).setVisibility(View.VISIBLE);
                    }
                });

        onView(withId(R.id.bell)).perform(click());
        onView(withId(R.id.goToNewTasks)).perform(click());
        onView(withId(R.id.newTaskNotificationLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void goToNewApplicantsNotification()  {
        //setup, making sure user is signed in
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        ((Button)activity.findViewById(R.id.bell)).setVisibility(View.VISIBLE);

                    }
                });
        onView(withId(R.id.bell)).perform(click());
        onView(withId(R.id.newApplicantsButton)).perform(click());

        //check screen is displayed
        onView(withId(R.id.myPostsLayout)).check(matches(isDisplayed()));
    }
     /**
     * clear all data from SharedPreferences
     */
    @After
    public void tearDown(){
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        UserStatusData.removeAllUserPreferences(activity);
                    }
                });
    }
}

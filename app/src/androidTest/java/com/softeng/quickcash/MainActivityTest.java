package com.softeng.quickcash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


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

    /**
     * tests that the main activity is not showing if user is signed in
     */
    @Test
    public void welcomeActivityNotShowingAfterFirstRunTest(){
        //setup
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        UserStatusData.removeAllUserPreferences(activity);

                        //restart the activity
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                    }
                });

        //check sign-up screen is displayed
        onView(withId(R.id.mainActivityLayOut)).check(doesNotExist());

    }

    /**
     * testing sign out button
     */
    @Test
    public void signOutTest(){
        //setup, making sure user is signed in
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        UserStatusData.removeAllUserPreferences(activity);
                        UserStatusData.saveUserData("email","jojo@mo.com", activity);
                        //restarting activity
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                    }
                });

        onView(withId(R.id.signOut)).perform(click());

        //check screen is displayed
        onView(withId(R.id.mainActivityLayOut)).check(doesNotExist());

    }

    /**
     * testing go to profile button
     */
    @Test
    public void goToProfileTest(){
        //setup, making sure user is signed in
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        UserStatusData.removeAllUserPreferences(activity);
                        UserStatusData.saveUserData("email","jojo@mo.com", activity);
                        //restarting activity
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                    }
                });

        onView(withId(R.id.goToProfile)).perform(click());

        //check screen is displayed
        onView(withId(R.id.editProfileLayout)).check(matches(isDisplayed()));

    }

    /**
     * testing go to post a task page
     */
    @Test
    public void goToMyPostsActivity() throws InterruptedException {
        //setup, making sure user is signed in
        activityScenarioRule.getScenario().onActivity(
            new ActivityScenario.ActivityAction<MainActivity>() {
                @Override
                public void perform(MainActivity activity) {
                    UserSignUpData signUpData = new UserSignUpData("email","jojo@mo.com");
                    UserStatusData.setUserSignInToTrue(activity,signUpData);
                    //restarting activity
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                }
            });

        Thread.sleep(1200);
        onView(withId(R.id.postATask_main)).perform(click());

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

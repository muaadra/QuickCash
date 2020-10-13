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
     * tests that the welcome activity should show to first time users only
     */
    @Test
    public void welcomeActivityShowingForFirstRunTest(){
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
        onView(withId(R.id.welcomeLayout))
                .check(ViewAssertions.matches(isDisplayed()));

    }

    /**
     * tests that the welcome activity should not show after first run
     */
    @Test
    public void welcomeActivityNotShowingAfterFirstRunTest(){
        //setup
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        UserStatusData.removeAllUserPreferences(activity);
                        UserStatusData.setUserFirstRun(activity,false);

                        //restart the activity
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                    }
                });

        //check sign-up screen is displayed
        onView(withId(R.id.welcomeLayout)).check(doesNotExist());

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

        onView(withId(R.id.emailStatus))
                .check(matches(withText(R.string.YouAreSignedOut)));

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

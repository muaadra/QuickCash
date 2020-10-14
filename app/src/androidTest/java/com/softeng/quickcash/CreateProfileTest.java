package com.softeng.quickcash;


import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.BeforeClass;
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

public class CreateProfileTest {

        @Rule
        public ActivityScenarioRule<CreateProfile> activityScenarioRule = new ActivityScenarioRule<CreateProfile>(CreateProfile.class);
        /**
         * This will test the UI if the user creates the profile (checks against the create profile button label appearing)
         */
        @Test
        public void testCreateProfileLabel(){
            onView(withId(R.id.editProfileName)).perform(click()).perform(typeText("Jonathan Robichaud"));

            onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

            onView(withId(R.id.createProfile)).perform(click());

            onView(withId(R.id.textViewProfileConfirm)).check(matches(withText(R.string.profileCreated)));

        }

    /**
     * tests that the main activity is showing after user canceled creating a profile
     */
    @Test
    public void welcomeActivityNotShowingAfterFirstRunTest(){
        //setup, making sure user is signed in
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<CreateProfile>() {
                    @Override
                    public void perform(CreateProfile activity) {
                        UserStatusData.removeAllUserPreferences(activity);
                        UserStatusData.saveUserData("email","jojo@mo.com", activity);
                    }
                });

        onView(withId(R.id.cancelProfile)).perform(click());

        //check if screen is displayed
        onView(withId(R.id.mainActivityLayOut))
                .check(ViewAssertions.matches(isDisplayed()));
    }


}


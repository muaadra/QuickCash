package com.softeng.quickcash;


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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class CreateProfileEspressoTest {

        //  'CreateProfile' will be the name of the activity functionality
        // editProfileName will be the name of the textview for a users profile name
        // editProfileAboutMe will be the name of the textview for a users about me info
        // createProfile will be the button a user will click to create their profile
        // textViewProfileConfirm will be the name of the label i will use to acknowledge a profile being created
        // profileCreated will be a String reference I will use, this will be the text shown in the label

        @Rule
        public ActivityScenarioRule<CreateProfile> activityScenarioRule = new ActivityScenarioRule<CreateProfile>(CreateProfile.class);
        /**
         * This will test the UI if the user creates the profile (checks against the create profile button label appearing)
         */
        @Test
        public void testCreateProfileLabel(){
            onView(withId(R.id.editProfileName)).perform(click()).perform(typeText("Jonathan Robichaud"));

            onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

            // onView(withId(R.id.editProfileAboutMe)).perform(click()).perform(typeText("This is about me"));

            //  onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

            onView(withId(R.id.createProfile)).perform(click());

            onView(withId(R.id.textViewProfileConfirm)).check(matches(withText(R.string.profileCreated)));

        }


    }


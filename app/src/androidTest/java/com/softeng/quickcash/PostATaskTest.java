package com.softeng.quickcash;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class PostATaskTest {
    @Rule
    public ActivityScenarioRule<PostATaskActivity> activityScenarioRule
            = new ActivityScenarioRule<>(PostATaskActivity.class);

    @Test
    public void PostATaskActivityNotShowingTest(){

        onView(withId(R.id.datePickerPostAtask)).perform(click());

        //check screen is not displayed
        onView(withId(R.id.datePickerPostAtask)).check(doesNotExist());
    }
}

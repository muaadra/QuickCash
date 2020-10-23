package com.softeng.quickcash;

import android.content.Intent;
import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void taskTypesSpinnerIsNotEmptyTest(){
        final int[] count = {-1};
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<PostATaskActivity>() {
                    @Override
                    public void perform(PostATaskActivity activity) {
                        count[0] =((Spinner)activity.findViewById(R.id.tasksTypeSpinner_PostATask)).getCount();
                    }
                });

        assertNotEquals(count[0], 0);
    }
}

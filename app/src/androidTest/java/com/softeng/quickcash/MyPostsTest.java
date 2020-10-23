package com.softeng.quickcash;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class MyPostsTest {
    @Rule
    public ActivityScenarioRule<MyPosts> activityScenarioRule
            = new ActivityScenarioRule<>(MyPosts.class);



    @Test
    public void goToPostATaskActivityTest(){

        onView(withId(R.id.postNewTaskButton)).perform(click());

        //check screen is displayed
        onView(withId(R.id.postTasksLayout)).check(matches(isDisplayed()));
    }


}

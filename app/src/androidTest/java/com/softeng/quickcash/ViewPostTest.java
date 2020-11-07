package com.softeng.quickcash;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class ViewPostTest {

    @Rule
    public ActivityScenarioRule<ViewPost> activityRule =
            new ActivityScenarioRule<>(ViewPost.class);

    @Test
    public void goToUserProfileTest() {
        onView(withId(R.id.authorTV)).perform(click());
        onView(withId(R.id.viewProfileLayout)).check(matches(isDisplayed()));
    }
}

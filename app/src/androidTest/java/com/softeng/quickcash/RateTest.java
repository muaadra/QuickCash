package com.softeng.quickcash;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class RateTest {

    @Rule
    public ActivityScenarioRule<Rate> activityScenarioRule
            = new ActivityScenarioRule<>(Rate.class);


    @Test
    public void rateTest1() {
        onView(withId(R.id.r1)).perform(click());
        onView(withId(R.id.ratingLabel))
                .check(matches(withText("1")));
    }

    @Test
    public void rateTest2() {
        onView(withId(R.id.r2)).perform(click());
        onView(withId(R.id.ratingLabel))
                .check(matches(withText("2")));
    }
}

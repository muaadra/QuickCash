package com.softeng.quickcash;

import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

public class FilterActivityTest {
    @Rule
    public ActivityScenarioRule<FilterActivity> activityScenarioRule
            = new ActivityScenarioRule<>(FilterActivity.class);

    /**
     * setup method for remove UserStatus Data.
     */
    @Before
    public void setup() {
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<FilterActivity>() {
                    @Override
                    public void perform(FilterActivity activity) {
                        UserStatusData.removeAllUserPreferences(activity);
                        Intent intent = new Intent(activity, FilterActivity.class);
                        activity.startActivity(intent);
                    }
                });
    }

    /**
     * this Ui test if user change distance.
     */
    @Test
    public void filter_distance_test() {
        onView(withId(R.id.seekBar)).perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_LEFT, Press.FINGER));
        onView(withId(R.id.distance_text)).check(matches(withText("0km")));
        onView(withId(R.id.seekBar)).perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER, Press.FINGER));
        onView(withId(R.id.distance_text)).check(matches(withText((MainActivity.MAX_LOCAL_DISTANCE/2000) + "km")));
    }
    /**
     * this Ui test if user input correct number.
     */
    @Test
    public void filter_price_correct() {
        onView(withId(R.id.minPayTV)).perform(typeText("50"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());


    }
    /**
     * this Ui test if user input incorrect number.
     */
    @Test
    public void filter_price_incorrect() {
        onView(withId(R.id.minPayTV)).perform(typeText("50"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.err_msg_price)).check(matches(isDisplayed()));
    }

    @Test
    public void RecyclerViewContent_Test() {


        final int[] count = {-1};

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<FilterActivity>() {
                    @Override
                    public void perform(FilterActivity activity) {
                        count[0] =  ((RecyclerView) activity.findViewById(R.id.FilterRV))
                                .getAdapter().getItemCount();
                    }
                });

        assertEquals(TaskTypes.getTaskTypes().length, count[0]);
    }
    @Test
    public void userGoesToMainActivityAfterApplyFilterTest() {
        onView(withId(R.id.ApplyFilter)).perform(click());
        onView(withId(R.id.mainActivityLayOut)).check(matches(isDisplayed()));
    }

}

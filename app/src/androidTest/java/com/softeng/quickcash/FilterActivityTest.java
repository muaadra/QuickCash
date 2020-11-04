package com.softeng.quickcash;

import android.content.Intent;

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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class FilterActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * setup method for remove UserStatus Data.
     */
    @Before
    public void setup() {
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<MainActivity>() {
                    @Override
                    public void perform(MainActivity activity) {
                        UserStatusData.removeAllUserPreferences(activity);
                        Intent intent = new Intent(activity, WelcomeActivity.class);
                        activity.startActivity(intent);
                    }
                });
        onView(withId(R.id.continueAsGuestButton)).perform(click());
        onView(withId(R.id.btn_filter)).perform(click());
    }
    /**
     * this Ui test if user click categories.
     */
    @Test
    public void filter_categories_test() {
        onView(withId(R.id.btn_categories_1)).perform(click())
            .check(matches(isChecked()));
        onView(withId(R.id.btn_categories_2)).perform(click())
            .check(matches(isChecked()));
        onView(withId(R.id.btn_categories_3)).perform(click())
            .check(matches(isChecked()));
        onView(withId(R.id.btn_categories_4)).perform(click())
            .check(matches(isChecked()));
    }
    /**
     * this Ui test if user change distance.
     */
    @Test
    public void filter_distance_test() {
        onView(withId(R.id.seekBar)).perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_LEFT, Press.FINGER));
        onView(withId(R.id.distance_text)).check(matches(withText("0m")));
        onView(withId(R.id.seekBar)).perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER, Press.FINGER));
        onView(withId(R.id.distance_text)).check(matches(withText("50m")));
        onView(withId(R.id.seekBar)).perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_RIGHT, Press.FINGER));
        onView(withId(R.id.distance_text)).check(matches(withText("100m")));
    }
    /**
     * this Ui test if user input correct number.
     */
    @Test
    public void filter_price_correct() {
        onView(withId(R.id.price_minText)).perform(typeText("50"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.price_maxText)).perform(typeText("60"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());


    }
    /**
     * this Ui test if user input incorrect number.
     */
    @Test
    public void filter_price_incorrect() {
        onView(withId(R.id.price_minText)).perform(typeText("50"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.price_maxText)).perform(typeText("40"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.err_msg_price)).check(matches(isDisplayed()));
    }

//    /**
//     * this Ui test if user click filter cancel.
//     */
//    @Test
//    public void filter_cancel_click() {
//        onView(withId(R.id.btn_filter_cancel)).perform(scrollTo())
//                .perform(click());
//        onView(withId(R.id.duration_minText)).check(matches(withText("")));
//        onView(withId(R.id.duration_maxText)).check(matches(withText("")));
//        onView(withId(R.id.price_minText)).check(matches(withText("")));
//        onView(withId(R.id.price_maxText)).check(matches(withText("")));
//        onView(withId(R.id.seekBar)).perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_LEFT, Press.FINGER));
//        onView(withId(R.id.distance_text)).check(matches(withText("0m")));
//        onView(withId(R.id.btn_categories_1)).perform(click())
//                .check(matches(isNotChecked()));
//        onView(withId(R.id.btn_categories_2)).perform(click())
//                .check(matches(isNotChecked()));
//        onView(withId(R.id.btn_categories_3)).perform(click())
//                .check(matches(isNotChecked()));
//        onView(withId(R.id.btn_categories_4)).perform(click())
//                .check(matches(isNotChecked()));
//    }

}

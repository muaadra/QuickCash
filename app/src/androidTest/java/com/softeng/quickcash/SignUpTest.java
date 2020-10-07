package com.softeng.quickcash;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class SignUpTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);


    /**
     * this UI test checks if email entered is valid
     */
    @Test
    public void testValidUserName_Test1(){
        onView(withId(R.id.email_EditText))
                .perform(click())
                .perform(typeText("email@mail"));

        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.createAccount_Button))
                .perform(click());

        onView(withId(R.id.emailStatus))
                .check(matches(withText(R.string.InvalidEmail)));
    }


    /**
     * this UI test checks if email entered is valid
     */
    @Test
    public void testValidUserName_Test2(){
        onView(withId(R.id.email_EditText))
                .perform(click())
                .perform(typeText("email@mail.com"));

        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.createAccount_Button))
                .perform(click());

        onView(withId(R.id.emailStatus))
                .check(matches(withText(R.string.InvalidEmail)));
    }

}

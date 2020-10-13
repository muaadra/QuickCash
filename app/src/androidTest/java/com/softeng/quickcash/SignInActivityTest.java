package com.softeng.quickcash;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SignInActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        onView(withId(R.id.signInButton)).perform(click());
        onView(withId(R.id.SignInActivity_Layout)).check(matches(isDisplayed()));
    }

    /**
     * this UI test checks if email entered is valid
     */
    @Test
    public void test_user_correct_credential() {
        HashMap<String, String> signUpInfo = new HashMap<String, String>();
        signUpInfo.put("email", "signUp@test.com");
        signUpInfo.put("password", "testPassword");

        onView(withId(R.id.input_email)).perform(typeText("signUp@test;com"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.input_password)).perform(typeText("testPassword"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        onView(withId(R.id.msg_confirm)).check(matches(isDisplayed()));
    }

    /**
     * this Junit test if email and password are valid
     */
    @Test
    public void test_user_input() {
        HashMap<String, String> signUpInfo = new HashMap<String, String>();
        signUpInfo.put("email", "signUp@test.com");
        signUpInfo.put("password", "testPassword");

        InputValidation input = new InputValidation();
        assertTrue(input.isEmailValid(signUpInfo.get("email")));
        assertTrue(input.isEmailFormatValid(signUpInfo.get("email")));
        assertTrue(input.isEmailValid(signUpInfo.get("email")));
        assertTrue(input.isPasswordCaseValid(signUpInfo.get("password")));
        assertTrue(input.isPasswordLengthValid(signUpInfo.get("password")));
        assertTrue(input.isPasswordValid(signUpInfo.get("password")));
    }

    /**
     * this Ui test if entered password is invalid.
     */
    @Test
    public void test_user_wrong_credential() {
        HashMap<String, String> signUpInfo = new HashMap<String, String>();
        signUpInfo.put("email", "signUp@test.com");
        signUpInfo.put("password", "testPassword1111");   //Wrong password

        onView(withId(R.id.input_email)).perform(typeText("signUp@test.com"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.input_password)).perform(typeText("testPassword1111"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        onView(withId(R.id.err_msg_pw)).check(matches(isDisplayed()));
    }

    /**
     * this Ui test if user not found.
     */
    @Test
    public void test_user_not_found() {
        HashMap<String, String> signUpInfo = new HashMap<String, String>();
        signUpInfo.put("email", "signUp@test.com");
        signUpInfo.put("password", "testPassword");
        onView(withId(R.id.input_email)).perform(typeText("signUp@test.com"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.input_password)).perform(typeText("testPassword"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.btn_login)).perform(click());
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.err_msg_email)).check(matches(isDisplayed()));
    }


}
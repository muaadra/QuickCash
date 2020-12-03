package com.softeng.quickcash;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
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



}
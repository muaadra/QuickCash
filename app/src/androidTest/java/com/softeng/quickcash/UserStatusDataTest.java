package com.softeng.quickcash;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class UserStatusDataTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    final Context[] appContext = new Context[1];

    /**
     * clearing then setting us data in SharedPreferences
     */
    @Before
    public void setup(){
        activityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                appContext[0] = activity;
                UserStatusData.removeAllUserPreferences(activity);
                UserStatusData.saveUserData("email","jojo@mo.com", activity);
            }
        });
    }

    /**
     * tests if user preference in the setup is saved to sharedPreferences
     */
    @Test
    public void testValidUserWritePreferencesTest(){
        assertNotEquals("TT@mo.com", UserStatusData.getEmail(appContext[0]));
        assertEquals("jojo@mo.com", UserStatusData.getEmail(appContext[0]));
    }

    /**
     * tests if user status is signed in, based the setup test user is
     * expected to be signed in
     */
    @Test
    public void testValidUserSignInStatusTest(){
        assertTrue( UserStatusData.isUserSignIn(appContext[0]));
    }

    /**
     * clear all data from SharedPreferences
     */
    @After
    public void tearDown(){
        activityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                UserStatusData.removeAllUserPreferences(activity);
            }
        });
    }
}

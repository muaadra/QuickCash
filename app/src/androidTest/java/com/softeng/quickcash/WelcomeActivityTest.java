package com.softeng.quickcash;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WelcomeActivityTest {
    @Rule
    public ActivityScenarioRule<WelcomeActivity> activityRule =
            new ActivityScenarioRule<>(WelcomeActivity.class);

    @Test
    public void tapCreateAccount() {
        onView(withId(R.id.createAccountButton)).perform(click());
        onView(withId(R.id.SignUpActivity_Layout)).check(matches(isDisplayed()));
    }

    /**
     * test if user goes to sign-in activity after click on sign in button
     */
    @Test
    public void tapSignIn() {
        onView(withId(R.id.signInButton)).perform(click());
        onView(withId(R.id.SignInActivity_Layout)).check(matches(isDisplayed()));
    }
    @Test
    public void tapContinueAsGuest() {
        onView(withId(R.id.continueAsGuestButton)).perform(click());
        onView(withId(R.id.mainActivityLayOut)).check(matches(isDisplayed()));
    }
}

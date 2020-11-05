/**
 * Jonathan Robichaud B00802259
 * QuickCash Group 17 Development
 */
package com.softeng.quickcash;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class EditProfileTest {
    @Rule
    public ActivityScenarioRule<EditProfile> activityScenarioRule = new ActivityScenarioRule<EditProfile>(EditProfile.class);

    /**
     * This will test the UI if the user edits the profile (checks against the edit profile button label appearing)
     */
    @Test
    public void testEditProfileLabel(){
        onView(withId(R.id.editProfileName)).perform(click()).perform(typeText("Jonathan Robichaud"));

        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.createProfile)).perform(click());

        onView(withId(R.id.textViewEditProfileConfirm)).check(matches(withText(R.string.profileEdited)));

    }
    /**
     * This will test the UI if the user deletes the profile, the profile screen closes
     */
    @Test
    public void deleteProfileScreenClose(){
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.deleteProfile)).perform(click());

        onView(withId(R.id.mainActivityLayOut)).check(matches(isDisplayed()));

    }

    /**
     * tests that the main activity is showing after user canceled editing a profile
     */
    @Test
    public void mainActivityShowingAfterUserCancels(){
        //setup, making sure user is signed in
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<EditProfile>() {
                    @Override
                    public void perform(EditProfile activity) {
                        UserStatusData.removeAllUserPreferences(activity);
                        UserSignUpData signUpData =
                                new UserSignUpData("email","jojo@mo.com");
                        UserStatusData.setUserSignInToTrue(activity,signUpData);
                    }
                });

        onView(withId(R.id.cancelProfile)).perform(click());

        //check if screen is displayed
        onView(withId(R.id.mainActivityLayOut))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * This will test that welcome activity is shown when user signs out
     */
    @Test
    public void signOutTest(){

        onView(withId(R.id.signOut)).perform(click());

        onView(withId(R.id.welcomeLayout)).check(matches(isDisplayed()));

    }

}

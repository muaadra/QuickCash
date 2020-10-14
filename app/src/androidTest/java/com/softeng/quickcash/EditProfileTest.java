/**
 * Jonathan Robichaud B00802259
 * QuickCash Group 17 Development
 */
package com.softeng.quickcash;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
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

        // onView(withId(R.id.editProfileAboutMe)).perform(click()).perform(typeText("This is about me"));

        // onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.createProfile)).perform(click());

        onView(withId(R.id.textViewEditProfileConfirm)).check(matches(withText(R.string.profileEdited)));

    }
    /**
     * This will test the UI if the user deletes the profile (checks against the delete profile button label appearing)
     */
    @Test
    public void testDeleteProfileLabel(){
        onView(withId(R.id.editProfileName)).perform(click()).perform(typeText("Jonathan Robichaud"));

        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        // onView(withId(R.id.editProfileAboutMe)).perform(click()).perform(typeText("This is about me"));

        // onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.deleteProfile)).perform(click());

        onView(withId(R.id.textViewDeleteProfileConfirm)).check(matches(withText(R.string.profileDelete)));

    }

}

package com.softeng.quickcash;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SignInActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);
    static FirebaseDatabase database = null;
    static DatabaseReference itemRef = null;
    static Map<String, HashMap<String, HashMap<String, HashMap<String, String>>>> info;

    @Before
    public void setup() {
        onView(withId(R.id.signInButton)).perform(click());
        onView(withId(R.id.SignInActivity_Layout)).check(matches(isDisplayed()));
    }

    /**
     * Build : Builds a database for a firebase test.
     */
    @BeforeClass
    public static void build() {
        database = FirebaseDatabase.getInstance();
        itemRef = database.getReference();
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                info = (Map<String, HashMap<String, HashMap<String, HashMap<String, String>>>>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * AT2-1 : Check correct credential
     */
    @Test
    public void test_check_user_correct_credential() {
        HashMap<String, String> signUpInfo = new HashMap<String, String>();
        signUpInfo.put("email", "signUp@test.com");
        signUpInfo.put("password", "testPassword");

        onView(withId(R.id.input_email)).perform(typeText("signUp@test;com"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.input_password)).perform(typeText("testPassword"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        assertTrue(signUpInfo.equals(info.get("users").get("signUp@test;com").get("SignUpInfo")));
        onView(withId(R.id.msg_confirm)).check(matches(isDisplayed()));
    }

    /**
     * AT2-3 : Check wrong credentials
     */
    @Test
    public void test_check_user_wrong_credential(){
        HashMap<String, String> signUpInfo = new HashMap<String, String>();
        signUpInfo.put("email", "signUp@test.com");
        signUpInfo.put("password", "testPassword1111");   //Wrong password

        onView(withId(R.id.input_email)).perform(typeText("signUp@test.com"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.input_password)).perform(typeText("testPassword1111"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        assertFalse(signUpInfo.equals(info.get("users").get("signUp@test;com").get("SignUpInfo")));
        onView(withId(R.id.err_msg_pw)).check(matches(isDisplayed()));
    }

}
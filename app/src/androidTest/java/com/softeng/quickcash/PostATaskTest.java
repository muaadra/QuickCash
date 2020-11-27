package com.softeng.quickcash;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PostATaskTest {
    @Rule
    public ActivityScenarioRule<PostATaskActivity> activityScenarioRule
            = new ActivityScenarioRule<>(PostATaskActivity.class);

    @Test
    public void PostATaskActivityNotShowingTest() {
        onView(withId(R.id.datePickerPostAtask)).perform(click());

        //check screen is not displayed
        onView(withId(R.id.datePickerPostAtask)).check(doesNotExist());
    }


    @Test
    public void taskTypesSpinnerIsNotEmpty_Test() {
        final int[] count = {-1};
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<PostATaskActivity>() {
                    @Override
                    public void perform(PostATaskActivity activity) {
                        count[0] = ((Spinner) activity.findViewById(R.id.tasksTypeSpinner_PostATask)).getCount();
                    }
                });

        assertNotEquals(count[0], 0);
    }

    @Test
    public void taskTypesSpinnerHasSameCount_Test() {
        final int[] count = {-1};
        final List<String>[] taskTypes = new List[]{new ArrayList<>()};

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<PostATaskActivity>() {
                    @Override
                    public void perform(PostATaskActivity activity) {
                        taskTypes[0] = Arrays.asList(TaskTypes.getTaskTypes());
                        count[0] = ((Spinner) activity.findViewById(R.id.tasksTypeSpinner_PostATask)).getCount();
                    }
                });

        assertEquals(taskTypes[0].size(), count[0]);
    }

    @Test
    public void ExpectedDateFieldIsNotEmptyWhenSubmit_Test() {

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<PostATaskActivity>() {
                    @Override
                    public void perform(PostATaskActivity activity) {
                        TaskTypes.getTaskTypes()[1] = "Task 1";
                        activity.spinnerSetup();
                    }
                });

        onView(withId(R.id.tasksTypeSpinner_PostATask)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Task 1"))).perform(click());

        onView(withId(R.id.taskDescEditTxt)).perform(typeText("my description"));

        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.costEditTxt)).perform(click())
                .perform(typeText("30"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.applyToTask)).perform(click());

        onView(withId(R.id.postATaskStatus))
                .check(matches(withText(R.string.ExpectedDateError)));

    }

    @Test
    public void CostFieldIsNotEmptyWhenPostingATask_Test() {

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<PostATaskActivity>() {
                    @Override
                    public void perform(PostATaskActivity activity) {
                        TaskTypes.getTaskTypes()[1] = "Task 1";
                        activity.spinnerSetup();
                        activity.expectedDate = Calendar.getInstance();
                    }
                });

        onView(withId(R.id.tasksTypeSpinner_PostATask)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Task 1"))).perform(click());

        onView(withId(R.id.taskDescEditTxt)).perform(typeText("my description"));

        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.applyToTask)).perform(click());

        onView(withId(R.id.postATaskStatus))
                .check(matches(withText(R.string.costMissingPostATask)));

    }


    @Test
    public void descriptionFieldIsNotEmptyWhenPostingATask_Test() {

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<PostATaskActivity>() {
                    @Override
                    public void perform(PostATaskActivity activity) {
                        TaskTypes.getTaskTypes()[1] = "Task 1";
                        activity.spinnerSetup();
                        activity.expectedDate = Calendar.getInstance();
                    }
                });

        onView(withId(R.id.tasksTypeSpinner_PostATask)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Task 1"))).perform(click());


        onView(withId(R.id.costEditTxt)).perform(click())
                .perform(typeText("30"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.applyToTask)).perform(click());

        onView(withId(R.id.postATaskStatus))
                .check(matches(withText(R.string.missingDescPostATask)));

    }

    @Test
    public void taskDeleted_Test() {
        final int[] count = {-1};
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<PostATaskActivity>() {
                    @Override
                    public void perform(PostATaskActivity activity) {
                        count[0] = ((Spinner) activity.findViewById(R.id.tasksTypeSpinner_PostATask)).getCount();
                    }
                });

        assertNotEquals(count[0], 0);
    }

    @Test
    public void DeleteButtonNotShowingInNewPosts_Test() {

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<PostATaskActivity>() {
                    @Override
                    public void perform(PostATaskActivity activity) {
                        activity.taskPostFromDB = null;
                    }
                });

        onView(withId(R.id.deletedPosts)).check(doesNotExist());

    }
    @Test
    public void goToApplicantsActivity() throws InterruptedException {

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<PostATaskActivity>() {
                    @Override
                    public void perform(PostATaskActivity activity) {
                        ((Button)activity.findViewById(R.id.applicantsButton)).setVisibility(View.VISIBLE);
                    }
                });

        onView(withId(R.id.applicantsButton)).perform(click());
        onView(withId(R.id.applicantsLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void goToPayPalPayment()  {
        //setup, making sure user is signed in
        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<PostATaskActivity>() {
                    @Override
                    public void perform(PostATaskActivity activity) {
                        ((Button)activity.findViewById(R.id.payEmployee)).setVisibility(View.VISIBLE);
                    }
                });

        onView(withId(R.id.payEmployee)).perform(click());

        //check screen is displayed
        onView(withId(R.id.paymentLayout)).check(matches(isDisplayed()));
    }
}

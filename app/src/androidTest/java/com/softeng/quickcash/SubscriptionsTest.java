package com.softeng.quickcash;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SubscriptionsTest {

    @Rule
    public ActivityScenarioRule<Subscription> activityScenarioRule
            = new ActivityScenarioRule<>(Subscription.class);


    @Test
    public void recyclerViewTest() {

        final int[] count = {-1};

        activityScenarioRule.getScenario().onActivity(
                new ActivityScenario.ActivityAction<Subscription>() {
                    @Override
                    public void perform(Subscription activity) {
                        List<Boolean> checkList = Arrays.asList(true,true,true,false);
                        activity.createRecyclerView(TaskTypes.getTaskTypes(), checkList);
                        count[0] =  ((RecyclerView) activity.findViewById(R.id.SubscriptionRV))
                                .getAdapter().getItemCount();
                    }
                });

        assertEquals(TaskTypes.getTaskTypes().length, count[0]);
    }

}

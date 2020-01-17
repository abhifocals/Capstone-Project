package com.focals.myreddit;


import android.util.EventLogTags;
import android.view.View;

import com.focals.myreddit.activity.PopularsActivity;
import com.focals.myreddit.utils.RecyclerViewTestHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class PopularActivityTest {

    @Rule
    public ActivityTestRule<PopularsActivity> popularsActivityActivityTestRule = new ActivityTestRule<>(PopularsActivity.class);

    @Test
    public void testPopularActivity() {

        onView(withId(R.id.rv_populars)).check(matches(RecyclerViewTestHelper.atPosition(0,
                ViewMatchers.hasDescendant(ViewMatchers.hasDescendant(withText("news"))))));

        onView(withId(R.id.rv_populars)).check(matches(RecyclerViewTestHelper.atPosition(0,
                ViewMatchers.hasDescendant(ViewMatchers.hasDescendant(withId(R.id.iv_subredditImage))))));

        onView(withId(R.id.rv_populars)).check(matches(RecyclerViewTestHelper.atPosition(0,
                ViewMatchers.hasDescendant(ViewMatchers.hasDescendant(withId(R.id.tv_subredditDesc))))));

        onView(withId(R.id.rv_populars)).check(matches(RecyclerViewTestHelper.atPosition(0,
                ViewMatchers.hasDescendant(ViewMatchers.hasDescendant(withId(R.id.tv_subscriberCount))))));

        onView(withId(R.id.rv_populars)).check(matches(RecyclerViewTestHelper.atPosition(0,
                ViewMatchers.hasDescendant(ViewMatchers.hasDescendant(withId(R.id.ib_addToFavorites))))));

    }


}

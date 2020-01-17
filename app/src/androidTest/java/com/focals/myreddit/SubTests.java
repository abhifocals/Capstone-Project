package com.focals.myreddit;


import com.focals.myreddit.activity.PopularsActivity;
import com.focals.myreddit.utils.TestHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class SubTests {

    @Rule
    public ActivityTestRule<PopularsActivity> popularsActivityActivityTestRule = new ActivityTestRule<>(PopularsActivity.class);

    @Test
    public void testPopularActivity() {

        // Verify title of first item
        onView(withId(R.id.rv_populars)).check(matches(TestHelper.atPosition(0, hasDescendant(hasDescendant(withText("news"))))));

        // Verify image of first item
        onView(withId(R.id.rv_populars)).check(matches(TestHelper.atPosition(0,
                hasDescendant(hasDescendant(withId(R.id.iv_subredditImage))))));

        // Verify description of first item
        onView(withId(R.id.rv_populars)).check(matches(TestHelper.atPosition(0,
                hasDescendant(hasDescendant(withId(R.id.tv_subredditDesc))))));

        // Verify subscriber count of first item
        onView(withId(R.id.rv_populars)).check(matches(TestHelper.atPosition(0,
                hasDescendant(hasDescendant(withId(R.id.tv_subscriberCount))))));

        // Verify add/remove fav button of first item
        onView(withId(R.id.rv_populars)).check(matches(TestHelper.atPosition(0,
                hasDescendant(hasDescendant(withId(R.id.ib_addToFavorites))))));

    }
}

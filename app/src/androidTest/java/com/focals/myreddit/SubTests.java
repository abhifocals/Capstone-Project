package com.focals.myreddit;


import com.focals.myreddit.activity.PopularsActivity;
import com.focals.myreddit.utils.TestHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


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

    @Test
    public void testPostsActivitty() {

        // Click on first Sub to get to Posts Activity
        onView(withText("news")).perform(click());

        // Verify same post is displayed in Posts Activity
        // onView(allOf(withId(R.id.expandedImage), withText("news"))).check(matches(isDisplayed()));

        // Verify add/remove fav button
        onView(withId(R.id.ib_addToFavorites)).check(matches(isDisplayed()));

        // Verify item has comments count
        onView(withId(R.id.rv_posts)).check(matches(TestHelper.atPosition(0,
                hasDescendant((withId(R.id.tv_commentsCount))))));
    }

    @Test
    public void testCommentsActivitty() {

        // Click on first Sub to get to Posts Activity
        onView(withText("news")).perform(click());

        // Click on first Post to get to Comments Activity
        onView(withId(R.id.rv_posts)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Verify same post is displayed in Posts Activity
        // onView(allOf(withId(R.id.expandedImage), withText("news"))).check(matches(isDisplayed()));

        // Verify add/remove fav button
        onView(withId(R.id.ib_addToFavorites)).check(matches(isDisplayed()));

        onView(withId(R.id.tv_postText)).check(matches(isDisplayed()));

        // Verify item has comments count
        onView(allOf(withId(R.id.rv_comments))).
                check(matches(hasDescendant(hasDescendant(withId(R.id.tv_comment))))).
                check(matches(isDisplayed()));
    }
}

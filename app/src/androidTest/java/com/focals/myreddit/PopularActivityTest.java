package com.focals.myreddit;


import com.focals.myreddit.activity.PopularsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

@RunWith(AndroidJUnit4.class)
public class PopularActivityTest {

    @Rule
    public ActivityTestRule<PopularsActivity> popularsActivityActivityTestRule = new ActivityTestRule<>(PopularsActivity.class);

    @Test
    public void testPopularActivity() {
        
        System.out.println();


    }




}

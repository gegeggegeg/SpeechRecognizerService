package com.example.peterphchen.speechrecognizerservice;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SillyTest {
    @Rule public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class,true);

    @Test
    public void thisIsReallySilly(){

    }

}

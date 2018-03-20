package com.example.android.bakingapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.DisplayMetrics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
//Check Stack overflow for solution to test for phone or tablet
//https://stackoverflow.com/questions/26231752/android-espresso-tests-for-phone-and-tablet

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest extends ActivityInstrumentationTestCase2<RecipeDetailActivity>{

        private RecipeDetailActivity mActivity;
        private boolean mIsScreenSw600dp;

        public RecipeDetailActivityTest() {
            super(RecipeDetailActivity.class);
        }

        @Before
        public void setUp() throws Exception {
            injectInstrumentation(InstrumentationRegistry.getInstrumentation());
            setActivityInitialTouchMode(false);
            mActivity = this.getActivity();
            mIsScreenSw600dp = isScreenSw600dp();
        }

        @After
        public void tearDown() throws Exception {
            mActivity.finish();
        }

        @Test
        public void checkInstructionsFragmentTabletOnly(){
            if (!mIsScreenSw600dp) {
                //This test is only run on a phone as this fragment does not exist on the tablet in
                //the RecipeDetailActivity. Run RecipeActivity to test this on a Tablet
                InstructionsFragment fragment = new InstructionsFragment();
                mActivity.getSupportFragmentManager()
                        .beginTransaction().add(R.id.instruction_layout, fragment).commit();
                onView(withId(R.id.instruction_layout))
                        .check(matches( hasDescendant(withId(R.id.instructions_fragment_tv))));
            }
        }

        @Test
        public void checkVideoFragmentTabletOnly(){
            if (!mIsScreenSw600dp) {
                //This test is only run on a phone as this fragment does not exist on the tablet in
                //the RecipeDetailActivity. Run RecipeActivity to test this on a Tablet
                VideoFragment fragment = new VideoFragment();
                mActivity.getSupportFragmentManager()
                        .beginTransaction().add(R.id.video_layout, fragment).commit();
                onView(withId(R.id.video_layout))
                        .check(matches( hasDescendant(withId(R.id.video_view))));
                onView(withId(R.id.video_layout))
                        .check(matches( hasDescendant(withId(R.id.video_error_tv))));
                onView(withId(R.id.video_layout))
                        .check(matches( hasDescendant(withId(R.id.video_progressBar))));

            }
        }

        private boolean isScreenSw600dp() {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float widthDp = displayMetrics.widthPixels / displayMetrics.density;
            float heightDp = displayMetrics.heightPixels / displayMetrics.density;
            float screenSw = Math.min(widthDp, heightDp);
            return screenSw >= 600;
        }
    }

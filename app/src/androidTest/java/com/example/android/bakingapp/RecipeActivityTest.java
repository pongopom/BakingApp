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
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)

//Check Stack overflow for solution to test for phone or tablet
//https://stackoverflow.com/questions/26231752/android-espresso-tests-for-phone-and-tablet

public class RecipeActivityTest extends ActivityInstrumentationTestCase2<RecipeActivity> {

    private RecipeActivity mActivity;
    private boolean mIsScreenSw600dp;

    public RecipeActivityTest() {
        super(RecipeActivity.class);
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
    public void checkIngredientsFragmentPhoneAndTablet() {
            IngredientsFragment fragment = new IngredientsFragment();
       mActivity.getSupportFragmentManager()
                .beginTransaction().add(R.id.ingredient_layout, fragment).commit();
        onView(withId(R.id.ingredient_layout))
                .check(matches( hasDescendant(withText(""))));
       onView(withId(R.id.ingredient_layout))
               .check(matches( hasDescendant(withId(R.id.ingredient_fragment_tv))));
    }

        @Test
    public void checkStepsFragmentPhoneAndTablet() {
       StepsFragment fragment = new StepsFragment();
        mActivity.getSupportFragmentManager()
                .beginTransaction().add(R.id.step_layout, fragment).commit();
        onView(withId(R.id.step_layout))
                .check(matches( hasDescendant(withId(R.id.steps_rv))));

    }

    @Test
    public void checkInstructionsFragmentTabletOnly(){
        if (mIsScreenSw600dp) {
            //This test is only run on a tablet as this fragment does not exist on the phone in
            //the RecipeActivity. Run RecipeDetailActivity to test this on a phone
            InstructionsFragment fragment = new InstructionsFragment();
            mActivity.getSupportFragmentManager()
                    .beginTransaction().add(R.id.instruction_layout, fragment).commit();
            onView(withId(R.id.instruction_layout))
                    .check(matches( hasDescendant(withId(R.id.instructions_fragment_tv))));
        }
    }

    @Test
    public void checkVideoFragmentTabletOnly(){
        if (mIsScreenSw600dp) {
            //This test is only run on a tablet as this fragment does not exist on the phone in
            //the RecipeActivity. Run RecipeDetailActivity to test this on a phone
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






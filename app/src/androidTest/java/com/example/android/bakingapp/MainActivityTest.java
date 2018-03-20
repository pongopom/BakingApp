package com.example.android.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.bakingapp.utils.EspressoTestingIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.android.exoplayer2.util.Assertions.checkNotNull;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule public ActivityTestRule<MainActivity>mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        // Volley start background task
        IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource()); }

    @After
    public void unregisterIdlingResource()
        //Volley end background task
    { IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.getIdlingResource()); }


    @Test
    public void RecyclerViewIsBeingDisplayed()  {
        onView(withId(R.id.bake_rv)).check(matches(isDisplayed()));
    }

@Test
    public void RecyclerViewTextViewsHaveCorrectText()  {
        onView(withId(R.id.bake_rv))
                .check(matches(atPosition(1, hasDescendant(withText("Brownies")))));
        onView(withId(R.id.bake_rv))
               .check(matches(atPosition(1, hasDescendant(withText("serves 8")))));

    }

    @Test
    public void TestClickOnRecyclerView() throws InterruptedException {
         onView(withId(R.id.bake_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
    }

    @Test
   public void testIntent() {
        Intent intent = new Intent(mainActivityActivityTestRule.getActivity(),MainActivity.class);
        mainActivityActivityTestRule.launchActivity(intent);
    }


    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                // has no item on such position
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

}

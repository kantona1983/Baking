package com.basha.karim.baking;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.basha.karim.baking.UI.RecipesActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by karim on 4/8/2018.
 */

@RunWith(AndroidJUnit4.class)
public class UITests {

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityTestRule = new ActivityTestRule<> (RecipesActivity.class);

    @Test
    public void checkRecyclerViewRecipeText() {
        onView(ViewMatchers.withId(R.id.rv_recipes)).perform(RecyclerViewActions.scrollToPosition(2));
        onView(withText("Yellow Cake")).check(matches(isDisplayed()));
    }

    @Test
    public void checkDetailActivityStepRecyclerViewHeritage() {
        onView(ViewMatchers.withId(R.id.rv_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(ViewMatchers.withId(R.id.rv_detail_steps)).check(matches(isDescendantOfA(withId(R.id.sv_detail_steps))));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}


package com.bakeit.balascageorge.bakeit;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class LoadingFragmentsTest {

    private static final Intent MY_ACTIVITY_INTENT = new Intent(InstrumentationRegistry.getTargetContext(), RecipeDetailsActivity.class);

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> mActivityTestRule = new ActivityTestRule<>(RecipeDetailsActivity.class);

    @Before
    public void setup() {
        mActivityTestRule.launchActivity(MY_ACTIVITY_INTENT);
    }

    @Test
    public void loadingRecipeDetailsFragment_IntoDetailsActivity() {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_details_fragment_id, fragment).commit();

        onView(withId(R.id.recycler_view)).check(matches((isEnabled())));
    }

    @Test
    public void loadingRecipeStepDetailsFragment_IntoDetailsActivity() {
        RecipeStepDetailsFragment fragment = new RecipeStepDetailsFragment();
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_details_fragment_id, fragment).commit();

        onView(withId(R.id.player_view)).check(matches((isDisplayed())));
    }

}


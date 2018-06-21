package com.bakeit.balascageorge.bakeit;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.hamcrest.Matchers.anything;

import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class OpenRecipeDetailsTest {

    public static final String INGREDIENTS = "Ingredients";
    private static final Intent MY_ACTIVITY_INTENT = new Intent(InstrumentationRegistry.getTargetContext(), RecipesActivity.class);

    @Rule
    public IntentsTestRule<RecipesActivity> intentsTestRule = new IntentsTestRule<>(RecipesActivity.class);

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> mDetailsActivityRule = new ActivityTestRule<>(
            RecipeDetailsActivity.class,
            true,
            false); // Activity is not launched immediately


    @Before
    public void setup() {
    }

    @Test
    public void clickGridViewItem_OpensRecipeDetailsActivity() {

        onData(anything()).inAdapterView(withId(R.id.recipes_grid)).atPosition(1).perform(click());

        intended(hasComponent(RecipeDetailsActivity.class.getName()));

//        RecipeDetailsFragment fragment = new RecipeDetailsFragment();

        // TODO: make details activity load content from net, so I can test properly! I cannot use the second activity..

//        mDetailsActivityRule = this.
//        mDetailsActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
//                .add(R.id.details_fragment_container, fragment).commit();
//
//        onView(withId(R.id.details_fragment_container)).check(matches((isDisplayed())));

    }

}

package com.bakeit.balascageorge.bakeit;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.core.internal.deps.guava.collect.Iterables;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SupportActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.anything;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;
import  android.support.v7.widget.Toolbar;

import java.util.Collection;


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

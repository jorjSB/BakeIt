package com.bakeit.balascageorge.bakeit;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bakeit.balascageorge.bakeit.models.Recipe;
import com.bakeit.balascageorge.bakeit.utils.JsonUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;


@RunWith(AndroidJUnit4.class)
public class RecipeObjectParcelableTest {

    Intent intent;
    ArrayList<Recipe> recipesArray;

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> recipeDetailsTestRule = new ActivityTestRule<>(
            RecipeDetailsActivity.class,
            true,
            false); // Activity is not launched immediately


//    @Rule
//    public IntentsTestRule<RecipeDetailsActivity> recipeDetailsTestRule = new IntentsTestRule<>(RecipeDetailsActivity.class);

    @Before
    public void setUp() {
        intent = new Intent();
        Context context = getInstrumentation().getTargetContext();

        InputStream in = recipeDetailsTestRule.getClass().getClassLoader().getResourceAsStream("data.json");
        String data = convertStreamToString(in);

        recipesArray = JsonUtils.getRecipesArray(data);
    }

    @Test
    public void recipe_is_parcelable() {

        Recipe recipe = recipesArray.get(0);

        Parcel parcel = Parcel.obtain();
        recipe.writeToParcel(parcel, recipe.describeContents());
        parcel.setDataPosition(0);

        Recipe createdFromParcel = (Recipe) Recipe.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getId(), is(1));
        assertThat(createdFromParcel.getName(), is("Nutella Pie"));
    }


    @Test
    public void new_activity_init_properly_and_loads_fragment_recycler_view(){

        intent.putExtra("recipe", recipesArray.get(0));
        recipeDetailsTestRule.launchActivity(intent);

        onView(withId(R.id.recycler_view)).check(matches((isDisplayed())));

        onView(withText("Ingredients")).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

        onView(withText("Graham Cracker crumbs")).check(matches(isDisplayed()));
    }



    // helper method to convert locally saved json
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
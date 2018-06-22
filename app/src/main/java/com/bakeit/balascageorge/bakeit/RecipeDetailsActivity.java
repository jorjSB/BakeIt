package com.bakeit.balascageorge.bakeit;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.bakeit.balascageorge.bakeit.models.Ingredient;
import com.bakeit.balascageorge.bakeit.models.Recipe;
import com.bakeit.balascageorge.bakeit.models.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity implements
        RecipeDetailsFragment.OnFragmentInteractionListener{

    @Nullable
    @BindView(R.id.details_fragment_container)
    FrameLayout mFragmentContainer;

    private Recipe mRecipe;
    private boolean mTwoPane;
    private FragmentManager fragmentManager;

    private RecipeDetailsFragment recipeDetailsFragment;
    private RecipeIngredientsFragment recipeIngredientsFragment;
    private RecipeStepDetailsFragment recipeStepDetailsFragment;

    private static final String TAG_DETAILS_FRAGMENT = "details_fragment";
    private static final String TAG_INGREDIENTS_FRAGMENT = "ingredients_fragment";
    private static final String TAG_STEP_DETAILS_FRAGMENT = "step_details_fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        // if mTwoPane
        if(mFragmentContainer != null)
            mTwoPane = true;


        fragmentManager = getSupportFragmentManager();
        // Initially display the ingredients list(nothing clicked)
        recipeDetailsFragment = (RecipeDetailsFragment) fragmentManager.findFragmentByTag(TAG_DETAILS_FRAGMENT);
        recipeIngredientsFragment = (RecipeIngredientsFragment) fragmentManager.findFragmentByTag(TAG_INGREDIENTS_FRAGMENT);
        recipeStepDetailsFragment = (RecipeStepDetailsFragment) fragmentManager.findFragmentByTag(TAG_STEP_DETAILS_FRAGMENT);

        // init the list with details
        if(mRecipe == null) {
            Bundle data = getIntent().getExtras();
            mRecipe = data != null ? (Recipe) data.getParcelable("recipe") : null;
            if(mRecipe != null)
                setFragmentRecipeDetails();
        }

        // if tablet add the ingredients list
        if(mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelableArrayList("ingredients", (ArrayList<Ingredient>) mRecipe.getIngredients());

            if(recipeIngredientsFragment == null){
                recipeIngredientsFragment = new RecipeIngredientsFragment();
                recipeIngredientsFragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .add(R.id.details_fragment_container, recipeIngredientsFragment, TAG_INGREDIENTS_FRAGMENT)
                        .addToBackStack(null)
                        .commit();
            }
        }

    }

    /**
     * Sdets the list with ingredients/steps
     */
    private void setFragmentRecipeDetails() {
        Bundle args = new Bundle();
        args.putParcelable("recipe", mRecipe);

        if(recipeDetailsFragment == null){
            recipeDetailsFragment = new RecipeDetailsFragment();
            recipeDetailsFragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_details_fragment_id, recipeDetailsFragment, TAG_DETAILS_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * Handle here the Fragment state and navigation, based on the screen size
     * @param value
     */
    @Override
    public void onItemSelected(Object value) {
        if (value != null)
            // step
            if (value instanceof Step) {

                // tablet
                if (mTwoPane) {
                    if (recipeStepDetailsFragment == null)
                        recipeStepDetailsFragment = new RecipeStepDetailsFragment();

                    Bundle args = new Bundle();
                    args.putParcelable("step", (Step) value);
                    args.putBoolean("twoPane", mTwoPane);
                    recipeStepDetailsFragment.setArguments(args);

                    replaceFragment(R.id.details_fragment_container, recipeStepDetailsFragment, TAG_STEP_DETAILS_FRAGMENT);

                     if(recipeStepDetailsFragment.isResumed())
                        recipeStepDetailsFragment.updateView((Step) value);

                    // phone
                } else {
                    Bundle args = new Bundle();
                    args.putParcelable("step", (Step) value);

                    if(recipeStepDetailsFragment == null)
                        recipeStepDetailsFragment = new RecipeStepDetailsFragment();

                    recipeStepDetailsFragment.setArguments(args);

                    replaceFragment(R.id.recipe_details_fragment_id, recipeStepDetailsFragment, TAG_STEP_DETAILS_FRAGMENT);
                }
            // ingredients
            } else if(value instanceof ArrayList<?>){
                // tablet
                if (mTwoPane) {

                    fragmentManager.popBackStack(TAG_STEP_DETAILS_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    if(recipeIngredientsFragment == null)
                        recipeIngredientsFragment = new RecipeIngredientsFragment();

                    replaceFragment(R.id.details_fragment_container, recipeIngredientsFragment, TAG_INGREDIENTS_FRAGMENT);

                }
                // phone
                else {
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("ingredients", (ArrayList<Ingredient>) value);

                    if(recipeIngredientsFragment == null)
                        recipeIngredientsFragment = new RecipeIngredientsFragment();

                    recipeIngredientsFragment.setArguments(args);

                    replaceFragment(R.id.recipe_details_fragment_id, recipeIngredientsFragment, TAG_INGREDIENTS_FRAGMENT);
                }
            }
    }

    @Override
    public void onBackPressed(){
        if(mTwoPane || fragmentManager.getBackStackEntryCount() == 1)
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        onCreate(savedInstanceState);
    }


    private void replaceFragment (int placeholderID , Fragment fragment, String tag){
        boolean fragmentPopped = fragmentManager.popBackStackImmediate (tag, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            fragmentManager.beginTransaction()
                .replace(placeholderID, fragment)
                .addToBackStack(tag)
                .commit();
        }
    }

}

package com.bakeit.balascageorge.bakeit;

import android.app.Fragment;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

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
    private String TAG = RecipeDetailsActivity.class.getSimpleName();
    private RecipeDetailsFragment recipeDetailsFragment;
    private boolean mTwoPane;
    private FragmentManager fragmentManager;
    private RecipeIngredientsFragment recipeIngredientsFragment;
    private RecipeStepDetailsFragment recipeStepDetailsFragment;

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
        recipeDetailsFragment = new RecipeDetailsFragment();
        recipeIngredientsFragment = new RecipeIngredientsFragment();
        recipeStepDetailsFragment = new RecipeStepDetailsFragment();

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
            recipeIngredientsFragment.setArguments(args);

            fragmentManager.beginTransaction()
                    .add(R.id.details_fragment_container, recipeIngredientsFragment, RecipeIngredientsFragment.class.getSimpleName())
                    .commit();
        }

    }

    /**
     * Sdets the list with ingredients/steps
     */
    private void setFragmentRecipeDetails() {
        Bundle args = new Bundle();
        args.putParcelable("recipe", mRecipe);
        recipeDetailsFragment.setArguments(args);

        fragmentManager.beginTransaction()
                .add(R.id.recipe_details_fragment_id, recipeDetailsFragment, RecipeDetailsFragment.class.getSimpleName())
                .commit();
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
            if (value != null && value instanceof Step) {

                // tablet
                if (mTwoPane) {
                    RecipeStepDetailsFragment f = (RecipeStepDetailsFragment) fragmentManager.findFragmentByTag(RecipeStepDetailsFragment.class.getSimpleName());

                    if (f == null) {
                        Bundle args = new Bundle();
                        args.putParcelable("step", (Step) value);
                        args.putBoolean("twoPane", mTwoPane);
                        recipeStepDetailsFragment.setArguments(args);

                        fragmentManager.beginTransaction()
                                .replace(R.id.details_fragment_container, recipeStepDetailsFragment, RecipeStepDetailsFragment.class.getSimpleName())
                                .commit();
                    } else if (f != null)
                        f.updateView((Step) value);

                    // phone
                } else {
                    Bundle args = new Bundle();
                    args.putParcelable("step", (Step) value);
                    recipeStepDetailsFragment.setArguments(args);

                    fragmentManager.beginTransaction()
                            .replace(R.id.recipe_details_fragment_id, recipeStepDetailsFragment, RecipeStepDetailsFragment.class.getSimpleName())
                            .addToBackStack(null)
                            .commit();
                }
            // ingredients
            } else if(value != null  && value instanceof ArrayList<?>){
            // tablet
                if (mTwoPane) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.details_fragment_container, recipeIngredientsFragment, RecipeIngredientsFragment.class.getSimpleName())
                            .commit();
                }
                // phone
                else {
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("ingredients", (ArrayList<Ingredient>) value);
                    recipeIngredientsFragment.setArguments(args);

                    fragmentManager.beginTransaction()
                            .replace(R.id.recipe_details_fragment_id, recipeIngredientsFragment, RecipeIngredientsFragment.class.getSimpleName())
                            .addToBackStack(null)
                            .commit();
                }
            }
    }

    @Override
    public void onBackPressed(){
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
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
        onCreate(savedInstanceState);
    }

}

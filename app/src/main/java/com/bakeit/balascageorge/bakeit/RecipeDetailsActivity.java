package com.bakeit.balascageorge.bakeit;

import android.app.Fragment;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bakeit.balascageorge.bakeit.dummy.DummyContent;
import com.bakeit.balascageorge.bakeit.models.Ingredient;
import com.bakeit.balascageorge.bakeit.models.Recipe;
import com.bakeit.balascageorge.bakeit.models.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity implements
        RecipeDetailsFragment.OnFragmentInteractionListener,
        RecipeIngredientsFragment.OnListFragmentInteractionListener,
        RecipeStepDetailsFragment.OnFragmentInteractionListener{

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

        if(mRecipe == null) {
            Bundle data = getIntent().getExtras();
            mRecipe = data != null ? (Recipe) data.getParcelable("recipe") : null;
            if(mRecipe != null)
                setFragmentRecipeDetails();
        }

        // if mTwoPane
        if(mFragmentContainer != null)
            mTwoPane = true;


        // Initially display the ingredients list(nothing clicked)
        recipeIngredientsFragment = new RecipeIngredientsFragment();
        recipeStepDetailsFragment = new RecipeStepDetailsFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.details_fragment_container, recipeIngredientsFragment, RecipeIngredientsFragment.class.getSimpleName())
                .commit();

    }

    private void setFragmentRecipeDetails() {
        if (getSupportFragmentManager().findFragmentById(R.id.recipe_details_fragment_id) != null) {
            recipeDetailsFragment = (RecipeDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_details_fragment_id);
            recipeDetailsFragment.setRecipe(mRecipe);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onItemSelected(Object value) {

        if (value != null && value instanceof Step) {
            RecipeStepDetailsFragment f = (RecipeStepDetailsFragment) fragmentManager.findFragmentByTag(RecipeStepDetailsFragment.class.getSimpleName());

            if(f == null){
                Bundle args = new Bundle();
                args.putParcelable("step", (Step) value);
                recipeStepDetailsFragment.setArguments(args);

                fragmentManager.beginTransaction()
                        .replace(R.id.details_fragment_container, recipeStepDetailsFragment, RecipeStepDetailsFragment.class.getSimpleName() )
                        .commit();
            }else if (f != null)
                f.updateView((Step) value);

        } else if(value != null  && value instanceof ArrayList<?>){
            // Toast.makeText(this,"Clicked ingredient " + ((ArrayList<Ingredient>) value).get(0).getIngredient(), Toast.LENGTH_SHORT).show();
            int replaced = fragmentManager.beginTransaction()
                    .replace(R.id.details_fragment_container, recipeIngredientsFragment, RecipeIngredientsFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

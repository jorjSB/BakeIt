package com.bakeit.balascageorge.bakeit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.bakeit.balascageorge.bakeit.adapters.RecipesAdapter;
import com.bakeit.balascageorge.bakeit.models.Recipe;
import com.bakeit.balascageorge.bakeit.utils.JsonUtils;
import com.bakeit.balascageorge.bakeit.utils.NetUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesActivity extends AppCompatActivity {

    @BindView(R.id.no_internet)
    TextView noInternetTV;

    @BindView(R.id.recipes_grid)
    GridView gridView;

    private RecipesAdapter adapter;
    private ArrayList<Recipe> recipesArray;
    private String TAG = RecipesActivity.class.getSimpleName();
    private int lastListPosition = -1;
    int recipeIdExtra = -1;
    private String LIST_POSITION_STATE_KEY = "last_position";
    private String RECIPES_ARRAY_STATE_KEY = "recipes_array";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        if(getIntent().getData() != null && getIntent().getData().getSchemeSpecificPart() != null)
            recipeIdExtra = Integer.valueOf(getIntent().getData().getSchemeSpecificPart());
//        recipeIdExtra = getIntent().getIntExtra("recipeId", -1);

        recipesArray = new ArrayList<>();
        adapter = new RecipesAdapter(this,
                recipesArray, false);

        // attach the adapter to the GridView
        gridView.setAdapter(adapter);

        // if Tablet change the grid column numbers.
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            gridView.setNumColumns(4);
        }

        loadRecipesData();
    }

    /**
     * Loads the initial data
     */
    private void loadRecipesData() {
        // fetch result
        if(NetUtils.isOnline(this)){
            new AsyncFetchData().execute(NetUtils.DATA_URL);
            noInternetTV.setVisibility(View.GONE);
        } else {
            noInternetTV.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        }
    }

    /**
     * AsyncTask to fetch data from the internet (avoid networking in main tread) add inflate the views
     */
    public class AsyncFetchData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                // use netUtils to fetch data from url param provided
                return NetUtils.getResponseFromHttpUrl(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String data) {
            if (data != null) {
                recipesArray = JsonUtils.getRecipesArray(data);
                updateGridView();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if(gridView != null)
            lastListPosition = gridView.getFirstVisiblePosition();

        // Save the state of item position
        outState.putInt(LIST_POSITION_STATE_KEY, lastListPosition);
        outState.putParcelableArrayList(RECIPES_ARRAY_STATE_KEY, recipesArray);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Read the state of item position
        lastListPosition = savedInstanceState.getInt(LIST_POSITION_STATE_KEY);
        recipesArray = savedInstanceState.getParcelableArrayList(RECIPES_ARRAY_STATE_KEY);

    }

    /**
     * Create adapter and inflate the grid view
     */
    private void updateGridView() {
            // reset views
            gridView.setVisibility(View.VISIBLE);

            // notify adapter, update data
            adapter.updateAdapter(recipesArray);

        //         scroll to position
            if(lastListPosition != -1)
                gridView.setSelection(lastListPosition);

        if(recipeIdExtra != -1){
            for (Recipe recipe: recipesArray) {
                if(recipe.getId() == recipeIdExtra){
                    Class destinationClass = RecipeDetailsActivity.class;
                    Intent intentToStartDetailActivity = new Intent(this, destinationClass);
                    intentToStartDetailActivity.putExtra("recipe", recipe);
                    this.startActivity(intentToStartDetailActivity);
                }
            }
        }

    }
}

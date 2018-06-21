package com.bakeit.balascageorge.bakeit;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bakeit.balascageorge.bakeit.adapters.RecipesArrayAdapter;
import com.bakeit.balascageorge.bakeit.models.Ingredient;
import com.bakeit.balascageorge.bakeit.models.Recipe;
import com.bakeit.balascageorge.bakeit.utils.JsonUtils;
import com.bakeit.balascageorge.bakeit.utils.NetUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetConfigurationActivity extends AppCompatActivity {

    private ArrayList<Recipe> recipesArray;
    private RecipesArrayAdapter adapter;
    private Context mContext;

    @BindView(R.id.recipes_grid)
    GridView gridView;

    @BindView(R.id.no_internet)
    TextView noInternetTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);
        setResult(RESULT_CANCELED);

        mContext = this;

        recipesArray = new ArrayList<>();
        adapter = new RecipesArrayAdapter(mContext,
                recipesArray, true);

        // attach the adapter to the GridView
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleSetupWidget(position);
            }
        });

        loadRecipesData();

    }


    int appWidgetId;

    private void handleSetupWidget(int position) {
        appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

        //Retrieve the App Widget ID from the Intent that launched the Activity//

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            //If the intent doesn’t have a widget ID, then call finish()//

            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }

            //TO DO, Perform the configuration and get an instance of the AppWidgetManager//
            // saveIntervalPref(this, appWidgetId, "extra");
            saveIntervalPref(mContext, appWidgetId, recipesArray.get(position).getIngredients(), recipesArray.get(position).getName());

            // Push widget update to surface with newly set data
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            RecipeWidgetProvider.updateAppWidget(this, appWidgetManager,
                    appWidgetId);


            //Create the return intent//
            Intent resultValue = new Intent();

            //Pass the original appWidgetId//
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            //Set the results from the configuration Activity//
            setResult(RESULT_OK, resultValue);

            //Finish the Activity//
            finish();
        }
    }

    // Store in shared prefs the name and ingredients for the recipe selected
    static void saveIntervalPref(Context context, int appWidgetId, ArrayList<Ingredient> ingredients, String name) {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();

        Set<String> ingredientsSet = parseRecipeToIngredientsList(ingredients);
        prefs.putStringSet(String.valueOf(appWidgetId),  ingredientsSet);
        prefs.putString("name_" + String.valueOf(appWidgetId),  name);

        prefs.commit();
    }

    // returns the ingredients based in widget id
    static Set<String> loadRecipeIngredientsPref(Context context, int appWidgetId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Set<String> ingredients = prefs.getStringSet(String.valueOf(appWidgetId), null);
        return ingredients;
    }

    // returns the recipe name based in widget id
    static String loadRecipeNamePref(Context context, int appWidgetId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String name = prefs.getString("name_" + String.valueOf(appWidgetId), context.getResources().getString(R.string.na));
        return name;
    }

    private static Set<String> parseRecipeToIngredientsList(ArrayList<Ingredient> ingredients) {
        HashSet<String> stringIngredients = new HashSet<String>();

        for (Ingredient ingredient: ingredients) {
            if (ingredient != null)
            stringIngredients.add(  ingredient.getQuantity() + " " + ingredient.getMeasure() + "(s) of " +  ingredient.getIngredient() );
        }
        return stringIngredients;
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

    /**
     * Create adapter and inflate the grid view
     */
    private void updateGridView() {
        // reset views
        gridView.setVisibility(View.VISIBLE);

        // notify adapter, update data
        adapter.updateAdapter(recipesArray);
    }

    /**
     * Loads the initial data
     */
    private void loadRecipesData() {
        // fetch result
        if(NetUtils.isOnline(this)){
            new AsyncFetchData().execute(NetUtils.DATA_URL);
            noInternetTV.setVisibility(View.GONE);
            Toast.makeText(this, getString(R.string.widget_config_text), Toast.LENGTH_LONG).show();
        } else {
            noInternetTV.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        }
    }

}

package com.bakeit.balascageorge.bakeit;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.bakeit.balascageorge.bakeit.adapters.RecipesArrayAdapter;
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

    private RecipesArrayAdapter adapter;
    private ArrayList<Recipe> recipesArray;
    private String TAG = RecipesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        recipesArray = new ArrayList<>();
        adapter = new RecipesArrayAdapter(this,
                recipesArray);

        // attach the adapter to the GridView
        gridView.setAdapter(adapter);

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

    /**
     * Create adapter and inflate the grid view
     */
    private void updateGridView() {
        // reset views
        gridView.setVisibility(View.VISIBLE);

        // notify adapter, update data
        adapter.updateAdapter(recipesArray);

        // scroll to position
//        if(lastListPosition != -1)
//            gridView.setSelection(lastListPosition);

    }
}

package com.bakeit.balascageorge.bakeit.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bakeit.balascageorge.bakeit.R;
import com.bakeit.balascageorge.bakeit.RecipeDetailsActivity;
import com.bakeit.balascageorge.bakeit.models.Recipe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesArrayAdapter extends ArrayAdapter<Recipe> {

    private ArrayList<Recipe> mMovies;
    private Context mContext;
    private static final String TAG = RecipesArrayAdapter.class.getSimpleName();

    public RecipesArrayAdapter(Context ctx, ArrayList<Recipe> movies){
        super(ctx, 0, movies);
        mMovies = movies;
        mContext = ctx;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position = position of the list
     * @param convertView = the view (Grid View item)
     * @param parent = the Grid
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.recipe_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.recipePosterView.setAdjustViewBounds(true);
        holder.recipePosterView.setPadding(0, 0, 0, 0);


        // Gets the Recipe object from the ArrayAdapter at the appropriate position
        final Recipe mRecipe = mMovies.get(position);


        // check load the appropiate image(FROM URL OF FROM LOCAL FILE)
        if(mRecipe != null)
            if( URLUtil.isValidUrl( mRecipe.getImageUrl() ))
                Picasso.get()
                        .load( mRecipe.getImageUrl() )
                        .error(R.drawable.default_recipe_image)
                        .into(holder.recipePosterView);

        holder.recipeNameTV.setText(mRecipe.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class destinationClass = RecipeDetailsActivity.class;
                Intent intentToStartDetailActivity = new Intent(mContext, destinationClass);
                intentToStartDetailActivity.putExtra("recipe", mRecipe);
                mContext.startActivity(intentToStartDetailActivity);
            }
        });

        return convertView;
    }

    public void updateAdapter(ArrayList<Recipe> newMovies){
        this.mMovies.clear();
        this.mMovies.addAll(newMovies);
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.recipe_image)
        ImageView recipePosterView;

        @BindView(R.id.recipe_name)
        TextView recipeNameTV;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

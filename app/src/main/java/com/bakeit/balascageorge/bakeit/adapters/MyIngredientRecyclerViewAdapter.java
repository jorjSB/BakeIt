package com.bakeit.balascageorge.bakeit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bakeit.balascageorge.bakeit.R;
import com.bakeit.balascageorge.bakeit.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyIngredientRecyclerViewAdapter extends RecyclerView.Adapter<MyIngredientRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Ingredient> mIngredients;

    public MyIngredientRecyclerViewAdapter(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText( mIngredients.get(position).getIngredient());
        holder.mContentView.setText(mIngredients.get(position).getQuantity().toString()
                + " ("
                + mIngredients.get(position).getMeasure()
                + ")");
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_number)
        TextView mIdView;

        @BindView(R.id.content)
        TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

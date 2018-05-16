package com.bakeit.balascageorge.bakeit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bakeit.balascageorge.bakeit.R;
import com.bakeit.balascageorge.bakeit.RecipeDetailsActivity;
import com.bakeit.balascageorge.bakeit.models.Ingredient;
import com.bakeit.balascageorge.bakeit.models.Recipe;
import com.bakeit.balascageorge.bakeit.models.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_INGREDIENTS = 0;
    private final int VIEW_TYPE_STEP = 1;
    private final OnItemClick mCallback;

    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;
    private Context mContext;
    private  static  final String TAG = RecipeDetailsActivity.class.getSimpleName();

    public RecipeDetailsArrayAdapter(Context mContext, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, OnItemClick listener) {
        this.mContext = mContext;
        this.mIngredients = ingredients;
        this.mSteps = steps;
        this.mCallback = listener;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_TYPE_INGREDIENTS;
        else
            return VIEW_TYPE_STEP;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_INGREDIENTS: {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item, parent, false);
                return new ViewHolderIngredient(itemView);
            }
            default: {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
                return new ViewHolderStep(itemView);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_INGREDIENTS:
                ViewHolderIngredient viewHolderIngredient = (ViewHolderIngredient) holder;
                if (mIngredients != null)
                    viewHolderIngredient.bindViews(position);
                break;
            default:
                ViewHolderStep viewHolderStep = (ViewHolderStep) holder;
                if (mSteps != null)
                    viewHolderStep.bindViews(position - 1);
                break;

        }
    }

    @Override
    public int getItemCount() {
        int total = 1;

        if(mSteps != null && mSteps.size() != 0)
            total += mSteps.size();

        return total;
    }


    /**
     * Ingredient ViewHolder
     */
    public class ViewHolderIngredient extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredientsTV)
        TextView ingredientsTV;

        private View mView;

        public ViewHolderIngredient(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mView = itemView;
        }

        public void bindViews(final int position){
            ingredientsTV.setText(mIngredients.get(position).getIngredient());
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onClick(mIngredients);
                }
            });
        }
    }

    /**
     * Step ViewHolder
     */
    public class ViewHolderStep extends RecyclerView.ViewHolder {
        @BindView(R.id.stepTV)
        TextView stepTV;

        private View mView;

        public ViewHolderStep(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mView = itemView;
        }

        public void bindViews(final int position){
            stepTV.setText(mSteps.get(position).getDescription());
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onClick(mSteps.get(position) );
                }
            });
        }
    }

    public void update(Recipe mRecipe) {
        this.mIngredients = mRecipe.getIngredients();
        this.mSteps = mRecipe.getSteps();
        notifyDataSetChanged();
    }


    // interface - listens to clicks
    public interface OnItemClick {
        void onClick(Object value);
    }
}

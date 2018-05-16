package com.bakeit.balascageorge.bakeit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bakeit.balascageorge.bakeit.adapters.RecipeDetailsArrayAdapter;
import com.bakeit.balascageorge.bakeit.models.Ingredient;
import com.bakeit.balascageorge.bakeit.models.Recipe;
import com.bakeit.balascageorge.bakeit.models.Step;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailsFragment#} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsFragment extends Fragment implements RecipeDetailsArrayAdapter.OnItemClick{
    private RecyclerView recyclerView;
    private RecipeDetailsArrayAdapter recipeDetailsArrayAdapter;
    private Recipe mRecipe;

    private OnFragmentInteractionListener mListener;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Load the saved state (the list of images and list index) if there is one
        if(savedInstanceState != null) {
//            mImageIds = savedInstanceState.getIntegerArrayList(IMAGE_ID_LIST);
//            mListIndex = savedInstanceState.getInt(LIST_INDEX);
        }

        // Inflate the Android-Me fragment layout

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        // get the view ref.
        recyclerView = rootView.findViewById(R.id.recycler_view);

        recipeDetailsArrayAdapter = new RecipeDetailsArrayAdapter(getContext(), null, null, this);
        recyclerView.setAdapter(recipeDetailsArrayAdapter);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setRecipe(Recipe recipe) {
        this.mRecipe = recipe;
         inflateRecipeDetails();
    }

    @Override
    public void onClick(Object value) {
        mListener.onItemSelected(value);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onItemSelected(Object object);
    }

    private void inflateRecipeDetails() {
        recipeDetailsArrayAdapter.update(mRecipe);
    }
}

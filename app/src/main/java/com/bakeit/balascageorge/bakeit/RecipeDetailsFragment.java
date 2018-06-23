package com.bakeit.balascageorge.bakeit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakeit.balascageorge.bakeit.adapters.RecipeDetailsAdapter;
import com.bakeit.balascageorge.bakeit.models.Recipe;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailsFragment#} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsFragment extends Fragment implements RecipeDetailsAdapter.OnItemClick{
    private static final String RECIPE_TAG = "recipe";
    private RecyclerView recyclerView;
    private RecipeDetailsAdapter recipeDetailsAdapter;
    private Recipe mRecipe;

    private OnFragmentInteractionListener mListener;
    private String ADAPTER_SELECTED_POSITION_TAG = "adapterSelectedPosition";
    private Bundle savedState;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        if (getArguments() != null)
            mRecipe = getArguments().getParcelable(RECIPE_TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int adapterSelectedPosition = 0;
        // Load the saved state (the list of images and list index) if there is one
        if(savedState != null)
            adapterSelectedPosition = savedState.getInt(ADAPTER_SELECTED_POSITION_TAG);

        // Inflate the Android-Me fragment layout

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        // get the view ref.
        recyclerView = rootView.findViewById(R.id.recycler_view);

        if(mRecipe != null) {
            recipeDetailsAdapter = new RecipeDetailsAdapter(getContext(), mRecipe.getIngredients(), mRecipe.getSteps(), this, adapterSelectedPosition);
            recyclerView.setAdapter(recipeDetailsAdapter);
            // change title bar
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(mRecipe.getName());
        }
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


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        savedState = new Bundle();
        if(recipeDetailsAdapter != null)
            savedState.putInt(ADAPTER_SELECTED_POSITION_TAG, recipeDetailsAdapter.returnSelectedPosition());
    }

}

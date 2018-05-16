package com.bakeit.balascageorge.bakeit.utils;

import com.bakeit.balascageorge.bakeit.models.Ingredient;
import com.bakeit.balascageorge.bakeit.models.Recipe;
import com.bakeit.balascageorge.bakeit.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class JsonUtils {

    /**
     * Returns a list with all recipe objects after parsing the response to json
     *
     * @param data string fetched from url
     */
    public static ArrayList<Recipe> getRecipesArray(String data){

        // check if data
        if(data == null || data.isEmpty())
            return null;

        // try to convert string result to Json Object
        JSONArray recipesJsonArray;
        try {
            recipesJsonArray = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        // create the array containing objects
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();

        // add objects in the array created
        for(int i=0; i<recipesJsonArray.length(); i++){
            recipeArrayList.add( getRecipeObjectFromJsonObject(recipesJsonArray.optJSONObject(i) ) );
        }

        return recipeArrayList;
    }


    /**
     * Parses a jsonObject into a Recipe object
     *
     * @param data - data fetched from url
     */
    private static Recipe getRecipeObjectFromJsonObject(JSONObject data){

        Recipe recipe = new Recipe();

        recipe.setId(data.optInt("id"));
        recipe.setImageUrl(data.optString("title"));
        recipe.setName(data.optString("name"));
        recipe.setServings(data.optInt("servings"));
        recipe.setIngredients(getRecipeIngredientsArray(data.optJSONArray("ingredients")));
        recipe.setSteps(getRecipeStepsArray(data.optJSONArray("steps")));
        return recipe;
    }


    /**
     * Build Aray with Ingredients
     * @param ingredients
     * @return
     */
    private static ArrayList<Ingredient> getRecipeIngredientsArray(JSONArray ingredients) {
        if (ingredients == null || ingredients.length() == 0)
            return null;

        // create the array containing  objects
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();

        // add objects in the array created
        for(int i=0; i<ingredients.length(); i++){
            ingredientArrayList.add( getIngredientFromJsonObject(ingredients.optJSONObject(i)) );
        }

        return ingredientArrayList;
    }

    /**
     * Parse jsonObj to Ingredient Object
     * @param jsonObject
     * @return
     */
    private static Ingredient getIngredientFromJsonObject(JSONObject jsonObject) {
        Ingredient ingredient = new Ingredient();
        ingredient.setQuantity( BigDecimal.valueOf(jsonObject.optDouble("quantity")).floatValue() );
        ingredient.setMeasure(jsonObject.optString("measure"));
        ingredient.setIngredient(jsonObject.optString("ingredient"));

        return ingredient;
    }

    /**
     * Build Aray with Steps
     * @param steps
     * @return
     */
    private static ArrayList<Step> getRecipeStepsArray(JSONArray steps) {
        if (steps == null || steps.length() == 0)
            return null;

        // create the array containing  objects
        ArrayList<Step> stepsArrayList = new ArrayList<>();

        // add objects in the array created
        for(int i=0; i< steps.length(); i++){
            stepsArrayList.add( getStepFromJsonObject(steps.optJSONObject(i)) );
        }

        return stepsArrayList;
    }

    /**
     * Parse jsonObj to Ingredient Object
     * @param jsonObject
     * @return
     */
    private static Step getStepFromJsonObject(JSONObject jsonObject) {
        Step step = new Step();

        step.setId(jsonObject.optInt("id"));
        step.setShortDescription(jsonObject.optString("shortDescription"));
        step.setDescription(jsonObject.optString("description"));
        step.setVideoURL(jsonObject.optString("videoURL"));
        step.setThumbnailURL(jsonObject.optString("thumbnailURL"));

        return step;
    }
}

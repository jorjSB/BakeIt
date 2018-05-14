package com.bakeit.balascageorge.bakeit.utils;

import com.bakeit.balascageorge.bakeit.models.Recipe;

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

        // create the array containing movie objects
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();

        // add movie objects in the array created
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

        return recipe;
    }
}

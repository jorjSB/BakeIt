package com.bakeit.balascageorge.bakeit.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable{

    private int id;
    private String name;
    private int servings;
    private String imageUrl;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;

    // empty constructor
    public Recipe(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getId());
        dest.writeString(this.getName());
        dest.writeInt(this.getServings());
        dest.writeString(this.getImageUrl());
        dest.writeList(this.getIngredients());
        dest.writeList(this.getSteps());
    }

    // Parcelling part
    private Recipe(Parcel in){
        this.setId(in.readInt());
        this.setName(in.readString());
        this.setServings(in.readInt());
        this.setImageUrl(in.readString());
        this.setIngredients(in.readArrayList(null));
        this.setSteps(in.readArrayList(null));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}

package com.bakeit.balascageorge.bakeit.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{

    private Float quantity;
    private String measure;
    private String ingredient;

    // empty constructor
    public Ingredient(){
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.getQuantity());
        dest.writeString(this.getMeasure());
        dest.writeString(this.getIngredient());
    }

    // Parcelling part
    private Ingredient(Parcel in){
        this.setQuantity(in.readFloat());
        this.setMeasure(in.readString());
        this.setIngredient(in.readString());
    }

    public static final Creator CREATOR = new Creator() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}

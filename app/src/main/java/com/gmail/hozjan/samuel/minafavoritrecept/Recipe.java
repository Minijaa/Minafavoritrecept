package com.gmail.hozjan.samuel.minafavoritrecept;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Recipe {

    private UUID mId;
    private String mName;
    private String mDescription;
    private String mCategory;
    private ArrayList<Ingredient> mIngredients;
    private int mPortions;
    private int mTimeRequired;

    public Recipe() {
        mId = UUID.randomUUID();
        mIngredients = new ArrayList<>();
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public int getPortions() {
        return mPortions;
    }

    public void setPortions(int portions) {
        mPortions = portions;
    }

    public int getTimeRequired() {
        return mTimeRequired;
    }

    public void setTimeRequired(int timeRequired) {
        mTimeRequired = timeRequired;
    }

    public String getImageFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public void addIngredient(Ingredient ingredient) {
        mIngredients.add(ingredient);
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public String getIngredientsAsString() {
        String ingredients = "";
        for (Ingredient i : mIngredients) {
            if (i.getName() != null) {
                ingredients += i.getName() + "\n";
            }
        }
        StringBuilder strB = new StringBuilder(ingredients);
        strB.deleteCharAt(ingredients.length() - 1);
        ingredients = strB.toString();
        return ingredients;
    }

    public void removeIngredient(Ingredient ingredient) {
        mIngredients.remove(ingredient);
    }
}


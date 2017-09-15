package com.gmail.hozjan.samuel.minafavoritrecept;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Recipe implements Serializable{

    private UUID mId;
    private String mName;
    private String mDescription;
    private String mCategory;
    private ArrayList<Ingredient> mIngredients;

    Recipe() {
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

    void setName(String mName) {
        this.mName = mName;
    }

    String getDescription() {
        return mDescription;
    }

    void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    String getCategory() {
        return mCategory;
    }

    void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    String getImageFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    void addIngredient(Ingredient ingredient) {
        mIngredients.add(ingredient);
    }

    List<Ingredient> getIngredients() {
        return mIngredients;
    }

    String getIngredientsAsString() {
        String ingredients = "";
        for (Ingredient i : mIngredients) {
            if (i.getName() != null) {
                ingredients += i.getName() + "\n";
            }
        }
        if (!ingredients.equals("")) {
            StringBuilder strB = new StringBuilder(ingredients);
            strB.deleteCharAt(ingredients.length() - 1);
            ingredients = strB.toString();
        }
        return ingredients;
    }

    void removeIngredient(Ingredient ingredient) {
        mIngredients.remove(ingredient);
    }
}


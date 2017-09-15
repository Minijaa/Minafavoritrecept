package com.gmail.hozjan.samuel.minafavoritrecept;

import java.text.DateFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

class ShoppingList implements Serializable {
    private UUID mId;
    private String name;
    private Date mDate;
    private List<Ingredient> mIngredients;

    ShoppingList() {
        mIngredients = new ArrayList<>();
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    List<Ingredient> getIngredients() {
        return mIngredients;
    }

    void addIngredient(Ingredient ingredient) {
        mIngredients.add(ingredient);
    }

    void removeIngredient(Ingredient ingredient) {
        mIngredients.remove(ingredient);
    }

    void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    String getDateAsString() {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(mDate);

    }

    String getQuantity() {
        if (mIngredients != null) {
            return "(" + mIngredients.size() +")";
        }
        return "(0)";
    }

}

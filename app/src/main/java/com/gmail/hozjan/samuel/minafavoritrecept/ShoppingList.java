package com.gmail.hozjan.samuel.minafavoritrecept;

import java.text.DateFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ShoppingList implements Serializable {
    private UUID mId;
    private String name;
    private Date mDate;
    private List<Ingredient> mIngredients;

    public ShoppingList() {
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

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    //public Ingredient getIngredient(UUID id){

    //}
    public void addIngredient(Ingredient ingredient) {
        mIngredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        mIngredients.remove(ingredient);
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getDateAsString() {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(mDate);

    }

    public String getQuantity() {
        if (mIngredients != null) {
            return "(" + mIngredients.size() +")";
        }
        return "(0)";
    }
}

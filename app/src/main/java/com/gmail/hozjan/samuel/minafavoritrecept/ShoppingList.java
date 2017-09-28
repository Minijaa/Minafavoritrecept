package com.gmail.hozjan.samuel.minafavoritrecept;

import java.text.DateFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//Modell-klass som håller datat för en inköpslista.
class ShoppingList implements Serializable {
    private UUID mId;
    private String name;
    private Date mDate;
    private List<Ingredient> mIngredients;

    //Konstruktor som instansierar en tom ingredienslista samt genererar ett unikt UUID och skapar
    // ett datumobjekt.
    ShoppingList() {
        mIngredients = new ArrayList<>();
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    // Getter och setters
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
    void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    // Lägger till en ingrediens.
    void addIngredient(Ingredient ingredient) {
        mIngredients.add(ingredient);
    }
    void addIngredient(int position, Ingredient ingredient) {
        mIngredients.add(position,ingredient);
    }

    // Tar bort en ingrediens.
    void removeIngredient(Ingredient ingredient) {
        mIngredients.remove(ingredient);
    }

    // Returnerar en stärng av inköpslistans "skapar-datum".
    String getDateAsString() {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(mDate);

    }

    // Returnerar en sträng över antalet ingredienser i inköpslistan.
    String getQuantity() {
        if (mIngredients != null) {
            return "(" + mIngredients.size() + ")";
        }
        return "(0)";
    }
}

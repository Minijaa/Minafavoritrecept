/*
Namn: Samuel Hozjan
Personnr: 820612-0159
elevId: saho0099
email: samuel.hozjan@gmail.com
 */
package com.gmail.hozjan.samuel.minafavoritrecept;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Modell-klass som håller datat för ett recept.
class Recipe implements Serializable{

    private UUID mId;
    private String mName;
    private String mDescription;
    private String mCategory;
    private ArrayList<Ingredient> mIngredients;

    //Konstruktor som instansierar en tom ingredienslista samt genererar ett unikt UUID.
    Recipe() {
        mId = UUID.randomUUID();
        mIngredients = new ArrayList<>();
    }
    //Getter och setters
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
    List<Ingredient> getIngredients() {
        return mIngredients;
    }

    //Returnerar ett unikt filnamn.
    String getImageFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    //Lägg till en specifierad ingrediens till receptet.
    void addIngredient(Ingredient ingredient) {
        mIngredients.add(ingredient);
    }

    void addIngredient(int position, Ingredient ingredient){
        mIngredients.add(position, ingredient);
    }

    // Returnerar receptets alla ingredienser som en sträng.
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

    //Raderar specifierar ingrediens ur receptet.
    void removeIngredient(Ingredient ingredient) {
        mIngredients.remove(ingredient);
    }
}


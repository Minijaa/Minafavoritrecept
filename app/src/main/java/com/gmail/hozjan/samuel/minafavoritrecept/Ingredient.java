package com.gmail.hozjan.samuel.minafavoritrecept;

import java.util.UUID;

public class Ingredient {

    private UUID mId;
    private String mName;
    private String mCategory;

    public Ingredient(){
        mId = UUID.randomUUID();
    }
    // Tillfällig konstruktor. Ta bort.
    public Ingredient(String name, String categoryName){
        mId = UUID.randomUUID();
        mName = name;
        mCategory = categoryName;
    }
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }
    public UUID getId() {
        return mId;
    }

}

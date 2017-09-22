package com.gmail.hozjan.samuel.minafavoritrecept;

import java.io.Serializable;
import java.util.UUID;

//Modellklass som håller datat för en ingrediens.
class Ingredient implements Serializable {

    private UUID mId;
    private String mName;
    private String mCategory;
    private boolean mIsMarked;

    //Konstruktor som genererar ett unikt UUID och sätter det boolska värdet för huruvida ingrediensen är flaggad som markerad som falsk.
    Ingredient() {
        mId = UUID.randomUUID();
        mIsMarked = false;
    }

    //Getter och setters
    boolean isMarked() {
        return mIsMarked;
    }
    void setMarked(boolean marked) {
        mIsMarked = marked;
    }
    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    String getCategory() {
        return mCategory;
    }
    void setCategory(String category) {
        mCategory = category;
    }
    public UUID getId() {
        return mId;
    }
}

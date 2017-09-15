package com.gmail.hozjan.samuel.minafavoritrecept;

import java.io.Serializable;
import java.util.UUID;

class Ingredient implements Serializable {

    private UUID mId;
    private String mName;
    private String mCategory;
    private boolean mIsMarked;


    boolean isMarked() {
        return mIsMarked;
    }

    void setMarked(boolean marked) {
        mIsMarked = marked;
    }


    Ingredient() {
        mId = UUID.randomUUID();
        mIsMarked = false;
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

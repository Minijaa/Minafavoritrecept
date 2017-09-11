package com.gmail.hozjan.samuel.minafavoritrecept;

import java.io.Serializable;
import java.util.UUID;

public class Ingredient implements Serializable {

    private UUID mId;
    private String mName;
    private String mCategory;
    private boolean mIsMarked;

    public boolean isMarked() {
        return mIsMarked;
    }

    public void setMarked(boolean marked) {
        mIsMarked = marked;
    }


    public Ingredient(){
        mId = UUID.randomUUID();
        mIsMarked = false;
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

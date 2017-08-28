package com.gmail.hozjan.samuel.minafavoritrecept;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by KOS on 2017-08-23.
 */

public class Recipe {

    private UUID mId;
    private String mName;
    private String mDescription;
    private String mCategory;
    private int mPortions;
    private int mTimeRequired;
    private ArrayList<Ingredient> mIngrediences;
    private Date mDateAdded;

    public Recipe(){
        mId = UUID.randomUUID();
    }
    public Recipe(String mName, String mDescription, int mPortions, int mTimeRequired){
        this.mName = mName;
        this.mDescription = mDescription;
        this.mPortions = mPortions;
        this.mTimeRequired = mTimeRequired;
        mId = UUID.randomUUID();
        mDateAdded = new Date();

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
}

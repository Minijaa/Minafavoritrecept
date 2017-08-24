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
    private int mPortions;
    private int mTimeRequired;
    private ArrayList<Ingredient> mIncidences;
    private Date mDate;


    public Recipe(String mName, String mDescription, int mPortions, int mTimeRequired){
        this.mName = mName;
        this.mDescription = mDescription;
        this.mPortions = mPortions;
        this.mTimeRequired = mTimeRequired;
        mId = UUID.randomUUID();
        mDate = new Date();

    }

    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }
}

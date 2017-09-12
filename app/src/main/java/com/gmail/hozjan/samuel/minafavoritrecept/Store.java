package com.gmail.hozjan.samuel.minafavoritrecept;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Store implements Serializable{
    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    private UUID mId;
    private String mName;
    private ArrayList <String> mCategories;

    public Store(){
        mId = UUID.randomUUID();

    }
}

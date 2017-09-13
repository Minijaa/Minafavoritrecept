package com.gmail.hozjan.samuel.minafavoritrecept;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Store implements Serializable{
    private UUID mId;
    private String mName;
    private List <String> mCategories;

    public Store(){
        mId = UUID.randomUUID();
        mCategories = new ArrayList<>();
        mCategories.addAll(Arrays.asList("Bageri", "Barnmat och tillbehör", "Bröd", "Dryck", "Fisk och skaldjur", "Fryst", "Frukt och grönt", "Glutenfritt", "Godis och läsk", "Hem och husgeråd", "Kött och chark", "Läkemedel", "Mejeri",
                "Ost", "Skafferi", "Snacks", "Tobak", "Övrigt"));
    }
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

    public List<String> getCategories() {
        return mCategories;
    }

}

package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by KOS on 2017-08-26.
 */

public class RecipeStorage {
    private static RecipeStorage sRecipestorage;
    private List<Recipe> mRecipes;

    public static RecipeStorage get(Context context){
        if (sRecipestorage == null){
            sRecipestorage = new RecipeStorage(context);
        }
        return sRecipestorage;
    }


    private RecipeStorage(Context context){
        mRecipes = new ArrayList<>();
        for (int i = 0; i<100;i++){
            Recipe r = new Recipe();
            r.setmName("Recept #" + i);
            r.setCategory("Kötträtter");
            mRecipes.add(r);
        }

    }

    public List<Recipe> getRecipes(){
        return mRecipes;
    }

    public Recipe getRecipe(UUID id){
        for (Recipe recipe : mRecipes){
            if (recipe.getId().equals(id)){
                return recipe;
            }
        }
    return null;
    }


}

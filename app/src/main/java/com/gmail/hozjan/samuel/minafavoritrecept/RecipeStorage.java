package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by KOS on 2017-08-26.
 */

public class RecipeStorage {
    private static RecipeStorage sRecipestorage;
    private List<Recipe> mRecipes;
    private Context mContext;

    public static RecipeStorage get(Context context){
        if (sRecipestorage == null){
            sRecipestorage = new RecipeStorage(context);
        }
        return sRecipestorage;
    }


    private RecipeStorage(Context context){
        mRecipes = new ArrayList<>();
        mContext = context.getApplicationContext();
//        for (int i = 0; i<100;i++){
//            Recipe r = new Recipe();
//            r.setmName("Recept #" + i);
//            r.setCategory("Kötträtter");
//            mRecipes.add(r);
//        }

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

    public void addRecipe(Recipe recipe){
        mRecipes.add(recipe);
    }

    public File getImageFile(Recipe recipe){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, recipe.getImageFilename());
    }


}

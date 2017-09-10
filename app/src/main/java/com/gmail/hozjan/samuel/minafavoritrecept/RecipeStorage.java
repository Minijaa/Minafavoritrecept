package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeStorage {
    private static RecipeStorage sRecipestorage;
    private List<Recipe> mRecipes;
    private Context mContext;
    public static final String mDataFile = "recipe_data.dat";

    public static RecipeStorage get(Context context) {
        if (sRecipestorage == null) {
            sRecipestorage = new RecipeStorage(context);
        }
        return sRecipestorage;
    }


    private RecipeStorage(Context context) {
        mRecipes = new ArrayList<>();
        mContext = context.getApplicationContext();
    }

    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    //Borde göra om till en TreeMap som sorterar på recept-namnet.
    public Recipe getRecipe(UUID id) {
        for (Recipe recipe : mRecipes) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    public void storeData() {
        try {
            FileOutputStream outFile = mContext.getApplicationContext().openFileOutput(mDataFile, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outFile);
            oos.writeObject(mRecipes);
            oos.close();
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        try {
            FileInputStream inFile = mContext.getApplicationContext().openFileInput(mDataFile);
            ObjectInputStream ois = new ObjectInputStream(inFile);
            mRecipes = (ArrayList) ois.readObject();
            ois.close();
            inFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addRecipe(Recipe recipe) {
        mRecipes.add(recipe);
    }

    public void deleteRecipe(Recipe recipe) {
        mRecipes.remove(recipe);
        storeData();
    }

    public File getImageFile(Recipe recipe) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, recipe.getImageFilename());
    }


}

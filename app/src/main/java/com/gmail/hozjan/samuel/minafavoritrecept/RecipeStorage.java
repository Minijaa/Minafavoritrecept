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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RecipeStorage {
    private static RecipeStorage sRecipestorage;
    private List<Recipe> mRecipes;
    private List<ShoppingList> mShoppingLists;
    private List<Store> mStores;
    private Context mContext;
    private static final String mDataFile = "recipe_data.dat";

    public static RecipeStorage get(Context context) {
        if (sRecipestorage == null) {
            sRecipestorage = new RecipeStorage(context);
        }
        return sRecipestorage;
    }

    private RecipeStorage(Context context) {
        mRecipes = new ArrayList<>();
        mShoppingLists = new ArrayList<>();
        mStores = new ArrayList<>();
        mContext = context.getApplicationContext();
    }

    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    public Recipe getRecipe(UUID id) {
        for (Recipe recipe : mRecipes) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    public List<Ingredient> getSortedShoppingList(String storeName, List<Ingredient> shoppingList) {
        List<Ingredient> sortedIngredientList = new ArrayList<>();
        List<Ingredient> unSortedIngredientList = shoppingList;
        List<String> chosenStoreCategories = new ArrayList<>();
        //Map<String, List<Ingredient>> ingredientsByCategory = new HashMap<>();

        if (!storeName.equals("-Butik-")){
            for (Store s : mStores) {
                if (s.getName().equals(storeName)) {
                    chosenStoreCategories = s.getCategories();
                }
            }
        }else {
            chosenStoreCategories = new Store().getCategories();
        }
        for (String kategori : chosenStoreCategories){
            for (Ingredient ingredient : unSortedIngredientList){
                if (ingredient.getCategory().equals(kategori)){
                    sortedIngredientList.add(ingredient);
                }
            }
        }
        return sortedIngredientList;
    }

    public void storeData() {
        try {
            FileOutputStream outFile = mContext.getApplicationContext().openFileOutput(mDataFile, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outFile);
            oos.writeObject(mRecipes);
            oos.writeObject(mShoppingLists);
            oos.writeObject(mStores);
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
            mShoppingLists = (ArrayList) ois.readObject();
            mStores = (ArrayList) ois.readObject();
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

    public void addShoppingList(ShoppingList shoppingList) {
        mShoppingLists.add(shoppingList);
    }

    public void deleteShoppingList(ShoppingList shoppingList) {
        mShoppingLists.remove(shoppingList);
        storeData();
    }

    public List<ShoppingList> getShoppingLists() {
        return mShoppingLists;
    }

    public ShoppingList getShoppingList(UUID id) {
        for (ShoppingList s : mShoppingLists) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public void addStore(Store store) {
        mStores.add(store);
    }

    public void deleteStore(Store store) {
        mStores.remove(store);
        storeData();
    }

    public List<Store> getStores() {
        return mStores;
    }

    public Store getStore(UUID id) {
        for (Store s : mStores) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }
}

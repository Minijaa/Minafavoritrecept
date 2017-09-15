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

class RecipeStorage {
    private static RecipeStorage sRecipeStorage;
    private List<Recipe> mRecipes;
    private List<ShoppingList> mShoppingLists;
    private List<Store> mStores;
    private Context mContext;
    private static final String mDataFile = "recipe_data.dat";
    private static int mRecipeNo;


    private static int mShoppingListNo;
    private static int mStoreNo;

    static RecipeStorage get(Context context) {
        if (sRecipeStorage == null) {
            sRecipeStorage = new RecipeStorage(context);
        }
        return sRecipeStorage;
    }

    private RecipeStorage(Context context) {
        mRecipes = new ArrayList<>();
        mShoppingLists = new ArrayList<>();
        mStores = new ArrayList<>();
        mContext = context.getApplicationContext();
        mRecipeNo = 0;
        mShoppingListNo = 0;
        mStoreNo = 0;
    }

    List<Recipe> getRecipes() {
        return mRecipes;
    }

    Recipe getRecipe(UUID id) {
        for (Recipe recipe : mRecipes) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    List<Ingredient> getSortedShoppingList(String storeName, List<Ingredient> unsortedIngredientList) {
        List<Ingredient> sortedIngredientList = new ArrayList<>();
        List<String> chosenStoreCategories = new ArrayList<>();

        if (!storeName.equals("-Butik-")) {
            for (Store s : mStores) {
                if (s.getName().equals(storeName)) {
                    chosenStoreCategories = s.getCategories();
                }
            }
        } else {
            chosenStoreCategories = new Store().getCategories();
        }
        for (String kategori : chosenStoreCategories) {
            for (Ingredient ingredient : unsortedIngredientList) {
                if (ingredient.getCategory().equals(kategori)) {
                    sortedIngredientList.add(ingredient);
                }
            }
        }
        return sortedIngredientList;
    }

    void storeData() {
        try {
            FileOutputStream outFile = mContext.getApplicationContext().openFileOutput(mDataFile, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outFile);
            oos.writeObject(mRecipes);
            oos.writeObject(mShoppingLists);
            oos.writeObject(mStores);
            oos.writeInt(mRecipeNo);
            oos.writeInt(mShoppingListNo);
            oos.writeInt(mStoreNo);
            oos.close();
            outFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadData() {
        try {
            FileInputStream inFile = mContext.getApplicationContext().openFileInput(mDataFile);
            ObjectInputStream ois = new ObjectInputStream(inFile);
            mRecipes = (ArrayList) ois.readObject();
            mShoppingLists = (ArrayList) ois.readObject();
            mStores = (ArrayList) ois.readObject();
            mRecipeNo = ois.readInt();
            mShoppingListNo = ois.readInt();
            mStoreNo = ois.readInt();
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

    void addRecipe(Recipe recipe) {
        mRecipes.add(recipe);
    }

    void deleteRecipe(Recipe recipe) {
        mRecipes.remove(recipe);
        storeData();
    }

    File getImageFile(Recipe recipe) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, recipe.getImageFilename());
    }

    void addShoppingList(ShoppingList shoppingList) {
        mShoppingLists.add(shoppingList);
    }

    void deleteShoppingList(ShoppingList shoppingList) {
        mShoppingLists.remove(shoppingList);
        storeData();
    }

    List<ShoppingList> getShoppingLists() {
        return mShoppingLists;
    }

    ShoppingList getShoppingList(UUID id) {
        for (ShoppingList s : mShoppingLists) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    void addStore(Store store) {
        mStores.add(store);
    }

    void deleteStore(Store store) {
        mStores.remove(store);
        storeData();
    }

    List<Store> getStores() {
        return mStores;
    }

    Store getStore(UUID id) {
        for (Store s : mStores) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    static int getNoNameNr(String type) {
        if (type.equals("recipe")) {
            int lastNo = mRecipeNo;
            mRecipeNo++;
            return lastNo;
        } else if (type.equals("shoppinglist")) {
            int lastNo = mShoppingListNo;
            mShoppingListNo++;
            return lastNo;
        }
        int lastNo = mStoreNo;
        mStoreNo++;
        return lastNo;
    }
}

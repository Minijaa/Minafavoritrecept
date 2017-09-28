/*
Namn: Samuel Hozjan
Personnr: 820612-0159
elevId: saho0099
email: samuel.hozjan@gmail.com
 */
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

//Singleton-klass som lagrar och hanterar all data i appen.
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

    //Getter och setters
    List<Recipe> getRecipes() {
        return mRecipes;
    }
    List<ShoppingList> getShoppingLists() {
        return mShoppingLists;
    }
    List<Store> getStores() {
        return mStores;
    }

    Recipe getRecipe(UUID id) {
        for (Recipe recipe : mRecipes) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    //Tar ett butiksnamn och en lista över ingredienser som argument och returnerar en lista innehållande
    //samma ingredienser men som är sorterad efter den valda butikens sorteringsordning. Denna
    //sorteringsordning bestäms efter i vilken ordning de olika ingredienskategorierna är listade för
    //aktuell butik. Om butiksnamnet i argumentet är "-Butik-" vilket är default-sorteringen skapas en ny butik med
    //dess standardutförande av kategoriordning och ingredienslistan sorteras då efter denna ordning.
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

    // Lagra all data över recept, inköpslistor och butiker på fil i det interna minnet. Lagra även
    // de heltal som används för autogenerering av namn för namnlösa recept, inköpslistor och butiker.
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

    // Läs in all data över recept, inköpslistor och butiker från fil i det interna minnet. Läs även
    // in de heltal som används för autogenerering av namn för namnlösa recept, inköpslistor och butiker.
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

    // Lägg till recept i listan över recept.
    void addRecipe(Recipe recipe) {
        mRecipes.add(recipe);
    }

    // Radera ett recept och spara ändringarna på fil.
    void deleteRecipe(Recipe recipe) {
        mRecipes.remove(recipe);
        storeData();
    }

    // Returnera filobjekt för aktuellt recepts bild.
    File getImageFile(Recipe recipe) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, recipe.getImageFilename());
    }

    // Lägg till en inköpslista
    void addShoppingList(ShoppingList shoppingList) {
        mShoppingLists.add(shoppingList);
    }

    // Radera en inköpslista och spara ändringarna på fil.
    void deleteShoppingList(ShoppingList shoppingList) {
        mShoppingLists.remove(shoppingList);
        storeData();
    }

    // Ta ett id som argument och returnerar motsvarande inköpslista.
    ShoppingList getShoppingList(UUID id) {
        for (ShoppingList s : mShoppingLists) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    // Lägg till en butik.
    void addStore(Store store) {
        mStores.add(store);
    }

    // Radera en butik och spara ändringarna på fil.
    void deleteStore(Store store) {
        mStores.remove(store);
        storeData();
    }

    // Ta ett id som argument och returnerar motsvarande butik.
    Store getStore(UUID id) {
        for (Store s : mStores) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    // Returnera ett heltal för automatiskt namngivning av namnlösa recept, butiker eller inköpslistor.
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

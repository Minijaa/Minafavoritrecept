package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

//Aktivitets-klass som håller en instans av RecipeEditFragment.
public class RecipeEditActivity extends SingleFragmentActivity {
    public static final String EXTRA_RECIPE_ID = "com.gmail.hozjan.samuel.minafavoritrecept.recipe_id";

    public static Intent newIntent(Context packageContext, UUID recipeID){
        Intent intent = new Intent(packageContext, RecipeEditActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new RecipeEditFragment();
    }
}

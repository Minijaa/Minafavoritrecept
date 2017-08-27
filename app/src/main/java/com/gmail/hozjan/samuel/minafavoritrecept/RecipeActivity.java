package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class RecipeActivity extends SingleFragmentActivity {
    public static final String EXTRA_RECIPE_ID = "com.gmail.hozjan.samuel.minafavoritrecept.recipe_id";

    public static Intent newIntent(Context packageContext, UUID recipeID){
        Intent intent = new Intent(packageContext, RecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new RecipeFragment();
    }
}

/*
Namn: Samuel Hozjan
Personnr: 820612-0159
elevId: saho0099
email: samuel.hozjan@gmail.com
 */
package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import java.util.UUID;

//Aktivitets-klass som håller en instans av RecipeFragment.
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

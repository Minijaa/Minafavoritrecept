package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeListActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, RecipeListActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new RecipeListFragment();
    }
}

package com.gmail.hozjan.samuel.minafavoritrecept;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new RecipeListFragment();
    }
}

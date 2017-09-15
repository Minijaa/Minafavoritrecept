package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class ShoppingListActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context packageContext){
        return new Intent(packageContext, ShoppingListActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return new ShoppingListFragment();
    }
}

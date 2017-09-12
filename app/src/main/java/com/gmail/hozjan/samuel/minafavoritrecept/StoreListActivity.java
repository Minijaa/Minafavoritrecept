package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class StoreListActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, StoreListActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new StoreListFragment();
    }
}

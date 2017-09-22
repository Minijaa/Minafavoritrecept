package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

// Aktivitets-klass som håller en instans av StoreFragment.
public class StoreActivity extends SingleFragmentActivity {
    public static final String EXTRA_STORE_ID = "com.gmail.hozjan.samuel.minafavoritrecept.store_id";

    public static Intent newIntent(Context packageContext, UUID storeID) {
        Intent intent = new Intent(packageContext, StoreActivity.class);
        intent.putExtra(EXTRA_STORE_ID, storeID);
        return intent;

    }

    @Override
    protected Fragment createFragment() {
        return new StoreFragment();
    }
}


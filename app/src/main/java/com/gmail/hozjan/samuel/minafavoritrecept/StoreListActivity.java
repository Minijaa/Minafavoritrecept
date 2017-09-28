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

//Aktivitets-klass som håller en instans av StoreListFragment.
public class StoreListActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context packageContext){
        return new Intent(packageContext, StoreListActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return new StoreListFragment();
    }
}

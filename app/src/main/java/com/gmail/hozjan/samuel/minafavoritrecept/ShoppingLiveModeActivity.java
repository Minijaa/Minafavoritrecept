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

//Aktivitets-klass som håller en instans av ShoppingLiveModeFragment.
public class ShoppingLiveModeActivity extends SingleFragmentActivity {
    public static final String EXTRA_SHOPPINGLIST_ID = "com.gmail.hozjan.samuel.minafavoritrecept.shoppinglist_id";

    @Override
    protected Fragment createFragment() {
        return new ShoppingLiveModeFragment();
    }

    public static Intent newIntent(Context packageContext, UUID shoppingListID){
        Intent intent = new Intent(packageContext, ShoppingLiveModeActivity.class);
        intent.putExtra(EXTRA_SHOPPINGLIST_ID, shoppingListID);
        return intent;
    }
}

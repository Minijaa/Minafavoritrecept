package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class ShoppingListFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_shopping_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_shoppinglist) {
            Intent intent = RecipeListActivity.newIntent(getActivity());
            startActivity(intent);
//            Recipe recipe = new Recipe();
//            RecipeStorage.get(getActivity()).addRecipe(recipe);
//            Intent intent = RecipeEditActivity.newIntent(getActivity(), recipe.getId());
//            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.navigate_to_recipe_list) {
            Intent intent = RecipeListActivity.newIntent(getActivity());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}

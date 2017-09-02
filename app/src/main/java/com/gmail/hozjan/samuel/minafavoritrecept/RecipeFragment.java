package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.UUID;

/**
 * Created by KOS on 2017-08-25.
 */

public class RecipeFragment extends Fragment {
    private Recipe mRecipe;
    private TextView mNameField;
    private TextView mInsctructions;
    private TextView mIngrediences;
    private TextView mRecipeCategory;
    private ImageView mRecipeImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID recipeId = (UUID) getActivity().getIntent().getSerializableExtra(RecipeActivity.EXTRA_RECIPE_ID);
        mRecipe = RecipeStorage.get(getActivity()).getRecipe(recipeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);
        mNameField = (TextView) v.findViewById(R.id.recipe_name);
        mNameField.setText(mRecipe.getName());
        mRecipeCategory = (TextView) v.findViewById(R.id.recipe_category);
        mRecipeCategory.setText(mRecipe.getCategory());
        mIngrediences = (TextView) v.findViewById(R.id.recipe_ingrediences);
        mIngrediences.setText(mRecipe.getIngrediences());
        mInsctructions = (TextView) v.findViewById(R.id.recipe_instructions);
        mInsctructions.setText(mRecipe.getDescription());
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_recipe, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.appbar_edit_recipe_button) {
            Intent intent = RecipeEditActivity.newIntent(getActivity(), mRecipe.getId());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void updateUI(){
       //RecipeFragment fragment = new RecipeFragment();
        // getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

    }
    @Override
    public void onResume() {
        super.onResume();

    }
}

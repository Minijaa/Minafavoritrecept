package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
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
    private ImageView mRecipeImageView;
    private File mRecipeImageFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID recipeId = (UUID) getActivity().getIntent().getSerializableExtra(RecipeActivity.EXTRA_RECIPE_ID);
        mRecipe = RecipeStorage.get(getActivity()).getRecipe(recipeId);
        mRecipeImageFile = RecipeStorage.get(getActivity()).getImageFile(mRecipe);
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
        mIngrediences.setText(mRecipe.getIngrediencesTEMPREMOVESOON());
        mInsctructions = (TextView) v.findViewById(R.id.recipe_instructions);
        mInsctructions.setText(mRecipe.getDescription());
        mRecipeImageView = (ImageView) v.findViewById(R.id.recipe_image);

        updateImageView();
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

    public void updateUI() {
        UUID recipeId = (UUID) getActivity().getIntent().getSerializableExtra(RecipeActivity.EXTRA_RECIPE_ID);
        mRecipe = RecipeStorage.get(getActivity()).getRecipe(recipeId);
        if (mRecipe != null) {
            mNameField.setText(mRecipe.getName());
            mRecipeCategory.setText(mRecipe.getCategory());
            mIngrediences.setText(mRecipe.getIngrediencesTEMPREMOVESOON());
            mInsctructions.setText(mRecipe.getDescription());
            updateImageView();
        }else {
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();


    }

    private void updateImageView() {
        if (mRecipeImageFile == null || !mRecipeImageFile.exists()) {
            //mRecipeImageView.setImageDrawable(null);
            Drawable dr = getResources().getDrawable(R.drawable.matratt_test);
            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 360, 240, true));
            mRecipeImageView.setImageDrawable(d);
        } else {
//            Point size = new Point();
//            getActivity().getWindowManager().getDefaultDisplay().getSize(size);
//            Bitmap bitmapUnscaled = BitmapFactory.decodeFile(mRecipeImageFile.getAbsolutePath());
//            Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmapUnscaled, size.y, size.x, true);
            Bitmap bitmapScaled = ImageHandler.getScaledBitmap(mRecipeImageFile.getPath(), getActivity());
            mRecipeImageView.setImageBitmap(bitmapScaled);
        }
    }
}

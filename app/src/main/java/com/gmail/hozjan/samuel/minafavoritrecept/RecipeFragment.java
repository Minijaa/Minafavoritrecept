package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
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

//Fragment-klass som hanterar vyn för att visa ett enskilt recept.
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
    // Skapa vyn och ställ in alla knappar/textfält.
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);
        mNameField = (TextView) v.findViewById(R.id.recipe_name);
        mNameField.setText(mRecipe.getName());
        mRecipeCategory = (TextView) v.findViewById(R.id.recipe_category);
        mRecipeCategory.setText(mRecipe.getCategory());
        mIngrediences = (TextView) v.findViewById(R.id.recipe_ingrediences);
        mIngrediences.setText(mRecipe.getIngredientsAsString());
        mInsctructions = (TextView) v.findViewById(R.id.recipe_instructions);
        mInsctructions.setText(mRecipe.getDescription());
        mRecipeImageView = (ImageView) v.findViewById(R.id.recipe_image);

        updateImageView();
        return v;
    }

    // Kopplar upp layout-filen för toolbar-menyn.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_recipe, menu);
    }

    // Sköter funktionalitet för knapparna i toolbaren.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.appbar_edit_recipe_button) {
            Intent intent = RecipeEditActivity.newIntent(getActivity(), mRecipe.getId());
            startActivity(intent);
            return true;
        }else if (item.getItemId() == R.id.appbar_add_to_shoppinglist){
            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setIngredients(mRecipe.getIngredients());
            shoppingList.setName(mRecipe.getName());
            RecipeStorage.get(getActivity()).addShoppingList(shoppingList);
            Intent intent = ShoppingActivity.newIntent(getActivity(), shoppingList.getId());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Uppdaterar UI:n. Avslutar aktiviteten om inget recept finns kopplat. Dvs om användaren valt att radera receptet.
    public void updateUI() {
        UUID recipeId = (UUID) getActivity().getIntent().getSerializableExtra(RecipeActivity.EXTRA_RECIPE_ID);
        mRecipe = RecipeStorage.get(getActivity()).getRecipe(recipeId);
        if (mRecipe != null) {
            mNameField.setText(mRecipe.getName());
            mRecipeCategory.setText(mRecipe.getCategory());
            mIngrediences.setText(mRecipe.getIngredientsAsString());
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
    // Uppdaterar ImageViewn med fotograferad bild nedskalad till skärmens storlek, alternativt en standard-bild.
    private void updateImageView() {
        if (mRecipeImageFile == null || !mRecipeImageFile.exists()) {
            Drawable dr = ResourcesCompat.getDrawable(getResources(), R.drawable.default_image_red_jpg, null);
            if (dr != null){
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 360, 240, true);
                mRecipeImageView.setImageBitmap(scaled);
            }
        } else {
            Bitmap bitmapScaled = ScaleImageHandler.getScaledBitmap(mRecipeImageFile.getPath(), getActivity());
            mRecipeImageView.setImageBitmap(bitmapScaled);
        }
    }
}

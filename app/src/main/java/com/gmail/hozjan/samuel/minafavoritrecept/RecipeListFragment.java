package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Created by KOS on 2017-08-26.
 */

public class RecipeListFragment extends Fragment {
    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIinstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mRecipeRecyclerView = (RecyclerView) view.findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        return view;
    }

    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTextView;
        private TextView mCategoryTextView;
        private ImageView mThumbnailImageView;
        private Recipe mRecipe;
        private File mRecipeImageFile;
        private ImageButton mDeleteButton;

        public RecipeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));
            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.recipe_list_item_name);
            mCategoryTextView = (TextView) itemView.findViewById(R.id.recipe_list_item_category);
            mThumbnailImageView = (ImageView) itemView.findViewById(R.id.recipe_list_item_image);
            mDeleteButton = (ImageButton)itemView.findViewById(R.id.delete_button);

        }

        private void bind(Recipe recipe) {
            mRecipe = recipe;
            mNameTextView.setText(mRecipe.getName());
            mCategoryTextView.setText(mRecipe.getCategory());
            mRecipeImageFile = RecipeStorage.get(getActivity()).getImageFile(recipe);
            updateImageView();

            // kanske kan använda den vanliga onclick-listenern istället och bara köra en if-sats om man klickar på papperskorg-knappen.
            //Else starta ny recipefragment.
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeStorage.get(getActivity()).deleteRecipe(mRecipe);
                    mAdapter.notifyDataSetChanged();
                }
            });
            //mThumbnailImageView.setImageDrawable(getResources().getDrawable(R.drawable.matratt_test));
        }

        @Override
        public void onClick(View v) {
            Intent intent = RecipeActivity.newIntent(getActivity(), mRecipe.getId());
            startActivity(intent);
        }
        private void updateImageView(){
            if (mRecipeImageFile == null || !mRecipeImageFile.exists()){
                //mThumbnailImageView.setImageDrawable(getResources().getDrawable(R.drawable.default_image));
                //mThumbnailImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Drawable dr = getResources().getDrawable(R.drawable.matratt_test);
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 170, 120, true));
                mThumbnailImageView.setImageDrawable(d);
            }else {
                //Bitmap bitmap = ImageHandler.getScaleBitMap(mRecipeImageFile.getPath(),135,180);

                //Läs in bilden som nu bör finnas där vi sa att den skulle placeras
                Bitmap bm= BitmapFactory.decodeFile(mRecipeImageFile.getAbsolutePath());

                //Skala om bilden så att den passar i imageviewn
                Bitmap bm2=Bitmap.createScaledBitmap(bm,
                        170, 120,
                        true);
                mThumbnailImageView.setImageBitmap(bm2);
            }
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {
        private List<Recipe> mRecipes;

        public RecipeAdapter(List<Recipe> recipes) {
            mRecipes = recipes;
        }

        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RecipeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(RecipeHolder holder, int position) {
            Recipe recipe = mRecipes.get(position);
            holder.bind(recipe);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }
    }

    private void updateUI() {
        RecipeStorage recipeStorage = RecipeStorage.get(getActivity());
        List<Recipe> recipes = recipeStorage.getRecipes();
        if (mAdapter == null){
            mAdapter = new RecipeAdapter(recipes);
            mRecipeRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_recipe_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_recipe) {
            Recipe recipe = new Recipe();
            //recipe.setmName("testnamnet");
            RecipeStorage.get(getActivity()).addRecipe(recipe);
            Intent intent = RecipeEditActivity.newIntent(getActivity(), recipe.getId());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}

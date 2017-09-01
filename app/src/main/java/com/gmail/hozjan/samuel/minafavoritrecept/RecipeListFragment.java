package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

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

        public RecipeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));
            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.recipe_list_item_name);
            mCategoryTextView = (TextView) itemView.findViewById(R.id.recipe_list_item_category);
            mThumbnailImageView = (ImageView) itemView.findViewById(R.id.recipe_list_item_image);
        }

        private void bind(Recipe recipe) {
            mRecipe = recipe;
            mNameTextView.setText(mRecipe.getName());
            mCategoryTextView.setText(mRecipe.getCategory());
            //mThumbnailImageView.setImageDrawable(getResources().getDrawable(R.drawable.matratt_test));
        }

        @Override
        public void onClick(View v) {
            Intent intent = RecipeActivity.newIntent(getActivity(), mRecipe.getId());
            startActivity(intent);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}

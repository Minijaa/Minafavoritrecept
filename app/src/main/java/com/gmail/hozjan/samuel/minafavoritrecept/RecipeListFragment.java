package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

// Fragment-klass som hanterar visning av alla inlagda recept i en lista.
public class RecipeListFragment extends Fragment {
    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;
    private static final String TAG = "Minafavoritrecept";

    //Läser in sparad data från fil.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        RecipeStorage.get(getActivity()).loadData();
    }

    // Skapa vyn och koppla upp recyclerviewn.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIinstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mRecipeRecyclerView = (RecyclerView) v.findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    // ViewHolder-klass som håller receptens vyer.
    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTextView;
        private TextView mCategoryTextView;
        private ImageView mThumbnailImageView;
        private Recipe mRecipe;
        private File mRecipeImageFile;
        private ImageButton mDeleteButton;

        //Konstruktor som initierar de olika widgetsen för varje recept i listan.
        RecipeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));
            itemView.setOnClickListener(this);
            mNameTextView = (TextView) itemView.findViewById(R.id.recipe_list_item_name);
            mCategoryTextView = (TextView) itemView.findViewById(R.id.recipe_list_item_category);
            mThumbnailImageView = (ImageView) itemView.findViewById(R.id.recipe_list_item_image);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);

        }

        //Startar en instans av RecipeActivity som i sin tur skapar en instans av RecipeFragment
        // innehållande receptet användaren klickat på.
        @Override
        public void onClick(View v) {
            Intent intent = RecipeActivity.newIntent(getActivity(), mRecipe.getId());
            startActivity(intent);
        }

        //Binder ett recept till RecipeHoldern(ViewHoldern).
        private void bind(Recipe recipe) {
            mRecipe = recipe;
            mNameTextView.setText(mRecipe.getName());
            mCategoryTextView.setText(mRecipe.getCategory());
            mRecipeImageFile = RecipeStorage.get(getActivity()).getImageFile(recipe);
            updateImageView();
            setUpDeleteButton();
        }

        //Ställer in delete-knappen för ett enskilt recept.
        private void setUpDeleteButton() {
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Varning!");
                    if (mRecipe.getName() != null) {
                        alert.setMessage("Är du säker på att du vill ta bort receptet " + "\"" + mRecipe.getName() + "\"");
                    } else {
                        alert.setMessage("Är du säker på att du vill ta bort det namnlösa receptet?");
                    }
                    alert.setPositiveButton("JA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Radera bildfilen från det interna minnet om receptet innehåller en bild
                            if (RecipeStorage.get(getActivity()).getImageFile(mRecipe)!= null){
                                if (getActivity().deleteFile(mRecipe.getImageFilename())){
                                    Log.d(TAG, mRecipe.getImageFilename() + " borttagen!");
                                }
                            }
                            RecipeStorage.get(getActivity()).deleteRecipe(mRecipe);
                            mAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    alert.setNegativeButton("NEJ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
            });
        }

        // Uppdaterar ImageViewn med fotograferad bild, alternativt en standard-bild.
        private void updateImageView() {
            if (mRecipeImageFile == null || !mRecipeImageFile.exists()) {
                Drawable dr = ResourcesCompat.getDrawable(getResources(), R.drawable.default_image_red_jpg, null);
                if (dr != null) {
                    Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 170, 120, true));
                    mThumbnailImageView.setImageDrawable(d);
                }
            } else {
                Bitmap bm = BitmapFactory.decodeFile(mRecipeImageFile.getAbsolutePath());
                Bitmap bm2 = Bitmap.createScaledBitmap(bm,
                        170, 120,
                        true);
                mThumbnailImageView.setImageBitmap(bm2);
            }
        }
    }

    //Adapterklass som skapar RecipeHolder-objekt samt binder recept till dessa.
    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {
        private List<Recipe> mRecipes;
        RecipeAdapter(List<Recipe> recipes) {
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

    //Uppdaterar UI:n
    private void updateUI() {
        RecipeStorage recipeStorage = RecipeStorage.get(getActivity());
        List<Recipe> recipes = recipeStorage.getRecipes();
        if (mAdapter == null) {
            mAdapter = new RecipeAdapter(recipes);
            mRecipeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    // Kopplar upp layout-filen för toolbar-menyn.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_recipe_list, menu);
    }

    // Sköter funktionalitet för knapparna i toolbaren.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_recipe) {
            Recipe recipe = new Recipe();
            RecipeStorage.get(getActivity()).addRecipe(recipe);
            Intent intent = RecipeEditActivity.newIntent(getActivity(), recipe.getId());
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.navigate_to_shoppinglists) {
            Intent intent = ShoppingListActivity.newIntent(getActivity());
            startActivity(intent);
        } else if (item.getItemId() == R.id.shopping_stores){
            Intent intent = StoreListActivity.newIntent(getActivity());
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

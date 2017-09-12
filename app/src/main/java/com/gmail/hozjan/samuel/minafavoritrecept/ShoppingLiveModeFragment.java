package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;

public class ShoppingLiveModeFragment extends Fragment {
    private RecyclerView mShoppingIngredientsRecyclerView;
    private ShoppingList mShoppingList;
    private IngredientAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID shoppingListId = (UUID)getActivity().getIntent().getSerializableExtra(ShoppingActivity.EXTRA_SHOPPINGLIST_ID);
        mShoppingList = RecipeStorage.get(getActivity()).getShoppingList(shoppingListId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopping_live_mode, container, false);

        mShoppingIngredientsRecyclerView = (RecyclerView)v.findViewById(R.id.shopping_live_mode_recyclerview);
        mShoppingIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }
    private void updateUI() {
        List<Ingredient> ingredients = mShoppingList.getIngredients();
        mAdapter = new IngredientAdapter(ingredients);
        mShoppingIngredientsRecyclerView.setAdapter(mAdapter);
    }
    private void sortIngredients(String sortMethod){
        
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_shopping_live_mode, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //sorteringsspinner
        if (item.getItemId() == R.id.shopping_new_ingredient){
            mAdapter.notifyDataSetChanged();

        }else if (item.getItemId() == R.id.shopping_enter_edit_mode){
            getActivity().finish();

        }
        return super.onOptionsItemSelected(item);
    }
    private class IngredientHolder extends RecyclerView.ViewHolder{
        private CheckBox mCheckBox;
        private Spinner mChooseStoreSpinner;
        private Ingredient mIngredient;
        private TextView mName;
        private TextView mCategory;


        public IngredientHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_shoppinglist_shopping_mode, parent, false));
            mCheckBox = (CheckBox)itemView.findViewById(R.id.shopping_live_mode_ingredient_checkbox);
            mName = (TextView)itemView.findViewById(R.id.shopping_live_mode_ingredient_name_textview);
            mCategory = (TextView)itemView.findViewById(R.id.shopping_live_mode_ingredient_category_textview);

        }
        public void bind(final Ingredient ingredient){
            mCheckBox.setChecked(ingredient.isMarked());
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ingredient.setMarked(isChecked);
                }
            });
            mIngredient = ingredient;
            mName.setText(mIngredient.getName());
            mCategory.setText(mIngredient.getCategory());
        }
    }
    private class IngredientAdapter extends RecyclerView.Adapter<IngredientHolder> {
        private List<Ingredient> mIngredients;

        public IngredientAdapter(List<Ingredient> ingredients) {
            mIngredients = ingredients;
        }

        @Override
        public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new IngredientHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(IngredientHolder holder, int position) {
            Ingredient ingredient = mIngredients.get(position);
            holder.bind(ingredient);
        }

        @Override
        public int getItemCount() {
            return mIngredients.size();
        }
    }



}

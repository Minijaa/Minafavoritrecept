package com.gmail.hozjan.samuel.minafavoritrecept;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShoppingLiveModeFragment extends Fragment {
    private RecyclerView mShoppingIngredientsRecyclerView;
    private ShoppingList mShoppingList;
    private IngredientAdapter mAdapter;
    private List<String> mStoreNames;
    private int mNameFlag;
    private int mCategoryFlag;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID shoppingListId = (UUID) getActivity().getIntent().getSerializableExtra(ShoppingActivity.EXTRA_SHOPPINGLIST_ID);
        mShoppingList = RecipeStorage.get(getActivity()).getShoppingList(shoppingListId);
        mStoreNames = new ArrayList<>();
        for (Store s : RecipeStorage.get(getActivity()).getStores()) {
            mStoreNames.add(s.getName());
        }
        Store standardStore = new Store();
        standardStore.setName("-Butik-");
        if (!mStoreNames.contains(standardStore.getName())) {
            mStoreNames.add(0, standardStore.getName());
        }
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopping_live_mode, container, false);


        mShoppingIngredientsRecyclerView = (RecyclerView) v.findViewById(R.id.shopping_live_mode_recyclerview);
        mShoppingIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    private void updateUI() {
        List<Ingredient> ingredients = mShoppingList.getIngredients();
        mAdapter = new IngredientAdapter(ingredients);
        mShoppingIngredientsRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_shopping_live_mode, menu);

        MenuItem spinnerItem = menu.findItem(R.id.store_spinner);
        Spinner storeSpinner = (Spinner) MenuItemCompat.getActionView(spinnerItem);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mStoreNames);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        storeSpinner.setAdapter(spinnerArrayAdapter);
        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String storeName = (String) parent.getItemAtPosition(position);
                List<Ingredient> sortedShoppingList = RecipeStorage.get(getActivity()).getSortedShoppingList(storeName, mShoppingList.getIngredients());
                mShoppingList.setIngredients(sortedShoppingList);

                updateUI();
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.shopping_enter_edit_mode) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class IngredientHolder extends RecyclerView.ViewHolder {
        private CheckBox mCheckBox;
        private Ingredient mIngredient;
        private TextView mName;
        private TextView mCategory;


        IngredientHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_shoppinglist_shopping_mode, parent, false));
            mCheckBox = (CheckBox) itemView.findViewById(R.id.shopping_live_mode_ingredient_checkbox);
            mName = (TextView) itemView.findViewById(R.id.shopping_live_mode_ingredient_name_textview);
            mCategory = (TextView) itemView.findViewById(R.id.shopping_live_mode_ingredient_category_textview);
            mNameFlag = mName.getPaintFlags();
            mCategoryFlag = mCategory.getPaintFlags();
        }

        void bind(final Ingredient ingredient) {
            mCheckBox.setChecked(ingredient.isMarked());
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ingredient.setMarked(isChecked);

                    if (isChecked) {

                        mName.setPaintFlags(mName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        mCategory.setPaintFlags(mCategory.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        mName.setPaintFlags(mNameFlag);
                        mCategory.setPaintFlags(mCategoryFlag);
                    }
                }
            });
            mIngredient = ingredient;
            mName.setText(mIngredient.getName());
            mCategory.setText(mIngredient.getCategory());
        }
    }

    private class IngredientAdapter extends RecyclerView.Adapter<IngredientHolder> {
        private List<Ingredient> mIngredients;

        IngredientAdapter(List<Ingredient> ingredients) {
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

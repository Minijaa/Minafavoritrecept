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

//Fragment-klass som hanterar vyn för att visa shoppingläget för en enskild shoppinglista..
public class ShoppingLiveModeFragment extends Fragment {
    private RecyclerView mShoppingIngredientsRecyclerView;
    private ShoppingList mShoppingList;
    private IngredientAdapter mAdapter;
    private List<String> mStoreNames;
    private int mNameFlag;
    private int mCategoryFlag;
    private int mStoreSpinnerIndex = 0;
    private static final String STORE_SPINNER_INDEX = "index";

    //Läs in aktuell inköpslista, initiera lista över butik-namn och läs ev in position för markerad butik.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            mStoreSpinnerIndex = savedInstanceState.getInt(STORE_SPINNER_INDEX);
        }
        UUID shoppingListId = (UUID) getActivity().getIntent().getSerializableExtra(ShoppingActivity.EXTRA_SHOPPINGLIST_ID);
        mShoppingList = RecipeStorage.get(getActivity()).getShoppingList(shoppingListId);
        setUpStoreNamesList();
        setHasOptionsMenu(true);

    }
    // Initiera lista med alla butik-namn och lägg till en standardbutik på den första positionen
    private void setUpStoreNamesList() {
        mStoreNames = new ArrayList<>();
        for (Store s : RecipeStorage.get(getActivity()).getStores()) {
            mStoreNames.add(s.getName());
        }
        Store standardStore = new Store();
        standardStore.setName("-Butik-");
        if (!mStoreNames.contains(standardStore.getName())) {
            mStoreNames.add(0, standardStore.getName());
        }
    }

    // Spara undan positionen för den för tillfället markerade butiken (sorteringsordningen)
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STORE_SPINNER_INDEX, mStoreSpinnerIndex);
    }

    @Nullable
    @Override
    // Skapa vyn och ställ in alla knappar/textfält.
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopping_live_mode, container, false);
        mShoppingIngredientsRecyclerView = (RecyclerView) v.findViewById(R.id.shopping_live_mode_recyclerview);
        mShoppingIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    // Uppdaterar UI:n.
    private void updateUI() {
        List<Ingredient> ingredients = mShoppingList.getIngredients();
        mAdapter = new IngredientAdapter(ingredients);
        mShoppingIngredientsRecyclerView.setAdapter(mAdapter);
    }

    // Kopplar upp layout-filen för toolbar-menyn.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_shopping_live_mode, menu);
        setUpStoreSpinner(menu);
    }

    // Kopplar upp Spinnern som används för att välja butik och därmed också ändrar ingredienslistans
    // sorteringsordning.
    private void setUpStoreSpinner(Menu menu) {
        MenuItem spinnerItem = menu.findItem(R.id.store_spinner);
        Spinner storeSpinner = (Spinner) MenuItemCompat.getActionView(spinnerItem);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.store_spinner_item, mStoreNames);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.storespinner_dropdown_item);
        storeSpinner.setAdapter(spinnerArrayAdapter);
        storeSpinner.setSelection(mStoreSpinnerIndex);
        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String storeName = (String) parent.getItemAtPosition(position);
                List<Ingredient> sortedShoppingList = RecipeStorage.get(getActivity()).getSortedShoppingList(storeName, mShoppingList.getIngredients());
                mStoreSpinnerIndex = position;
                mShoppingList.setIngredients(sortedShoppingList);
                updateUI();
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // Sköter funktionalitet för knappen i toolbaren.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.shopping_enter_edit_mode) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // ViewHolder-klass som håller ingrediensernas vyer.
    private class IngredientHolder extends RecyclerView.ViewHolder {
        private CheckBox mCheckBox;
        private Ingredient mIngredient;
        private TextView mName;
        private TextView mCategory;

        //Konstruktor som initierar de olika widgetsen för varje ingrediens i listan.
        IngredientHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_shoppinglist_shopping_mode, parent, false));
            mCheckBox = (CheckBox) itemView.findViewById(R.id.shopping_live_mode_ingredient_checkbox);
            mName = (TextView) itemView.findViewById(R.id.shopping_live_mode_ingredient_name_textview);
            mCategory = (TextView) itemView.findViewById(R.id.shopping_live_mode_ingredient_category_textview);
            mNameFlag = mName.getPaintFlags();
            mCategoryFlag = mCategory.getPaintFlags();
        }

        //Binder en ingrediens till IngredientHoldern(ViewHoldern).
        void bind(final Ingredient ingredient) {
            setUpCheckBox(ingredient);
            mIngredient = ingredient;
            mName.setText(mIngredient.getName());
            mCategory.setText(mIngredient.getCategory());
        }

        // Koppla upp checkboxen som används för att markera och avmarkera en ingrediens. Gör namntexten
        // och kategoritexten genomstruken om ingrediensen är markerad (Checked).
        private void setUpCheckBox(final Ingredient ingredient) {
            mCheckBox.setChecked(ingredient.isMarked());
            if (ingredient.isMarked()) {
                mName.setPaintFlags(mName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mCategory.setPaintFlags(mCategory.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mName.setPaintFlags(mNameFlag);
                mCategory.setPaintFlags(mCategoryFlag);
            }
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ingredient.setMarked(isChecked);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    //Adapterklass som skapar IngredientHolder-objekt samt binder ingredienser till dessa.
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

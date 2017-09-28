/*
Namn: Samuel Hozjan
Personnr: 820612-0159
elevId: saho0099
email: samuel.hozjan@gmail.com
 */
package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import java.util.List;
import java.util.UUID;

//Fragment-klass som hanterar vyn för att visa en enskild inköpslista.
public class ShoppingFragment extends Fragment {
    private RecyclerView mShoppingIngredientsRecyclerView;
    private ShoppingList mShoppingList;
    private IngredientAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID shoppingListId = (UUID) getActivity().getIntent().getSerializableExtra(ShoppingActivity.EXTRA_SHOPPINGLIST_ID);
        mShoppingList = RecipeStorage.get(getActivity()).getShoppingList(shoppingListId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    // Skapa vyn och ställ in alla knappar/textfält samt recyclerview.
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopping, container, false);
        setUpShoppingListNameEditText(v);
        mShoppingIngredientsRecyclerView = (RecyclerView) v.findViewById(R.id.shopping_ingredients_recycler_view);
        mShoppingIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    // Ställer in EditText-fältet för att namnge inköpslistan.
    private void setUpShoppingListNameEditText(View v) {
        EditText name = (EditText) v.findViewById(R.id.store_edit_name);
        name.setText(mShoppingList.getName());
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mShoppingList.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // Uppdaterar UI:n.
    private void updateUI() {
        List<Ingredient> ingredients = mShoppingList.getIngredients();
        mAdapter = new IngredientAdapter(ingredients);
        mShoppingIngredientsRecyclerView.setAdapter(mAdapter);
    }

    //Tar fram position i spinnern för specifierad söksträng.
    private int getSpinnerIndex(Spinner spinner, String searchString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(searchString)) {
                index = i;
            }
        }
        return index;
    }

    // Kopplar upp layout-filen för toolbar-menyn.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_shopping, menu);
    }

    //Sköter funktionalitet för knapparna i toolbaren.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Skapar en ny ingrediens och ser till att den hamnar överst samt får fokus.
        if (item.getItemId() == R.id.shopping_new_ingredient) {
            Ingredient newIngredient = new Ingredient();
            for (Ingredient i : mShoppingList.getIngredients()){
                i.setFocused(false);
            }
            newIngredient.setFocused(true);
            mShoppingList.addIngredient(0,newIngredient);
            mAdapter.notifyDataSetChanged();
        } else if (item.getItemId() == R.id.shopping_enter_shopping_mode) {
            Intent intent = ShoppingLiveModeActivity.newIntent(getContext(), mShoppingList.getId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // ViewHolder-klass som håller ingrediensernas vyer.
    private class IngredientHolder extends RecyclerView.ViewHolder {
        private EditText mNameEditText;
        private Spinner mCategorySpinner;
        private ImageButton mDeleteButton;
        private Ingredient mIngredient;

        //Konstruktor som initierar de olika widgetsen för varje ingrediens i listan.
        IngredientHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_shoppinglist, parent, false));
            mNameEditText = (EditText) itemView.findViewById(R.id.shopping_ingredient_name_edittext);
            mCategorySpinner = (Spinner) itemView.findViewById(R.id.shopping_ingredient_categoryspinner);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.shopping_ingredient_delete_button);
        }

        //Binder en ingrediens till IngredientHoldern (View Holdern).
        void bind(final Ingredient ingredient) {
            mIngredient = ingredient;
            setUpIngredientCategorySpinner();
            setUpIngredientNameEditText();
            setUpIngredientDeleteButton();
        }

        //Ställer in delete-knappen för en enskild ingrediens.
        private void setUpIngredientDeleteButton() {
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Varning!");
                    if (mIngredient.getName() != null) {
                        alert.setMessage("Är du säker på att du vill ta bort Ingrediensen " + "\"" + mIngredient.getName() + "\"");
                    } else {
                        alert.setMessage("Är du säker på att du vill ta bort den namnlösa Ingrediensen?");
                    }
                    alert.setPositiveButton("JA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mShoppingList.removeIngredient(mIngredient);
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

        // Ställer in EditText-fältet för att kunna namnge en ingrediens.
        private void setUpIngredientNameEditText() {
            mNameEditText.setText(mIngredient.getName());
            if (mIngredient.isFocused()){
                mNameEditText.requestFocus();

            }


            mNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mIngredient.setName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        // Ställer in Spinnern för att välja vilken kategori en specifik ingredient tillhör.
        private void setUpIngredientCategorySpinner() {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ingredient_category_choices, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            mCategorySpinner.setAdapter(adapter);
            mCategorySpinner.setSelection(getSpinnerIndex(mCategorySpinner, mIngredient.getCategory()));
            mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mIngredient.setCategory((String) parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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

    //Döper inköpslistan till ett autogenererat namn om inget namn valts, samt lagrar data på fil.
    @Override
    public void onPause() {
        super.onPause();
        if (mShoppingList.getName() == null || mShoppingList.getName().equals("")) {
            RecipeStorage.get(getActivity());
            mShoppingList.setName("Inköpslista #" + RecipeStorage.getNoNameNr("shoppinglist"));
        }
        for (Ingredient i : mShoppingList.getIngredients()){
            if (i.getName() == null || i.getName().equals("")){
                i.setName("Namnlös ingredient");
            }
        }
        RecipeStorage.get(getActivity()).storeData();
    }

}

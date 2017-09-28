/*
Namn: Samuel Hozjan
Personnr: 820612-0159
elevId: saho0099
email: samuel.hozjan@gmail.com
 */
package com.gmail.hozjan.samuel.minafavoritrecept;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;
import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

//Fragment-klass som hanterar vyn för att visa en enskild butik.
public class StoreFragment extends Fragment {
    private RecyclerView mCategoryRecyclerView;
    private Store mStore;
    private CategoryAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID storeId = (UUID) getActivity().getIntent().getSerializableExtra(StoreActivity.EXTRA_STORE_ID);
        mStore = RecipeStorage.get(getActivity()).getStore(storeId);
        setHasOptionsMenu(true);
        mItemTouchHelper = new ItemTouchHelper(callbackItemTouchHelper);

    }

    @Nullable
    @Override
    // Skapa vyn och ställ in textfält och recyclerview
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_store, container, false);
        setUpStoreNameEditText(v);
        mCategoryRecyclerView = (RecyclerView) v.findViewById(R.id.store_category_recycler_view);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mItemTouchHelper.attachToRecyclerView(mCategoryRecyclerView);
        updateUI();
        return v;
    }

    // Ställer in EditText-fältet för att kunna namnge butiken.
    private void setUpStoreNameEditText(View v) {
        EditText name = (EditText) v.findViewById(R.id.store_edit_name);
        name.setText(mStore.getName());
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStore.setName(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Anonym klass som hanterar Drag and drop funktion på kategorierna i listan. På så vis kan
    // användaren sortera om kategori-listan för aktuell butik.
    private ItemTouchHelper.Callback callbackItemTouchHelper = new ItemTouchHelper.Callback() {

        // Bestäm att vyerna i listan ska kunna dras uppåt och nedåt, dock tillåts inte swiping åt
        // varken höger eller vänster.
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(UP | DOWN, 0);
        }

        // Aktiveras när användaren drar en vy. Flyttar upp/ner vyn steg för steg i listan tills den når
        // sin targetPosition(där användaren släppt vyn).
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            List<String> categories = mStore.getCategories();
            final int sourcePosition = viewHolder.getAdapterPosition();
            final int targetPosition = target.getAdapterPosition();

            if (sourcePosition < targetPosition) {
                for (int i = sourcePosition; i < targetPosition; i++) {
                    Collections.swap(categories, i, i + 1);
                }
            } else {
                for (int i = sourcePosition; i> targetPosition; i--){
                    Collections.swap(categories, i, i - 1);
                }
            }
            mAdapter.notifyItemMoved(sourcePosition, targetPosition);
            return true;
        }

        // Metoden krävs men används dock inte.
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}

        // Markerar den dragna vyn genom att ändra färg på dess bakgrund.
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState == ACTION_STATE_DRAG) {
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#26a69a"));
            }
        }

        //Återställer  vyns bakgrundsfärg vid avslutad dragning.
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);
        }
    };
    // Uppdaterar UI:n
    private void updateUI() {
        List<String> categories = mStore.getCategories();
        mAdapter = new CategoryAdapter(categories);
        mCategoryRecyclerView.setAdapter(mAdapter);
    }

    // ViewHolder-klass som håller kategoriernas vyer.
    private class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;

        //Konstruktor som skapar vyerna samt initierar widgets för varje kategori i listan.
        CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_category, parent, false));
            mNameTextView = (TextView) itemView.findViewById(R.id.store_category_name_textview);
        }

        //Binder en kategori till CategoryHoldern(ViewHoldern).
        void bind(final String category) {
            mNameTextView.setText(category);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.ingredient_category_choices, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        }
    }

    //Adapterklass som skapar CategoryHolder-objekt samt binder kategorier till dessa.
    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private List<String> mCategories;
        CategoryAdapter(List<String> categories) {
            mCategories = categories;
        }
        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CategoryHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(CategoryHolder holder, int position) {
            String category = mCategories.get(position);
            holder.bind(category);
        }
        @Override
        public int getItemCount() {
            return mCategories.size();
        }
    }

    //Döper butiken till ett autogenererat namn om inget namn valts, samt lagrar data på fil.
    @Override
    public void onPause() {
        super.onPause();
        if (mStore.getName() == null || mStore.getName().equals("")){
            RecipeStorage.get(getActivity());
            mStore.setName("Butik #" + RecipeStorage.getNoNameNr("store"));
        }
        RecipeStorage.get(getActivity()).storeData();
    }
}

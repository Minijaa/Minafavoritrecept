package com.gmail.hozjan.samuel.minafavoritrecept;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import java.util.List;
import java.util.UUID;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class StoreFragment extends Fragment {
    private EditText mName;
    private RecyclerView mCategoryRecyclerView;
    private Store mStore;
    private CategoryAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID storeId = (UUID) getActivity().getIntent().getSerializableExtra(StoreActivity.EXTRA_STORE_ID);
        mStore = RecipeStorage.get(getActivity()).getStore(storeId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_store, container, false);
        mName = (EditText) v.findViewById(R.id.store_edit_name);
        mName.setText(mStore.getName());
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStore.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCategoryRecyclerView = (RecyclerView) v.findViewById(R.id.store_category_recycler_view);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    private void updateUI() {
        List<String> categories = mStore.getCategories();
        mAdapter = new CategoryAdapter(categories);
        mCategoryRecyclerView.setAdapter(mAdapter);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private String mCategory;


        public CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_category, parent, false));
            mNameTextView = (TextView) itemView.findViewById(R.id.store_category_name_textview);
        }

        public void bind(final String category) {
            mCategory = category;
            mNameTextView.setText(category);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ingredient_category_choices, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private List<String> mCategories;

        public CategoryAdapter(List<String> categories) {
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

    @Override
    public void onPause() {
        super.onPause();
        //RecipeStorage.get(getActivity()).storeData();
    }

}


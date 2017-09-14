package com.gmail.hozjan.samuel.minafavoritrecept;

import android.graphics.Color;
import android.hardware.camera2.params.ColorSpaceTransform;
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
import android.widget.ImageButton;
import android.widget.TextView;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;
import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;


public class StoreFragment extends Fragment {
    private EditText mName;
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
        mItemTouchHelper.attachToRecyclerView(mCategoryRecyclerView);
        updateUI();

        return v;
    }

    private ItemTouchHelper.Callback callbackItemTouchHelper = new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int move = makeMovementFlags(UP | DOWN, 0);
            return move;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            List<String> categories = mStore.getCategories();
            final int sourcePosition = viewHolder.getAdapterPosition();
            final int targetPosition = target.getAdapterPosition();
            //String movedCategory = mStore.getCategories().get(sourcePosition);
            //categories.remove(sourcePosition);
            if (sourcePosition < targetPosition) {
                for (int i = sourcePosition; i < targetPosition; i++) {
                    Collections.swap(categories, i, i + 1);
                }
            } else {
                for (int i = sourcePosition; i> targetPosition; i--){
                    Collections.swap(categories, i, i-1);
                }
            }

            //String movedCategory = categories.remove(sourcePosition);
            //categories.add(targetPosition > sourcePosition ? targetPosition - 1 : targetPosition, movedCategory);

            mAdapter.notifyItemMoved(sourcePosition, targetPosition);

            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState == ACTION_STATE_DRAG) {
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#26a69a"));
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);
        }
    };

    private void updateUI() {
        List<String> categories = mStore.getCategories();
        mAdapter = new CategoryAdapter(categories);
        mCategoryRecyclerView.setAdapter(mAdapter);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        //private ImageButton mMoveButton;
        private String mCategory;


        public CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_category, parent, false));
            mNameTextView = (TextView) itemView.findViewById(R.id.store_category_name_textview);
            //mMoveButton = (ImageButton) itemView.findViewById(R.id.store_move_button);
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
        RecipeStorage.get(getActivity()).storeData();
    }

}



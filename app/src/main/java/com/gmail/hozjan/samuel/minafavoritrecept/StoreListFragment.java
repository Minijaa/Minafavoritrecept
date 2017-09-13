package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;


public class StoreListFragment extends Fragment {
    private RecyclerView mListRecyclerView;
    private StoreListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //RecipeStorage.get(getActivity()).loadData();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_store_list, container, false);

        mListRecyclerView = (RecyclerView) v.findViewById(R.id.store_list_recycler_view);
        mListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }

    private class StoreListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Store mStore;
        private TextView mStoreName;
        private ImageButton mDeleteButton;

        public StoreListHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_store, parent, false));
            itemView.setOnClickListener(this);

            mStoreName = (TextView) itemView.findViewById(R.id.store_name_textview);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.store_move_button);
        }

        @Override
        public void onClick(View v) {
            Intent intent = StoreActivity.newIntent(getActivity(), mStore.getId());
            startActivity(intent);
        }

        private void bind(Store store) {
            mStore = store;
            mStoreName.setText(mStore.getName());
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Varning!");
                    if (mStore.getName() != null) {
                        alert.setMessage("Är du säker på att du vill ta bort affären " + "\"" + mStore.getName() + "\"");
                    } else {
                        alert.setMessage("Är du säker på att du vill ta bort den namnlösa inköpslistan?");
                    }
                    alert.setPositiveButton("JA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RecipeStorage.get(getActivity()).deleteStore(mStore);
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
    }

    private class StoreListAdapter extends RecyclerView.Adapter<StoreListHolder> {
        private List<Store> mStores;

        public StoreListAdapter(List<Store> stores) {
            mStores = stores;
        }

        @Override
        public StoreListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new StoreListHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(StoreListHolder holder, int position) {
            Store store = mStores.get(position);
            holder.bind(store);
        }

        @Override
        public int getItemCount() {
            return mStores.size();
        }
    }

    private void updateUI() {
        RecipeStorage recipeStorage = RecipeStorage.get(getActivity());
        List<Store> stores = recipeStorage.getStores();
        if (mAdapter == null) {
            mAdapter = new StoreListAdapter(stores);
            mListRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_store_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.store_list_new_store) {
            Store store = new Store();
            RecipeStorage.get(getActivity()).addStore(store);
            Intent intent = StoreActivity.newIntent(getActivity(), store.getId());
            startActivity(intent);
            return true;

        } else if (item.getItemId() == R.id.store_navigate_to_recipe_list) {
            Intent intent = RecipeListActivity.newIntent(getActivity());
            startActivity(intent);
        } else if (item.getItemId() == R.id.store_navigate_to_shoppinglists) {
            Intent intent = ShoppingListActivity.newIntent(getActivity());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        //RecipeStorage recipeStorage = RecipeStorage.get(getActivity());
        //recipeStorage.loadData();
        updateUI();
    }

}

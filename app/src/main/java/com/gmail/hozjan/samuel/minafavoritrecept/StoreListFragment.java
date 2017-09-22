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

// Fragment-klass som hanterar visning av alla skapade butiker i en lista.
public class StoreListFragment extends Fragment {
    private RecyclerView mListRecyclerView;
    private StoreAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    // Skapa vyn och koppla upp recyclerviewn.
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_store_list, container, false);
        mListRecyclerView = (RecyclerView) v.findViewById(R.id.store_list_recycler_view);
        mListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    // ViewHolder-klass som håller butikernas vyer.
    private class StoreHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Store mStore;
        private TextView mStoreName;
        private ImageButton mDeleteButton;

        //Konstruktor som initierar textviewn för namn och deleteknappen, samt gör den klickbar.
        StoreHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_store, parent, false));
            itemView.setOnClickListener(this);
            mStoreName = (TextView) itemView.findViewById(R.id.store_name_textview);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.store_move_button);
        }

        //Startar en instans av StoreActivity som i sin tur skapar en instans av StoreFragment
        // innehållande butiken användaren klickat på.
        @Override
        public void onClick(View v) {
            Intent intent = StoreActivity.newIntent(getActivity(), mStore.getId());
            startActivity(intent);
        }

        //Binder en butik till StoreHoldern(ViewHoldern).
        private void bind(Store store) {
            mStore = store;
            mStoreName.setText(mStore.getName());
            setUpDeleteButton();
        }

        // Ställer in delete-knappen för en enskild butik.
        private void setUpDeleteButton() {
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

    //Adapterklass som skapar StoreHolder-objekt samt binder butiker till dessa.
    private class StoreAdapter extends RecyclerView.Adapter<StoreHolder> {
        private List<Store> mStores;
        StoreAdapter(List<Store> stores) {
            mStores = stores;
        }

        @Override
        public StoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new StoreHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(StoreHolder holder, int position) {
            Store store = mStores.get(position);
            holder.bind(store);
        }
        @Override
        public int getItemCount() {
            return mStores.size();
        }
    }

    //Uppdaterar UI:n.
    private void updateUI() {
        RecipeStorage recipeStorage = RecipeStorage.get(getActivity());
        List<Store> stores = recipeStorage.getStores();
        if (mAdapter == null) {
            mAdapter = new StoreAdapter(stores);
            mListRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    // Kopplar upp layout-filen för toolbar-menyn.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_store_list, menu);
    }

    // Sköter funktionalitet för knapparna i toolbaren.
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
        updateUI();
    }
}

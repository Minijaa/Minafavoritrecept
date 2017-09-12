package com.gmail.hozjan.samuel.minafavoritrecept;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import org.w3c.dom.Text;

import java.util.List;


public class ShoppingListFragment extends Fragment {
    private RecyclerView mShoppingListRecyclerView;
    private ShoppingListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        RecipeStorage.get(getActivity()).loadData();
        
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        mShoppingListRecyclerView = (RecyclerView)v.findViewById(R.id.shoppinglists_recycler_view);
        mShoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }

    private class ShoppingListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ShoppingList mShoppingList;
        private TextView mNameTextView;
        private TextView mQuantityTextView;
        private TextView mDateTextView;
        private ImageButton mDeleteButton;

        public ShoppingListHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_list_of_shoppinglists, parent, false));
            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.shoppinglist_shopping_name);
            mQuantityTextView = (TextView) itemView.findViewById(R.id.shoppinglist_shopping_antal_artiklar);
            mDateTextView = (TextView) itemView.findViewById(R.id.shoppinglist_shopping_date);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.shoppinglist_shopping_delete_button);
        }

        @Override
        public void onClick(View v) {
            Intent intent = ShoppingActivity.newIntent(getActivity(), mShoppingList.getId());
            startActivity(intent);
        }

        private void bind(ShoppingList shoppingList) {
            mShoppingList = shoppingList;
            mNameTextView.setText(mShoppingList.getName());
            mQuantityTextView.setText(mShoppingList.getQuantity());
            mDateTextView.setText(mShoppingList.getDateAsString());

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Varning!");
                    if (mShoppingList.getName() != null) {
                        alert.setMessage("Är du säker på att du vill ta bort inköpslistan " + "\"" + mShoppingList.getName() + "\"");
                    } else {
                        alert.setMessage("Är du säker på att du vill ta bort den namnlösa inköpslistan?");
                    }
                    alert.setPositiveButton("JA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RecipeStorage.get(getActivity()).deleteShoppingList(mShoppingList);
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
    private class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListHolder> {
        private List<ShoppingList> mShoppingLists;

        public ShoppingListAdapter(List<ShoppingList> shoppingLists){
            mShoppingLists = shoppingLists;
        }


        @Override
        public ShoppingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ShoppingListHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ShoppingListHolder holder, int position) {
            ShoppingList shoppingList = mShoppingLists.get(position);
            holder.bind(shoppingList);
        }

        @Override
        public int getItemCount() {
            return mShoppingLists.size();
        }
    }
    private void updateUI() {
        RecipeStorage recipeStorage = RecipeStorage.get(getActivity());
        List<ShoppingList> shoppingLists = recipeStorage.getShoppingLists();
        if (mAdapter == null) {
            mAdapter = new ShoppingListAdapter(shoppingLists);
            mShoppingListRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_shopping_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_shoppinglist) {
            ShoppingList shoppingList = new ShoppingList();
            RecipeStorage.get(getActivity()).addShoppingList(shoppingList);
            Intent intent = ShoppingActivity.newIntent(getActivity(), shoppingList.getId());
            startActivity(intent);
            return true;

        } else if (item.getItemId() == R.id.navigate_to_recipe_list) {
            Intent intent = RecipeListActivity.newIntent(getActivity());
            startActivity(intent);
        }else if (item.getItemId() == R.id.shopping_stores){

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

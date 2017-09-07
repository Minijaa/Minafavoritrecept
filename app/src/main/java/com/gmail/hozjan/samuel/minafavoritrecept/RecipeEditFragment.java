package com.gmail.hozjan.samuel.minafavoritrecept;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class RecipeEditFragment extends Fragment {
    private static final int REQUEST_PHOTO = 2;
    private Recipe mRecipe;
    private EditText mRecipeName;
    private RecyclerView mIngredientsRecyclerView;
    private EditText mInsctructions;
    private File mRecipeImageFile;
    private IngredientAdapter mAdapter;
    private ImageButton mPhotoButton;
    private ImageView mRecipeImageView;
    private ImageButton mAddIngredientButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID recipeId = (UUID) getActivity().getIntent().getSerializableExtra(RecipeEditActivity.EXTRA_RECIPE_ID);
        mRecipe = RecipeStorage.get(getActivity()).getRecipe(recipeId);
        mRecipeImageFile = RecipeStorage.get(getActivity()).getImageFile(mRecipe);
        setHasOptionsMenu(true);

    }

    public RecipeEditFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_recipe_edit, container, false);
        mRecipeName = (EditText) v.findViewById(R.id.edit_recipe_name);
        mRecipeName.setText(mRecipe.getName());
        mRecipeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecipe.setmName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        PackageManager packageManager = getActivity().getPackageManager();
        mRecipeImageView = (ImageView) v.findViewById(R.id.edit_recipe_image);
        updateImageView();
        mPhotoButton = (ImageButton) v.findViewById(R.id.edit_photo_button);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mRecipeImageFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.gmail.hozjan.samuel.minafavoritrecept.fileprovider", mRecipeImageFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivites = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivites) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mAddIngredientButton = (ImageButton) v.findViewById(R.id.edit_add_ingredient_button);
        mAddIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecipe.addIngredient(new Ingredient());
                mAdapter.notifyDataSetChanged();
            }
        });
        mIngredientsRecyclerView = (RecyclerView) v.findViewById(R.id.ingredients_recycler_view);
        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mInsctructions = (EditText) v.findViewById(R.id.edit_recipe_description);
        mInsctructions.setText(mRecipe.getDescription());
        mInsctructions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecipe.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Spinner categorySpinner = (Spinner) v.findViewById(R.id.recipe_edit_categoryspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.category_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelection(getSpinnerIndex(categorySpinner, mRecipe.getCategory()));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mRecipe.setCategory((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        }
        );
        updateUI();
        return v;
    }


    private void updateImageView() {
        if (mRecipeImageFile == null || !mRecipeImageFile.exists()) {
            mRecipeImageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = ImageHandler.getScaledBitmap(mRecipeImageFile.getPath(), getActivity());
            mRecipeImageView.setImageBitmap(bitmap);
        }
    }

    // Ta emot resultatet från kamera-appen
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.gmail.hozjan.samuel.minafavoritrecept.fileprovider", mRecipeImageFile);
                getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                updateImageView();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_recipe_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.appbar_delete_recipe_button) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Varning!");
            if (mRecipe.getName() != null) {
                alert.setMessage("Är du säker på att du vill ta bort receptet " + "\"" + mRecipe.getName() + "\"");
            } else {
                alert.setMessage("Är du säker på att du vill ta bort det namnlösa receptet?");
            }
            alert.setPositiveButton("JA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RecipeStorage.get(getActivity()).deleteRecipe(mRecipe);
                    dialog.dismiss();
                    getActivity().finish();
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
        return super.onOptionsItemSelected(item);


        //Intent intent = RecipeEditActivity.newIntent(getActivity(), mRecipe.getId());
        //startActivity(intent);

    }

    private class IngredientHolder extends RecyclerView.ViewHolder {
        private EditText mIngredientNameEditText;
        private Spinner mIngredientCategorySpinner;
        private ImageButton mIngredientDeleteButton;
        private Ingredient mIngredient;

        public IngredientHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_ingredient, parent, false));
            mIngredientNameEditText = (EditText) itemView.findViewById(R.id.ingredient_name_edittext);
            mIngredientCategorySpinner = (Spinner) itemView.findViewById(R.id.ingredient_categoryspinner);
            mIngredientDeleteButton = (ImageButton) itemView.findViewById(R.id.ingredient_delete_button);
        }

        public void bind(final Ingredient ingredient) {
            mIngredient = ingredient;
            mIngredientNameEditText.setText(mIngredient.getName());
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ingredient_category_choices, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            mIngredientCategorySpinner.setAdapter(adapter);
            mIngredientCategorySpinner.setSelection(getSpinnerIndex(mIngredientCategorySpinner, mIngredient.getCategory()));
            mIngredientCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIngredient.setCategory((String)parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mIngredientNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mIngredient.setName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            mIngredientDeleteButton.setOnClickListener(new View.OnClickListener() {

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
                            mRecipe.removeIngredient(mIngredient);
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

    private void updateUI() {
        List<Ingredient> ingredients = mRecipe.getIngredients();
        mAdapter = new IngredientAdapter(ingredients);
        mIngredientsRecyclerView.setAdapter(mAdapter);
    }

    private int getSpinnerIndex(Spinner spinner, String searchString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(searchString)) {
                index = i;
            }
        }
        return index;
    }


}


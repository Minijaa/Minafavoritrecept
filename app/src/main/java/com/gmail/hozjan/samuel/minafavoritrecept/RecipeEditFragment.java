package com.gmail.hozjan.samuel.minafavoritrecept;


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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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

public class RecipeEditFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final int REQUEST_PHOTO = 2;
    private Recipe mRecipe;
    private EditText mRecipeName;
    private EditText mInsctructions;
    private EditText mIngrediences;
    private File mRecipeImageFile;


    private ImageButton mPhotoButton;
    private ImageView mRecipeImageView;





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID recipeId = (UUID) getActivity().getIntent().getSerializableExtra(RecipeEditActivity.EXTRA_RECIPE_ID);
        mRecipe = RecipeStorage.get(getActivity()).getRecipe(recipeId);
        mRecipeImageFile = RecipeStorage.get(getActivity()).getImageFile(mRecipe);

    }

    public RecipeEditFragment() {
        // Required empty public constructor
    }


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
        mRecipeImageView = (ImageView)v.findViewById(R.id.edit_recipe_image);
        updateImageView();
        mPhotoButton = (ImageButton)v.findViewById(R.id.edit_photo_button);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mRecipeImageFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.gmail.hozjan.samuel.minafavoritrecept.fileprovider", mRecipeImageFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivites = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivites){
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mIngrediences = (EditText)v.findViewById(R.id.edit_recipe_ingrediences);
        mIngrediences.setText(mRecipe.getIngrediences());
        mIngrediences.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecipe.setIngrediences(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInsctructions = (EditText)v.findViewById(R.id.edit_recipe_description);
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
        Spinner categorySpinner = (Spinner)v.findViewById(R.id.recipe_edit_categoryspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.category_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(this);

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mRecipe.setCategory((String)parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void updateImageView(){
        if (mRecipeImageFile == null || !mRecipeImageFile.exists()){
            mRecipeImageView.setImageDrawable(null);
            }else {
            Bitmap bitmap = ImageHandler.getScaledBitmap(mRecipeImageFile.getPath(), getActivity());
            mRecipeImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.gmail.hozjan.samuel.minafavoritrecept.fileprovider", mRecipeImageFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateImageView();
        }
    }
}

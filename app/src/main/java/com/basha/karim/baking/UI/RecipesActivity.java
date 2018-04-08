package com.basha.karim.baking.UI;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.basha.karim.baking.R;
import com.basha.karim.baking.adapter.RecipesAdapter;
import com.basha.karim.baking.models.Recipe;
import com.basha.karim.baking.retrofit.RetrofitBuilder;
import com.basha.karim.baking.utils.SimpleIdlingResource;
import com.basha.karim.baking.utils.ToastUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesActivity extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler {

    private String LOG_TAG = RecipesActivity.class.getSimpleName();

    private ArrayList<Recipe> mRecipesList;
    private RecipesAdapter mRecipesAdapter;
    public static String KEY_RECIPES_BUNDLE = "key-recipes-bundle";
    public static String KEY_STEP_LIST_DETAIL_EXTRA = "key-step_list_detail-extra";
    public static String KEY_STEP_INDEX_DETAIL_EXTRA = "key-step_index_detail-extra";
    public static String KEY_RECIPE_DETAIL_EXTRA = "key-recipe_detail-extra";
    private SimpleIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        RecyclerView recyclerViewRecipes = (RecyclerView) findViewById(R.id.rv_recipes);
        mRecipesAdapter = new RecipesAdapter(this);
        recyclerViewRecipes.setAdapter(mRecipesAdapter);
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            GridLayoutManager gridLayoutManager;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                gridLayoutManager = new GridLayoutManager(this, 3);
            } else {
                gridLayoutManager = new GridLayoutManager(this, 2);
            }
            recyclerViewRecipes.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerViewRecipes.setLayoutManager(linearLayoutManager);
        }
        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "savedInstanceState != null");
            ArrayList<Recipe> tempList = savedInstanceState.getParcelableArrayList(KEY_RECIPES_BUNDLE);
            if (tempList != null) {
                mRecipesList = tempList;
                mRecipesAdapter.setRecipesData(mRecipesList);
            }
        } else {
            Log.d(LOG_TAG, "savedInstanceState == null");
            RetrofitBuilder.RecipesInterface recipesInterface = RetrofitBuilder.fetchRecipes();
            final Call<ArrayList<Recipe>> recipesListTask = recipesInterface.getRecipesListTask();

            getIdlingResource();
            mIdlingResource.setIdle(false);

            recipesListTask.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    mRecipesList = response.body();
                    if (mRecipesList != null) {
                        mRecipesAdapter.setRecipesData(mRecipesList);
                    } else {
                        ToastUtils.createToast(getApplicationContext(), getString(R.string.toast_http_error), Toast.LENGTH_SHORT);
                    }
                    mIdlingResource.setIdle(true);
                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                    Log.d(LOG_TAG, t.getMessage());
                    ToastUtils.createToast(getApplicationContext(), getString(R.string.toast_http_error), Toast.LENGTH_SHORT);
                }
            });
        }
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Log.d(LOG_TAG, "onRecipeClick");
        Intent recipeDetailIntent = new Intent(this, RecipeDetailActivity.class);
        recipeDetailIntent.putExtra(KEY_RECIPE_DETAIL_EXTRA, recipe);
        startActivity(recipeDetailIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRecipesList != null)
            outState.putParcelableArrayList(KEY_RECIPES_BUNDLE, mRecipesList);
        super.onSaveInstanceState(outState);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return (IdlingResource) mIdlingResource;
    }
}

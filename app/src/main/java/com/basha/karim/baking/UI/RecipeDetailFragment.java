package com.basha.karim.baking.UI;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.basha.karim.baking.R;
import com.basha.karim.baking.adapter.RecipesDetailAdapter;
import com.basha.karim.baking.models.Ingredient;
import com.basha.karim.baking.models.Recipe;
import com.basha.karim.baking.models.Step;
import com.basha.karim.baking.widget.UpdateWidgetService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karim on 4/7/2018.
 */

public class RecipeDetailFragment extends Fragment {

    private static final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();
    private RecipesDetailAdapter mAdapter;
    private Recipe mRecipe = null;
    private static final String KEY_RECIPE_DETAIL_BUNDLE = "key-recipe_detail-bundle";
    private static final String KEY_SCROLL_VIEW_BUNDLE = "key-scroll_view-bundle";
    private ScrollView mIngredientsScrollView;
    private int[] mIngredientsScrollViewPosition = {0,0};

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        TextView ingredientsTextView = (TextView) rootView.findViewById(R.id.tv_detail_recipe_ingredients);
        RecyclerView recyclerViewSteps = (RecyclerView) rootView.findViewById(R.id.rv_detail_steps);
        mIngredientsScrollView = (ScrollView) rootView.findViewById(R.id.sv_ingredients);
        mAdapter = new RecipesDetailAdapter((RecipesDetailAdapter.RecipesDetailAdapterOnClickHandler) getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSteps.setAdapter(mAdapter);
        recyclerViewSteps.setLayoutManager(linearLayoutManager);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(KEY_RECIPE_DETAIL_BUNDLE);
            mIngredientsScrollViewPosition = savedInstanceState.getIntArray(KEY_SCROLL_VIEW_BUNDLE);
        } else {
            mRecipe = getArguments().getParcelable(RecipesActivity.KEY_RECIPE_DETAIL_EXTRA);
        }
        if (mRecipe != null) {
            loadIngredientsIntoUIandWidget(mRecipe.getIngredientList(), ingredientsTextView);
            loadStepsIntoUI((ArrayList<Step>) mRecipe.getStepList());
        }
        return rootView;
    }

    private void loadStepsIntoUI(ArrayList<Step> stepsList) {
        if (stepsList != null) {
            mAdapter.setStepsData(stepsList);
        }
    }

    private void loadIngredientsIntoUIandWidget(List<Ingredient> ingredientsList, TextView ingredientsTextView) {
        if (ingredientsList != null) {
            ArrayList<String> ingredientsListString = new ArrayList<>();
            ingredientsListString.add(mRecipe.getName());
            for (int x = 0; x < ingredientsList.size(); x++) {
                Ingredient i = ingredientsList.get(x);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (x == ingredientsList.size() - 1) {
                        CharSequence text = Html.fromHtml("-<b>" + i.getName() +
                                "</b>" + "<br>" + "    Quantity: " + i.getQuantity() + " " + i.getMeasure(), 0);
                        ingredientsListString.add(text.toString());
                        ingredientsTextView.append(text);
                    } else {
                        CharSequence text = Html.fromHtml("-<b>" + i.getName() +
                                "</b>" + "<br>" + "    Quantity: " + i.getQuantity() + " " + i.getMeasure(), 0);
                        ingredientsListString.add(text.toString());
                        ingredientsTextView.append(Html.fromHtml("-<b>" + i.getName() +
                                "</b>" + "<br>" + "    Quantity: " + i.getQuantity() + " " + i.getMeasure() + "<br>", 0));
                    }
                } else {
                    if (x == ingredientsList.size() - 1) {
                        CharSequence text = Html.fromHtml("-<b>" + i.getName() +
                                "</b>" + "<br>" + "    Quantity: " + i.getQuantity() + " " + i.getMeasure());
                        ingredientsListString.add(text.toString());
                        ingredientsTextView.append(text);
                    } else {
                        CharSequence text = Html.fromHtml("-<b>" + i.getName() +
                                "</b>" + "<br>" + "    Quantity: " + i.getQuantity() + " " + i.getMeasure() + "<br>");
                        ingredientsListString.add(text.toString());
                        ingredientsTextView.append(text);
                    }
                }
            }
            mIngredientsScrollView.scrollTo(mIngredientsScrollViewPosition[0],mIngredientsScrollViewPosition[1]);
            UpdateWidgetService.startUpdateWidgetService(getContext(), ingredientsListString);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mRecipe != null) {
            int[] i = new int[]{mIngredientsScrollView.getScrollX(),
                    mIngredientsScrollView.getScrollY()};
            outState.putIntArray(KEY_SCROLL_VIEW_BUNDLE, i);
            outState.putParcelable(KEY_RECIPE_DETAIL_BUNDLE, mRecipe);
        }
        super.onSaveInstanceState(outState);
    }


}


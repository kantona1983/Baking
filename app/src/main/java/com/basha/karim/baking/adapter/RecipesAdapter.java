package com.basha.karim.baking.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.basha.karim.baking.R;
import com.basha.karim.baking.models.Recipe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by karim on 4/7/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    private String LOG_TAG = RecipesAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Recipe> mRecipesData;
    private RecipesAdapterOnClickHandler mClickHandler;

    public interface RecipesAdapterOnClickHandler {
        void onRecipeClick(Recipe recipe);
    }

    public RecipesAdapter(RecipesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.recipe_list_item, parent, false);
        return new RecipesAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecipesAdapterViewHolder holder, final int position) {
        String imageURLString = mRecipesData.get(position).getImageString();
        if (imageURLString != null) {
            Uri imageURI = Uri.parse(imageURLString).buildUpon().build();
            Picasso.with(mContext).load(imageURI).into(holder.recipePhoto, new Callback() {
                @Override
                public void onSuccess() {
                    holder.recipeTitle.setText(mRecipesData.get(position).getName());
                }

                @Override
                public void onError() {
                    holder.recipeTitle.setText(mRecipesData.get(position).getName());
                }
            });
            return;
        }
        holder.recipeTitle.setText(mRecipesData.get(position).getName());
    }

    public void setRecipesData(ArrayList<Recipe> recipesList) {
        mRecipesData = recipesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRecipesData == null)
            return 0;
        else {
            return mRecipesData.size();
        }
    }

    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView recipePhoto;
        TextView recipeTitle;

        public RecipesAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            recipeTitle = (TextView) itemView.findViewById(R.id.tv_recipe_title);
            recipePhoto = (ImageView) itemView.findViewById(R.id.iv_recipe);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onRecipeClick(mRecipesData.get(getAdapterPosition()));
        }
    }
}


package com.basha.karim.baking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basha.karim.baking.R;
import com.basha.karim.baking.models.Step;

import java.util.ArrayList;

/**
 * Created by karim on 4/7/2018.
 */

public class RecipesDetailAdapter extends RecyclerView.Adapter<RecipesDetailAdapter.RecipesDetailAdapterViewHolder> {

    private String LOG_TAG = RecipesDetailAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Step> mStepsData;
    private RecipesDetailAdapterOnClickHandler mClickHandler;

    public interface RecipesDetailAdapterOnClickHandler {
        void onStepClick(ArrayList<Step> stepsList, int stepIndex);
    }

    public RecipesDetailAdapter(RecipesDetailAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public RecipesDetailAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.recipe_detail_step_item, parent, false);
        return new RecipesDetailAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecipesDetailAdapterViewHolder holder, final int position) {
        holder.shortDescTextView.setText((position) + "." + mStepsData.get(position).getShortDescription());
    }

    public void setStepsData(ArrayList<Step> stepsList) {
        mStepsData = stepsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mStepsData == null)
            return 0;
        else {
            return mStepsData.size();
        }
    }

    public class RecipesDetailAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView shortDescTextView;

        public RecipesDetailAdapterViewHolder(View itemView) {
            super(itemView);
            shortDescTextView = (TextView) itemView.findViewById(R.id.tv_step_short_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onStepClick(mStepsData, getAdapterPosition());
        }
    }
}



package com.basha.karim.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.basha.karim.baking.R;

import java.util.List;

/**
 * Created by karim on 4/7/2018.
 */

public class RecipeIngredientsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListViewFactory(this.getApplicationContext(),
                intent));
    }

    class ListViewFactory implements RemoteViewsService.RemoteViewsFactory {

        private List<String> mIngredientsList;
        private Context mContext;

        public ListViewFactory(Context context, Intent intent) {
            mContext = context;
            mIngredientsList = intent.getStringArrayListExtra(UpdateWidgetService.KEY_WIDGET_INGREDIENTS_LIST);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            this.mIngredientsList = RecipeIngredientsWidgetProvider.mIngredientsList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mIngredientsList != null) {
                return mIngredientsList.size();
            } else
                return 1;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews widgetItem = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item_widget);
            if (mIngredientsList==null) {
                widgetItem.setTextViewText(R.id.tv_ingredient_title_widget, "No data to display. Click here to select a recipe");
            } else {
                widgetItem.setTextViewText(R.id.tv_ingredient_title_widget, mIngredientsList.get(i));
            }

            Intent fillInIntent = new Intent();
            widgetItem.setOnClickFillInIntent(R.id.tv_ingredient_title_widget, fillInIntent);
            return widgetItem;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}


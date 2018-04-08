package com.basha.karim.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.basha.karim.baking.R;
import com.basha.karim.baking.UI.RecipesActivity;

import java.util.ArrayList;

/**
 * Created by karim on 4/7/2018.
 */

public class RecipeIngredientsWidgetProvider extends AppWidgetProvider {

    public static ArrayList<String> mIngredientsList;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<String> mIngredientsList) {

        // Construct the RemoteViews object
        RemoteViews widgets = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);
        Intent recipesIntent = new Intent(context, RecipesActivity.class);
        PendingIntent recipePendingIntent = PendingIntent.getActivity(context,10,recipesIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        widgets.setPendingIntentTemplate(R.id.lv_widget,recipePendingIntent);
        //widgets.setOnClickPendingIntent(R.id.lv_widget,recipePendingIntent);


        Intent widgetServiceIntent = new Intent(context,RecipeIngredientsWidgetService.class);
        widgetServiceIntent.putStringArrayListExtra(UpdateWidgetService.KEY_WIDGET_INGREDIENTS_LIST,mIngredientsList);
        widgets.setRemoteAdapter(R.id.lv_widget, widgetServiceIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, widgets);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        /*for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }*/
    }

    public void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, ArrayList<String> mIngredientsList) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, mIngredientsList);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,RecipeIngredientsWidgetProvider.class));
        if (intent.getAction().equals(UpdateWidgetService.ACTION_UPDATE_WIDGET)) {
            mIngredientsList=intent.getStringArrayListExtra(UpdateWidgetService.KEY_WIDGET_INGREDIENTS_LIST);
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget);
        updateWidgets(context,appWidgetManager,appWidgetIds,mIngredientsList);
        super.onReceive(context, intent);
    }

}



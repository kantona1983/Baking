package com.basha.karim.baking.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by karim on 4/7/2018.
 */

public class UpdateWidgetService extends IntentService {

    public static final String KEY_WIDGET_INGREDIENTS_LIST = "key-widget_ingredients-list";
    public static final String ACTION_UPDATE_WIDGET = "android.appwidget.action.APPWIDGET_UPDATE";

    public UpdateWidgetService() {
        super("UpdateWidgetService");
    }

    public static void startUpdateWidgetService(Context context, ArrayList<String> ingredientsList) {
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.putExtra(KEY_WIDGET_INGREDIENTS_LIST,ingredientsList);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent!=null) {
            ArrayList<String> ingredientsList = intent.getStringArrayListExtra(KEY_WIDGET_INGREDIENTS_LIST);
            Intent updateWidgetIntent = new Intent(ACTION_UPDATE_WIDGET);
            updateWidgetIntent.setAction(ACTION_UPDATE_WIDGET);
            updateWidgetIntent.putExtra(KEY_WIDGET_INGREDIENTS_LIST,ingredientsList);
            sendBroadcast(updateWidgetIntent);
        }
    }
}


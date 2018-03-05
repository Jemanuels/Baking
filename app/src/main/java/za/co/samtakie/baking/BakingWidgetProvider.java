package za.co.samtakie.baking;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import za.co.samtakie.baking.activity.RecipeActivity;
import za.co.samtakie.baking.activity.WidgetConfigurationActivity;

/**
 * Implementation of App Widget functionality.
 * Last updated 2018/02/07
 */
@SuppressWarnings("ALL")
public class BakingWidgetProvider extends AppWidgetProvider {


    public static final String PREFS_NAME = "AppWidget";
    public static final String PREF_PREFIX_KEY = "appwidget";
    public static final String PREF_PREFIX_WIDGETID = "widgetid";
    private static final String PREF_PREFIX_POSITION = "recipePosition";

    public static final String ACTION_TOAST = "za.co.samtakie.baking.ACTION_TOAST";
    public static final String EXTRA_STRING = "za.co.samtakie.baking.EXTRA_STRING";
    public static final String EXTRA_INT = "za.co.samtakie.baking.EXTRA_INT";
    public static final String CONFIGURE_START = "za.co.samtakie.baking.CONFIGURE_START";
    static SharedPreferences prefss;
    private int finalPosition;


    @SuppressLint("NewApi")
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
       BakingIntentService.startActionUpdateAppWidgets(context, true);

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        BakingIntentService.startActionBakingWidget(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    private static RemoteViews initViews(Context context, AppWidgetManager widgetManager, int widgetId, int position, String recipeName){
        RemoteViews mView = new RemoteViews(context.getPackageName(), R.layout.stack_widget_provider);

        Intent intent = new Intent(context, GridWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.putExtra(BakingWidgetProvider.CONFIGURE_START, position);
        mView.setTextViewText(R.id.txvWidgetTitle, recipeName);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        mView.setRemoteAdapter(widgetId, R.id.widgetCollectionList, intent);

        Intent onItemClick = new Intent(context, RecipeActivity.class);

        onItemClick.setData(Uri.parse(onItemClick.toUri(Intent.URI_INTENT_SCHEME)));
        onItemClick.putExtra("recipeName", recipeName);
        onItemClick.putExtra("position", position);
        PendingIntent onClickPendingIntent = PendingIntent.getActivity(context, 0, onItemClick, PendingIntent.FLAG_UPDATE_CURRENT);
        mView.setPendingIntentTemplate(R.id.widgetCollectionList, onClickPendingIntent);

        return mView;
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){


        prefss = context.getSharedPreferences(WidgetConfigurationActivity.PREFS_NAME, 0);
        String recipeName = prefss.getString(PREF_PREFIX_KEY + appWidgetId, null);
        int position = prefss.getInt(PREF_PREFIX_POSITION + appWidgetId, 0);
        RemoteViews mView = initViews(context, appWidgetManager, appWidgetId, position, recipeName);
        appWidgetManager.updateAppWidget(appWidgetId, mView);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

        for(int widgetId : appWidgetIds){
            deleteTitlePref(context, widgetId);
        }
        super.onDeleted(context, appWidgetIds);
    }

    public static void updateAllAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for (int appWidgetId: appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    /***
     * The widget will be deleted by Id when this function has been been called.
     * Delete all the data of the widget by Id in the SharedPrederence file.
     *
     * @param context      access to this application resources
     * @param appWidgetId  the app that should be deleted by Id
     */
    public static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor edit = prefss.edit();
        edit.remove(PREF_PREFIX_KEY + appWidgetId);
        edit.remove(PREF_PREFIX_POSITION + appWidgetId);
        edit.remove(PREF_PREFIX_WIDGETID);
        edit.commit();
    }
}
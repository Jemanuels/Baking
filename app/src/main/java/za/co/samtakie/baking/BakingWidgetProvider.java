package za.co.samtakie.baking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import za.co.samtakie.baking.activity.RecipeActivity;

/**
 * Implementation of App Widget functionality.
 * Last updated 2018/02/07
 */
@SuppressWarnings("ALL")
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int imgRes,
                                int bakingId, int appWidgetId) {
        RemoteViews rv;
        rv = getBakingGridRemoteViews(context);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        /* Start the intent service update widget action, the service takes care of updating the widget UI */
        BakingService.startActionBakingWidget(context);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager, int imgRes, int bakingId, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, imgRes, bakingId, appWidgetId);
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

    private static RemoteViews getBakingGridRemoteViews(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stack_widget_provider);

        /* Set the GridWidgetService intent to act as the adapter for the GridView */
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.stack_view, intent);

        /* Set the RecipeActivity intent to launch when clicked */
        Intent appIntent;
        appIntent = new Intent(context, RecipeActivity.class);

        // The id of the channel.
        String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle("Baking Notification");



        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, appPendingIntent);



        /* Handle empty baking */
        views.setEmptyView(R.id.stack_view, R.id.empty_view);
        return views;
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        BakingService.startActionBakingWidget(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

}
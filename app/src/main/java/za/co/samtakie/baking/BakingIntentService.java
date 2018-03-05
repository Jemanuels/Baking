package za.co.samtakie.baking;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.os.Build;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 *
 * helper methods.
 */
public class BakingIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_UPDATE_LIST_VIEW
    public static final String UPDATE_BAKING_WIDGET = "za.co.samtakie.baking.action.UPDATE_BAKING_WIDGET";
    private static final String ACTION_UPDATE_LIST_VIEW = "za.co.samtakie.baking.action.update_app_widget_list";
    private final String CHANNEL_ID = "channel_1";

    public BakingIntentService() {
        super("BakingIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startActionBakingWidget(Context context) {
        Intent intent = new Intent(context, BakingIntentService.class);
        intent.setAction(UPDATE_BAKING_WIDGET);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = "Samtakie Baking";
            String description = "Samtakie Recipe";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);


            Notification.Builder mBuilder =
                    new Notification.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Baking Notification");

            notificationManager.notify(1, mBuilder.build());
            startForeground(1,mBuilder.build());
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (UPDATE_BAKING_WIDGET.equals(action)) {
                handleActionBaking();
            } else if(ACTION_UPDATE_LIST_VIEW.equals(action)){
                handleActionUpdateListView();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaking() {
        // You can do any task regarding this process you want to do here, then update the widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        BakingWidgetProvider.updateAllAppWidget(this,appWidgetManager, appWidgetIds);
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpdateListView() {



        // You can do any task regarding this process you want to do here, then update the widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        BakingWidgetProvider.updateAllAppWidget(this,appWidgetManager, appWidgetIds);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetCollectionList);

    }

    public static void startActionUpdateAppWidgets(Context context, boolean forListView) {
        Intent intent = new Intent(context, BakingIntentService.class);
        if (forListView) {
            intent.setAction(ACTION_UPDATE_LIST_VIEW);

        } else {
            intent.setAction(UPDATE_BAKING_WIDGET);
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }
}
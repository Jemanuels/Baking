package za.co.samtakie.baking;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import za.co.samtakie.baking.data.BakingContract;
import za.co.samtakie.baking.data.BakingImages;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * Last updated on the 2018/02/07
 * helper methods.
 */
@SuppressWarnings("ALL")
public class BakingService extends IntentService {

    private static final String ACTION_UPDATE_BAKING_WIDGETS = "za.co.samtakie.baking.action.update_baking_widgets";
    private NotificationManager mNotificationManager;

    @SuppressWarnings("OctalInteger")
    public static final int   NOTIFICATION_ID = 001;

    public BakingService() {
        super("BakingService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Starts this service to perform action startActionBakingWidget with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @param context passed the Context to enable this service to have access to the app.
     */
    public static void startActionBakingWidget(Context context) {
        Intent intent = new Intent(context, BakingService.class);
        intent.setAction(ACTION_UPDATE_BAKING_WIDGETS);

        /* If the android version is O or greater */
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1){
            /* If O or greater start this service */
            context.startForegroundService(intent);
        } else {
            /* if Older version start this service */
            context.startService(intent);
        }

    }


    /**
     * Process the intent with the data that has been received, start the handleActionUpdateWidgetBakingWidgets
     * based if it equals the ACTION_UPDATE_BAKING_WIDGETS.
     *
     * @param intent passed in the intent holding all the data
     *
     *
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_BAKING_WIDGETS.equals(action)) {

                handleActionUpdateWidgetBakingWidgets();

            }
        }
    }

    /**
     * Handle action handleActionUpdateWidgetBakingWidgets in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpdateWidgetBakingWidgets() {
        // TODO: Handle action Foo
        /* Query to get the baking */
        Uri bakingUri = BakingContract.BakingEntry.buildRecipeAllUri();
        Cursor cursor = getContentResolver().query(
                bakingUri,
                null,
                null,
                null,
                null
        );
        /* Extract the baking details */
        int imgRes = R.drawable.brownies; /* Default image in case our baking platform is empty */
        int bakingId = BakingContract.INVALID_BAKING_ID;

        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();

            int bakingindexId = cursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_ID);
            int bakingName = cursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_NAME);

            bakingId = cursor.getInt(bakingindexId);
            cursor.close();

            imgRes = BakingImages.getBakingImages().get(bakingId);
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));

        /* Trigger data update to handle the GridView widgets and force a data refresh */
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);



        /* Now update all widgets */
        BakingWidgetProvider.updateBakingWidgets(this, appWidgetManager, imgRes, bakingId, appWidgetIds);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String id = "my01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The user-visible name of the channel.
            CharSequence name = getString(R.string.app_name);
// The user-visible description of the channel.
            String description = "Faya";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
// Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            //mChannel.enableVibration(true);
            //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);

            Notification.Builder builder = new Notification.Builder(this, id)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Baking")
                    .setSmallIcon(R.drawable.notification)
                    .setAutoCancel(true);

            Notification notification = builder.build();
            mNotificationManager.notify(NOTIFICATION_ID, builder.build());
            startForeground(NOTIFICATION_ID, notification);


        } else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Baking")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notification = builder.build();

            startForeground(1, notification);
        }
        return START_NOT_STICKY;
    }
}
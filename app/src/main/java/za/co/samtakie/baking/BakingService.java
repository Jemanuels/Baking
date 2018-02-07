package za.co.samtakie.baking;

import android.app.IntentService;
import android.app.Notification;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import za.co.samtakie.baking.data.BakingContentProvider;
import za.co.samtakie.baking.data.BakingContract;
import za.co.samtakie.baking.data.BakingImages;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * Last updated on the 2018/02/07
 * helper methods.
 */
public class BakingService extends IntentService {

    private static final String ACTION_UPDATE_BAKING_WIDGETS = "za.co.samtakie.baking.action.update_baking_widgets";

    public BakingService() {
        super("BakingService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1,new Notification());
    }

    /**
     * Starts this service to perform action startActionBakingWidget with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @param context passed the Context to enable this service to have acces to the app.
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
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);

        /* Now update all widgets */
        BakingWidgetProvider.updateBakingWidgets(this, appWidgetManager, imgRes, bakingId, appWidgetIds);
    }
}
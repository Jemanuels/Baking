package za.co.samtakie.baking.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import za.co.samtakie.baking.data.BakingContract;

/**
 * Created by jemanuels on 2018/01/22.
 * Last updated on 2018/02/02
 */

@SuppressWarnings("ALL")
public class BakingSyncUtils {

    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context, final Uri uri) {

        /*
         * Only perform initialization once per app lifetime. If initialization has already been
         * performed, we have nothing to do in this method.
         */
        if (sInitialized) return;

        sInitialized = true;



        /*
         * We need to check to see if our ContentProvider has data to display in our forecast
         * list. However, performing a query on the main thread is a bad idea as this may
         * cause our UI to lag. Therefore, we create a thread in which we will run the query
         * to check the contents of our ContentProvider.
         */
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                 * Since this query is going to be used only as a check to see if we have any
                 * data (rather than to display data), we just need to PROJECT the ID of each
                 * row. In our queries where we display data, we need to PROJECT more columns
                 * to determine what recipe details need to be displayed.
                 */
                String[] projectionColumns = {BakingContract.BakingEntry._ID};


                /* Here, we perform the query to check to see if we have any recipe data */
                Cursor cursor = context.getContentResolver().query(
                        uri,
                        projectionColumns,
                        null,
                        null,
                        null);
                /*
                 * A Cursor object can be null for various different reasons. A few are
                 * listed below.
                 *
                 *   1) Invalid URI
                 *   2) A certain ContentProvider's query method returns null
                 *   3) A RemoteException was thrown.
                 *
                 * Bottom line, it is generally a good idea to check if a Cursor returned
                 * from a ContentResolver is null.
                 *
                 * If the Cursor was null OR if it was empty, we need to sync immediately to
                 * be able to display data to the user.
                 */
                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                /* Make sure to close the Cursor to avoid memory leaks! */
                assert cursor != null;
                cursor.close();
            }
        });

        /* Finally, once the thread is prepared, fire it off to perform our checks. */
        checkForEmpty.start();
    }


    public static void startImmediateSync(@NonNull final Context context) {
        //start the BakingSyncIntentService
        Intent intentToSyncImmediately = new Intent(context, BakingSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
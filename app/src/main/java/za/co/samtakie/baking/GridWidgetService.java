package za.co.samtakie.baking;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import za.co.samtakie.baking.data.BakingContract;
import za.co.samtakie.baking.data.BakingImages;

/**
 * Created by jemanuels on 2018/02/01.
 * Last updated 2018/02/02
 */

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext;
        Cursor mCursor;

        public GridRemoteViewsFactory(Context applicationContext) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            /* Get all baking ifo */
            Uri bakingUri = BakingContract.BakingEntry.buildRecipeAllUri();
            if(mCursor != null){
                mCursor.close();
            }
            mCursor = mContext.getContentResolver().query(
                    bakingUri,
                    null,
                    null,
                    null,
                    null
            );

        }

        @Override
        public void onDestroy() {
            mCursor.close();
        }

        @Override
        public int getCount() {
            if (mCursor == null) {

                return 0;
            } else{
                return mCursor.getCount();
            }
        }

        /**
         * This method acts like the onBindViewHolder method in an Adapter
         *
         * @param position The current position of the item in the GridView to be displayed
         * @return The RemoteViews object to display for the provided position
         */
        @Override
        public RemoteViews getViewAt(int position) {
            if (mCursor == null || mCursor.getCount() == 0) {
                return null;
            }

            mCursor.moveToPosition(position);
            int bakingindexId = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_ID);
            int bakingName = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_NAME);

            int bakingId = mCursor.getInt(bakingindexId);
            String recipeName = mCursor.getString(bakingName);
            Log.d(GridWidgetService.class.getSimpleName(), "Baking ID " + bakingId);
            Log.d(GridWidgetService.class.getSimpleName(), "Total amount of data " + mCursor.getCount());

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_widget_provider);

            /* Update the image */
            int imgRes = BakingImages.getBakingImages().get(position);
            views.setImageViewResource(R.id.widget_baking_image, imgRes);
            views.setTextViewText(R.id.widget_baking_text, recipeName);

            Bundle extras = new Bundle();
            extras.putInt("position", position);
            extras.putString("recipeName", recipeName);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.widget_baking_image, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1; /* Treat all items in the GridView the same */
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

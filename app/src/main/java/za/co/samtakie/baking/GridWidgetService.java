package za.co.samtakie.baking;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import za.co.samtakie.baking.activity.IngredientsActivity;
import za.co.samtakie.baking.data.WidgetDataModel;

/**
 * Created by jemanuels on 2018/02/01.
 * Last updated 2018/02/02
 */

@SuppressWarnings("ALL")
public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(), WidgetDataModel.getIngredientData(getApplicationContext(),intent.getExtras().getInt(BakingWidgetProvider.CONFIGURE_START)), intent);
    }


}

    @SuppressWarnings("ALL")
    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext;
        Cursor mCursor;
        int positionId;

        public GridRemoteViewsFactory(Context applicationContext, Cursor cursor, Intent intent) {

            this.mContext = applicationContext;
            this.positionId = intent.getExtras().getInt(BakingWidgetProvider.CONFIGURE_START);
            this.mCursor = cursor;
        }

        @Override
        public void onCreate() {

        }



        @Override
        public void onDataSetChanged() {

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

            String ingredient = mCursor.getString(IngredientsActivity.INDEX_COLUMN_INGREDIENTS_INGREDIENT);
            String measure = mCursor.getString(IngredientsActivity.INDEX_COLUMN_INGREDIENTS_MEASURE);
            String quantity = mCursor.getString(IngredientsActivity.INDEX_COLUMN_INGREDIENTS_QUANTITY);
            String ingredients = ingredient + " - " + measure + " - " + quantity;

            RemoteViews views = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
            views.setTextViewText(android.R.id.text1, ingredients);
            views.setTextColor(android.R.id.text1, Color.BLACK);

            final Intent fillInIntent = new Intent();
            fillInIntent.setAction(BakingWidgetProvider.ACTION_TOAST);
            final Bundle bundle = new Bundle();
            bundle.putString(BakingWidgetProvider.EXTRA_STRING, ingredients);
            fillInIntent.putExtras(bundle);
            views.setOnClickFillInIntent(android.R.id.text1, fillInIntent);


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
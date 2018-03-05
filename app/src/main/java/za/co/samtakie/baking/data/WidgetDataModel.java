package za.co.samtakie.baking.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by jemanuels on 2018/02/27.
 * An ingredient model to get the data from the database via a getContentResolver and return it in a
 * cursor.
 */

public class WidgetDataModel {

    static Cursor mCursor;

    public static Cursor getIngredientData(Context context, int position){
        String mSelectionClause = BakingContract.BakingEntry.COLUMN_INGREDIENTS_RECIPEID + " = ? ";
        String[] mSelectionArgs = {""};
        mSelectionArgs[0] = String.valueOf(position);

            /* Get all baking info */
        Uri bakingUri = BakingContract.BakingEntry.buildIngredientsUri();
        if(mCursor != null){
            mCursor.close();
        }
        mCursor = context.getContentResolver().query(
                bakingUri,
                null,
                mSelectionClause,
                mSelectionArgs,
                null
        );

        return mCursor;
    }
}
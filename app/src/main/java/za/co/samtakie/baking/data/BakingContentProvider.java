package za.co.samtakie.baking.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by jemanuels on 2018/01/22.
 * First updated: 2018/02/02, add comments and comment out Logs
 * Register the BakingContentProvider in the manifest file
 * Set name, authorities, and exported attributes
 * exported = false limits access to this ContentProvider to only this app
 */

public class BakingContentProvider extends ContentProvider {

    /* It's convention to use 100, 200, 300 etc for directories
     * and related int (101, 201, 301, ..) for items in that directories */
    public static final int BAKING = 100;
    public static final int INGREDIENTS = 200;
    //public static final int INGREDIENTS_ID = 201; // This variable is for future use
    public static final int STEPS = 300;
    public static final int STEPS_ID = 301;
    public static final int BAKING_ID = 101;

    /* Declare a static variable for the Uri match that we contract */
    private final static UriMatcher sUriMatcher = buildUriMatcher();

    /* Member variable for a BakingDbHelper */
    private BakingDbHelper mBakingDbHelper;

    /**
     * Define a static UriMatcher method that associates URI's with their int match
     * @return the UriMatcher either a single item or a directory
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /* Add matches with addUri(String authority, String path, int code)
        * directory for baking */
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_BAKING, BAKING);

        /* directory for ingredients */
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_INGREDIENTS, INGREDIENTS);

        /* directory for steps */
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_STEPS, STEPS);

        /* Single item for the baking recipe /# = represent a numerical id */
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_BAKING + "/#", BAKING_ID);

        /* Single item for the baking recipe /# = represent a numerical id */
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_STEPS + "/#", STEPS_ID);

        /* Return the Uri link */
        return uriMatcher;
    }

    /**
     *  Instantiate the mBakingDbHelper
     * @return true to signify success performing setup
     */
    @Override
    public boolean onCreate() {
         /* Instantiate our mBakingDbHelper */
         mBakingDbHelper = new BakingDbHelper(getContext());

        /* return true from onCreate to signify success performing setup */
        return true;
    }

    /**
     * Handles requests to insert a set of new rows.
     * @param uri the content:// URI of the insertion request
     * @param values An array of sets of column_name/value pairs to add to the database
     *               this must not be{@code null}
     * @return The number of values that were inserted.
     */
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        /* Create a reference to SQLiteDatabase to open a database that will be
         * used for reading and writing. */
        final SQLiteDatabase db = mBakingDbHelper.getWritableDatabase();

        /* Here's is the switch statement that, given a URI, will determine what kind of insertion is
        * being made and insert the database accordingly. */
        switch(sUriMatcher.match(uri)){

            /* Only perform our implementation of bulkInsert if the URI matches the Baking code */
            case BAKING:
                db.beginTransaction();
                int rowInserted = 0;
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(BakingContract.BakingEntry.TABLE_NAME, null, value);
                        if(_id != -1){
                            rowInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if(rowInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowInserted;

            case INGREDIENTS:
                db.beginTransaction();
                int rowInsertedIn = 0;
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(BakingContract.BakingEntry.TABLE_INGREDIENTS_NAME, null, value);
                        if(_id != -1){
                            rowInsertedIn++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if(rowInsertedIn > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowInsertedIn;

            case STEPS:
                db.beginTransaction();
                int rowInsertedSteps = 0;
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(BakingContract.BakingEntry.TABLE_STEPS_NAME, null, value);
                        if(_id != -1){
                            rowInsertedSteps++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if(rowInsertedSteps > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowInsertedSteps;

            default:
                return super.bulkInsert(uri, values);
        }

    }

    /**
     * Handles query requests from clients. We will use this method in Baking App to query for all
     * of our recipe, ingredients and steps data.
     * @param uri               The URI query
     * @param projection        The list of columns to put into the Cursor. If null, all column are
     *                          included
     * @param selection         A selection criteria to apply when filtering rows. If null, then all
     *                          rows are included
     * @param selectionArgs     You may include ?s in selection, which will be replaced by
     *                          the values from selectionArgs, in order that they appear in the
     *                          selection
     * @param sortOrder         How the rows in the cursor should be sorted
     * @return                  A Cursor containing the results of the query. In our implementation
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        /* Initialize the cursor variable */
        Cursor cursor;

        /* Here's is the switch statement that, given a URI, will determine what kind of request is
        * being made and query the database accordingly. */
        switch(sUriMatcher.match(uri)){
            /* When sUriMatcher match method is called with a URI that looks something like this
            *
            *   content://za.co.samtakie.baking/baking/1
            *
            *   sUriMatcher's match method will return the code that indicates to us that we need
            *   to return the baking data
            *   In this case, we want to return a cursor that contains one row of baking data
            * */
            case BAKING_ID:{
                String[] selectionArguments = new String[]{uri.getLastPathSegment()};

                cursor = mBakingDbHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        BakingContract.BakingEntry.TABLE_NAME,
                        /*
                         * A projection designates the columns we want returned in our Cursor.
                         * Passing null will return all columns of data within the Cursor.
                         * However, if you don't need all the data from the table, it's best
                         * practice to limit the columns returned in the Cursor with a projection.
                         */
                        projection,
                        /*
                         * The URI that matches RECIPE_ID
                         */
                        BakingContract.BakingEntry.COLUMN_RECIPE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case STEPS_ID:{
                String[] selectionArguments = new String[]{uri.getLastPathSegment()};

                cursor = mBakingDbHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        BakingContract.BakingEntry.TABLE_STEPS_NAME,
                        /*
                         * A projection designates the columns we want returned in our Cursor.
                         * Passing null will return all columns of data within the Cursor.
                         * However, if you don't need all the data from the table, it's best
                         * practice to limit the columns returned in the Cursor with a projection.
                         */
                        projection,
                        /*
                         * The URI that matches STEPS_ID
                         */
                        BakingContract.BakingEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            /*
             * When sUriMatcher's match method is called with a URI that looks EXACTLY like this
             *
             *      content://za.co.samtakie.baking/baking/
             *
             * sUriMatcher's match method will return the code that indicates to us that we need
             * to return all of the recipe in our baking table.
             *
             * In this case, we want to return a cursor that contains every row of recipe data
             * in our baking table.
             */
            case BAKING:{
                cursor = mBakingDbHelper.getReadableDatabase().query(
                        BakingContract.BakingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case INGREDIENTS:{
                cursor = mBakingDbHelper.getReadableDatabase().query(
                        BakingContract.BakingEntry.TABLE_INGREDIENTS_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case STEPS:{
                cursor = mBakingDbHelper.getReadableDatabase().query(
                        BakingContract.BakingEntry.TABLE_STEPS_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        /* Get access to the baking database (to write new data to)*/
        final SQLiteDatabase db = mBakingDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the baking directory
        int match = sUriMatcher.match(uri);

        // Insert new values into the database
        // Set the value for the returnedUri and write the default case for the unknown URI's
        Uri returnUri;

        switch (match){
            case BAKING:
                // Inserting values into movielist table
                long id = db.insert(BakingContract.BakingEntry.TABLE_NAME, null, values);
                if(id > 0){
                    // success
                    returnUri = ContentUris.withAppendedId(BakingContract.BakingEntry.CONTENT_URI_RECIPE, id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;

            case INGREDIENTS:
                // Inserting values into ingredients table

                try{
                    db.insertOrThrow(BakingContract.BakingEntry.TABLE_INGREDIENTS_NAME, null, values);
                    /*String idRecipe = values.getAsString("recipeID");
                    Log.d(TAG, idRecipe);*/
                    returnUri = null;

                }catch (android.database.sqlite.SQLiteConstraintException e) {
                    /*String idRecipe = values.getAsString("recipeID");
                    *//*Log.d(TAG, idRecipe);
                    Log.e(TAG, "SQLiteConstraintException:" + e.getMessage());*/
                    returnUri =  null;
                }
                catch (android.database.sqlite.SQLiteException e) {
                    /*Log.e(TAG, "SQLiteException:" + e.getMessage());*/
                    returnUri = null;
                }
                catch (Exception e) {
                    /*Log.e(TAG, "Exception:" + e.getMessage());*/
                    returnUri = null;
                }

                break;


            case STEPS:
                // Inserting values into STEPS table

                try{
                    db.insertOrThrow(BakingContract.BakingEntry.TABLE_STEPS_NAME, null, values);
                    /*String idRecipe = values.getAsString("recipeID");
                    Log.d(TAG, idRecipe);*/
                    returnUri = null;

                }catch (android.database.sqlite.SQLiteConstraintException e) {
                    /*String idRecipe = values.getAsString("recipeID");
                    Log.d(TAG, idRecipe);
                    Log.e(TAG, "SQLiteConstraintException:" + e.getMessage());*/
                    returnUri =  null;
                }
                catch (android.database.sqlite.SQLiteException e) {
                    /*Log.e(TAG, "SQLiteException:" + e.getMessage());*/
                    returnUri = null;
                }
                catch (Exception e) {
                    /*Log.e(TAG, "Exception:" + e.getMessage());*/
                    returnUri = null;
                }

                break;

            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed
        //getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection){
            selection = "1";
        }

        switch (sUriMatcher.match(uri)) {

            // Only implement the functionality, given the proper URI,
            // to delete ALL rows in the given table (recipe, ingredients and steps)
            case BAKING:
                numRowsDeleted = mBakingDbHelper.getWritableDatabase().delete(
                        BakingContract.BakingEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case INGREDIENTS:
                numRowsDeleted = mBakingDbHelper.getWritableDatabase().delete(
                        BakingContract.BakingEntry.TABLE_INGREDIENTS_NAME,
                        selection,
                        selectionArgs);

                break;

            case STEPS:
                numRowsDeleted = mBakingDbHelper.getWritableDatabase().delete(
                        BakingContract.BakingEntry.TABLE_STEPS_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
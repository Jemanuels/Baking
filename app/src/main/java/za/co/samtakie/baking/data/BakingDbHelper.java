package za.co.samtakie.baking.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jemanuels on 2018/01/22.
 * Last updated on 2018/02/02 unused imports has been removed.
 * This class will make sure we can create the database and tables.
 */

@SuppressWarnings("ALL")
public class BakingDbHelper extends SQLiteOpenHelper {

    /* Set the database name */
    private static final String DATABASE_NAME = "baking.db";

    /* Set the database version number, always update the version number if you change
    * the database scheme */
    private static final int DATABASE_VERSION = 5;

    /* Set table values for the Baking Table */
    private static final String SQL_CREATE_BAKING_TABLE = "CREATE TABLE " +
            BakingContract.BakingEntry.TABLE_NAME + " (" +
            BakingContract.BakingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BakingContract.BakingEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL," +
            BakingContract.BakingEntry.COLUMN_RECIPE_IMAGE + " TEXT NULL," +
            BakingContract.BakingEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL," +
            BakingContract.BakingEntry.COLUMN_RECIPE_SERVINGS + " INTEGER NOT NULL" + ");";

    /* Set table values for the  ingredients table */
    private static final String SQL_CREATE_INGREDIENTS = "CREATE TABLE " +
            BakingContract.BakingEntry.TABLE_INGREDIENTS_NAME + " (" +
            BakingContract.BakingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BakingContract.BakingEntry.COLUMN_INGREDIENTS_INGREDIENT + " TEXT NOT NULL," +
            BakingContract.BakingEntry.COLUMN_INGREDIENTS_MEASURE + " TEXT NOT NULL," +
            BakingContract.BakingEntry.COLUMN_INGREDIENTS_QUANTITY + " REAL NOT NULL," +
            BakingContract.BakingEntry.COLUMN_INGREDIENTS_RECIPEID + " INTEGER NOT NULL" + ");";

    /* Set table values for the steps Table */
    private static final String SQL_CREATE_STEPS = "CREATE TABLE " +
            BakingContract.BakingEntry.TABLE_STEPS_NAME + " (" +
            BakingContract.BakingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BakingContract.BakingEntry.COLUMN_STEP_DESCRIPTION + " TEXT NOT NULL," +
            BakingContract.BakingEntry.COLUMN_STEP_ID + " INTEGER NOT NULL," +
            BakingContract.BakingEntry.COLUMN_STEP_SHORTDESCRIPTION + " TEXT NOT NULL," +
            BakingContract.BakingEntry.COLUMN_STEP_THUMBNAILURL + " TEXT NOT NULL," +
            BakingContract.BakingEntry.COLUMN_STEP_VIDEOURL + " TEXT NOT NULL," +
            BakingContract.BakingEntry.COLUMN_STEP_RECIPEID + " INTEGER NOT NULL" + ");";

    private static final String SQL_DELETE_BAKING = "DROP TABLE IF EXISTS " +
            BakingContract.BakingEntry.TABLE_NAME;

    private static final String SQL_DELETE_INGREDIENTS = "DROP TABLE IF EXISTS " +
            BakingContract.BakingEntry.TABLE_INGREDIENTS_NAME;

    private static final String SQL_DELETE_STEPS= "DROP TABLE IF EXISTS " +
            BakingContract.BakingEntry.TABLE_STEPS_NAME;

    public BakingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_BAKING_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENTS);
        sqLiteDatabase.execSQL(SQL_CREATE_STEPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        /* Update the database by using an if statement for the version number
        * At the moment no update is being carried out. Check web page for a best practice
        * on using onUpgrade: https://thebhwgroup.com/blog/how-android-sqlite-onupgrade */
    }
}
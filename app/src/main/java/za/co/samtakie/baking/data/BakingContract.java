package za.co.samtakie.baking.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jemanuels on 2018/01/21.
 * Last updated on 2018/02/02
 * This contract class defines constants that helps the application work with the content URIs,
 * column names, intent actions, and other features of the Baking content provider.
 */

@SuppressWarnings("ALL")
public class BakingContract {

    /* The authority, which is how the code knows which Content Provider to access */
    public static final String AUTHORITY = "za.co.samtakie.baking";

    /* The base content URI = "content://" + <authority> */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /* Define the possible paths for accessing data in this contract */
    /* This is the path for the baking directory. */
    public static final String PATH_BAKING = "baking";

    /* This is the path for the ingredients directory. */
    public static final String PATH_INGREDIENTS = "ingredients";

    /* This is the path for the steps directory. */
    public static final String PATH_STEPS = "steps";

    /* This is a variable for the Widget app in case there is no valid recipe ID */
    public static final int INVALID_BAKING_ID = -1;

    /**
     * Create a inner class named BakingEntry class that implements the BaseColumns interface
     * Create static final members for the table name and each of the db columns
     * in the @BakingEntry class.
     */
    public static final class BakingEntry implements BaseColumns{

        /* BakingEntry content URI = base content URI + path for the Baking directory */
        public static final Uri CONTENT_URI_RECIPE = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BAKING).build();

        /* BakingEntry content URI = base content URI + path for the ingredients directory*/
        public static final Uri CONTENT_URI_INGREDIENTS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        /* BakingEntry content URI = base content URI + path for the steps directory */
        public static final Uri CONTENT_URI_STEPS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

        /* Create static final members for the table name and each of the db columns */
        /* Baking Table and columns */
        // TABLE_NAME -> baking;
        public static final String TABLE_NAME = "baking";
        // COLUMN_RECIPE_ID -> id
        public static final String COLUMN_RECIPE_ID = "id";
        // COLUMN_RECIPE_IMAGE -> image
        public static final String COLUMN_RECIPE_IMAGE = "image";
        // COLUMN_RECIPE_NAME -> name
        public static final String COLUMN_RECIPE_NAME = "name";
        // COLUMN_RECIPE_SERVINGS -> servings
        public static final String COLUMN_RECIPE_SERVINGS = "servings";

        /* Ingredients Table and columns */
        // TABLE_INGREDIENTS_NAME -> ingredients
        public static final String TABLE_INGREDIENTS_NAME = "ingredients";
        // COLUMN_INGREDIENTS_INGREDIENT -> ingredient
        public static final String COLUMN_INGREDIENTS_INGREDIENT = "ingredient";
        // COLUMN_INGREDIENTS_MEASURE -> measure
        public static final String COLUMN_INGREDIENTS_MEASURE = "measure";
        // COLUMN_INGREDIENTS_QUANTITY -> quantity
        public static final String COLUMN_INGREDIENTS_QUANTITY = "quantity";
        // COLUMN_INGREDIENTS_RECIPEID -> recipeid
        public static final String COLUMN_INGREDIENTS_RECIPEID = "recipeid";

        /* Steps Table and columns */
        // TABLE_NAME -> steps
        public static final String TABLE_STEPS_NAME = "steps";
        // COLUMN_STEP_DESCRIPTION -> description
        public static final String COLUMN_STEP_DESCRIPTION = "description";
        // COLUMN_STEP_ID -> id
        public static final String COLUMN_STEP_ID = "id";
        // COLUMN_STEP_SHORTDESCRIPTION -> shortdescription
        public static final String COLUMN_STEP_SHORTDESCRIPTION = "shortdescription";
        // COLUMN_STEP_THUMBNAILURL -> thumbnailURL
        public static final String COLUMN_STEP_THUMBNAILURL = "thumbnailURL";
        // COLUMN_STEP_VIDEOURL -> videoURL
        public static final String COLUMN_STEP_VIDEOURL = "videoURL";
        // COLUMN_STEP_RECIPEID -> recipeid
        public static final String COLUMN_STEP_RECIPEID = "recipeid";


        /**
         * This method builds an URI that adds the recipe id to the end of the baking content URI path.
         * This is used to query details about a single recipe entry by id. This is what we will
         * use for Ingredients and Steps view query.
         * For future use.
         *
         * @param recipeID This is the id of the recipe in the Database
         * @return Uri to the query details about single recipe entry
         */
        public static Uri buildRecipeItemUri(int recipeID){
            return CONTENT_URI_RECIPE.buildUpon()
                    .appendPath(Integer.toString(recipeID))
                    .build();
        }

        /**
         * This method builds an URI that returns all the recipe in the baking database.
         *
         * @return Uri to query all recipe in the baking database
         */
        public static Uri buildRecipeAllUri(){
            return CONTENT_URI_RECIPE.buildUpon()
                    .build();
        }

        /**
         * This method builds an URI that adds the recipe id to the end of the ingredients content URI path.
         * For future use.
         * @param recipeID This is the id of the recipe in the Database from table ingredients
         * @return Uri to the query details about all ingredients where the recipe id is the same
         */
        public static Uri buildIngredientItemUri(int recipeID){
            return CONTENT_URI_INGREDIENTS.buildUpon()
                    .appendPath(Integer.toString(recipeID))
                    .build();
        }

        /**
         * This method builds an URI that returns all the recipe in the baking database.
         *
         * @return Uri to query all Ingredients in the baking database
         */
        public static Uri buildIngredientsUri(){
            return CONTENT_URI_INGREDIENTS.buildUpon()
                    .build();
        }

        /**
         * This method builds an URI that adds the recipe id to the end of the steps content URI path.
         *
         * @param recipeID This is the id of the recipe in the Database from table steps
         * @return Uri to the query details about all steps where the recipe id is the same
         */
        public static Uri buildStepsItemUri(int recipeID){
            return CONTENT_URI_STEPS.buildUpon()
                    .appendPath(Integer.toString(recipeID))
                    .build();
        }

        /**
         * This method builds an URI that returns all the recipe in the baking database.
         *
         * @return Uri to query all Steps in the baking database
         */
        public static Uri buildStepsUri(){
            return CONTENT_URI_STEPS.buildUpon()
                    .build();
        }
    }
}
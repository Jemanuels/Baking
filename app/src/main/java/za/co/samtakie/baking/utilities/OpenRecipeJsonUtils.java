package za.co.samtakie.baking.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import za.co.samtakie.baking.data.BakingContract;

/**
 * Created by jemanuels on 2017/12/09.
 * Last updated on 2017/02/19
 */

@SuppressWarnings("ALL")
public class OpenRecipeJsonUtils {

    public static final String RECIPE_NAME = "name";
    public static final String RECIPE_ID = "id";
    public static final String RECIPE_IMAGE = "image";
    public static final String RECIPE_INGREDIENTS = "ingredients";
    public static final String RECIPE_STEPS = "steps";
    public static final String RECIPE_INGREDIENTS_QUANTITY = "quantity";
    public static final String RECIPE_INGREDIENTS_MEASURE = "measure";
    public static final String RECIPE_INGREDIENTS_INGREDIENT = "ingredient";
    public static final String RECIPE_STEPS_DESCRIPTION = "description";
    public static final String RECIPE_STEPS_ID = "id";
    public static final String RECIPE_STEPS_SHORTDESCRIPTION = "shortDescription";
    public static final String RECIPE_STEPS_THUMBNAILURL = "thumbnailURL";
    public static final String RECIPE_STEPS_VIDEOURL = "videoURL";
    public static final String RECIPE_SERVINGS = "servings";


    public static ContentValues[] getSimpleRecipeStringFromJson(Context context, String jsonResponse) throws JSONException{

        JSONArray recipeArray = new JSONArray(jsonResponse);

        ContentValues[] recipeContentValues = new ContentValues[recipeArray.length()];

        for (int i = 0; i < recipeArray.length(); i++){
            ContentValues recipeValues = new ContentValues();

            String recipeName;
            Integer recipeID;
            String recipeImage;
            Integer recipeServings;

            JSONObject jsonObject = recipeArray.getJSONObject(i);

            recipeName = jsonObject.getString(RECIPE_NAME);
            recipeID = jsonObject.getInt(RECIPE_ID);
            recipeImage = jsonObject.getString(RECIPE_IMAGE);
            recipeServings = jsonObject.getInt(RECIPE_SERVINGS);


            recipeValues.put(BakingContract.BakingEntry.COLUMN_RECIPE_NAME, recipeName);
            recipeValues.put(BakingContract.BakingEntry.COLUMN_RECIPE_ID, recipeID);
            recipeValues.put(BakingContract.BakingEntry.COLUMN_RECIPE_IMAGE, recipeImage);
            recipeValues.put(BakingContract.BakingEntry.COLUMN_RECIPE_SERVINGS, recipeServings);

            JSONArray jsonArrayIngredients = jsonObject.getJSONArray(RECIPE_INGREDIENTS);

            recipeContentValues[i] = recipeValues;
        }

        return recipeContentValues;
    }

    public static ArrayList<ContentValues> getSimpleIngredientsStringFromJson(Context context, String jsonResponse) throws JSONException {

        JSONArray recipeArray = new JSONArray(jsonResponse);

        ContentValues[] iContentValues = new ContentValues[0];

        for (int i = 0; i < recipeArray.length(); i++) {

        }


        ArrayList<ContentValues> ingredientsContentValues = new ArrayList<>();
        for (int i = 0; i < recipeArray.length(); i++) {

            Integer recipeID;

            JSONObject jsonObject = recipeArray.getJSONObject(i);

            recipeID = jsonObject.getInt(RECIPE_ID);

            JSONArray jsonArrayIngredients = jsonObject.getJSONArray(RECIPE_INGREDIENTS);


            for (int k = 0; k < jsonArrayIngredients.length(); k++) {

                ContentValues ingredientsValues = new ContentValues();

                JSONObject jsonObjectIngredients = jsonArrayIngredients.getJSONObject(k);

                String ingredient = jsonObjectIngredients.getString(RECIPE_INGREDIENTS_INGREDIENT);
                String measure = jsonObjectIngredients.getString(RECIPE_INGREDIENTS_MEASURE);
                Double quantity = jsonObjectIngredients.getDouble(RECIPE_INGREDIENTS_QUANTITY);
                ingredientsValues.put(BakingContract.BakingEntry.COLUMN_INGREDIENTS_INGREDIENT, ingredient);
                ingredientsValues.put(BakingContract.BakingEntry.COLUMN_INGREDIENTS_MEASURE, measure);
                ingredientsValues.put(BakingContract.BakingEntry.COLUMN_INGREDIENTS_QUANTITY, quantity);
                ingredientsValues.put(BakingContract.BakingEntry.COLUMN_INGREDIENTS_RECIPEID, recipeID);

                ingredientsContentValues.add(ingredientsValues);

            }


        }

        Log.d("Size of Content ", ingredientsContentValues.size() + "");
        return ingredientsContentValues;

    }



    public static ArrayList<ContentValues> getSimpleStepsStringFromJson(Context context, String jsonResponse) throws JSONException {

        JSONArray recipeArray = new JSONArray(jsonResponse);

        ArrayList<ContentValues> stepsContentValues = new ArrayList<>();
        for (int i = 0; i < recipeArray.length(); i++) {

            Integer recipeID;

            JSONObject jsonObject = recipeArray.getJSONObject(i);

            recipeID = jsonObject.getInt(RECIPE_ID);

            JSONArray jsonArraySteps = jsonObject.getJSONArray(RECIPE_STEPS);

            for (int k = 0; k < jsonArraySteps.length(); k++) {

                ContentValues stepsValues = new ContentValues();

                JSONObject jsonObjectIngredients = jsonArraySteps.getJSONObject(k);

                String description = jsonObjectIngredients.getString(RECIPE_STEPS_DESCRIPTION);
                int stepID = jsonObjectIngredients.getInt(RECIPE_STEPS_ID);
                String shortDescription = jsonObjectIngredients.getString(RECIPE_STEPS_SHORTDESCRIPTION);
                String thumbnailUrl = jsonObjectIngredients.getString(RECIPE_STEPS_THUMBNAILURL);
                String videoUrl = jsonObjectIngredients.getString(RECIPE_STEPS_VIDEOURL);

                stepsValues.put(BakingContract.BakingEntry.COLUMN_STEP_DESCRIPTION, description);
                stepsValues.put(BakingContract.BakingEntry.COLUMN_STEP_ID, stepID);
                stepsValues.put(BakingContract.BakingEntry.COLUMN_STEP_SHORTDESCRIPTION, shortDescription);
                stepsValues.put(BakingContract.BakingEntry.COLUMN_STEP_THUMBNAILURL, thumbnailUrl);
                stepsValues.put(BakingContract.BakingEntry.COLUMN_STEP_VIDEOURL, videoUrl);
                stepsValues.put(BakingContract.BakingEntry.COLUMN_STEP_RECIPEID, recipeID);

                stepsContentValues.add(stepsValues);

            }


        }


        Log.d("Size of Content ", stepsContentValues.size() + "");
        return stepsContentValues;

    }
}
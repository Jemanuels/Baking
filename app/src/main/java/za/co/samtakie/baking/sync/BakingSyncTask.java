package za.co.samtakie.baking.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;

import za.co.samtakie.baking.data.BakingContract;
import za.co.samtakie.baking.utilities.NetworkUtils;
import za.co.samtakie.baking.utilities.OpenRecipeJsonUtils;

/**
 * Created by jemanuels on 2018/01/22.
 * Last updated on 2018/02/02
 */

@SuppressWarnings("ALL")
public class BakingSyncTask {


    /**
     * Performs the network request for updated movie, parses the JSON from that request, and
     * inserts the new movie information into our ContentProvider.
     *
     * @param context Used to access utility methods and the ContentResolver
     */
    synchronized public static void syncBaking(Context context) {
        try{
            /*
             * The getUrl method will return the URL that we need to get the movie data JSON for the
             * movie. It will decide whether to create a URL based off the top rated or popular
             */
            URL bakingRequestUrl = NetworkUtils.buildUrl();

            String jsonBakingResponse = NetworkUtils.getResponseFromHttpUrl(bakingRequestUrl);

            ContentValues[] bakingValues = OpenRecipeJsonUtils.getSimpleRecipeStringFromJson(context, jsonBakingResponse);

            ArrayList<ContentValues> ingredientsValues = OpenRecipeJsonUtils.getSimpleIngredientsStringFromJson(context, jsonBakingResponse);
            ContentValues[] ingredientsValue = ingredientsValues.toArray(new ContentValues[ingredientsValues.size()]);

            ArrayList<ContentValues> stepsValues = OpenRecipeJsonUtils.getSimpleStepsStringFromJson(context, jsonBakingResponse);
            ContentValues[] stepsValue = stepsValues.toArray(new ContentValues[stepsValues.size()]);


            if(bakingValues != null && bakingValues.length != 0){
                ContentResolver bakingContentResolver = context.getContentResolver();

                bakingContentResolver.delete(
                        BakingContract.BakingEntry.CONTENT_URI_RECIPE,
                        null,
                        null);

                // insert our new data
                bakingContentResolver.bulkInsert(
                        BakingContract.BakingEntry.CONTENT_URI_RECIPE,
                        bakingValues);



            }

            if(ingredientsValues != null && ingredientsValues.size() != 0){
                ContentResolver ingredientsContentResolver = context.getContentResolver();

                Log.d("Size of the content ", ingredientsValues.size() + "");

                for (int i = 0; i < ingredientsValues.size(); i++) {
                    Log.d("Inner Values ", ingredientsValues.get(i).toString());
                }

                ingredientsContentResolver.delete(
                        BakingContract.BakingEntry.CONTENT_URI_INGREDIENTS,
                        null,
                        null);

                // insert our new data
                ingredientsContentResolver.bulkInsert(
                        BakingContract.BakingEntry.CONTENT_URI_INGREDIENTS,
                        ingredientsValue);



            }

            if(stepsValues != null && stepsValues.size() != 0){
                ContentResolver stepsContentResolver = context.getContentResolver();

                Log.d("Size of the content ", stepsValues.size() + "");

                for (int i = 0; i < stepsValues.size(); i++) {
                    Log.d("Inner Values ", stepsValues.get(i).toString());
                }

                stepsContentResolver.delete(
                        BakingContract.BakingEntry.CONTENT_URI_STEPS,
                        null,
                        null);

                // insert our new data
                stepsContentResolver.bulkInsert(
                        BakingContract.BakingEntry.CONTENT_URI_STEPS,
                        stepsValue);

            }

        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
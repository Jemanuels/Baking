package za.co.samtakie.baking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import za.co.samtakie.baking.data.BakingContract;
import za.co.samtakie.baking.sync.BakingSyncUtils;

/**
 * Created by Jurgen Emanuels
 * Last updated on the 2018/02/07
 */

public class MainActivity extends AppCompatActivity implements ItemFragment.RecipeAdapterOnClickHandler{

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    /*public static final int INDEX_ID = 0;// For future use*/
    public static final int INDEX_COLUMN_RECIPE_ID = 1;
    /*public static final int INDEX_COLUMN_RECIPE_IMAGE = 2;// For future use*/
    public static final int INDEX_COLUMN_RECIPE_NAME = 3;
    /*public static final int INDEX_COLUMN_RECIPE_SERVINGS = 4; // For future use*/

    /*// Get the class name and assign it to the constant TAG for Logging purposes
    private String TAG = MainActivity.class.getSimpleName();*/ //For testing purposes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Check if data has been loaded in the database, if not load the Json data in the database. */
        BakingSyncUtils.initialize(this, BakingContract.BakingEntry.CONTENT_URI_RECIPE);

    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param recipePosition the position of the item that was clicked
     * @param view           the view hosting the item
     */

    @Override
    public void recipeAdapterOnClickHandler(int recipePosition, View view) {

        /* Create a Rri object to hold the link of the item that has been clicked. */
        Uri uriRecipeClicked;

        /* This Context will allow access to the application-specific resources and classes,
         * as well as up-calls for application-level operations such as launching activities,
         * broadcasting and receiving intents */
        Context context = this;

        /* The Class file that that the Intent will load. */
        Class destinationClass = RecipeActivity.class;

        /* This intent will start the RecipeActivity */
        Intent intentToStartRecipeActivity = new Intent(context, destinationClass);

        /* Set the recipe item link to the uriRecipeClicked variable. */
        uriRecipeClicked = BakingContract.BakingEntry.buildStepsUri();

        /* Set the Uri via the setData to the intent as this method accept a Uri parameter. */
        intentToStartRecipeActivity.setData(uriRecipeClicked);

        /* Get the name of the recipe and set it to the recipeName, this will be used,
         * in the RecipeActivity to show the recipe name. */
        String recipeName = ((TextView) view.findViewById(R.id.recipeName)).getText().toString();

        /* Put the recipePosition and the recipename name in the intent */
        intentToStartRecipeActivity.putExtra(getResources().getString(R.string.position), recipePosition);
        intentToStartRecipeActivity.putExtra(getResources().getString(R.string.recipeName), recipeName);

        /* Finally start the activity */
        startActivity(intentToStartRecipeActivity);

        /*Log.d("Button Ingredients " , recipePosition + "");*/
        //Toast.makeText(context, "Position clicked = " + recipePosition, Toast.LENGTH_SHORT).show();
    }
}
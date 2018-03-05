package za.co.samtakie.baking.activity;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import za.co.samtakie.baking.IngredientsFragment;
import za.co.samtakie.baking.R;

/**
 * Created by Jurgen Emanuels
 * Last updated on the 2018/03/03
 */
@SuppressWarnings("ALL")
public class IngredientsActivity extends AppCompatActivity {

    /* Static variable linked to the column index for passing into the Cursor to get the correct
    * column data */
    public static final int INDEX_COLUMN_INGREDIENTS_INGREDIENT = 1;
    public static final int INDEX_COLUMN_INGREDIENTS_MEASURE = 2;
    public static final int INDEX_COLUMN_INGREDIENTS_QUANTITY = 3;

    /* Variable to hold the data for passing through to the Fragment*/
    private int position;
    private String sPosition;
    private Uri ingredientUri;
    private String recipeName;

    /***
     * Load the Fragment with the given data
     *
     * @param savedInstanceState hold data that has been saved in the onSavedInstanceState method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        /* Get the Bundle data send from the RecipeActivity */
        Bundle extras = getIntent().getExtras();

        if(savedInstanceState == null){

            IngredientsFragment ingredientsFragment = new IngredientsFragment();

            /* Get the correct position, recipeName and the uriRecipeClicked */
            ingredientUri = getIntent().getData();
            position = extras.getInt(getResources().getString(R.string.position));
            recipeName = extras.getString(getResources().getString(R.string.recipeName));
            sPosition = String.valueOf(position);

            /* This will set the title of the activity to the recipe name */
            setTitle(recipeName);

            /* Set the data to the following methods: setStepUri, setPosition, setRecipeName, setSposition.
             * This will be used in the Fragment to process and request a query */
            ingredientsFragment.setIngredientUri(ingredientUri);
            ingredientsFragment.setPosition(position);
            ingredientsFragment.setsPosition(sPosition);

            /* Create a Fragment Manager that will hold the ingredients Fragment*/
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_ingredients, ingredientsFragment)
                    .commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /* Respond to the action bar's Up/Home button */
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
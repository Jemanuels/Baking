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
 * Last updated on the 2018/02/07
 */
@SuppressWarnings("ALL")
public class IngredientsActivity extends AppCompatActivity {

    /*public static final int INDEX_ID = 0; // For future use*/
    public static final int INDEX_COLUMN_INGREDIENTS_INGREDIENT = 1;
    public static final int INDEX_COLUMN_INGREDIENTS_MEASURE = 2;
    public static final int INDEX_COLUMN_INGREDIENTS_QUANTITY = 3;
    /*public static final int INDEX_COLUMN_INGREDIENTS_RECIPEID = 4; // For future use*/

    private int position;
    private String sPosition;
    private Uri ingredientUri;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        Bundle extras = getIntent().getExtras();

        if(savedInstanceState == null){

            IngredientsFragment ingredientsFragment = new IngredientsFragment();

            /* Get the correct position, recipeName and the uriRecipeClicked */
            ingredientUri = getIntent().getData();
            position = extras.getInt(getResources().getString(R.string.position));
            recipeName = extras.getString(getResources().getString(R.string.recipeName));
            sPosition = String.valueOf(position);

            setTitle(recipeName);

            /* Set the data to the following methods: setStepUri, setPosition, setRecipeName, setSposition. */
            ingredientsFragment.setIngredientUri(ingredientUri);
            ingredientsFragment.setPosition(position);
            ingredientsFragment.setsPosition(sPosition);

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
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                /*Log.d("Up button", "The up button is working");*/
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
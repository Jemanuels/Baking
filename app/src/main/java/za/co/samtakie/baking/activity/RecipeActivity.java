package za.co.samtakie.baking.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import za.co.samtakie.baking.IngredientsFragment;
import za.co.samtakie.baking.R;
import za.co.samtakie.baking.RecipeFragment;
import za.co.samtakie.baking.StepsFragment;
import za.co.samtakie.baking.data.*;

/**
 * Created by Jurgen Emanuels
 * Last updated 2018/02/07
 */
@SuppressWarnings("ALL")
public class RecipeActivity extends AppCompatActivity implements RecipeFragment.RecipeItemOnClickHandler {

    public static final String[] STEPS_PROJECTION  = {

                    BakingContract.BakingEntry._ID,
                    BakingContract.BakingEntry.COLUMN_STEP_DESCRIPTION,
                    BakingContract.BakingEntry.COLUMN_STEP_ID,
                    BakingContract.BakingEntry.COLUMN_STEP_SHORTDESCRIPTION,
                    BakingContract.BakingEntry.COLUMN_STEP_THUMBNAILURL,
                    BakingContract.BakingEntry.COLUMN_STEP_VIDEOURL,
                    BakingContract.BakingEntry.COLUMN_STEP_RECIPEID
    };

    public static final int INDEX_ID = 0;
    public static final int INDEX_COLUMN_STEP_SHORTDESCRIPTION = 3;
    public static final int INDEX_COLUMN_STEP_THUMBNAILURL = 4;

    //private TextView recipeName;
    // Get the class name and assign it to the constant TAG for Logging purposes
    private String TAG = RecipeActivity.class.getSimpleName();
    private int position;
    private String sPosition;
    private int positionSteps;
    private Uri stepUri;
    private String recipeName;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Bundle extras = getIntent().getExtras();
        /* Set the Uri for the StepsActivity and Fragment */
        stepUri = BakingContract.BakingEntry.buildStepsUri();
        if(findViewById(R.id.android_me_linear_layout) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {

                RecipeFragment recipeFragment = new RecipeFragment();

               /* Get the correct position, recipeName and the uriRecipeClicked */
                position = extras.getInt(getResources().getString(R.string.position));
                recipeName = extras.getString(getResources().getString(R.string.recipeName));
                sPosition = String.valueOf(position);

                /* Set the data to the following methods: setStepUri, setPosition, setRecipeName, setSposition. */
                recipeFragment.setStepUri(stepUri);
                recipeFragment.setPosition(position);
                recipeFragment.setRecipeName(recipeName);
                recipeFragment.setSposition(sPosition);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_recipe_master, recipeFragment)
                        .commit();
                /* Set the activity title */
                setTitle(recipeName);
            }

        } else {
            mTwoPane = false;

            if (savedInstanceState == null) {
                RecipeFragment recipeFragment = new RecipeFragment();

               /* Get the correct position, recipeName and the uriRecipeClicked */
                position = extras.getInt(getResources().getString(R.string.position));
                sPosition = String.valueOf(position);
                recipeName = extras.getString(getResources().getString(R.string.recipeName));

               /* Set the data to the following methods: setStepUri, setPosition, setRecipeName, setSposition. */
                recipeFragment.setStepUri(stepUri);
                recipeFragment.setPosition(position);
                recipeFragment.setRecipeName(recipeName);
                recipeFragment.setSposition(sPosition);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_recipe, recipeFragment)
                        .commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt(getResources().getString(R.string.position), position);
        savedInstanceState.putString(getResources().getString(R.string.sPosition), sPosition);
        savedInstanceState.putString(getResources().getString(R.string.stepUri), stepUri.toString());
        savedInstanceState.putString(getResources().getString(R.string.stepUri), stepUri.toString());
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        stepUri = Uri.parse(savedInstanceState.getString(getResources().getString(R.string.stepUri)));
        position = savedInstanceState.getInt(getResources().getString(R.string.position));
        sPosition = savedInstanceState.getString(getResources().getString(R.string.sPosition));

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showIngredients(View v)
    {
        if(mTwoPane){
            IngredientsFragment ingredientsFragment = new IngredientsFragment();

            Uri ingredient = BakingContract.BakingEntry.buildIngredientsUri();
            ingredientsFragment.setIngredientUri(ingredient);
            ingredientsFragment.setPosition(position);
            ingredientsFragment.setsPosition(sPosition);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.fragment_recipe, ingredientsFragment)
                    .commit();

        }else {

            Uri uriRecipeClicked;

            Context context = this;
            Class destinationClass = IngredientsActivity.class;
            Intent intentToStartRecipeActivity = new Intent(context, destinationClass);
            uriRecipeClicked = BakingContract.BakingEntry.buildIngredientsUri();
            intentToStartRecipeActivity.setData(uriRecipeClicked);
            intentToStartRecipeActivity.putExtra(getResources().getString(R.string.position), position);
            intentToStartRecipeActivity.putExtra(getResources().getString(R.string.recipeName), recipeName);
            startActivity(intentToStartRecipeActivity);
        }
    }


    @Override
    public void recipeItemOnClickHandler(int stepPosition, View view, int adapterPosition, int countSteps) {

        if(mTwoPane){
            StepsFragment stepsFragment = new StepsFragment();
            Uri uriRecipeClicked;
            uriRecipeClicked = BakingContract.BakingEntry.buildStepsItemUri(stepPosition);
            stepsFragment.setItemStepUri(uriRecipeClicked);
            stepsFragment.setPosition(stepPosition);
            stepsFragment.setRecipeName(recipeName);
            stepsFragment.setsPosition(sPosition);
            stepsFragment.setCountSteps(countSteps);
            stepsFragment.setPositionSteps(adapterPosition);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.fragment_recipe, stepsFragment)
                    .commit();



        }else {

            Uri uriRecipeClicked;

            Context context = this;
            Class destinationClass = StepsActivity.class;
            positionSteps = adapterPosition;

            uriRecipeClicked = BakingContract.BakingEntry.buildStepsItemUri(stepPosition);
            Intent intentToStartRecipeActivity = new Intent(context, destinationClass);
            intentToStartRecipeActivity.setData(uriRecipeClicked);
            intentToStartRecipeActivity.putExtra(getResources().getString(R.string.position), stepPosition);
            intentToStartRecipeActivity.putExtra(getResources().getString(R.string.sPosition), sPosition);
            intentToStartRecipeActivity.putExtra(getResources().getString(R.string.countSteps), countSteps);
            intentToStartRecipeActivity.putExtra(getResources().getString(R.string.recipeName), recipeName);
            intentToStartRecipeActivity.putExtra(getResources().getString(R.string.positionSteps), adapterPosition);

            /*Log.d(TAG, countSteps + "");*/
            startActivity(intentToStartRecipeActivity);
        }

    }
}
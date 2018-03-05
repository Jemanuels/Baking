package za.co.samtakie.baking.activity;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import za.co.samtakie.baking.R;
import za.co.samtakie.baking.StepsFragment;

/**
 * Created by Jurgen Emanuels
 * Last updated on the 2018/03/03
 */
@SuppressWarnings("ALL")
public class StepsActivity extends AppCompatActivity{

    private String recipeName;
    private Uri itemStepUri;
    private int itemPosition;
    private int countSteps;
    private String sPosition;
    private int itempositionSteps;
    private StepsFragment stepsFragment;

    private static final String TAG_MY_FRAGMENT = "stepFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        /* Get the Bundle from the Widget and Parent App */
        Bundle extras = getIntent().getExtras();
        stepsFragment = new StepsFragment();
        if (savedInstanceState == null) {


            /* Get the data assign them to the respective variable and passed them through set methods
            * to the Fragment */
            itemStepUri = getIntent().getData();
            itemPosition = extras.getInt(getResources().getString(R.string.position));
            countSteps = extras.getInt(getResources().getString(R.string.countSteps));
            itempositionSteps = extras.getInt(getResources().getString(R.string.positionSteps));
            recipeName = extras.getString(getResources().getString(R.string.recipeName));
            sPosition = extras.getString(getResources().getString(R.string.sPosition));

            /* Set the title of the Activity to the recipe name*/
            setTitle(recipeName);

            /* Set the data to the following methods: setStepUri, setPosition, setRecipeName, setSposition.
            * The data will be available in the Fragment */
            stepsFragment.setItemStepUri(itemStepUri);
            stepsFragment.setPosition(itemPosition);
            stepsFragment.setRecipeName(recipeName);
            stepsFragment.setsPosition(sPosition);
            stepsFragment.setCountSteps(countSteps);
            stepsFragment.setPositionSteps(itempositionSteps);



            /* A FragmentManager to add and start and load the Fragment */
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_steps, stepsFragment, TAG_MY_FRAGMENT)
                    .commit();

        }else {
            itemStepUri = Uri.parse(savedInstanceState.getString("itemStepUri"));
            itemPosition = savedInstanceState.getInt("itemPosition");
            countSteps = savedInstanceState.getInt("countSteps");
            itempositionSteps = savedInstanceState.getInt("itempositionSteps");
            recipeName = savedInstanceState.getString("recipeName");
            sPosition = savedInstanceState.getString("sPosition");

            setTitle(recipeName);

            /* Set the data to the following methods: setStepUri, setPosition, setRecipeName, setSposition.
            * The data will be available in the Fragment */
            stepsFragment.setItemStepUri(itemStepUri);
            stepsFragment.setPosition(itemPosition);
            stepsFragment.setRecipeName(recipeName);
            stepsFragment.setsPosition(sPosition);
            stepsFragment.setCountSteps(countSteps);
            stepsFragment.setPositionSteps(itempositionSteps);
            stepsFragment = (StepsFragment) getSupportFragmentManager().findFragmentByTag(TAG_MY_FRAGMENT);
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

                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("itemStepUri", itemStepUri.toString());
        outState.putInt("itemPosition", itemPosition);
        outState.putInt("countSteps", countSteps);
        outState.putInt("itempositionSteps", itempositionSteps);
        outState.putString("recipeName", recipeName);
        outState.putString("sPosition",sPosition);
        outState.putString("recipeName",recipeName);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
    }
}
package za.co.samtakie.baking;


import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Jurgen Emanuels
 * Last updated on the 2018/02/07
 */
public class StepsActivity extends AppCompatActivity{

    String recipeName;
    Uri itemStepUri;
    int itemPosition;
    int countSteps;
    String sPosition;
    int itempositionSteps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        /* Get the Bundle from the Widget and Parent App */
        Bundle extras = getIntent().getExtras();

        if (savedInstanceState == null) {
            StepsFragment stepsFragment = new StepsFragment();

            /* Get the data assign them to the respective variable and passed them through set methods
            * to the Fragment */
            itemStepUri = getIntent().getData();
            itemPosition = extras.getInt(getResources().getString(R.string.position));
            countSteps = extras.getInt(getResources().getString(R.string.countSteps));
            itempositionSteps = extras.getInt(getResources().getString(R.string.positionSteps));
            recipeName = extras.getString(getResources().getString(R.string.recipeName));
            sPosition = extras.getString(getResources().getString(R.string.sPosition));

            /*Log.d("Item step uri ", itemStepUri.toString());
            Log.d("The position clicked ", itemPosition + "");
            Log.d("The total step is ", countSteps + "");
            Log.d("The position step is ", itempositionSteps + "");
            Log.d("The recipe name ", recipeName + "");*/

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
                    .add(R.id.fragment_steps, stepsFragment)
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
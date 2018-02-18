package za.co.samtakie.baking;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import za.co.samtakie.baking.activity.MainActivity;
import static android.support.test.espresso.Espresso.onView;
import android.support.test.espresso.contrib.RecyclerViewActions;


/**
 * Created by jemanuels on 2018/02/15.
 * Last updated 2018/02/19
 */

@SuppressWarnings("ALL")
@RunWith(AndroidJUnit4.class)
public class IdlingResourceMenuActivityTest {

    private static final int ITEM_BELOW_THE_FOLD = 1;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void init(){
        mActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction().commit();
    }

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void idlingResourceTest(){
        //onData(anything()).inAdapterView(withId(R.id.card_view)).atPosition(0).perform(click());
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.fragment_item))
                .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BELOW_THE_FOLD, MyViewAction.clickChildViewWithId(R.id.card_view)));
    }


     /* Remember to unregister resources when not needed to avoid malfunction. */
     @After
    public void unregisterIdlingResource(){
         if(mIdlingResource != null){
             Espresso.unregisterIdlingResources(mIdlingResource);
         }
     }
}

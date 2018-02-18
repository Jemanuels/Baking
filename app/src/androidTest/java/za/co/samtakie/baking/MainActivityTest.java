package za.co.samtakie.baking;


import android.support.test.rule.ActivityTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import za.co.samtakie.baking.activity.MainActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by jemanuels on 2018/02/18.
 * Last updated 2018/02/19
 */

@SuppressWarnings("ALL")
public class MainActivityTest {
    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @Before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */
    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void yourSetUPFragment() {
        mActivityTestRule.getActivity()
                .getFragmentManager().beginTransaction().commit();
    }

    /**
     * Clicks on a Ingredient button item and checks it opens up the IngredientsActivity with the correct details.
     */
    @Test
    public void clickCardViewItem_OpensIngredientActivity() {

        onView(allOf(withId(R.id.recipeName), withText("Nutella Pie"))).perform(click());

        // Checks that the IngredientActivity opens.
        onView(withId(R.id.ingredient_bt)).perform(click());
    }

    /**
     * Clicks on a CardView item and checks it opens up the StepsActivity with the correct details.
     */
    @Test
    public void clickCardViewItem_OpensStepsActivity() {

        onView(allOf(withId(R.id.recipeName), withText("Nutella Pie"))).perform(click());

        onView(allOf(withId(R.id.recipeSteps), withText("Recipe Introduction"))).perform(click());


        onView(withId(R.id.nextBtn)).perform(click());

    }

}
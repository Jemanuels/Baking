package za.co.samtakie.baking.activity;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.samtakie.baking.BakingWidgetProvider;
import za.co.samtakie.baking.ItemFragment;
import za.co.samtakie.baking.R;

public class WidgetConfigurationActivity extends AppCompatActivity implements ItemFragment.RecipeAdapterOnClickHandler{

    private static SharedPreferences prefs;
    public static final String PREFS_NAME = "AppWidget";
    public static final String PREF_PREFIX_KEY = "appwidget";
    public static final String PREF_PREFIX_WIDGETID = "widgetid";
    private static final String PREF_PREFIX_POSITION = "recipePosition";


    @BindView(R.id.cancelBtn) Button cancelBtn;

    private int mAppWidgetId = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configuration);
        ButterKnife.bind(this);
         prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        setTitle(getString(R.string.widget_configuration_title));
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cancelClicked();
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

    }



    private void cancelClicked() {
        finish();
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

        String recipeName = ((TextView) view.findViewById(R.id.recipeName)).getText().toString();
        if(saveDataPref(mAppWidgetId, recipeName, recipePosition)){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());

            BakingWidgetProvider.updateAppWidget(this, appWidgetManager, mAppWidgetId);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

            setResult(RESULT_OK, resultValue);
            finish();
        }
    }

    // Write the prefix to the SharedPreferences object for this widget
    static boolean saveDataPref(int appWidgetId, String recipeName, int position) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(PREF_PREFIX_KEY + appWidgetId, recipeName);
        edit.putInt(PREF_PREFIX_POSITION + appWidgetId, position);
        edit.putInt(PREF_PREFIX_WIDGETID, appWidgetId);
        edit.commit();

        return true;
    }
}
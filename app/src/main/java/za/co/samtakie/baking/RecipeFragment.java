package za.co.samtakie.baking;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.samtakie.baking.activity.RecipeActivity;
import za.co.samtakie.baking.data.BakingContract;
import za.co.samtakie.baking.data.StepAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeFragment.RecipeItemOnClickHandler} interface
 * to handle interaction events.
 * Use the {@link RecipeFragment#} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("ALL")
public class RecipeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private LinearLayoutManager layoutManager;
    private StepAdapter stepAdapter;
    private int countSteps;
    private RecipeItemOnClickHandler mListener;
    private Uri stepUri;
    private int position;
    private String recipeName;
    private String sPosition;

    private static final int ID_STEP_LOADER = 36;

    @BindView(R.id.ingredient_bt) Button ingredientButton;
    @BindView(R.id.step_rv) RecyclerView recyclerView;

    public RecipeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        getLoaderManager().initLoader(ID_STEP_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState != null){
            recipeName = savedInstanceState.getString(getResources().getString(R.string.recipeName));
            sPosition = savedInstanceState.getString(getResources().getString(R.string.sPosition));
            stepUri = Uri.parse(savedInstanceState.getString("stepUri"));
            position = savedInstanceState.getInt("position");
        }
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);

        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        recyclerView.setHasFixedSize(true);

        /* The RecipeAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our recipe data.
         * */
        stepAdapter = new StepAdapter(mListener, getContext());

        ingredientButton.setText(getResources().getString((R.string.ingredients_button_text)));

        /*
         * Setting the adapter attaches it to the RecyclerView in our layout.
         */
        recyclerView.setAdapter(stepAdapter);

        getActivity().setTitle(recipeName);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeItemOnClickHandler) {
            mListener = (RecipeItemOnClickHandler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getResources().getString(R.string.recipe_error_message));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface RecipeItemOnClickHandler {
        // TODO: Update argument type and name
        void recipeItemOnClickHandler(int recipeID, View view, int adapterPosition, int countSteps);

    }

    public void setStepUri(Uri stepUri){
        this.stepUri = stepUri;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setRecipeName(String recipeName){
        this.recipeName = recipeName;
    }

    public void setCountSteps(int countSteps) {
        this.countSteps = countSteps;
    }

    public void setSposition(String sPosition){
        this.sPosition = sPosition;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch(loaderId){
            case ID_STEP_LOADER:
                Uri recipeQueryUri = BakingContract.BakingEntry.CONTENT_URI_STEPS;

                String mSelectionClause = BakingContract.BakingEntry.COLUMN_STEP_RECIPEID + " = ? ";
                String[] mSelectionArgs = {""};
                mSelectionArgs[0] = sPosition;

                return new CursorLoader(getActivity(),
                        recipeQueryUri,
                        RecipeActivity.STEPS_PROJECTION,
                        mSelectionClause,
                        mSelectionArgs,
                        null);

            default:
                throw new RuntimeException(getResources().getString(R.string.loader_error_message) + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if(data != null && data.moveToFirst()){
            cursorHasValidData = true;
            /*Log.d("Cursor Data ", "There is data in the cursor");
            Log.d("Cursor Data ", String.valueOf(data.getCount()));*/
        }

        if(!cursorHasValidData){
            //Log.d("Data ", "There is no data in the cursor");
            return;
        }
        stepAdapter.swapCursor(data);

        countSteps = stepAdapter.getItemCount();
        /*Log.d("Count steps ",countSteps + "");*/

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(getResources().getString(R.string.recipeName), recipeName);
        outState.putString(getResources().getString(R.string.sPosition), sPosition);
        outState.putString("stepUri", stepUri.toString());
        outState.putInt("position", position);
    }
}
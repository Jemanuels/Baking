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

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.samtakie.baking.data.BakingContract;
import za.co.samtakie.baking.data.IngredientsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientsFragment#} factory method to
 * create an instance of this fragment.
 * Last updated 2018/02/2017
 */
@SuppressWarnings("ALL")
public class IngredientsFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{

    /*
     * This ID will be used to identify the Loader responsible for loading our movie data.
     * We will still use this ID to initialize the loader and create the loader for best practice.
     */
    private static final int ID_INGREDIENT_LOADER = 40;

    private static final String[] INGREDIENTS_PROJECTION  =
            {
                    BakingContract.BakingEntry._ID,
                    BakingContract.BakingEntry.COLUMN_INGREDIENTS_INGREDIENT,
                    BakingContract.BakingEntry.COLUMN_INGREDIENTS_MEASURE,
                    BakingContract.BakingEntry.COLUMN_INGREDIENTS_QUANTITY,
                    BakingContract.BakingEntry.COLUMN_INGREDIENTS_RECIPEID
            };

    private IngredientsAdapter ingredientsAdapter;
    private LinearLayoutManager layoutManager;

    private int position;
    private String sPosition;
    private Uri ingredientUri;
    @BindView(R.id.ingredients_rv) RecyclerView recyclerView;



    public IngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients_list, container, false);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        ButterKnife.bind(this, view);

        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        recyclerView.setHasFixedSize(true);

        /*
         * The RecipeAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our recipe data.
         * */
        ingredientsAdapter = new IngredientsAdapter(getActivity());

        /*
         * Setting the adapter attaches it to the RecyclerView in our layout.
         */
        recyclerView.setAdapter(ingredientsAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        BakingContract.BakingEntry.buildRecipeAllUri();
        getLoaderManager().initLoader(ID_INGREDIENT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch(loaderId){
            case ID_INGREDIENT_LOADER:
                Uri ingredientQueryUri = ingredientUri;
                /*Log.d("INGREDIENT ID", "ID_INGREDIENT_LOADER");*/
                String mSelectionClause = BakingContract.BakingEntry.COLUMN_INGREDIENTS_RECIPEID + " = ? ";
                String[] mSelectionArgs = {""};
                mSelectionArgs[0] = sPosition;
                return new CursorLoader(getActivity(),
                        ingredientQueryUri,
                        INGREDIENTS_PROJECTION,
                        mSelectionClause,
                        mSelectionArgs,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
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
        ingredientsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setsPosition(String sPosition) {
        this.sPosition = sPosition;
    }

    public void setIngredientUri(Uri ingredientUri) {
        this.ingredientUri = ingredientUri;
    }
}
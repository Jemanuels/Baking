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
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.samtakie.baking.data.BakingContract;
import za.co.samtakie.baking.data.RecipeAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemFragment.RecipeAdapterOnClickHandler} interface
 * to handle interaction events.
 */
@SuppressWarnings("ALL")
public class ItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecipeAdapterOnClickHandler mListener;
    private RecipeAdapter recipeAdapter;
    @BindView(R.id.my_rv) RecyclerView recyclerView;

    /*
     * The columns of data that we are interested in displaying within our list of
     * recipe data.
     */
    private static final String[] RECIPE_PROJECTION  =
            {
                    BakingContract.BakingEntry._ID,
                    BakingContract.BakingEntry.COLUMN_RECIPE_ID,
                    BakingContract.BakingEntry.COLUMN_RECIPE_IMAGE,
                    BakingContract.BakingEntry.COLUMN_RECIPE_NAME,
                    BakingContract.BakingEntry.COLUMN_RECIPE_SERVINGS
            };


    /*
     * This ID will be used to identify the Loader responsible for loading our baking data.
     */
    private static final int ID_RECIPE_LOADER = 36;

    public ItemFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        BakingContract.BakingEntry.buildRecipeAllUri();
        getLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_item_list, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();

        recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns()));

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        recyclerView.setHasFixedSize(true);
        /*
         * The RecipeAdapter is responsible for linking our baking data with the Views that
         * will end up displaying our recipe data.
         */
        recipeAdapter = new RecipeAdapter(mListener, getActivity());

        /*
         * Setting the adapter attaches it to the RecyclerView in our layout.
         */
        recyclerView.setAdapter(recipeAdapter);
        return view;
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) {
            return 1;
        }
        return nColumns;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeAdapterOnClickHandler) {
            mListener = (RecipeAdapterOnClickHandler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getResources().getText(R.string.handler_error_message));
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface RecipeAdapterOnClickHandler {
        // TODO: Update argument type and name
        void recipeAdapterOnClickHandler(int recipeID, View view);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch(loaderId){
            case ID_RECIPE_LOADER:
                Uri recipeQueryUri = BakingContract.BakingEntry.CONTENT_URI_RECIPE;
                /*Log.d("RECIPE ID", "ID_RECIPE_LOADER");*/
                return new CursorLoader(getActivity(),
                        recipeQueryUri,
                        RECIPE_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException(getResources().getString(R.string.loader_error_message) + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount() != 0){
            /*Log.d("data ", "has mre then one data");*/
        }
        recipeAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recipeAdapter.swapCursor(null);
    }
}
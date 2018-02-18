package za.co.samtakie.baking.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import za.co.samtakie.baking.R;
import za.co.samtakie.baking.activity.RecipeActivity;
import za.co.samtakie.baking.RecipeFragment.*;

/**
 * Created by jemanuels on 2018/01/17.
 * Last updated on 2018\02\02
 */

@SuppressWarnings("ALL")
public class StepAdapter extends RecyclerView.Adapter<StepAdapter.RecipeViewHolder> {

    /*private  String LOG_TAG = "StepAdapter"; //For Log view*/
    private Context context;
    private Cursor cursor;
    private int stepPosition;


    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * the RecyclerView
     */
    private final RecipeItemOnClickHandler mClickHandler;



    /**
     *  Creates a StepAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public StepAdapter(RecipeItemOnClickHandler clickHandler, Context context){
        this.mClickHandler = clickHandler;
        this.context = context;
    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType If your RecyclerView has more than one type of item (which ours doesn't) you
     *                 can use this viewType integer to provide a different layout. See
     *                 {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                 for more details
     * @return A new RecipeViewHolder that holds the View for each list item.
     */
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.fragment_recipe;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachtoParent = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachtoParent);
        return new RecipeViewHolder(view);
    }

    /**
     * onBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the step
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        cursor.moveToPosition(position);
        stepPosition = position;
        String step = cursor.getString(RecipeActivity.INDEX_COLUMN_STEP_SHORTDESCRIPTION);
        holder.steps.setText(step);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our step cursor
     */
    @Override
    public int getItemCount() {
        if(null == cursor){
            return 0;
        }
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        cursor = newCursor;
        notifyDataSetChanged();
    }


    /**
     * Cache of the children views for a recipe list item
     */
    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView steps;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            steps = itemView.findViewById(R.id.recipeSteps);
            itemView.setOnClickListener(this);

        }

        /**
         *  This gets called by the child views during a click.
         * @param view The View that was clicked
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            cursor.moveToPosition(adapterPosition);
            int stepID = cursor.getInt(RecipeActivity.INDEX_ID);

            mClickHandler.recipeItemOnClickHandler(stepID, view, adapterPosition, getItemCount());
        }
    }
}
package za.co.samtakie.baking.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import za.co.samtakie.baking.ItemFragment.*;
import za.co.samtakie.baking.MainActivity;
import za.co.samtakie.baking.R;

/**
 * Created by jemanuels on 2017/12/06.
 * Last updated on 2018/02/02
 */


/**
 * {@link RecipeAdapter} exposes a list of recipe to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    /*private  String LOG_TAG = "RecipeAdapter"; // For Log view*/
    private final Context context;
    private Cursor cursor;

    /*
     * An on-click handler that we've defined in FragmentRecipe to make it easy for an Activity to interface with
     * the RecyclerView
     */
    private final RecipeAdapterOnClickHandler mClickHandler;


    /**
     *  Creates a RecipeAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     * @param context
     */
    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler, Context context){
        mClickHandler = clickHandler;
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
     * @return A new RecipeAdapterViewHolder that holds the View for each list item.
     */
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.fragment_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachtoParent = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachtoParent);
        return new RecipeViewHolder(view);
    }

    /**
     * onBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the recipe
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecipeViewHolder holder, final int position) {

        cursor.moveToPosition(position);
        String recipe = cursor.getString(MainActivity.INDEX_COLUMN_RECIPE_NAME);
        holder.label.setText(recipe);
        holder.bakingImageView.setImageResource(BakingImages.getBakingImages().get(position));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our recipe
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

        TextView label;
        ImageView bakingImageView;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.recipeName);
            bakingImageView = (ImageView) itemView.findViewById(R.id.baking_iv);
            /*Log.i(LOG_TAG, "Adding Listener");*/
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
            int recipeID = cursor.getInt(MainActivity.INDEX_COLUMN_RECIPE_ID);
            mClickHandler.recipeAdapterOnClickHandler(recipeID, view);
        }
    }
}
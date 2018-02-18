package za.co.samtakie.baking.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import za.co.samtakie.baking.activity.IngredientsActivity;
import za.co.samtakie.baking.R;

/**
 * Created by jemanuels on 2017/12/06.
 * Last updated on 2018/02/02 clear all unused imports
 * {@link IngredientsAdapter} exposes a list of recipe to a
 * {@link RecyclerView}
 */
@SuppressWarnings("ALL")
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    /*private  String LOG_TAG = "IngredientsAdapter"; // For log view */
    private final Context context;
    private Cursor cursor;

    /**
     *  Creates a IngredientsAdapter.
     * @param context provide access to the application life cycle
     */
    public IngredientsAdapter(Context context){
        this.context = context;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType If your RecyclerView has more than one type of item (which ours doesn't) you
     *                 can use this viewType integer to provide a different layout. See
     *                 {@link RecyclerView.Adapter#getItemViewType(int)}
     *                 for more details
     * @return A new IngredientsViewHolder that holds the View for each list item.
     */
    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.fragment_ingredients;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachtoParent = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachtoParent);
        return new IngredientsViewHolder(view);
    }

    /**
     * onBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the ingredients
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        cursor.moveToPosition(position);
        String ingredient = cursor.getString(IngredientsActivity.INDEX_COLUMN_INGREDIENTS_INGREDIENT);
        String measure = cursor.getString(IngredientsActivity.INDEX_COLUMN_INGREDIENTS_MEASURE);
        String quantity = cursor.getString(IngredientsActivity.INDEX_COLUMN_INGREDIENTS_QUANTITY);
        String ingredients = ingredient + " - " + measure + " - " + quantity;
        holder.label.setText(ingredients);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our ingredients cursor
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
     * Cache of the children views for a ingredients list item
     */
    public class IngredientsViewHolder extends RecyclerView.ViewHolder{

        TextView label;
        public IngredientsViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.ingredients_tv);
        }
    }
}
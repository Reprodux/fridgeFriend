package com.example.fridge_friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * The type Fridge items adapter.
 */
public class FridgeItemsAdapter extends RecyclerView.Adapter<FridgeItemsAdapter.ViewHolder> {

    private List<FridgeItem> fridgeItems; // This should be List<FridgeItem>
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    /**
     * Instantiates a new Fridge items adapter.
     *
     * @param context     the context
     * @param fridgeItems the fridge items
     */
// Constructor should match the class name and use List<FridgeItem>
    public FridgeItemsAdapter(Context context, List<FridgeItem> fridgeItems) {
        this.mInflater = LayoutInflater.from(context);
        this.fridgeItems = fridgeItems; // Corrected variable name
    }

    // Inflate the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_fridge_detail, parent, false); // Make sure to use the correct layout file
        return new ViewHolder(view);
    }

    // Bind data to each item
    @Override
    public void onBindViewHolder(FridgeItemsAdapter.ViewHolder holder, int position) {
        FridgeItem item = fridgeItems.get(position);
        holder.textViewItemName.setText(item.getName()); // Make sure this ID exists in item_fridge_detail.xml
    }

    // Total number of rows
    @Override
    public int getItemCount() {
        return fridgeItems.size(); // Corrected variable name
    }

    /**
     * Gets item.
     *
     * @param id the id
     * @return the item
     */
// Convenience method for getting data at click position
    public FridgeItem getItem(int id) {
        return fridgeItems.get(id); // Should return a FridgeItem
    }

    /**
     * The interface Item click listener.
     */
// Parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        /**
         * On item click.
         *
         * @param position the position
         */
        void onItemClick(int position); // Simplified to a single method
    }

    /**
     * The type View holder.
     */
// Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /**
         * The Text view item name.
         */
        TextView textViewItemName; // This ID should match the one in item_fridge_detail.xml

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName); // Corrected ID
            itemView.setOnClickListener(this); // Only one click listener for the item view
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    /**
     * Sets click listener.
     *
     * @param itemClickListener the item click listener
     */
// Allows click events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}

package com.example.fridge_friend;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridge_friend.database.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * The type Fridge items adapter.
 */
public class FridgeItemsAdapter extends RecyclerView.Adapter<FridgeItemsAdapter.ViewHolder> {

    private List<Item> items;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    /**
     * Instantiates a new Fridge items adapter.
     *
     * @param context the context
     * @param items   the items
     */
// Constructor should match the class name and use List<FridgeItem>
    public FridgeItemsAdapter(Context context, List<Item> items) {
        this.mInflater = LayoutInflater.from(context);
        this.items = items; // Corrected variable name
    }

    // Inflate the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_fridge_detail, parent, false);
        return new ViewHolder(view);
    }

    // Bind data to each item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Item currentItem = items.get(position);
        holder.textViewItemName.setText(currentItem.getName());

        if (isExpiryWithinFourDays(currentItem.getExpiry())) {
            holder.textViewItemName.setTextColor(Color.RED);
        } else {
            holder.textViewItemName.setTextColor(Color.BLACK);
        }
    }
    private boolean isExpiryWithinFourDays(String expiryDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date expiry = dateFormat.parse(expiryDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expiry);
            calendar.add(Calendar.DAY_OF_YEAR, -4);
            Date fourDaysBeforeExpiry = calendar.getTime();

            // ComparING with the current date
            return new Date().after(fourDaysBeforeExpiry);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Total number of rows
    @Override
    public int getItemCount() {
        return items.size(); // Corrected variable name
    }

    // Convenience method for getting data at click position
//    public FridgeItem getItem(int id) {
//        return fridgeItems.get(id); // Should return a FridgeItem
//    }

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
         * The Text view expiry date.
         */
        TextView textViewExpiryDate;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewExpiryDate = itemView.findViewById(R.id.textViewExpiryDate);// Corrected ID
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
     * Sets items.
     *
     * @param items the items
     */
    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
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

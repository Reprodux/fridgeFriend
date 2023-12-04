package com.example.fridge_friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * The type Fridge adapter.
 */
public class FridgeAdapter extends RecyclerView.Adapter<FridgeAdapter.ViewHolder> {

    private List<String> mFridgeNames;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    /**
     * Instantiates a new Fridge adapter.
     *
     * @param context     the context
     * @param fridgeNames the fridge names
     */
// Constructor should be public
    public FridgeAdapter(Context context, List<String> fridgeNames) {
        this.mInflater = LayoutInflater.from(context);
        this.mFridgeNames = fridgeNames;
    }

    // Inflate the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_fridge, parent, false);
        return new ViewHolder(view);
    }

    // Bind data to each item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String fridge = mFridgeNames.get(position);
        holder.myTextView.setText(fridge);
    }

    // Total number of rows
    @Override
    public int getItemCount() {
        return mFridgeNames.size();
    }

    /**
     * Gets item.
     *
     * @param id the id
     * @return the item
     */
// Convenience method for getting data at click position
    public String getItem(int id) {
        return mFridgeNames.get(id);
    }

    /**
     * The interface Item click listener.
     */
// Parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        /**
         * On fridge click.
         *
         * @param view     the view
         * @param position the position
         */
        void onFridgeClick(View view, int position);

        /**
         * On join fridge click.
         *
         * @param view     the view
         * @param position the position
         */
        void onJoinFridgeClick(View view, int position);
    }

    /**
     * The type View holder.
     */
// Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /**
         * The My text view.
         */
        TextView myTextView;
        /**
         * The My image view.
         */
        ImageView myImageView;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textViewFridgeName);
            myImageView = itemView.findViewById(R.id.imageViewJoinFridge);
            itemView.setOnClickListener(this);
            myImageView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                if (view.getId() == myImageView.getId()) {
                    mClickListener.onJoinFridgeClick(view, getAdapterPosition());
                } else {
                    mClickListener.onFridgeClick(view, getAdapterPosition());
                }
            }
        }
    }

    /**
     * Update data.
     *
     * @param newFridgeNames the new fridge names
     */
//updating data in the adapter
    public void updateData(List<String> newFridgeNames){
        this.mFridgeNames = newFridgeNames;
        notifyDataSetChanged();
    }


    /**
     * Sets click listener.
     *
     * @param itemClickListener the item click listener
     */
// Allows clicks events to be caught, should be public
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}

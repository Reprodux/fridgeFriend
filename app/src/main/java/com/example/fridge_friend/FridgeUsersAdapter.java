package com.example.fridge_friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Fridge users adapter.
 */
public class FridgeUsersAdapter extends RecyclerView.Adapter<FridgeUsersAdapter.ViewHolder> {
    private final Map<String,String> userMap;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    /**
     * Instantiates a new Fridge users adapter.
     *
     * @param context the context
     * @param userMap the user map
     */
    public FridgeUsersAdapter(Context context, Map<String, String> userMap) {
        this.mInflater = LayoutInflater.from(context);
        this.userMap = userMap;
    }

    private List<String> getUserIds() {
        List<String> userIds = new ArrayList<>(userMap.keySet());
        userIds.sort((o1, o2) -> {
            String user1 = userMap.get(o1);
            String user2 = userMap.get(o2);
            if (user1 == null) {
                return -1;
            }
            if (user2 == null) {
                return 1;
            }
            return user1.compareToIgnoreCase(user2);
        });
        return userIds;
    }

    // Inflate the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_user_list, parent, false); // Make sure to use the correct layout file
        return new FridgeUsersAdapter.ViewHolder(view);
    }

    // Bind data to each item
    @Override
    public void onBindViewHolder(@NonNull FridgeUsersAdapter.ViewHolder holder, int position) {
        String id = getUserIds().get(position);
        holder.textViewUserName.setText(userMap.get(id));
    }

    /**
     * Gets user id.
     *
     * @param position the position
     * @return the user id
     */
    public String getUserId(int position) {
        return getUserIds().get(position);
    }

    // Total number of rows
    @Override
    public int getItemCount() {
        return userMap.size();
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
         * The Text view user name.
         */
        TextView textViewUserName;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            itemView.setOnClickListener(this);
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

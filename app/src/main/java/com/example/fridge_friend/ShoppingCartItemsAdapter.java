package com.example.fridge_friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridge_friend.database.local.ShoppingCartItem;

import java.util.List;

public class ShoppingCartItemsAdapter extends RecyclerView.Adapter<ShoppingCartItemsAdapter.ViewHolder> {

    private final List<ShoppingCartItem> cartItems;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public ShoppingCartItemsAdapter(Context context, List<ShoppingCartItem> cartItems) {
        this.mInflater = LayoutInflater.from(context);
        this.cartItems = cartItems;
    }

    // Inflate the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_fridge_detail, parent, false); // Make sure to use the correct layout file
        return new ShoppingCartItemsAdapter.ViewHolder(view);
    }

    // Bind data to each item
    @Override
    public void onBindViewHolder(@NonNull ShoppingCartItemsAdapter.ViewHolder holder, int position) {
        ShoppingCartItem item = cartItems.get(position);
        holder.textViewItemName.setText(item.getName());
    }

    // Total number of rows
    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // Convenience method for getting data at click position
    public ShoppingCartItem getItem(int position) {
        return cartItems.get(position);
    }

    // Parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int position); // Simplified to a single method
    }

    // Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewItemName;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    // Allows click events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}

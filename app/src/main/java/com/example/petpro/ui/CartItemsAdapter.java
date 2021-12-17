package com.example.petpro.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petpro.R;
import com.example.petpro.db.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Title: CartItemsAdapter.java
 * Abstract: Sets up the adaptor for the RecyclerView
 * Author: Arielle Lauper
 * Date: 7 - Dec - 2021
 * References: Class materials
 * All videos in playlist: https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-1-introduction
 * RecyclerView: https://guides.codepath.com/android/using-the-recyclerview
 * Format price: https://stackoverflow.com/questions/9366280/android-round-to-2-decimal-places
 */

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.ViewHolder> {

  private CartItemsAdapter.OnItemClickListener mListener;
  private CartItemsAdapter.OnItemClickListener mListener1;

  private List<CartItem> mItems = new ArrayList<>();

  private int mPosition;

  public CartItemsAdapter(List<CartItem> items) {
    mItems = items;
  }

  @NonNull
  @Override
  public CartItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View itemsView;

    itemsView = inflater.inflate(R.layout.product_item_layout, parent, false);

    CartItemsAdapter.ViewHolder viewHolder = new CartItemsAdapter.ViewHolder(itemsView);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull CartItemsAdapter.ViewHolder holder, int position) {
    CartItem currentItem = mItems.get(position);
    holder.textViewItemName.setText(currentItem.getName());
    holder.textViewItemPrice.setText(String.format(Locale.US, "%.2f", currentItem.getPrice()));
    holder.textViewItemQuantity.setText(String.valueOf(currentItem.getQuantity()));
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  public int getPosition() {
    return mPosition;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    private TextView textViewItemName;
    private TextView textViewItemPrice;
    private TextView textViewItemQuantity;
    private TextView textViewItemRemove;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      textViewItemName = itemView.findViewById(R.id.textViewItemName);
      textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
      textViewItemQuantity = itemView.findViewById(R.id.textViewItemQuantity);
      textViewItemRemove = itemView.findViewById(R.id.textViewItemRemove);
      textViewItemRemove.setVisibility(View.VISIBLE);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int position = getAdapterPosition();
          if (mListener != null && position != RecyclerView.NO_POSITION) {
            mListener.onItemClick(mItems.get(position));
          }
        }
      });

      textViewItemRemove.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int position = getAdapterPosition();
          if (mListener1 != null && position != RecyclerView.NO_POSITION) {
            mListener1.onItemClick(mItems.get(position));
            mPosition = position;
          }
        }
      });
    }
  }

  public interface OnItemClickListener {
    void onItemClick(CartItem item);
  }

  public void setOnItemClickListener(CartItemsAdapter.OnItemClickListener listener) {
    mListener = listener;
  }

  public void setOnRemoveItemClickListener(CartItemsAdapter.OnItemClickListener listener) {
    mListener1 = listener;
  }
}

package com.example.petpro.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petpro.db.Item;
import com.example.petpro.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Title: ItemsAdapter.java
 * Abstract: Sets up the adaptor for the RecyclerView
 * Author: Arielle Lauper
 * Date: 7 - Dec - 2021
 * References: Class materials
 * All videos in playlist: https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-1-introduction
 * RecyclerView: https://guides.codepath.com/android/using-the-recyclerview
 * Format price: https://stackoverflow.com/questions/9366280/android-round-to-2-decimal-places
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

  private OnItemClickListener mListener;

  private List<Item> mItems = new ArrayList<>();

  private Context mContext;

  public ItemsAdapter(List<Item> items) {
    mItems = items;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View itemsView;

    itemsView = inflater.inflate(R.layout.product_item_layout, parent, false);

    return new ViewHolder(itemsView);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Item currentItem = mItems.get(position);
    holder.mTextViewItemName.setText(currentItem.getName());
    holder.mTextViewItemPrice.setText(String.format(Locale.US, "%.2f", currentItem.getPrice()));
    holder.mTextViewItemQuantity.setText(String.valueOf(currentItem.getQuantity()));
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    private TextView mTextViewItemName;
    private TextView mTextViewItemPrice;
    private TextView mTextViewItemQuantity;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      mTextViewItemName = itemView.findViewById(R.id.textViewItemName);
      mTextViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
      mTextViewItemQuantity = itemView.findViewById(R.id.textViewItemQuantity);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int position = getAdapterPosition();
          if (mListener != null && position != RecyclerView.NO_POSITION) {
            mListener.onItemClick(mItems.get(position));
          }
        }
      });
    }
  }

  public interface OnItemClickListener {
    void onItemClick(Item item);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    mListener = listener;
  }
}

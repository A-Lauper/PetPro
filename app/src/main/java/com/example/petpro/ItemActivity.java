package com.example.petpro;

import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.petpro.db.AppDatabase;
import com.example.petpro.db.CartItem;
import com.example.petpro.db.Item;
import com.example.petpro.db.PetProDAO;

import java.util.Locale;

/**
 * Title: ItemActivity.java
 * Abstract: Logic for the item page
 * Author: Arielle Lauper
 * Date: 7 - Dec - 2021
 * References: Class materials
 *             Format price: https://stackoverflow.com/questions/9366280/android-round-to-2-decimal-places
 */

public class ItemActivity extends BaseActivity {

  private static final String USER_ID_KEY = "com.example.petpro.USER_ID_KEY";
  private static final String ITEM_ID_KEY = "com.example.petpro.ITEM_ID_KEY";

  private PetProDAO mPetProDAO;

  private int mUserId;

  private int mItemId;
  private Item mItem;

  private TextView mTextViewItemName;
  private TextView mTextViewItemPrice;
  private TextView mTextViewQuantity;
  private Button mButtonAddToCart;
  private NumberPicker mNumberPicker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item);

    getDatabase();

    mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

    mItemId = getIntent().getIntExtra(ITEM_ID_KEY, -1);
    mItem = mPetProDAO.getItemByItemId(mItemId);

    wireUpDisplay();
  }

  private void wireUpDisplay() {
    mTextViewItemName = findViewById(R.id.textViewItemPageName);
    mTextViewItemPrice = findViewById(R.id.textViewItemPagePrice);
    mTextViewQuantity = findViewById(R.id.textViewItemPageQuantity);
    mButtonAddToCart = findViewById(R.id.buttonItemPageAddToCart);
    mNumberPicker = findViewById(R.id.numberPickerItemPageQuantity);

    mTextViewItemName.setText(mItem.getName());
    mTextViewItemPrice.setText(String.format(Locale.US,"%.2f", mItem.getPrice()));

    if (mItem.getQuantity() < 1) {
      mTextViewQuantity.setText(R.string.out_of_stock);
      mButtonAddToCart.setVisibility(View.INVISIBLE);
      mNumberPicker.setVisibility(View.INVISIBLE);
    } else {
      mNumberPicker.setMinValue(1);
      mNumberPicker.setMaxValue(mItem.getQuantity());

      mButtonAddToCart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mPetProDAO.getCartItemsByUserId(mUserId);
          CartItem cartItem = mPetProDAO.getCartItemByUserIdAndName(mUserId, mItem.getName());
          // add if not in cart
          if (cartItem == null) {
            cartItem = new CartItem(mUserId, mItem.getName(), mItem.getPrice(), mNumberPicker.getValue());
            mPetProDAO.insert(cartItem);
          } else { // update quantity if already in cart
            cartItem.setQuantity(cartItem.getQuantity() + mNumberPicker.getValue());
            mPetProDAO.update(cartItem);
          }
          Intent intent = CartActivity.intentFactory(getApplicationContext(), mUserId);
          startActivity(intent);
        }
      });
    }
  }

  private void getDatabase() {
    mPetProDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
        .allowMainThreadQueries()
        .build()
        .getPetProDAO();
  }

  public static Intent intentFactory(Context context, int userId, int itemId) {
    Intent intent = new Intent(context, ItemActivity.class);
    intent.putExtra(USER_ID_KEY, userId);
    intent.putExtra(ITEM_ID_KEY, itemId);
    return intent;
  }
}
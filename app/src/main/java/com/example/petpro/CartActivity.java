package com.example.petpro;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petpro.db.AppDatabase;
import com.example.petpro.db.CartItem;
import com.example.petpro.db.Item;
import com.example.petpro.db.OrderLog;
import com.example.petpro.db.PetProDAO;
import com.example.petpro.db.PurchasedItem;
import com.example.petpro.db.User;
import com.example.petpro.ui.CartItemsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Title: CartActivity.java
 * Abstract: Logic for the cart page
 * Author: Arielle Lauper
 * Date: 7 - Dec - 2021
 * References: Class materials
 *             Disable button clicks: https://stackoverflow.com/questions/5195321/remove-an-onclick-listener
 *             Button Color: https://stackoverflow.com/questions/28757390/how-to-change-button-color-dynamically/28757562
 */

public class CartActivity extends BaseActivity {

  private static final String USER_ID_KEY = "com.example.petpro.USER_ID_KEY";

  private PetProDAO mPetProDAO;

  private int mUserId;

  private double mTotal = 0;
  private TextView mTextViewTotal;
  private List<CartItem> mCartItems;
  private Button mButtonPurchase;
  private Button mButtonContinueShopping;

  Item mInStoreItem;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cart);

    mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
    getDatabase();

    wireUpDisplay();
  }

  private void wireUpDisplay() {

    setUpRecyclerView();

    mTextViewTotal = findViewById(R.id.textViewCartTotal);
    mTextViewTotal.setText(String.format(Locale.US,"%.2f", mTotal));
    mButtonPurchase = findViewById(R.id.buttonCartPurchase);
    mButtonContinueShopping = findViewById(R.id.buttonCartContinueShopping);

    mButtonContinueShopping.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = BrowseItemsActivity.intentFactory(getApplicationContext(), mUserId);
        startActivity(intent);
      }
    });

    if (!mCartItems.isEmpty()) {
      mButtonPurchase.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          StringBuilder purchaseHistBuilder = new StringBuilder();
          for (CartItem cartItem : mCartItems) { // for each item in cart
            mInStoreItem = mPetProDAO.getItemByName(cartItem.getName());
            // if items are available then purchase
            if (mInStoreItem != null && cartItem.getQuantity() <= mInStoreItem.getQuantity()) {
              mInStoreItem.setQuantity(mInStoreItem.getQuantity() - cartItem.getQuantity());
              mPetProDAO.update(mInStoreItem);
              mPetProDAO.delete(cartItem);
              // add to purchased items / purchase history???
              if (mPetProDAO.getPurchasedItemByUserIdAndName(mUserId, cartItem.getName()) == null) {
                PurchasedItem purchasedItem = new PurchasedItem(mUserId, cartItem.getName());
                mPetProDAO.insert(purchasedItem);
              }
              // build purchase history string
              purchaseHistBuilder.append(cartItem.getName()).append("\nQty: ")
                  .append(cartItem.getQuantity()).append(" | $").append(String.format(Locale.US,"%.2f", cartItem.getPrice())).append(" each\n\n");
            }
          }
          // add to purchaseHistory
          OrderLog orderLog = new OrderLog(mUserId, String.valueOf(purchaseHistBuilder), mTotal);
          mPetProDAO.insert(orderLog);
          Toast.makeText(getApplicationContext(), getString(R.string.thank_you), Toast.LENGTH_SHORT).show();
          Intent intent = LandingPageActivity.intentFactory(getApplicationContext(), mUserId);
          startActivity(intent);
        }
      });
    } else {
      mButtonPurchase.setClickable(false);
      mButtonPurchase.setBackgroundColor(getResources().getColor(R.color.gray));

      TextView textViewNoItemsInCart = findViewById(R.id.textViewNoItemsInCart);
      textViewNoItemsInCart.setVisibility(View.VISIBLE);
    }
  }

  private void setUpRecyclerView() {
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cartItemRecyclerView);

    mCartItems = new ArrayList<>();
    for (CartItem cartItem : mPetProDAO.getCartItemsByUserId(mUserId)) {
      mInStoreItem = mPetProDAO.getItemByName(cartItem.getName());
      if (mInStoreItem == null || mInStoreItem.getQuantity() < 1) {
        mPetProDAO.delete(cartItem); // remove from cart if unavailable
      } else {
        // constrain number in cart to number available
        if (cartItem.getQuantity() > mInStoreItem.getQuantity()) {
          cartItem.setQuantity(mInStoreItem.getQuantity());
        }
        // add to total cost and add item to cart
        mTotal += cartItem.getQuantity() * cartItem.getPrice();
        mCartItems.add(cartItem);
      }
    }

    CartItemsAdapter adapter = new CartItemsAdapter(mCartItems);

    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));


    adapter.setOnItemClickListener(new CartItemsAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(CartItem item) {
        mInStoreItem = mPetProDAO.getItemByName(item.getName());
        Intent intent = ItemActivity.intentFactory(CartActivity.this, mUserId, mInStoreItem.getItemId());
        startActivity(intent);
      }
    });

    adapter.setOnRemoveItemClickListener(new CartItemsAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(CartItem item) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CartActivity.this);

        alertBuilder.setMessage(getString(R.string.remove_from_cart));

        alertBuilder.setPositiveButton(getString(R.string.yes),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                // update total
                mTotal -= item.getPrice() * item.getQuantity();
                mTextViewTotal.setText(String.format(Locale.US,"%.2f", mTotal));

                //delete CartItem and update display
                mPetProDAO.delete(item);
                mCartItems.remove(item);
                adapter.notifyItemRemoved(adapter.getPosition());

                // indicate if cart is empty
                if (mCartItems.isEmpty()) {
                  mButtonPurchase.setClickable(false);
                  mButtonPurchase.setBackgroundColor(getResources().getColor(R.color.gray));
                  TextView textViewNoItemsInCart = findViewById(R.id.textViewNoItemsInCart);
                  textViewNoItemsInCart.setVisibility(View.VISIBLE);
                }
//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(getIntent());
              }
            });
        alertBuilder.setNegativeButton(getString(R.string.no),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                // do nothing
              }
            });
        alertBuilder.create().show();

      }
    });
  }

  private void getDatabase() {
    mPetProDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
        .allowMainThreadQueries()
        .build()
        .getPetProDAO();
  }

  public static Intent intentFactory(Context context, int userId) {
    Intent intent = new Intent(context, CartActivity.class);
    intent.putExtra(USER_ID_KEY, userId);
    return intent;
  }
}
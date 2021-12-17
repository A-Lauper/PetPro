package com.example.petpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.petpro.db.AppDatabase;
import com.example.petpro.db.OrderLog;
import com.example.petpro.db.PetProDAO;
import com.example.petpro.db.User;

import java.util.Locale;

/**
 * Title: PurchaseHistoryActivity.java
 * Abstract: Logic for PurchaseHistory page
 * Author: Arielle Lauper
 * Date: 9 - Dec - 2021
 * References: Class materials
 */

public class PurchaseHistoryActivity extends BaseActivity {

  private static final String USER_ID_KEY = "com.example.petpro.USER_ID_KEY";

  private PetProDAO mPetProDAO;

  private int mUserId;
  private User mUser;


  TextView mTextViewOrders;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_purchase_history);

    getDatabase();

    mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
    mUser = mPetProDAO.getUserByUserId(mUserId);

    wireUpDisplay();

    // print purchase hist
    StringBuilder purchaseHist = new StringBuilder();
    for (OrderLog log : mPetProDAO.getOrderLogByUserId(mUserId)) {
      purchaseHist.append("====================\n").append(log.getOrderString())
          .append("\n Total $").append(String.format(Locale.US,"%.2f", log.getTotal())).append("\n====================\n");
    }

    if (purchaseHist.length() <= 0) {
      purchaseHist.append(getString(R.string.no_order_hist));
    }
    mTextViewOrders.setText(purchaseHist.toString());
  }

  private void wireUpDisplay() {
    mTextViewOrders = findViewById(R.id.textViewPurchaseHistory);
    mTextViewOrders.setMovementMethod(new ScrollingMovementMethod());
  }

  private void getDatabase() {
    mPetProDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
        .allowMainThreadQueries()
        .build()
        .getPetProDAO();
  }

  public static Intent intentFactory(Context context, int userId) {
    Intent intent = new Intent(context, PurchaseHistoryActivity.class);
    intent.putExtra(USER_ID_KEY, userId);
    return intent;
  }
}
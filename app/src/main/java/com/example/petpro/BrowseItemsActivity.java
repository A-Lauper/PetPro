package com.example.petpro;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.petpro.db.AppDatabase;
import com.example.petpro.db.Item;
import com.example.petpro.db.PetProDAO;
import com.example.petpro.db.User;
import com.example.petpro.ui.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: BrowseItemsLoginActivity.java
 * Abstract: Logic for the browse items page
 * Author: Arielle Lauper
 * Date: 4 - Dec - 2021
 * References: Class materials
 */

public class BrowseItemsActivity extends BaseActivity {

  private static final String USER_ID_KEY = "com.example.petpro.USER_ID_KEY";

  private PetProDAO mPetProDAO;

  private int mUserId;

  private List<Item> mItems;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browse_items);

    mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
    getDatabase();

    // set up RecyclerView
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.itemRecyclerView);

    mItems = mPetProDAO.getAllItems();

    if (mItems.size() < 1) {
      TextView text = findViewById(R.id.textViewNoItems);
      text.setVisibility(View.VISIBLE);
    }

    ItemsAdapter adapter = new ItemsAdapter(mItems);

    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));


    // click items
    adapter.setOnItemClickListener(new ItemsAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(Item item) {
        Intent intent = ItemActivity.intentFactory(BrowseItemsActivity.this, mUserId, item.getItemId());
        startActivity(intent);
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
    Intent intent = new Intent(context, BrowseItemsActivity.class);
    intent.putExtra(USER_ID_KEY, userId);
    return intent;
  }
}
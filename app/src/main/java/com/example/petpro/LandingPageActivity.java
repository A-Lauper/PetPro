package com.example.petpro;

import androidx.appcompat.app.AlertDialog;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.petpro.db.AppDatabase;
import com.example.petpro.db.Item;
import com.example.petpro.db.PetProDAO;
import com.example.petpro.db.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: LandingPageActivity.java
 * Abstract: Logic for the landing page
 * Author: Arielle Lauper
 * Date: 2 - Dec - 2021
 * References: Class materials
 *             Autocomplete test: https://www.geeksforgeeks.org/android-auto-complete-textbox-and-how-to-create-it/
 */

public class LandingPageActivity extends BaseActivity {

  private static final String USER_ID_KEY = "com.example.petpro.USER_ID_KEY";

  private PetProDAO mPetProDAO;

  private int mUserId;
  private User mUser;

  private Button mButtonSearch;
  private Button mButtonBrowse;
  private Button mButtonBookGrooming;
  private Button mButtonAdmin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_landing_page);

    getDatabase();

    mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
    mUser = mPetProDAO.getUserByUserId(mUserId);

    wireUpDisplay();
  }

  private void wireUpDisplay() {

    mButtonSearch = findViewById(R.id.buttonSearch);
    mButtonBrowse = findViewById(R.id.buttonBrowseItems);
    mButtonBookGrooming = findViewById(R.id.buttonBookGrooming);
    mButtonAdmin = findViewById(R.id.buttonAdmin);


    mButtonBrowse.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = BrowseItemsActivity.intentFactory(getApplicationContext(), mUserId);
        startActivity(intent);
      }
    });

    mButtonSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        searchForm();
      }
    });

    mButtonBookGrooming.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = BookAppointmentActivity.intentFactory(getApplicationContext(), mUserId);
        startActivity(intent);
      }
    });

    // show the admin button for admin account
    if (mUser.isAdmin()) {
      mButtonAdmin.setVisibility(View.VISIBLE);
      mButtonAdmin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = AdminActivity.intentFactory(getApplicationContext());
          startActivity(intent);
        }
      });
    } else { // if not admin, hide admin button
      mButtonAdmin.setVisibility(View.INVISIBLE);
    }
  }

  private void searchForm() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LandingPageActivity.this);
    AlertDialog dialog;

    final View searchPopupView = getLayoutInflater().inflate(R.layout.search_popup, null);
    alertBuilder.setView(searchPopupView);

    // wire up form display
    AutoCompleteTextView editTextSearch = searchPopupView.findViewById(R.id.editTextSearch);
    Button buttonSearch = searchPopupView.findViewById(R.id.buttonConfirmSearch);
    Button buttonCancelSearch = searchPopupView.findViewById(R.id.buttonCancelSearch);

    // set up autocomplete text
    List<String> itemNames = new ArrayList<>();
    for (Item item : mPetProDAO.getAllItems()) {
      itemNames.add(item.getName());
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, itemNames);
    editTextSearch.setAdapter(adapter);

    dialog = alertBuilder.create();
    dialog.show();

    buttonSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // get values from dialog
        String itemNameString = editTextSearch.getText().toString().trim().toUpperCase();
        Item item = null;
        item = mPetProDAO.getItemByName(itemNameString);

        if (item != null) {
          // go to item page
          dialog.dismiss();
          Intent intent = ItemActivity.intentFactory(getApplicationContext(), mUserId,item.getItemId());
          startActivity(intent);
        } else {
          Toast toast = Toast.makeText(LandingPageActivity.this, "Could not find item", Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.CENTER,0,0);
          toast.show();
        }
      }
    });
    buttonCancelSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
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
    Intent intent = new Intent(context, LandingPageActivity.class);
    intent.putExtra(USER_ID_KEY, userId);
    return intent;
  }
}
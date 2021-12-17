package com.example.petpro;

import androidx.appcompat.app.AlertDialog;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petpro.db.AppDatabase;
import com.example.petpro.db.GroomingAppointment;
import com.example.petpro.db.PetProDAO;
import com.example.petpro.db.User;

import java.util.List;

/**
 * Title: ProfileActivity.java
 * Abstract: Logic for profile page
 * Author: Arielle Lauper
 * Date: 6 - Dec - 2021
 * References: Class materials
 *             Popups: https://www.youtube.com/watch?v=4GYKOzgQDWI
 */

public class ProfileActivity extends BaseActivity {

  private static final String USER_ID_KEY = "com.example.petpro.USER_ID_KEY";
  private static final String PREFERENCES_KEY = "com.example.petpro.PREFERENCES_KEY";

  private PetProDAO mPetProDAO;

  private int mUserId;
  private User mUser;

  private Button mButtonCart;
  private Button mButtonAppointments;
  private Button mButtonDelete;
  private Button mButtonBuyItAgain;
  private Button mButtonPurchaseHist;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    getDatabase();

    TextView username = findViewById(R.id.textViewProfileUsername);

    mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
    mUser = mPetProDAO.getUserByUserId(getIntent().getIntExtra(USER_ID_KEY, -1));
    username.setText(mUser.getUserName());

    wireUpDisplay();
  }

  private void wireUpDisplay() {
    mButtonDelete = findViewById(R.id.buttonDeleteUser);
    mButtonCart = findViewById(R.id.buttonProfileCart);
    mButtonAppointments = findViewById(R.id.buttonProfileAppointments);
    mButtonBuyItAgain = findViewById(R.id.buttonProfileBuyItAgain);
    mButtonPurchaseHist = findViewById(R.id.buttonProfilePurchaseHist);

    mButtonCart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = CartActivity.intentFactory(getApplicationContext(), mUserId);
        startActivity(intent);
      }
    });

    mButtonAppointments.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = MyAppointmentsActivity.intentFactory(getApplicationContext(), mUserId);
        startActivity(intent);
      }
    });

    mButtonDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteUseForm();
      }
    });

    mButtonBuyItAgain.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = BuyItAgainActivity.intentFactory(getApplicationContext(), mUserId);
        startActivity(intent);
      }
    });

    mButtonPurchaseHist.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = PurchaseHistoryActivity.intentFactory(getApplicationContext(), mUserId);
        startActivity(intent);
      }
    });
  }

  private boolean deleteUseForm() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ProfileActivity.this);
    AlertDialog dialog;

    final View deleteUserPopupView = getLayoutInflater().inflate(R.layout.delete_user_popup, null);
    alertBuilder.setView(deleteUserPopupView);

    // wire up form display
    EditText confirmUsernameEditText = deleteUserPopupView.findViewById(R.id.editTextConfirmDeleteUsername);
    EditText confirmPasswordEditText = deleteUserPopupView.findViewById(R.id.editTextConfirmDeletePassword);
    Button confirmDeleteUser = deleteUserPopupView.findViewById(R.id.buttonConfirmUserDelete);
    Button cancelDeleteUser = deleteUserPopupView.findViewById(R.id.buttonCancelUserDelete);

    dialog = alertBuilder.create();
    dialog.show();

    confirmDeleteUser.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // get values from dialog
        String confirmUsernameString = confirmUsernameEditText.getText().toString();
        String confirmPasswordString = confirmPasswordEditText.getText().toString();

        if (confirmUsernameString.equals(mUser.getUserName())
            && confirmPasswordString.equals(mUser.getPassword())) {
          // delete user info
          mPetProDAO.deleteCartItemsByUserId(mUserId);
          mPetProDAO.deletePurchasedItemsByUserId(mUserId);
          mPetProDAO.deleteOrderLogsByUserId(mUserId);

          // cancel appointments
          List<GroomingAppointment> appointments = mPetProDAO.getGroomingAppointmentsUserId(mUserId);
          for (GroomingAppointment appointment : appointments) {
            appointment.setUserId(-1);
            appointment.setBooked(false);
            mPetProDAO.update(appointment);
          }

          // log user out
          clearUserFromIntent();
          clearUserFromPref();
          // delete user
          mPetProDAO.delete(mUser);
          // send to main
          Toast.makeText(ProfileActivity.this, getString(R.string.account_deleted), Toast.LENGTH_SHORT).show();
          Intent intent = MainActivity.intentFactory(getApplicationContext());
          startActivity(intent);
        } else {
          Toast toast = Toast.makeText(ProfileActivity.this, getString(R.string.incorrect_imputes), Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.CENTER,0,0);
          toast.show();
        }
      }
    });

    cancelDeleteUser.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
    
    return true;
  }

  private void clearUserFromIntent() {
    getIntent().putExtra(USER_ID_KEY, -1);
  }

  private void clearUserFromPref() {
    if (mPreferences == null) {
      mPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }
    SharedPreferences.Editor editor = mPreferences.edit();
    editor.putInt(USER_ID_KEY, -1);
    editor.apply();
  }

  private void getDatabase() {
    mPetProDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
        .allowMainThreadQueries()
        .build()
        .getPetProDAO();
  }

  public static Intent intentFactory(Context context, int userId) {
    Intent intent = new Intent(context, ProfileActivity.class);
    intent.putExtra(USER_ID_KEY, userId);
    return intent;
  }
}
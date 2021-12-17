package com.example.petpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Title: BaseActivity.java
 * Abstract: Sets up menu bar logic
 * Author: Arielle Lauper
 * Date: 4 - Dec - 2021
 * References: Class materials
 *             https://www.youtube.com/watch?v=oh4YOj9VkVE
 *             https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
 */

public abstract class BaseActivity extends AppCompatActivity {

  private static final String USER_ID_KEY = "com.example.petpro.USER_ID_KEY";
  private static final String USER_NAME_KEY = "com.example.petpro.USER_NAME_KEY";
  private static final String PREFERENCES_KEY = "com.example.petpro.PREFERENCES_KEY";

  SharedPreferences mPreferences = null;

  private void logoutUser() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

    alertBuilder.setMessage(getString(R.string.logout));

    alertBuilder.setPositiveButton(getString(R.string.yes),
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            clearUserFromIntent();
            clearUserFromPref();
            Intent intent = MainActivity.intentFactory(getApplicationContext());
            startActivity(intent);
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.petpro_menu, menu);
    invalidateOptionsMenu();
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    if (mPreferences == null) {
      mPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }
    if (mPreferences != null) { // displaying username when logged in
      MenuItem item = menu.findItem(R.id.username);
      item.setTitle(mPreferences.getString(USER_NAME_KEY, getString(R.string.username)));
    }
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.pet_pro_home:
        Intent intent = LandingPageActivity.intentFactory(getApplicationContext(), mPreferences.getInt(USER_ID_KEY, -1));
        startActivity(intent);
        return true;
      case R.id.cart:
        intent = CartActivity.intentFactory(getApplicationContext(), mPreferences.getInt(USER_ID_KEY, -1));
        startActivity(intent);
        return true;
      case R.id.myAppointments:
        intent = MyAppointmentsActivity.intentFactory(getApplicationContext(), mPreferences.getInt(USER_ID_KEY, -1));
        startActivity(intent);
        return true;
      case R.id.buyItAgain:
        intent = BuyItAgainActivity.intentFactory(getApplicationContext(), mPreferences.getInt(USER_ID_KEY, -1));
        startActivity(intent);
        return true;
      case R.id.profile:
        goToProfile();
        return true;
      case R.id.purchaseHistory:
        intent = PurchaseHistoryActivity.intentFactory(getApplicationContext(), mPreferences.getInt(USER_ID_KEY, -1));
        startActivity(intent);
        return true;
      case R.id.logout:
        logoutUser();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void goToProfile() {

    Intent intent = ProfileActivity.intentFactory(getApplicationContext(), mPreferences.getInt(USER_ID_KEY, -1));
    startActivity(intent);
  }
}
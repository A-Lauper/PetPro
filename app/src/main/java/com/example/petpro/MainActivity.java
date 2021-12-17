package com.example.petpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.petpro.db.AppDatabase;
import com.example.petpro.db.GroomingAppointment;
import com.example.petpro.db.Item;
import com.example.petpro.db.PetProDAO;
import com.example.petpro.db.User;

/**
 * Title: MainActivity.java
 * Abstract: Logic for the main page
 * Author: Arielle Lauper
 * Date: 2 - Dec - 2021
 * References: Class materials
 *             Set app icon image: https://medium.com/hackmobile/how-to-change-your-android-app-icon-4cf6d1d326d8
 *             SharedPreferences: https://www.youtube.com/watch?v=jiD2fxn8iKA
 *             Edit themes to lowercase menu items: https://stackoverflow.com/questions/29579858/lowercase-option-menu-text-in-toolbar-android
 */

public class MainActivity extends AppCompatActivity {

  private static final String USER_ID_KEY = "com.example.petpro.USER_ID_KEY";
  private static final String PREFERENCES_KEY = "com.example.petpro.PREFERENCES_KEY";

  private static final boolean NOT_ADMIN = false;

  private Button mButtonLogin;
  private Button mButtonCreateAccount;

  private PetProDAO mPetProDAO;

  private int mUserId = -1;

  private SharedPreferences mPreferences = null;

  //private User mUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    getDatabase();

    checkForUser();

    wireUpDisplay();
  }

  private void checkForUser() {
    // do we have a user in the intent?
    mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
    if (mUserId != -1) {
      Intent intent = LandingPageActivity.intentFactory(this, mUserId);
      startActivity(intent);
      return;
    }
    // do we have a user in the preferences?
    if (mPreferences == null) {
      getPrefs();
    }

    mUserId = mPreferences.getInt(USER_ID_KEY, -1);

    if (mUserId != -1) {
      Intent intent = LandingPageActivity.intentFactory(this, mUserId);
      startActivity(intent);
      return;
    }
    // do we have a user at all?
    int userCount = mPetProDAO.getAllUsers().size();
    if (userCount <= 0) {
      // add default users
      User defaultUser = new User("testuser1", "testuser1", false);
      User defaultAdmin = new User("admin2", "admin2", true);
      mPetProDAO.insert(defaultUser, defaultAdmin);

      if (mPetProDAO.getAllItems().size() <= 0) {
        // add default items
        Item defaultItem1 = new Item("Dog Food", 15, 14);
        Item defaultItem2 = new Item("Cat Food", 10, 10);
        Item defaultItem3 = new Item("Dog Toy", 12, 7);
        Item defaultItem4 = new Item("Cat Toy", 7, 0);
        mPetProDAO.insert(defaultItem1, defaultItem2, defaultItem3, defaultItem4);
      }

      if (mPetProDAO.getAllGroomingAppointments().size() <= 0) {
        // add default aptts
        GroomingAppointment defaultAppointment1 = new GroomingAppointment(-1, "December 20th", "8:00am", "123 Street, CA. Local Store 00000", false);
        GroomingAppointment defaultAppointment2 = new GroomingAppointment(-1, "December 20th", "9:00am", "The Moon", false);
        mPetProDAO.insert(defaultAppointment1, defaultAppointment2);
      }
    }
  }

  private void getPrefs() {
    mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
  }

  private void wireUpDisplay() {
    mButtonLogin = findViewById(R.id.buttonMainLogin);
    mButtonCreateAccount = findViewById(R.id.buttonMainCreateAccount);

    mButtonLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = LoginActivity.intentFactory(getApplicationContext());
        startActivity(intent);
      }
    });

    mButtonCreateAccount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = CreateAccountActivity.intentFactory(getApplicationContext(), NOT_ADMIN);
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

  public static Intent intentFactory(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    //intent.putExtra(USER_ID_KEY, userId);
    return intent;
  }
}
package com.example.petpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.petpro.db.AppDatabase;
import com.example.petpro.db.PetProDAO;
import com.example.petpro.db.User;

/**
 * Title: LoginActivity.java
 * Abstract: Logic for the login page
 * Author: Arielle Lauper
 * Date: 2 - Dec - 2021
 * References: Class materials
 */

public class LoginActivity extends AppCompatActivity {

  private static final String USER_ID_KEY = "com.example.petpro.USER_ID_KEY";
  private static final String USER_NAME_KEY = "com.example.petpro.USER_NAME_KEY";
  private static final String PREFERENCES_KEY = "com.example.petpro.PREFERENCES_KEY";

  private EditText mUsernameEditText;
  private EditText mPasswordEditText;

  private String mUsernameString;
  private String mPasswordString;

  private Button mButton;

  private PetProDAO mPetProDAO;

  private User mUser;

  private SharedPreferences mPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    wireUpDisplay();

    getDatabase();
  }

  private void wireUpDisplay() {
    mUsernameEditText = findViewById(R.id.editTextConfirmLoginUsername);
    mPasswordEditText = findViewById(R.id.editTextConfirmLoginPassword);

    mButton = findViewById(R.id.buttonConfirmLogin);

    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getValuewFromDisplay();
        if (checkForUserInDatabase()) {
          if (!validatePassword()) {
            Toast toast = Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
          } else {
            if (mPreferences == null)
            {
              mPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
            }
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(USER_ID_KEY, mUser.getUserId());
            editor.putString(USER_NAME_KEY, mUser.getUserName());
            editor.apply();
            Toast.makeText(LoginActivity.this, "Welcome " + mUsernameString, Toast.LENGTH_SHORT).show();
            Intent intent = LandingPageActivity.intentFactory(getApplicationContext(), mUser.getUserId());
            startActivity(intent);
          }
        }
      }
    });
  }

  private void getValuewFromDisplay() {
    mUsernameString = mUsernameEditText.getText().toString();
    mPasswordString = mPasswordEditText.getText().toString();
  }

  private boolean checkForUserInDatabase() {
    mUser = mPetProDAO.getUserByUsername(mUsernameString);
    if (mUser == null) {
      Toast toast = Toast.makeText(this, "No user " + mUsernameString + " found", Toast.LENGTH_SHORT);
      toast.setGravity(Gravity.TOP,0,0);
      toast.show();
      return false;
    }
    return true;
  }

  private boolean validatePassword() {
    return mUser.getPassword().equals(mPasswordString);
  }

  private void getDatabase() {
    mPetProDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
        .allowMainThreadQueries()
        .build()
        .getPetProDAO();
  }

  public static Intent intentFactory(Context context) {
    Intent intent = new Intent(context, LoginActivity.class);
    return intent;
  }
}
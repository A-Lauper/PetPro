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

public class CreateAccountActivity extends AppCompatActivity {

  private static final String USER_ID_KEY = "com.example.petpro.USER_ID_KEY";
  private static final String USER_NAME_KEY = "com.example.petpro.USER_NAME_KEY";
  private static final String PREFERENCES_KEY = "com.example.petpro.PREFERENCES_KEY";
  private static final String IS_ADMIN_KEY = "com.example.petpro.IS_ADMIN_KEY";

  private EditText mUsernameEditText;
  private EditText mPasswordEditText;

  private String mUsernameString;
  private String mPasswordString;

  private Button mButton;

  private PetProDAO mPetProDAO;

  private User mUser;

  boolean mIsAdmin;

  private SharedPreferences mPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_account);

    wireUpDisplay();

    getDatabase();
  }

  private void wireUpDisplay() {
    mUsernameEditText = findViewById(R.id.editTextCreateAccountUsername);
    mPasswordEditText = findViewById(R.id.editTextCreateAccountPassword);

    mButton = findViewById(R.id.buttonCreateAccount);

    mIsAdmin = getIntent().getBooleanExtra(IS_ADMIN_KEY, false);

    if (mIsAdmin) {
      mButton.setText("Create Admin");
    }

    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getValuewFromDisplay();
        if (validateNewUserInDatabase()) {
          if (!validatePassword()) {
            Toast toast = Toast.makeText(CreateAccountActivity.this, "Password must have at least 5 characters", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
          } else {
            // insert new user
            mPetProDAO.insert(new User(mUsernameString, mPasswordString, mIsAdmin));

            if (!mIsAdmin) {
              // login as new user
              mUser = mPetProDAO.getUserByUsername(mUsernameString);
              if (mPreferences == null)
              {
                mPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
              }
              SharedPreferences.Editor editor = mPreferences.edit();
              editor.putInt(USER_ID_KEY, mUser.getUserId());
              editor.putString(USER_NAME_KEY, mUser.getUserName());
              editor.apply();

              Toast.makeText(CreateAccountActivity.this, "Welcome " + mUsernameString, Toast.LENGTH_SHORT).show();
              Intent intent = LandingPageActivity.intentFactory(getApplicationContext(), mUser.getUserId());
              startActivity(intent);
            } else {
              // stay logged in as current admin
              Toast toast = Toast.makeText(CreateAccountActivity.this, mUsernameString + " added", Toast.LENGTH_SHORT);
              toast.setGravity(Gravity.TOP,0,0);
              toast.show();
              Intent intent = AdminActivity.intentFactory(getApplicationContext());
              startActivity(intent);
            }
          }
        }
      }
    });
  }

  private void getValuewFromDisplay() {
    mUsernameString = mUsernameEditText.getText().toString().trim();
    mPasswordString = mPasswordEditText.getText().toString();
  }

  private boolean validateNewUserInDatabase() {
    mUser = mPetProDAO.getUserByUsername(mUsernameString);
    if (mUser != null) {
      Toast toast = Toast.makeText(this, "Username " + mUsernameString + " already taken", Toast.LENGTH_SHORT);
      toast.setGravity(Gravity.TOP,0,0);
      toast.show();
      mUsernameEditText.setText("");
      mPasswordEditText.setText("");
      return false;
    } else if (mUsernameString.length() < 5) {
      Toast toast = Toast.makeText(CreateAccountActivity.this, "Username must have at least 5 characters", Toast.LENGTH_SHORT);
      toast.setGravity(Gravity.TOP,0,0);
      toast.show();
      return false;
    }
    return true;
  }

  private boolean validatePassword() {
    return mPasswordString.length() >= 5;
  }

  private void getDatabase() {
    mPetProDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
        .allowMainThreadQueries()
        .build()
        .getPetProDAO();
  }

  public static Intent intentFactory(Context context, boolean isAdmin) {
    Intent intent = new Intent(context, CreateAccountActivity.class);
    intent.putExtra(IS_ADMIN_KEY, isAdmin);
    return intent;
  }
}
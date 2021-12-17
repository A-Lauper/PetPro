package com.example.petpro.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.petpro.db.AppDatabase;

/**
 * Title: User.java
 * Abstract: POJO for the user table
 * Author: Arielle Lauper
 * Date: 2 - Dec - 2021
 * References: Class materials
 */

@Entity(tableName = AppDatabase.USER_TABLE)
public class User {

  @PrimaryKey(autoGenerate = true)
  private int mUserId;

  private String mUserName;
  private String mPassword;

  private boolean mIsAdmin;

  public User(String userName, String password, boolean isAdmin) {
    mUserName = userName;
    mPassword = password;
    mIsAdmin = isAdmin;
  }

  public void setUserId(int userId) {
    mUserId = userId;
  }

  public int getUserId() {
    return mUserId;
  }

  public void setUserName(String userName) {
    mUserName = userName;
  }

  public String getUserName() {
    return mUserName;
  }

  public void setPassword(String password) {
    mPassword = password;
  }

  public String getPassword() {
    return mPassword;
  }

  public boolean isAdmin() {
    return mIsAdmin;
  }
}

package com.example.petpro.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Title: PurchasedItem.java
 * Abstract: POJO for the table holding previously purchased items
 * Author: Arielle Lauper
 * Date: 8 - Dec - 2021
 * References: Class materials
 */

@Entity(tableName = AppDatabase.PURCHASED_ITEM_TABLE)
public class PurchasedItem {

  @PrimaryKey(autoGenerate = true)
  private int mPurchasedItemKey;

  private int mUserId;
  private String mName;

  public PurchasedItem(int userId, String name) {
    mUserId = userId;
    mName = name;
  }

  public int getPurchasedItemKey() {
    return mPurchasedItemKey;
  }

  public void setPurchasedItemKey(int purchasedItemKey) {
    mPurchasedItemKey = purchasedItemKey;
  }

  public int getUserId() {
    return mUserId;
  }

  public void setUserId(int userId) {
    mUserId = userId;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name;
  }
}

package com.example.petpro.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.petpro.db.AppDatabase;

/**
 * Title: User.java
 * Abstract: POJO for the item table
 * Author: Arielle Lauper
 * Date: 4 - Dec - 2021
 * References: Class materials
 */

@Entity(tableName = AppDatabase.ITEM_TABLE)
public class Item {

  @PrimaryKey(autoGenerate = true)
  private int mItemId;

  private String mName;
  private double mPrice;
  private int mQuantity;

  public Item(String name, double price, int quantity) {
    mName = name.toUpperCase();
    mPrice = price;
    mQuantity = quantity;
  }

  public int getItemId() {
    return mItemId;
  }

  public void setItemId(int itemId) {
    mItemId = itemId;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name.toUpperCase();
  }

  public double getPrice() {
    return mPrice;
  }

  public void setPrice(double price) {
    mPrice = price;
  }

  public int getQuantity() {
    return mQuantity;
  }

  public void setQuantity(int quantity) {
    mQuantity = quantity;
  }
}

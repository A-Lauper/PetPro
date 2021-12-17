package com.example.petpro.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Title: CartItem.java
 * Abstract: POJO for the CartItem table
 * Author: Arielle Lauper
 * Date: 7 - Dec - 2021
 * References: Class materials
 */

@Entity(tableName = AppDatabase.CART_ITEM_TABLE)
public class CartItem {

  @PrimaryKey(autoGenerate = true)
  private int mCartItemId;

  private int mUserId;

  private String mName;
  private double mPrice;
  private int mQuantity;

  public CartItem(int userId, String name, double price, int quantity) {
    mUserId = userId;
    mName = name;
    mPrice = price;
    mQuantity = quantity;
  }

  public int getCartItemId() {
    return mCartItemId;
  }

  public void setCartItemId(int cartItemId) {
    mCartItemId = cartItemId;
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

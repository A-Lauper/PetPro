package com.example.petpro.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Title: OrderLog.java
 * Abstract: POJO for the OrderLog table
 * Author: Arielle Lauper
 * Date: 9 - Dec - 2021
 * References: Class materials
 */

@Entity(tableName = AppDatabase.ORDER_LOG_TABLE)
public class OrderLog {

  @PrimaryKey(autoGenerate = true)
  private int mOrderLogId;

  private int mUserId;

  private String mOrderString;
  private double mTotal;

  public OrderLog(int userId, String orderString, double total) {
    mUserId = userId;
    mOrderString = orderString;
    mTotal = total;
  }

  public int getOrderLogId() {
    return mOrderLogId;
  }

  public void setOrderLogId(int orderLogId) {
    mOrderLogId = orderLogId;
  }

  public int getUserId() {
    return mUserId;
  }

  public void setUserId(int userId) {
    mUserId = userId;
  }

  public String getOrderString() {
    return mOrderString;
  }

  public void setOrderString(String orderString) {
    mOrderString = orderString;
  }

  public double getTotal() {
    return mTotal;
  }

  public void setTotal(double total) {
    mTotal = total;
  }
}

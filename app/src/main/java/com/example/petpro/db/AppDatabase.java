package com.example.petpro.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Title: AppDatabase.java
 * Abstract: Database for PetPro
 * Author: Arielle Lauper
 * Date: 2 - Dec - 2021
 * References: Class materials
 */

@Database(entities = {User.class, Item.class, CartItem.class, PurchasedItem.class, OrderLog.class, GroomingAppointment.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
  public static final String DB_NAME = "PETPRO_DATABASE";
  public static final String USER_TABLE = "USER_TABLE";
  public static final String ITEM_TABLE = "ITEM_TABLE";
  public static final String CART_ITEM_TABLE = "CART_ITEM_TABLE";
  public static final String PURCHASED_ITEM_TABLE = "PURCHASED_ITEM_TABLE";
  public static final String ORDER_LOG_TABLE = "ORDER_LOG_TABLE";
  public static final String GROOMING_APPOINTMENT_TABLE = "GROOMING_APPOINTMENT_TABLE";

  public abstract PetProDAO getPetProDAO();
}

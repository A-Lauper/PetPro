package com.example.petpro.db;

/**
 * Title: PetProDAO.java
 * Abstract: DAO for all tables
 * Author: Arielle Lauper
 * Date: 2 - Dec - 2021
 * References: Class materials
 */


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PetProDAO {

  // User
  @Insert
  void insert(User... users);

  @Update
  void update(User... users);

  @Delete
  void delete(User user);

  @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
  List<User> getAllUsers();

  @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserName = :username")
  User getUserByUsername(String username);

  @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserId = :userId")
  User getUserByUserId(int userId);

  // Item
  @Insert
  void insert(Item... items);

  @Update
  void update(Item... items);

  @Delete
  void delete(Item item);

  @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " ORDER BY mName ASC")
  List<Item> getAllItems();

  @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " WHERE mName = :name")
  Item getItemByName(String name);

  @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " WHERE mItemId = :itemId")
  Item getItemByItemId(int itemId);

  // CartItem
  @Insert
  void insert(CartItem... cartItems);

  @Update
  void update(CartItem... cartItems);

  @Delete
  void delete(CartItem cartItem);

  @Query("Delete FROM " + AppDatabase.CART_ITEM_TABLE + " WHERE mUserId = :userId")
  void deleteCartItemsByUserId(int userId);

  @Query("SELECT * FROM " + AppDatabase.CART_ITEM_TABLE + " WHERE mUserId = :userId ORDER BY mName ASC")
  List<CartItem> getCartItemsByUserId(int userId);

  @Query("SELECT * FROM " + AppDatabase.CART_ITEM_TABLE + " WHERE mCartItemId = :CartItemId")
  CartItem getCartItemByCartItemId(int CartItemId);

  @Query("SELECT * FROM " + AppDatabase.CART_ITEM_TABLE + " WHERE mUserId = :userId AND mName = :name")
  CartItem getCartItemByUserIdAndName(int userId, String name);

  // PurchasedItem
  @Insert
  void insert(PurchasedItem... purchasedItems);

  @Update
  void update(PurchasedItem... purchasedItems);

  @Delete
  void delete(PurchasedItem purchasedItem);

  @Query("Delete FROM " + AppDatabase.PURCHASED_ITEM_TABLE + " WHERE mUserId = :userId")
  void deletePurchasedItemsByUserId(int userId);

  @Query("Delete FROM " + AppDatabase.PURCHASED_ITEM_TABLE + " WHERE mName = :name")
  void deletePurchasedItemsByName(String name);

  @Query("SELECT * FROM " + AppDatabase.PURCHASED_ITEM_TABLE + " WHERE mName = :name")
  List<PurchasedItem> getPurchasedItemsByName(String name);

  @Query("SELECT * FROM " + AppDatabase.PURCHASED_ITEM_TABLE + " WHERE mUserId = :userId ORDER BY mName ASC")
  List<PurchasedItem> getPurchasedItemsByUserId(int userId);

  @Query("SELECT * FROM " + AppDatabase.PURCHASED_ITEM_TABLE + " WHERE mUserId = :userId AND mName = :name")
  PurchasedItem getPurchasedItemByUserIdAndName(int userId, String name);

  // OrderLog
  @Insert
  void insert(OrderLog... orderLogs);

  @Update
  void update(OrderLog... orderLogs);

  @Delete
  void delete(OrderLog orderLog);

  @Query("Delete FROM " + AppDatabase.ORDER_LOG_TABLE + " WHERE mUserId = :userId")
  void deleteOrderLogsByUserId(int userId);

  @Query("SELECT * FROM " + AppDatabase.ORDER_LOG_TABLE + " WHERE mUserId = :userId")
  List<OrderLog> getOrderLogByUserId(int userId);

  // GroomingAppointment
  @Insert
  void insert(GroomingAppointment... groomingAppointments);

  @Update
  void update(GroomingAppointment... groomingAppointments);

  @Delete
  void delete(GroomingAppointment groomingAppointment);

  @Query("SELECT * FROM " + AppDatabase.GROOMING_APPOINTMENT_TABLE)
  List<GroomingAppointment> getAllGroomingAppointments();

  @Query("SELECT * FROM " + AppDatabase.GROOMING_APPOINTMENT_TABLE + " WHERE mIsBooked = :isBooked AND mUserId = :userId")
  List<GroomingAppointment> getGroomingAppointmentsByBookedAndUserId(boolean isBooked, int userId);

  @Query("SELECT * FROM " + AppDatabase.GROOMING_APPOINTMENT_TABLE + " WHERE mUserId = :userId")
  List<GroomingAppointment> getGroomingAppointmentsUserId(int userId);

  @Query("SELECT * FROM " + AppDatabase.GROOMING_APPOINTMENT_TABLE + " WHERE mIsBooked = :isBooked")
  List<GroomingAppointment> getGroomingAppointmentsByBooked(boolean isBooked);

  @Query("SELECT * FROM " + AppDatabase.GROOMING_APPOINTMENT_TABLE + " WHERE mDate = :date AND mTime = :time AND mLocation = :location")
  GroomingAppointment getGroomingAppointmentByDateTimeAndLocation(String date, String time, String location);
}

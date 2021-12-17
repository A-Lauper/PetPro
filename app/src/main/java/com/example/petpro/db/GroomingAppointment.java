package com.example.petpro.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Title: GroomingAppointment.java
 * Abstract: POJO for the GroomingAppointment table
 * Author: Arielle Lauper
 * Date: 10 - Dec - 2021
 * References: Class materials
 */

@Entity(tableName = AppDatabase.GROOMING_APPOINTMENT_TABLE)
public class GroomingAppointment {

  @PrimaryKey(autoGenerate = true)
  private int mGroomingAppointmentId;

  private int mUserId;

  private String mDate;
  private String mTime;
  private String mLocation;

  private boolean mIsBooked;

  public GroomingAppointment(int userId, String date, String time, String location, boolean isBooked) {
    mUserId = userId;
    mDate = date;
    mTime = time;
    mLocation = location;
    mIsBooked = isBooked;
  }

  public int getGroomingAppointmentId() {
    return mGroomingAppointmentId;
  }

  public void setGroomingAppointmentId(int groomingAppointmentId) {
    mGroomingAppointmentId = groomingAppointmentId;
  }

  public int getUserId() {
    return mUserId;
  }

  public void setUserId(int userId) {
    mUserId = userId;
  }

  public String getDate() {
    return mDate;
  }

  public void setDate(String date) {
    mDate = date;
  }

  public String getTime() {
    return mTime;
  }

  public void setTime(String time) {
    mTime = time;
  }

  public String getLocation() {
    return mLocation;
  }

  public void setLocation(String location) {
    mLocation = location;
  }

  public boolean isBooked() {
    return mIsBooked;
  }

  public void setBooked(boolean booked) {
    mIsBooked = booked;
  }
}

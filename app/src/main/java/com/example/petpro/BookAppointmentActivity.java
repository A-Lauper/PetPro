package com.example.petpro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petpro.db.AppDatabase;
import com.example.petpro.db.GroomingAppointment;
import com.example.petpro.db.Item;
import com.example.petpro.db.PetProDAO;
import com.example.petpro.db.User;
import com.example.petpro.ui.AppointmentsAdapter;
import com.example.petpro.ui.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: BookAppointmentActivity.java
 * Abstract: Logic for BookAppointment page
 * Author: Arielle Lauper
 * Date: 10 - Dec - 2021
 * References: Class materials
 *             Refresh with no animation: https://stackoverflow.com/questions/3053761/reload-activity-in-android
 */

public class BookAppointmentActivity extends BaseActivity {

  private static final String USER_ID_KEY = "com.example.petpro.USER_ID_KEY";

  private PetProDAO mPetProDAO;

  private int mUserId;

  private List<GroomingAppointment> mAppointments;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_book_appointment);

    mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
    getDatabase();


    // set up RecyclerView
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.appointmentRecyclerView);

    mAppointments = mPetProDAO.getGroomingAppointmentsByBooked(false);

    if (mAppointments.size() < 1) {
      TextView text = findViewById(R.id.textViewNoAppointments);
      text.setVisibility(View.VISIBLE);
    }

    AppointmentsAdapter adapter = new AppointmentsAdapter(mAppointments);

    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // click items
    adapter.setOnItemClickListener(new AppointmentsAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(GroomingAppointment appointment) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(BookAppointmentActivity.this);

        alertBuilder.setMessage(getString(R.string.book_appointment));

        alertBuilder.setPositiveButton(getString(R.string.yes),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                // book appointment and update display
                appointment.setUserId(mUserId);
                appointment.setBooked(true);
                mPetProDAO.update(appointment);

                // update display
                mAppointments.remove(appointment);
                adapter.notifyItemRemoved(adapter.getPosition());

                // indicate if no appointments
                if (mAppointments.size() < 1) {
                  TextView text = findViewById(R.id.textViewNoAppointments);
                  text.setVisibility(View.VISIBLE);
                }
//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(getIntent());
                Toast.makeText(BookAppointmentActivity.this, getString(R.string.appointment_booked), Toast.LENGTH_SHORT).show();
              }
            });
        alertBuilder.setNegativeButton(getString(R.string.no),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                // do nothing
              }
            });
        alertBuilder.create().show();
      }
    });
  }

  private void getDatabase() {
    mPetProDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
        .allowMainThreadQueries()
        .build()
        .getPetProDAO();
  }

  public static Intent intentFactory(Context context, int userId) {
    Intent intent = new Intent(context, BookAppointmentActivity.class);
    intent.putExtra(USER_ID_KEY, userId);
    return intent;
  }
}
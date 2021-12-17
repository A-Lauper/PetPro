package com.example.petpro;

import androidx.appcompat.app.AlertDialog;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.petpro.db.AppDatabase;
import com.example.petpro.db.GroomingAppointment;
import com.example.petpro.db.Item;
import com.example.petpro.db.PetProDAO;
import com.example.petpro.db.PurchasedItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: AdminActivity.java
 * Abstract: Logic for the admin page
 * Author: Arielle Lauper
 * Date: 6 - Dec - 2021
 * References: Class materials
 * Popups: https://www.youtube.com/watch?v=4GYKOzgQDWI
 *         Autocomplete test: https://www.geeksforgeeks.org/android-auto-complete-textbox-and-how-to-create-it/
 */

public class AdminActivity extends BaseActivity {

  private static final boolean IS_ADMIN = true;

  private PetProDAO mPetProDAO;

  Button mAddAdmin;
  Button mButtonAdd;
  Button mButtonEdit;
  Button mButtonDelete;
  Button mButtonGroomingAvailability;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);

    getDatabase();

    wireUpDisplay();
  }

  private void wireUpDisplay() {
    mAddAdmin = findViewById(R.id.buttonAddAdminAccount);
    mButtonAdd = findViewById(R.id.buttonAdminAddItem);
    mButtonEdit = findViewById(R.id.buttonAdminEditItem);
    mButtonDelete = findViewById(R.id.buttonAdminDeleteItem);
    mButtonGroomingAvailability = findViewById((R.id.buttonGroomingAvailability));

    mAddAdmin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = CreateAccountActivity.intentFactory(getApplicationContext(), IS_ADMIN);
        startActivity(intent);
      }
    });

    mButtonAdd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        addItemForm();
      }
    });

    mButtonEdit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        searchForm();
      }
    });

    mButtonDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteItemForm();
      }
    });

    mButtonGroomingAvailability.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        manageGroomingForm();
      }
    });
  }

  private void addItemForm() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminActivity.this);
    AlertDialog dialog;

    final View addItemPopupView = getLayoutInflater().inflate(R.layout.add_edit_item_popup, null);
    alertBuilder.setView(addItemPopupView);

    // wire up form display
    EditText itemToAddName = addItemPopupView.findViewById(R.id.editTextAddItemName);
    EditText itemToAddPrice = addItemPopupView.findViewById(R.id.editTextAddItemPrice);
    EditText itemToAddQty = addItemPopupView.findViewById(R.id.editTextAddItemQty);
    Button confirmAddItem = addItemPopupView.findViewById(R.id.buttonConfirmAddItem);
    Button cancelAddItem = addItemPopupView.findViewById(R.id.buttonCancelAddItem);

    dialog = alertBuilder.create();
    dialog.show();

    confirmAddItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        // get values from dialog
        String newItemNameString = itemToAddName.getText().toString().trim().toUpperCase();
        String newItemPriceString = itemToAddPrice.getText().toString();
        String newItemQtyString = itemToAddQty.getText().toString();

        if (newItemNameString.trim().isEmpty()
            || newItemPriceString.trim().isEmpty()
            || newItemQtyString.trim().isEmpty()) {
          Toast toast = Toast.makeText(AdminActivity.this, getString(R.string.add_all_item_info_warning), Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.CENTER,0,0);
          toast.show();
        } else {
          Item item = null;
          item = mPetProDAO.getItemByName(newItemNameString);

          if (item == null) {
            // add item
            item = new Item(newItemNameString, Double.parseDouble(newItemPriceString), Integer.parseInt(newItemQtyString));
            mPetProDAO.insert(item);
            dialog.dismiss();
            Toast.makeText(AdminActivity.this, getString(R.string.item_posted), Toast.LENGTH_SHORT).show();
          } else {
            Toast toast = Toast.makeText(AdminActivity.this, getString(R.string.item_already_exists), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
          }
        }
      }
    });
    cancelAddItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }

  private void searchForm() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminActivity.this);
    AlertDialog dialog;

    final View searchPopupView = getLayoutInflater().inflate(R.layout.search_popup, null);
    alertBuilder.setView(searchPopupView);

    // wire up form display
    AutoCompleteTextView editTextSearch = searchPopupView.findViewById(R.id.editTextSearch);
    Button buttonSearch = searchPopupView.findViewById(R.id.buttonConfirmSearch);
    Button buttonCancelSearch = searchPopupView.findViewById(R.id.buttonCancelSearch);

    // set up autocomplete text
    List<String> itemNames = new ArrayList<>();
    for (Item item : mPetProDAO.getAllItems()) {
      itemNames.add(item.getName());
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, itemNames);
    editTextSearch.setAdapter(adapter);

    dialog = alertBuilder.create();
    dialog.show();

    buttonSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // get values from dialog
        String itemNameString = editTextSearch.getText().toString().trim().toUpperCase();
        Item item = null;
        item = mPetProDAO.getItemByName(itemNameString);

        if (item != null) {
          // go to edit item popup
          dialog.dismiss();
          editItemForm(item);
        } else {
          Toast toast = Toast.makeText(AdminActivity.this, getString(R.string.could_note_find_item), Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.CENTER,0,0);
          toast.show();
        }
      }
    });
    buttonCancelSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }

  private void editItemForm(Item item) {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminActivity.this);
    AlertDialog dialog;

    final View editItemPopupView = getLayoutInflater().inflate(R.layout.add_edit_item_popup, null);
    alertBuilder.setView(editItemPopupView);

    int itemId = item.getItemId();
    String oldItemName = item.getName();

    // wire up form display
    EditText itemToEditName = editItemPopupView.findViewById(R.id.editTextAddItemName);
    EditText itemToEditPrice = editItemPopupView.findViewById(R.id.editTextAddItemPrice);
    EditText itemToEditQty = editItemPopupView.findViewById(R.id.editTextAddItemQty);
    Button confirmEditItem = editItemPopupView.findViewById(R.id.buttonConfirmAddItem);
    Button cancelEditItem = editItemPopupView.findViewById(R.id.buttonCancelAddItem);

    itemToEditName.setText(item.getName());
    itemToEditPrice.setText(String.valueOf(item.getPrice()));
    itemToEditQty.setText((String.valueOf(item.getQuantity())));

    confirmEditItem.setText(R.string.edit_item);

    dialog = alertBuilder.create();
    dialog.show();

    confirmEditItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // get values from dialog
        String newItemNameString = itemToEditName.getText().toString().trim().toUpperCase();
        String newItemPriceString = itemToEditPrice.getText().toString();
        String newItemQtyString = itemToEditQty.getText().toString();

        // to char for duplicate item names
        Item itemWithNewName = mPetProDAO.getItemByName(newItemNameString);

        if (newItemNameString.trim().isEmpty()
            || newItemPriceString.trim().isEmpty()
            || newItemQtyString.trim().isEmpty()) {
          Toast toast = Toast.makeText(AdminActivity.this, getString(R.string.add_all_item_info_warning), Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.CENTER,0,0);
          toast.show();
        } else if (itemWithNewName != null && itemWithNewName.getItemId() != item.getItemId()) {
          Toast toast = Toast.makeText(AdminActivity.this, getString(R.string.item_name_taken), Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.CENTER,0,0);
          toast.show();
        } else {
          // get item to be edited
          Item item = mPetProDAO.getItemByItemId(itemId);

          // set new values
          item.setName(newItemNameString);
          item.setPrice(Double.parseDouble(newItemPriceString));
          item.setQuantity(Integer.parseInt(newItemQtyString));

          // update item
          mPetProDAO.update(item);

          // update "buy it again" items
          for (PurchasedItem purchasedItem : mPetProDAO.getPurchasedItemsByName(oldItemName)) {
            purchasedItem.setName(item.getName());
            mPetProDAO.update(purchasedItem);
          }

          dialog.dismiss();

          Toast.makeText(AdminActivity.this, getString(R.string.item_updated), Toast.LENGTH_SHORT).show();
        }
      }
    });
    cancelEditItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }

  private void deleteItemForm() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminActivity.this);
    AlertDialog dialog;

    final View deleteItemPopupView = getLayoutInflater().inflate(R.layout.delete_item_popup, null);
    alertBuilder.setView(deleteItemPopupView);

    // wire up form display
    AutoCompleteTextView itemToDelete = deleteItemPopupView.findViewById(R.id.editTextConfirmDeleteItem);
    Button confirmDeleteItem = deleteItemPopupView.findViewById(R.id.buttonConfirmItemDelete);
    Button cancelDeleteItem = deleteItemPopupView.findViewById(R.id.buttonCancelItemDelete);

    // set up autocomplete text
    List<String> itemNames = new ArrayList<>();
    for (Item item : mPetProDAO.getAllItems()) {
      itemNames.add(item.getName());
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, itemNames);
    itemToDelete.setAdapter(adapter);

    dialog = alertBuilder.create();
    dialog.show();

    confirmDeleteItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // get values from dialog
        String confirmItemNameString = itemToDelete.getText().toString().trim().toUpperCase();
        Item item = null;
        item = mPetProDAO.getItemByName(confirmItemNameString);

        if (item != null) {
          // delete item from store
          mPetProDAO.delete(item);
          // delete item from all buy it again
          mPetProDAO.deletePurchasedItemsByName(item.getName());
          dialog.dismiss();
          Toast.makeText(AdminActivity.this, getString(R.string.item_removed), Toast.LENGTH_SHORT).show();
        } else {
          Toast toast = Toast.makeText(AdminActivity.this, getString(R.string.item_not_for_sale), Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.CENTER,0,0);
          toast.show();
        }
      }
    });
    cancelDeleteItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }

  private void manageGroomingForm() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminActivity.this);
    AlertDialog dialog;

    final View manageGroomingView = getLayoutInflater().inflate(R.layout.grooming_availability_popup, null);
    alertBuilder.setView(manageGroomingView);

    // wire up form display
    Button buttonAddAppt = manageGroomingView.findViewById(R.id.buttonChooseAddAppt);
    Button buttonRemoveAppt = manageGroomingView.findViewById(R.id.buttonChooseRemoveAppt);
    Button buttonCancelAddRemoveAppt = manageGroomingView.findViewById(R.id.buttonCancelAddRemoveAppt);

    dialog = alertBuilder.create();
    dialog.show();

    buttonAddAppt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        addApptForm();
        dialog.dismiss();
      }
    });

    buttonRemoveAppt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        removeApptForm();
        dialog.dismiss();
      }
    });

    buttonCancelAddRemoveAppt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }

  private void addApptForm() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminActivity.this);
    AlertDialog dialog;

    final View addItemPopupView = getLayoutInflater().inflate(R.layout.add_remove_grooming_appointment_popup, null);
    alertBuilder.setView(addItemPopupView);

    // wire up form display
    EditText editTextApptDate = addItemPopupView.findViewById(R.id.editTextGroomingApttDate);
    EditText editTextApptTime = addItemPopupView.findViewById(R.id.editTextGroomingApttTime);
    EditText editTextApptLocation = addItemPopupView.findViewById(R.id.editTextGroomingApttLocation);
    Button buttonConfirmAddAppt = addItemPopupView.findViewById(R.id.buttonConfirmAddGroomingappt);
    Button buttonCancelAddAppt = addItemPopupView.findViewById(R.id.buttonCancelAddGroomingappt);

    dialog = alertBuilder.create();
    dialog.show();

    buttonConfirmAddAppt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // get values from dialog
        String newApptDateString = editTextApptDate.getText().toString().trim();
        String newApptTimeString = editTextApptTime.getText().toString().trim();
        String newApptLocationString = editTextApptLocation.getText().toString().trim();

        if (newApptDateString.trim().isEmpty()
            || newApptTimeString.trim().isEmpty()
            || newApptLocationString.trim().isEmpty()) {
          Toast toast = Toast.makeText(AdminActivity.this, getString(R.string.add_all_aptt_info_warning), Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.CENTER,0,0);
          toast.show();
        } else {
          GroomingAppointment newAppt = null;
          newAppt = mPetProDAO.getGroomingAppointmentByDateTimeAndLocation(newApptDateString, newApptTimeString, newApptLocationString);
          if (newAppt == null) {
            newAppt = new GroomingAppointment(-1, newApptDateString, newApptTimeString, newApptLocationString, false);
            mPetProDAO.insert(newAppt);
            dialog.dismiss();
            Toast.makeText(AdminActivity.this, getString(R.string.aptt_created), Toast.LENGTH_SHORT).show();
          } else {
            Toast toast = Toast.makeText(AdminActivity.this, getString(R.string.aptt_already_exists), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
          }
        }
      }
    });
    buttonCancelAddAppt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }

  private void removeApptForm() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminActivity.this);
    AlertDialog dialog;

    final View addItemPopupView = getLayoutInflater().inflate(R.layout.add_remove_grooming_appointment_popup, null);
    alertBuilder.setView(addItemPopupView);

    // wire up form display
    AutoCompleteTextView editTextApptDate = addItemPopupView.findViewById(R.id.editTextGroomingApttDate);
    AutoCompleteTextView editTextApptTime = addItemPopupView.findViewById(R.id.editTextGroomingApttTime);
    AutoCompleteTextView editTextApptLocation = addItemPopupView.findViewById(R.id.editTextGroomingApttLocation);
    Button buttonConfirmAddAppt = addItemPopupView.findViewById(R.id.buttonConfirmAddGroomingappt);
    Button buttonCancelAddAppt = addItemPopupView.findViewById(R.id.buttonCancelAddGroomingappt);

    // set up autocomplete text
    List<String> apttDates = new ArrayList<>();
    List<String> apttTimes = new ArrayList<>();
    List<String> apttLocations = new ArrayList<>();
    for (GroomingAppointment appointment : mPetProDAO.getAllGroomingAppointments()) {
      if (!apttDates.contains(appointment.getDate())) {
        apttDates.add(appointment.getDate());
      }
      if (!apttTimes.contains(appointment.getTime())) {
        apttTimes.add(appointment.getTime());
      }
      if (!apttLocations.contains(appointment.getLocation())) {
        apttLocations.add(appointment.getLocation());
      }
    }
    ArrayAdapter<String> adapterDate = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, apttDates);
    ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, apttTimes);
    ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, apttLocations);
    editTextApptDate.setAdapter(adapterDate);
    editTextApptTime.setAdapter(adapterTime);
    editTextApptLocation.setAdapter(adapterLocation);

    buttonConfirmAddAppt.setText(getString(R.string.remove_aptt));

    dialog = alertBuilder.create();
    dialog.show();

    buttonConfirmAddAppt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // get values from dialog
        String confirmApptDateString = editTextApptDate.getText().toString().trim();
        String confirmApptTimeString = editTextApptTime.getText().toString().trim();
        String confirmApptLocaitonString = editTextApptLocation.getText().toString().trim();
        GroomingAppointment appointment = null;
        appointment = mPetProDAO.getGroomingAppointmentByDateTimeAndLocation(confirmApptDateString, confirmApptTimeString, confirmApptLocaitonString);

        if (appointment != null) {
          // delete item from store
          mPetProDAO.delete(appointment);
          dialog.dismiss();
          Toast.makeText(AdminActivity.this, getString(R.string.aptt_removed), Toast.LENGTH_SHORT).show();
        } else {
          Toast toast = Toast.makeText(AdminActivity.this, getString(R.string.np_aptt_exists), Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.CENTER,0,0);
          toast.show();
        }
      }
    });
    buttonCancelAddAppt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }

  private void getDatabase() {
    mPetProDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
        .allowMainThreadQueries()
        .build()
        .getPetProDAO();
  }

  public static Intent intentFactory(Context context) {
    Intent intent = new Intent(context, AdminActivity.class);
    return intent;
  }
}
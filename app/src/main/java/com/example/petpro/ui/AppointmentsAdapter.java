package com.example.petpro.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petpro.MyAppointmentsActivity;
import com.example.petpro.R;
import com.example.petpro.db.GroomingAppointment;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: AppointmentsAdapter.java
 * Abstract: Sets up the adaptor for the RecyclerView
 * Author: Arielle Lauper
 * Date: 10 - Dec - 2021
 * References: Class materials
 * All videos in playlist: https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-1-introduction
 * RecyclerView: https://guides.codepath.com/android/using-the-recyclerview
 */

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>{

  private AppointmentsAdapter.OnItemClickListener mListener;

  private List<GroomingAppointment> mAppointments = new ArrayList<>();

  private Context mContext;

  private int mPosition;

  public AppointmentsAdapter(List<GroomingAppointment> appointments) {
    mAppointments = appointments;
  }

  @NonNull
  @Override
  public AppointmentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(mContext);

    View itemsView;

    itemsView = inflater.inflate(R.layout.appointment_layout, parent, false);

    return new AppointmentsAdapter.ViewHolder(itemsView);
  }

  @Override
  public void onBindViewHolder(@NonNull AppointmentsAdapter.ViewHolder holder, int position) {
    GroomingAppointment currentAppointment = mAppointments.get(position);
    holder.mTextViewAppointmentDate.setText(currentAppointment.getDate());
    holder.mTextViewAppointmentTime.setText(currentAppointment.getTime());
    holder.mTextViewAppointmentLocation.setText(currentAppointment.getLocation());
  }

  @Override
  public int getItemCount() {
    return mAppointments.size();
  }

  public int getPosition() {
    return mPosition;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    private TextView mTextViewAppointmentDate;
    private TextView mTextViewAppointmentTime;
    private TextView mTextViewAppointmentLocation;
    private TextView mTextViewAppointmentCancel;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      mTextViewAppointmentDate = itemView.findViewById(R.id.textViewAppointmentDate);
      mTextViewAppointmentTime = itemView.findViewById(R.id.textViewAppointmentTime);
      mTextViewAppointmentLocation = itemView.findViewById(R.id.textViewAppointmentLocation);
      mTextViewAppointmentCancel = itemView.findViewById(R.id.textViewAppointmentCancel);
      if (mContext.getClass() == MyAppointmentsActivity.class) {
        mTextViewAppointmentCancel.setVisibility(View.VISIBLE);
      }

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int position = getAdapterPosition();
          if (mListener != null && position != RecyclerView.NO_POSITION) {
            mListener.onItemClick(mAppointments.get(position));
            mPosition = position;
          }
        }
      });
    }
  }

  public interface OnItemClickListener {
    void onItemClick(GroomingAppointment item);
  }

  public void setOnItemClickListener(AppointmentsAdapter.OnItemClickListener listener) {
    mListener = listener;
  }
}

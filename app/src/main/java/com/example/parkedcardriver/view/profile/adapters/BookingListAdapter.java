package com.example.parkedcardriver.view.profile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkedcardriver.Model.data.Booking;
import com.example.parkedcardriver.R;
import com.example.parkedcardriver.ViewModel.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

class BookingListViewHolder extends RecyclerView.ViewHolder {
    TextView location, fromTime, toTime, bookingStatus, bookingId, paymentStatus, totalPrice;
    Button button;
    public BookingListViewHolder(@NonNull View itemView) {
        super(itemView);
        location = itemView.findViewById(R.id.address);
        fromTime = itemView.findViewById(R.id.from_time);
        toTime = itemView.findViewById(R.id.to_time);
        bookingStatus = itemView.findViewById(R.id.booking_status);
        bookingId = itemView.findViewById(R.id.booking_id);
        paymentStatus = itemView.findViewById(R.id.payment_status);
        totalPrice = itemView.findViewById(R.id.total_fare);
        button = itemView.findViewById(R.id.button);
    }

    public void setBooking(Booking booking, ProfileViewModel viewModel) {
        location.setText(booking.getLocationAddress());
        fromTime.setText(String.format("From: %s", booking.getFromTime()));
        toTime.setText(String.format("To  :%s", booking.getToTime()));
        bookingStatus.setText(booking.getBookingStatus());
        bookingId.setText("Booking ID: #"+ booking.getBookingId());
        paymentStatus.setText(booking.getPaymentStatus());
        totalPrice.setText(String.valueOf(booking.getTotalPrice()));

        if((booking.getStatus().equalsIgnoreCase("active") || booking.getStatus().equalsIgnoreCase("completed")) && booking.getPaymentStatus().equalsIgnoreCase("unpaid")) {
            button.setText("Pay");
            button.setOnClickListener(v -> viewModel.payBooking(booking.getBookingId()));
            button.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            button.setVisibility(View.VISIBLE);
            button.setBackgroundColor(itemView.getResources().getColor(R.color.colorPrimary));
        }else if(booking.getStatus().equalsIgnoreCase("requested")){
            button.setText("Cancel");
            button.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            button.setVisibility(View.VISIBLE);
            button.setBackgroundColor(itemView.getResources().getColor(R.color.colorAccent));
        } else{
            button.setVisibility(View.GONE);
            button.setHeight(0);
        }
    }
}

public class BookingListAdapter extends RecyclerView.Adapter<BookingListViewHolder>{
    ProfileViewModel viewModel;
    List<Booking> bookings;

    public BookingListAdapter(ProfileViewModel viewModel) {
        this.viewModel = viewModel;
        this.bookings = new ArrayList<>();
        bookings.add(new Booking());
    }

    @NonNull
    @Override
    public BookingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.booking_item_card, parent, false);
        return new BookingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingListViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.setBooking(booking, viewModel);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public void setBookings(List<Booking> bookings){
        this.bookings = bookings;
    }
}

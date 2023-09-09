package com.example.parkedcardriver.view.BookingSlot;

import android.os.Bundle;

import com.example.parkedcardriver.Common.Common;
import com.example.parkedcardriver.Model.Event.SelectedPlaceEvent;
import com.example.parkedcardriver.Model.SlotModel;
import com.example.parkedcardriver.Repository.BookingSlotRepository;
import com.example.parkedcardriver.Repository.SearchSlotRepository;
import com.example.parkedcardriver.ViewModel.BookingRequestViewModel;
import com.example.parkedcardriver.ViewModel.SlotViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.parkedcardriver.databinding.ActivityBookingSlotBinding;

import com.example.parkedcardriver.R;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;

public class BookingSlotActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private BookingRequestViewModel bookingRequestViewModel;

    private SlotModel slotModel;
    LifecycleOwner owner;
    String from_time, from_date, to_date, to_time;

    TextView booking_slot_id, booking_slot_status, booking_slot_from_time, booking_slot_to_time, booking_slot_base_fare, booking_slot_time_fare, booking_slot_total_fare;
    EditText booking_slot_address, booking_slot_owner;
    Button booking_slot_cancel_button, booking_slot_pay_button;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        bookingRequestViewModel.clearComposite();
        super.onStop();
        if(EventBus.getDefault().hasSubscriberForEvent(SelectedPlaceEvent.class)){
            EventBus.getDefault().removeStickyEvent(SelectedPlaceEvent.class);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        bookingRequestViewModel.clearComposite();
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_slot);

        owner = this;

        slotModel = (SlotModel) getIntent().getSerializableExtra("Slot_Details");
        from_date = getIntent().getStringExtra("From_Date");
        from_time = getIntent().getStringExtra("From_Time");
        to_date = getIntent().getStringExtra("To_Date");
        to_time = getIntent().getStringExtra("To_Time");

        booking_slot_id = findViewById(R.id.booking_slot_id);
        booking_slot_status = findViewById(R.id.booking_slot_status);
        booking_slot_from_time = findViewById(R.id.booking_slot_from_time);
        booking_slot_to_time = findViewById(R.id.booking_slot_to_time);
        booking_slot_base_fare = findViewById(R.id.booking_slot_base_fare);
        booking_slot_time_fare = findViewById(R.id.booking_slot_time_fare);
        booking_slot_total_fare = findViewById(R.id.booking_slot_total_fare);
        booking_slot_address = findViewById(R.id.booking_slot_address);
        booking_slot_owner = findViewById(R.id.booking_slot_owner);
        booking_slot_cancel_button = findViewById(R.id.booking_slot_cancel_button);
        booking_slot_pay_button = findViewById(R.id.booking_slot_pay_button);

        initBookingReceipt();

        BookingSlotRepository bookingSlotRepository = BookingSlotRepository.getInstance();
        bookingRequestViewModel = new ViewModelProvider(this).get(BookingRequestViewModel.class);
        bookingRequestViewModel.setBookingSlotRepository(bookingSlotRepository);
        bookingRequestViewModel.getBookingId().observe(this, bookingId->{
            if(bookingId == null){
                Toast.makeText(BookingSlotActivity.this, "Something went wrong while booking slot!!", Toast.LENGTH_LONG).show();
                return;
            }
            booking_slot_id.setText(bookingId + "");
            bookingRequestViewModel.getBookingDataRequest(bookingId);
        });

        bookingRequestViewModel.getBookingDetails().observe(BookingSlotActivity.this, bookingDetails->{
            if(bookingDetails == null){
                Toast.makeText(BookingSlotActivity.this, "Something went wrong while booking slot!!", Toast.LENGTH_LONG).show();
                return;
            }
            booking_slot_base_fare.setText(bookingDetails.getBaseFare() + "");
            booking_slot_time_fare.setText(Double.parseDouble(bookingDetails.getTotalPrice().toString())- Double.parseDouble(bookingDetails.getBaseFare().toString()) + "");
            booking_slot_total_fare.setText(bookingDetails.getTotalPrice() + "");
        });

        long fromMilSec = Common.getSystemMilSec(from_date.split("/")[2], from_date.split("/")[1], from_date.split("/")[0],
                from_time.split(":")[0], from_time.split(":")[1]);
        long toMilSec = Common.getSystemMilSec(to_date.split("/")[2], to_date.split("/")[1], to_date.split("/")[0],
                to_time.split(":")[0], to_time.split(":")[1]);

        bookingRequestViewModel.sendBookingSlotRequest(slotModel.getId(), fromMilSec, toMilSec);
    }

    private void initBookingReceipt() {
        booking_slot_status.setText(Common.capitalize("requested"));
        booking_slot_from_time.setText(Common.generalizeDateAdTime(from_date, from_time));
        booking_slot_to_time.setText(Common.generalizeDateAdTime(to_date, to_time));
        booking_slot_address.setText(slotModel.getAddress());
        booking_slot_owner.setText(slotModel.getOwner_name());
    }
}
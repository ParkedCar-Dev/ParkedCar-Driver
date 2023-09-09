package com.example.parkedcardriver.view.BookingSlot;

import android.os.Bundle;

import com.example.parkedcardriver.Model.Event.SelectedPlaceEvent;
import com.example.parkedcardriver.Model.SlotModel;
import com.example.parkedcardriver.ViewModel.BookingRequestViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
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
    String from_time, from_date, to_date, to_time;

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

        slotModel = (SlotModel) getIntent().getSerializableExtra("Slot_Details");
        from_date = getIntent().getStringExtra("From_Date");
        from_time = getIntent().getStringExtra("From_Time");
        to_date = getIntent().getStringExtra("To_Date");
        to_time = getIntent().getStringExtra("To_Time");

        initBookingReceipt();
    }

    private void initBookingReceipt() {
    }
}
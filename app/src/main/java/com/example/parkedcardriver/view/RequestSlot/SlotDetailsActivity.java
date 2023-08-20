package com.example.parkedcardriver.view.RequestSlot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.example.parkedcardriver.Model.Event.SelectedPlaceEvent;
import com.example.parkedcardriver.Model.SlotModel;
import com.example.parkedcardriver.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

public class SlotDetailsActivity extends AppCompatActivity {

    private SlotModel selectedSlot;

    @Override
    protected void onStart() {
        super.onStart();
        // EventBus.getDefault().register(this);
    }

//    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
//    public void onSelectedSlotEvent(SlotModel event)
//    {
//        selectedSlot = event;
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_details);
//        SlotModel slotModel = (SlotModel) getIntent().getSerializableExtra("Slot_Details");
//        System.out.println("Slot address: " + selectedSlot.getAddress());
    }
}
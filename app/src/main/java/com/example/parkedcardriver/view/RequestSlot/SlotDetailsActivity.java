package com.example.parkedcardriver.view.RequestSlot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parkedcardriver.Model.SlotModel;
import com.example.parkedcardriver.R;
import com.example.parkedcardriver.view.BookingSlot.BookSlotActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SlotDetailsActivity extends AppCompatActivity {

    @BindView(R.id.slot_address_textview)
    TextView slot_address_textview;
    @BindView(R.id.slot_owner_name_textview)
    TextView slot_owner_name_textview;
    @BindView(R.id.slot_distance_textview)
    TextView slot_distance_textview;
    @BindView(R.id.slot_price_textview)
    TextView slot_price_textview;
    @BindView(R.id.slot_length_textview)
    TextView slot_length_textview;
    @BindView(R.id.slot_width_textview)
    TextView slot_width_textview;
    @BindView(R.id.slot_height_textview)
    TextView slot_height_textview;
    @BindView(R.id.slot_rating_textview)
    TextView slot_rating_textview;
    @BindView(R.id.slot_total_book_textview)
    TextView slot_total_book_textview;

    @BindView(R.id.slot_security_guard_cardView)
    CardView slot_security_guard_cardView;
    @BindView(R.id.slot_security_indoor_cardView)
    CardView slot_security_indoor_cardView;
    @BindView(R.id.slot_security_cctv_cardView)
    CardView slot_security_cctv_cardView;

    @BindView(R.id.book_slot_button)
    Button book_slot_button;

    SlotModel slotModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_details);
        slotModel = (SlotModel) getIntent().getSerializableExtra("Slot_Details");

        initSlot();
    }

    private void initSlot() {
        ButterKnife.bind(this, getWindow().getDecorView().getRootView());
        slot_address_textview.setText(slotModel.getAddress());
        slot_owner_name_textview.setText(slotModel.getOwner_name());
        slot_distance_textview.setText(String.format("%.2f",(slotModel.getDistance()/1000.0)) + " km");
        slot_price_textview.setText("BDT " + slotModel.getPrice().intValue());
        slot_length_textview.setText(slotModel.getLength() + "m");
        slot_width_textview.setText(slotModel.getWidth() + "m");
        slot_height_textview.setText(slotModel.getHeight() + "m");
        slot_rating_textview.setText(String.valueOf(slotModel.getRating()));
        slot_total_book_textview.setText(String.valueOf(slotModel.getTotalBooks()));

        String[] security_measures = slotModel.getSecurityMeasures().split("/");
        for(String measure: security_measures){
            Log.d("Security: ", measure);
            if(measure.equals("cctv")){
                slot_security_cctv_cardView.setBackgroundTintList(ColorStateList.valueOf(0xff87CEEB));
            }
            else if(measure.equals("guard")){
                Log.d("Eine aise: ", "eine aise");
                slot_security_guard_cardView.setBackgroundTintList(ColorStateList.valueOf(0xff87CEEB));
            }
            else if(measure.equals("indoor")){
                slot_security_indoor_cardView.setBackgroundTintList(ColorStateList.valueOf(0xff87CEEB));
            }
        }

        book_slot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookSlotActivity.class));
            }
        });
    }
}
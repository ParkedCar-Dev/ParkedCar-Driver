package com.example.parkedcardriver.view.BookingSlot;

import static com.example.parkedcardriver.Common.Common.step;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.parkedcardriver.Adapters.MyViewPagerAdapter;
import com.example.parkedcardriver.Common.Common;
import com.example.parkedcardriver.R;
import com.example.parkedcardriver.ViewModel.ViewPager.NonSwipeViewPager;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookSlotActivity extends AppCompatActivity {

    StepView stepView;
    NonSwipeViewPager viewPager;
    Button btn_previous_step;
    Button btn_next_step;
    LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if(step == 2){
//                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT,-1);
//            }
            btn_next_step.setEnabled(true);
            setColorButton();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_slot);

        stepView = findViewById(R.id.step_view);
        viewPager = findViewById(R.id.view_pager);
        btn_next_step  = findViewById(R.id.btn_next_step);
        btn_previous_step = findViewById(R.id.btn_previous_step);

        setupStepView();
        setColorButton();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 0) {
                    btn_previous_step.setEnabled(false);
                    btn_next_step.setText("Confirm");
                }
                else{
                    btn_previous_step.setEnabled(true);
                    btn_next_step.setText("Next");
                }
                setColorButton();
            }

            @Override
            public void onPageSelected(int position) {
                stepView.go(position,true);
                /**
                 * position 1 hoile previous button disabled thakbe cuz time already confirm korse, so no back
                 * So eita add korbo arekta page add korle
                 */
                if(position == 0) {
                    Log.d("Check State: ", "Hereeeeee!!!!");
                    btn_previous_step.setEnabled(false);
                    btn_next_step.setText("Confirm");
                }
                else{
                    btn_previous_step.setEnabled(true);
                    btn_next_step.setText("Next");
                }
//                if(position == 2)
//                    btn_next_step.setEnabled(false);
//                else
//                    btn_next_step.setEnabled(true);

                setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btn_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(step < 3 || step == 0 ){
                    step++ ;
//                    if(step == 2){
//                        confirmBooking();
//                    }
                    viewPager.setCurrentItem(step);
                }
            }
        });
        btn_previous_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(step == 3 || step > 0 ){
                    step-- ;
                    viewPager.setCurrentItem(step);
                }
            }
        });
    }



//    private void confirmBooking() {
//        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
//        localBroadcastManager.sendBroadcast(intent);
//    }

    @Override
    protected void onDestroy() {
        step = 0 ;
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    private void setColorButton() {
        if(btn_previous_step.isEnabled()){
            btn_previous_step.setBackgroundTintList(ColorStateList.valueOf(0xFF3700B3));
        }
        else{
            btn_previous_step.setBackgroundTintList(ColorStateList.valueOf(0xFF03DAC5));
        }
        if(btn_next_step.isEnabled()){
            btn_next_step.setBackgroundTintList(ColorStateList.valueOf(0xFF3700B3));
        }
        else{
            btn_next_step.setBackgroundTintList(ColorStateList.valueOf(0xFF03DAC5));
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Tme and Date");
        stepView.setSteps(stepList);
    }
}
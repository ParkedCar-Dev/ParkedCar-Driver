package com.example.parkedcardriver.view.BookingSlot;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.parkedcardriver.R;

import java.time.LocalDate;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BookingStepOne extends Fragment {

    @BindView(R.id.start_time_textview)
    TextView start_time_textview;
    @BindView(R.id.start_date_textview)
    TextView start_date_textview;
    @BindView(R.id.end_date_textview)
    TextView end_date_textview;
    @BindView(R.id.end_time_textview)
    TextView end_time_textview;


    static BookingStepOne instance;

    Calendar c = Calendar.getInstance();
    int year_today = c.get(Calendar.YEAR);
    int month_today = c.get(Calendar.MONTH);
    int day_today = c.get(Calendar.DAY_OF_MONTH);
    int hour_of_day = c.get(Calendar.HOUR_OF_DAY);
    int minutes = c.get(Calendar.MINUTE);

    public static BookingStepOne getInstance(){
        if(instance == null)
            instance = new BookingStepOne();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initStartAndEndTime(view);
    }

    private void initStartAndEndTime(View view) {
        ButterKnife.bind(this, view);

        start_date_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDateDialog(view);
            }
        });

        end_date_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDateDialog(view);
            }
        });

        start_time_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimeDialog(view);
            }
        });

        end_time_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimeDialog(view);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_step_one, container, false);
    }

    private void openDateDialog(View myView){
        DatePickerDialog dialog = new DatePickerDialog(getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                switch (myView.getId()){
                    case R.id.start_date_textview:
                        Log.d("Date: ", dayOfMonth+"/"+month+"/"+year);
                        start_date_textview.setText(dayOfMonth+"/"+month+"/"+year);
                    case R.id.end_date_textview:
                        end_date_textview.setText(dayOfMonth+"/"+month+"/"+year);
                }
            }
        }, year_today, month_today, day_today);

        dialog.show();
    }

    private void openTimeDialog(View myView){
        TimePickerDialog dialog = new TimePickerDialog(getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                switch (myView.getId()){
                    case R.id.start_time_textview:
                        start_time_textview.setText(hourOfDay+":"+minute);
                    case R.id.end_time_textview:
                        end_time_textview.setText(hourOfDay+":"+minute);
                }
            }
        }, hour_of_day, minutes, true);

        dialog.show();
    }
}
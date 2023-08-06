package com.example.parkedcardriver.Common;

import android.widget.TextView;

import java.util.Calendar;

public class Common {
    public static void setWelcomeMessage(TextView txt_welcome) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if(hour>=1 && hour <=12){
            txt_welcome.setText(new StringBuilder("Good Morning"));
        }
        else if(hour >= 13 && hour <= 17){
            txt_welcome.setText(new StringBuilder("Good Afternoon"));
        }
        else{
            txt_welcome.setText(new StringBuilder("Good Evening"));
        }
    }
}

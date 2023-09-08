package com.example.parkedcardriver.Common;

import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class Common {

    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static int step = 0;
    public static LatLng origin = null;
    public static LatLng destination = null;
    public static ZonedDateTime quickSearchTime = null;
    public static boolean isQuickSearch = true;

    public static final DecimalFormat df = new DecimalFormat("0.00");

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

    public static List<LatLng> decodePoly(String encoded) {
        List poly = new ArrayList();
        int index=0,len=encoded.length();
        int lat=0,lng=0;
        while(index < len)
        {
            int b,shift=0,result=0;
            do{
                b=encoded.charAt(index++)-63;
                result |= (b & 0x1f) << shift;
                shift+=5;

            }while(b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1):(result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do{
                b = encoded.charAt(index++)-63;
                result |= (b & 0x1f) << shift;
                shift +=5;
            }while(b >= 0x20);
            int dlng = ((result & 1)!=0 ? ~(result >> 1): (result >> 1));
            lng +=dlng;

            LatLng p = new LatLng((((double)lat / 1E5)),
                    (((double)lng/1E5)));
            poly.add(p);
        }
        return poly;
    }

    public static String formatDuration(String duration) {
        if(duration.contains("mins")){
            return duration.substring(0, duration.length() - 1);
        }
        else{
            return duration;
        }
    }

    public static String formatAddress(String start_address) {
        int firstIndexOfComma = start_address.indexOf(",");
        return start_address.substring(0, firstIndexOfComma); // Get only address
    }

    public static Calendar getCalenderInstance() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+6");
        Calendar calendar = Calendar.getInstance(timeZone);
        return calendar;
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static ZonedDateTime currentTime(){
//        ZoneId zoneId = ZoneId.of("Asia/Almaty"); // or ZoneId.of("Asia/Bishkek")
//
//        // Create an Instant from the current system time
//        Instant instant = Instant.now();
//
//        // Convert the Instant to a ZonedDateTime in the specified time zone
//        return instant.atZone(zoneId);
//    }
}

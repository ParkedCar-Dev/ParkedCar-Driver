package com.example.parkedcardriver;

import android.app.Application;
import android.content.Context;

public class DriverApplication extends Application {
    private static DriverApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static DriverApplication getInstance(){
        return instance;
    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }
}

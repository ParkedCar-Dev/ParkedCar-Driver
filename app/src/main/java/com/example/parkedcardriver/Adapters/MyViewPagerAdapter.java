package com.example.parkedcardriver.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.parkedcardriver.view.BookingSlot.BookingStepOne;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    public MyViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return BookingStepOne.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }
}

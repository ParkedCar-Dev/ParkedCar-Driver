package com.example.parkedcardriver.view.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.parkedcardriver.view.profile.fragments.CurrentBookingsFragment;
import com.example.parkedcardriver.view.profile.fragments.PreviousBookingsFragment;

public class ProfileViewpagerAdapter extends FragmentStateAdapter {
    public ProfileViewpagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new PreviousBookingsFragment();
        }
        return new CurrentBookingsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

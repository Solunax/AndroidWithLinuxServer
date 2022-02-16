package com.example.ownserver.ViewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ownserver.Fragment.DummyFragment;
import com.example.ownserver.Fragment.HomeFragment;
import com.example.ownserver.Fragment.SettingFragment;

import java.util.ArrayList;

public class HomeViewPagerAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragments;

    public HomeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> fragments) {
        super(fragmentActivity);
        this.fragments = fragments;
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }
}

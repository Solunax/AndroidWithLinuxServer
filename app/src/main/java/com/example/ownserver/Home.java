package com.example.ownserver;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ownserver.Fragment.DummyFragment;
import com.example.ownserver.Fragment.HomeFragment;
import com.example.ownserver.Fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {
    HomeFragment homeFragment = new HomeFragment();
    SettingFragment settingFragment = new SettingFragment();
    DummyFragment dummyFragment = new DummyFragment();

    String loginId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Intent beforeData = getIntent();
        loginId = beforeData.getStringExtra("id");

        settingFragment = new SettingFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                switch (item.getItemId()){
                    case R.id.dummy:
                        transaction.replace(R.id.fragment_container, dummyFragment).commitAllowingStateLoss();
                        return true;

                    case R.id.main:
                        transaction.replace(R.id.fragment_container, homeFragment).commitAllowingStateLoss();
                        return true;

                    case R.id.setting:
                        Bundle bundle = new Bundle();
                        bundle.putString("id", loginId);
                        settingFragment.setArguments(bundle);
                        transaction.replace(R.id.fragment_container, settingFragment).commitAllowingStateLoss();
                        return true;
                }
                return false;
            }
        });
    }
}

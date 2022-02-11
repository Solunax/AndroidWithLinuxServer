package com.example.ownserver;

import static com.example.ownserver.Fragment.SettingFragment.disposable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ownserver.Fragment.DummyFragment;
import com.example.ownserver.Fragment.HomeFragment;
import com.example.ownserver.Fragment.SettingFragment;
import com.example.ownserver.databinding.MainScreenBinding;
import com.example.ownserver.model.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;

public class Home extends AppCompatActivity {
    public static ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    private HomeFragment homeFragment = new HomeFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private DummyFragment dummyFragment = new DummyFragment();
    private String loginId;
    public HomeViewModel homeViewModel;
    private MainScreenBinding binding;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DESTROY", "HOME");
        disposable.dispose();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent beforeData = getIntent();
        loginId = beforeData.getStringExtra("id");

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

        binding.bottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
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

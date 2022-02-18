package com.example.ownserver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ownserver.Fragment.DummyFragment;
import com.example.ownserver.Fragment.HomeFragment;
import com.example.ownserver.Fragment.SettingFragment;
import com.example.ownserver.ViewPager.HomeViewPagerAdapter;
import com.example.ownserver.databinding.MainScreenBinding;
import com.example.ownserver.model.HomeViewModel;
import com.example.ownserver.model.UserModel;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class Home extends AppCompatActivity {
    public static ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    private HomeFragment homeFragment = new HomeFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private DummyFragment dummyFragment = new DummyFragment();
    private String loginId;
    public HomeViewModel homeViewModel;
    private MainScreenBinding binding;
    public static CompositeDisposable disposable  = new CompositeDisposable();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> fragmentValue = new ArrayList<>();
    private long lastTimeBackPressed;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish();
            return;
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DESTROY", "HOME ACTIVITY");

        disposable.dispose();
        binding.homeViewPager.setAdapter(null);
        binding = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HOME", "on Create");

        binding = MainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        disposable  = new CompositeDisposable();

        Intent beforeData = getIntent();
        loginId = beforeData.getStringExtra("id");

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        Bundle bundle = new Bundle();
        bundle.putString("id", loginId);
        settingFragment.setArguments(bundle);

        fragments.add(dummyFragment);
        fragments.add(homeFragment);
        fragments.add(settingFragment);

        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(this, fragments);
        binding.homeViewPager.setAdapter(homeViewPagerAdapter);
        binding.homeViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.bottomMenu.getMenu().getItem(position).setChecked(true);

                if(position == 0){
                    fragmentValue = homeViewModel.getViewModelList().getValue();
                    if(!(fragmentValue == null)){
                        DummyFragment df = (DummyFragment)fragmentManager.findFragmentByTag("f0");
                        df.setData(fragmentValue, false);
                    }
                }
            }
        });

        binding.homeViewPager.setCurrentItem(1);

        binding.bottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dummy:
                        binding.homeViewPager.setCurrentItem(0);
                        return true;

                    case R.id.main:
                        binding.homeViewPager.setCurrentItem(1);
                        return true;

                    case R.id.setting:
                        binding.homeViewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
    }
}

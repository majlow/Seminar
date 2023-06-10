package com.example.myapplication.NavigationFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MainNavigation extends AppCompatActivity {
    private Context context;
    private ViewPager2 mViewPager2;
    private ViewPagerAdapter mViewPagerAdapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main_navigation);
        mViewPager2 = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        mViewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager2.setAdapter(mViewPagerAdapter);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id){
                case R.id.qrcode:
                    mViewPager2.setCurrentItem(0);
                    break;
                case R.id.upload:
                    mViewPager2.setCurrentItem(1);
                    break;
                case R.id.verify_otp:
                    mViewPager2.setCurrentItem(2);
                    break;
            }
            return true;
        });
        // Dong bo fragment voi icon
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.qrcode).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.upload).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.verify_otp).setChecked(true);
                        break;

                }
            }
        });

    }

    private void replaceFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }
}

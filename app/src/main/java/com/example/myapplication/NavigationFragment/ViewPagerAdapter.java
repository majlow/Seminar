package com.example.myapplication.NavigationFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new QRCodeFragment();
            case 1:
                return new UploadFileFragment();
            case 2:
                return new OTPCodeFragment();
            default:
                return new QRCodeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

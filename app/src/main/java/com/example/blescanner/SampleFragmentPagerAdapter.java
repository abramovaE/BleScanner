package com.example.blescanner;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SampleFragmentPagerAdapter extends FragmentStateAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Scan", "Call"};
    private Context context;
    FragmentHandler fragmentHandler;

    public SampleFragmentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


//
//    public SampleFragmentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
//        super(fragmentManager,  ((MainActivity) context));
//    }


//    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
//        super(fm);
//        this.context = context;
//        fragmentHandler = new FragmentHandler(context);
//    }

//    @Override public int getCount() {
//        return PAGE_COUNT;
//    }
//
//    @Override public Fragment getItem(int position) {
//        switch (position){
//            case 0:
//                return new ScanningFragment();
//            case 1:
//                return new CallingFragment();
//        }
//        return new ScanningFragment();
//    }
//
//    @Override public CharSequence getPageTitle(int position) {
//        // генерируем заголовок в зависимости от позиции
//        return tabTitles[position];
//    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
//                fragmentHandler.changeFragment(FragmentHandler.SCANNING_FRAGMENT, true);
                return new ScanningFragment();
            case 1:
//                fragmentHandler.changeFragment(FragmentHandler.CALLING_FRAGMENT, true);
//
                return new CallingFragment();
        }
        return new ScanningFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
package com.example.blescanner;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class FragmentHandler {

    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    public final static String SET_RSSIS_DIALOG = "SET_RSSIS_DIALOG";

    public final static String CALLING_FRAGMENT = "CALLING_FRAGMENT";
    public final static String SCANNING_FRAGMENT = "SCANNING_FRAGMENT";


    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public FragmentHandler(Context context){
        fragmentManager = ((MainActivity) context).getSupportFragmentManager();
    }

    private Fragment getFragment(String fragmentTag){
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if(fragment == null){
            switch (fragmentTag){
                case CALLING_FRAGMENT:
                    return new CallingFragment();
                case SCANNING_FRAGMENT:
                    return new ScanningFragment();
            }
        }
        return fragment;
    }


    public void changeFragment(String fragmentTag, boolean stacked){
        if (currentFragment == null){
            setFragment(new ScanningFragment(), SCANNING_FRAGMENT, true);
        }
        Fragment fragment = getFragment(fragmentTag);
        setFragment(fragment, fragmentTag, stacked);
    }


    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    private void setFragment(Fragment fragment, String tag, boolean stacked){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragments, fragment, tag);
        if (stacked) {
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            fragmentTransaction.commitNowAllowingStateLoss();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentFragment = fragment;
    }





}
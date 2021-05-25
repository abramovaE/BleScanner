package com.example.blescanner;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class FragmentHandler {

    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    public final static String SET_RSSIS_DIALOG = "SET_RSSIS_DIALOG";

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

            }
        }
        return fragment;
    }






    public void changeFragment(String fragmentTag){
//        Logger.d(Logger.FRAGMENT_LOG, "change fragment to: " + fragmentTag);
//        Fragment fragment = getFragment(fragmentTag);
//        setFragment(fragment, fragmentTag, true);
    }

    public void changeFragment(String fragmentTag, boolean stacked){
//        Logger.d(Logger.FRAGMENT_LOG, "change fragment to: " + fragmentTag + ", stacked: " + stacked);

//        Fragment fragment = getFragment(fragmentTag);
//        setFragment(fragment, fragmentTag, stacked);
    }

    private void setFragment(Fragment fragment, String tag, boolean stacked){
//        Logger.d(Logger.FRAGMENT_LOG, "set fragment: " + tag + ", stacked: " + stacked);

//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
//        if (stacked) {
//            fragmentTransaction.addToBackStack(tag);
//        }
//        fragmentTransaction.commit();
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        currentFragment = fragment;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

}
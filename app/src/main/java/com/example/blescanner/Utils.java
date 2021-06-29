package com.example.blescanner;

import android.util.Log;

import java.util.List;

public class Utils {

    private Bluetooth bluetooth;
    private CustomViewModel viewModel;
    private Thread t;
    private MainActivity mainActivity;

    public Utils(Bluetooth bluetooth, CustomViewModel viewModel, MainActivity mainActivity) {
        this.bluetooth = bluetooth;
        this.viewModel = viewModel;
        this.mainActivity = mainActivity;
    }

    public void startRvTimer(){
        Log.d("TAG", "start rv timer: " + t);
        if(t != null){
            t.interrupt();
        }
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "new thread");
                while (true) {
                    switch (mainActivity.getFragmentHandler().getCurrentFragment().getTag()){
                        case FragmentHandler.SCANNING_FRAGMENT:
                            List<CustomScanResult> results = bluetooth.getScanningResults();
                            Log.d("TAG", "update scan results");
                            viewModel.updateResults(results);
                            break;
                        case FragmentHandler.CALLING_FRAGMENT:
                            List<CustomScanResult> callingResults = bluetooth.getCallingResults();
                            Log.d("TAG", "update call results");
                            viewModel.updateCallingResults(callingResults);
                            break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }
}
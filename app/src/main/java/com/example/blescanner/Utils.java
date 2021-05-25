package com.example.blescanner;

import android.util.Log;

import java.util.List;

public class Utils {

    private Bluetooth bluetooth;
    private CustomViewModel viewModel;
    private Thread t;

    public Utils(Bluetooth bluetooth, CustomViewModel viewModel) {
        this.bluetooth = bluetooth;
        this.viewModel = viewModel;
    }

    public void startRvTimer(){
        if(t != null){
            t.interrupt();
        }
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    List<CustomScanResult> results = bluetooth.getResults();
                    Log.d("TAG", "results: " + results.size());
                    viewModel.updateResults(results);
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
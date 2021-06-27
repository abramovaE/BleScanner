package com.example.blescanner;

import android.util.Log;

import java.util.List;

public class Utils {

    private Bluetooth bluetooth;
    private CustomViewModel viewModel;
    private Thread t;

    int counter;

    public Utils(Bluetooth bluetooth, CustomViewModel viewModel) {
        this.bluetooth = bluetooth;
        this.viewModel = viewModel;
    }

    public void startRvTimer(){
        counter = 0;
        if(t != null){
            t.interrupt();
        }
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    counter+=1;
                    List<CustomScanResult> results = bluetooth.getResults();
//                    if(counter%2 == 0) {
                        for(CustomScanResult result: results){
                            result.setCounter(bluetooth.getCounter(result));
                        }
//                        bluetooth.clearPackageCounter();
//                    }
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
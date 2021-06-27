package com.example.blescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import static com.example.blescanner.Bluetooth.REQUEST_BT_ENABLE;
import static com.example.blescanner.Bluetooth.REQUEST_FINE_LOCATION;
import static com.example.blescanner.Bluetooth.REQUEST_GPS_ENABLE;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private Bluetooth bluetooth;

    public static final String SHARED_PREF_NAME = "ble_scanner_pref";

    Utils utils;
    SharedPreferences sharedPreferences;

    FragmentHandler fragmentHandler;
    CustomViewModel viewModel;

    public static final int minRssiDefault = -200;
    public static final int maxRssiDefault = 200;
    public static final String MIN_RSSI_TAG = "min_rssi";
    public static final String MAX_RSSI_TAG = "max_rssi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAG", "activity on create");
        bluetooth = new Bluetooth(this);
        viewModel = ViewModelProviders.of(this, new CustomViewModel.ModelFactory()).get(CustomViewModel.class);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        fragmentHandler = new FragmentHandler(this);
        utils = new Utils(bluetooth, viewModel);
        fragmentHandler.changeFragment(FragmentHandler.SCANNING_FRAGMENT, true);
    }


    public Bluetooth getBluetooth() {
        return bluetooth;
    }
    public FragmentHandler getFragmentHandler() {
        return fragmentHandler;
    }
    public Utils getUtils() {
        return utils;
    }
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
    public CustomViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_FINE_LOCATION:
//                bluetooth.startScan();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivity result, request code: " + requestCode);
        switch (requestCode){
            case REQUEST_BT_ENABLE:
//                bluetooth.startScan();
                break;
            case REQUEST_GPS_ENABLE:
//                bluetooth.startScan();
                break;
        }
    }

    @Override
    public void onTaskCompleted(Bundle bundle) {
        int code = bundle.getInt("code");
        String message = "";
        if (code == SendReportToServer.RESPONSE_SUCCESS){
            message = "The report was sent successfully";
        } else {
            message = "Sending the report failed";
        }
        String finalMessage = message;
        Context context = this;
        runOnUiThread(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(context, finalMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        Log.d("TAG", "onTaskCompleted bundle: " + message);
    }


    public void saveToSharedPrefs(int minRssi, int maxRssi){
        sharedPreferences.edit().putInt(MIN_RSSI_TAG, minRssi).commit();
        sharedPreferences.edit().putInt(MAX_RSSI_TAG, maxRssi).commit();
    }
}
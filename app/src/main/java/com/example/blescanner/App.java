package com.example.blescanner;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

public class App extends Application {

    private int analysisTime;

    private static App instance;
    private SharedPreferences mPreferences;

    private static final String PREF_NAME = "ble_scanner_pref";

    private static final Integer ANALYSIS_TIME_DEFAULT = 10;

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        analysisTime =  mPreferences.getInt("analysisTime", ANALYSIS_TIME_DEFAULT);
    }

    public static App get() {
        return instance;
    }


    public int getAnalysisTime() {
        Log.d("TAG", "analysisTime: " + analysisTime);
        return analysisTime;
    }

    public void setAnalysisTime(int analysisTime) {
        this.analysisTime = analysisTime;
        mPreferences.edit().putInt("analysisTime", analysisTime).apply();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public String getBrand() {
        return Build.BRAND;
    }

    public String getModel() {
        return Build.MODEL;
    }
}

package com.example.blescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.blescanner.Bluetooth.REQUEST_BT_ENABLE;
import static com.example.blescanner.Bluetooth.REQUEST_FINE_LOCATION;
import static com.example.blescanner.Bluetooth.REQUEST_GPS_ENABLE;

public class MainActivity extends AppCompatActivity implements ScanningLabelUpdate, OnTaskCompleted {

    private Bluetooth bluetooth;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MainRVAdapter mainRVAdapter;
    private CustomViewModel viewModel;
    TextView tvMinRssi, tvMaxRssi;
    Spinner analysisTime;

    public static final int minRssiDefault = -200;
    public static final int maxRssiDefault = 200;

    public static final String MIN_RSSI_TAG = "min_rssi";
    public static final String MAX_RSSI_TAG = "max_rssi";
    public static final String SHARED_PREF_NAME = "ble_scanner_pref";

    Utils utils;
    SharedPreferences sharedPreferences;
    Button startScanning;
    TextView scanningLabel;
    FragmentHandler fragmentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAG", "activity on create");


        bluetooth = new Bluetooth(this);
        bluetooth.setScanningTimeInSeconds(App.get().getAnalysisTime());
//        bluetooth.startScan();
        recyclerView = findViewById(R.id.main_rv);
        mainRVAdapter = new MainRVAdapter(new ArrayList<>());
        recyclerView.setAdapter(mainRVAdapter);
        viewModel = ViewModelProviders.of(this, new CustomViewModel.ModelFactory()).get(CustomViewModel.class);
        viewModel.getResults().observe(this, this::updateAdapter);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tvMinRssi = findViewById(R.id.tv_min_rssi);
        tvMaxRssi = findViewById(R.id.tv_max_rssi);
        int currentMinRssi = sharedPreferences.getInt(MIN_RSSI_TAG, minRssiDefault);
        int currentMaxRssi = sharedPreferences.getInt(MAX_RSSI_TAG, maxRssiDefault);
        updateRssiTv(currentMinRssi, currentMaxRssi);
        fragmentHandler = new FragmentHandler(this);
        utils = new Utils(bluetooth, viewModel);
        analysisTime = findViewById(R.id.spn_analysis);
        String str0 = getValues0();
        String[] values = {str0, "10", "30", "60", "180", "360"};
        ArrayAdapter<String> analysisTimeSpnAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, values);
        analysisTimeSpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        analysisTime.setPrompt(getString(R.string.analysisTime));
        analysisTime.setAdapter(analysisTimeSpnAdapter);
        analysisTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    App.get().setAnalysisTime(Integer.parseInt(values[position]));
                    values[0] = getValues0();
                    analysisTime.setSelection(0);
                    bluetooth.setScanningTimeInSeconds(Integer.parseInt(values[position]));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        startScanning = findViewById(R.id.btn_start_scan);
        startScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.startScan();
            }
        });
        scanningLabel = findViewById(R.id.scanningLabel);
    }

    private String getValues0(){
        return getString(R.string.analysis) + ": " + App.get().getAnalysisTime();
    }

    private void updateRssiTv(int minRssi, int maxRssi){
        tvMinRssi.setText(getString(R.string.current_min_rssi) + " " + minRssi);
        tvMaxRssi.setText(getString(R.string.current_max_rssi) + " " + maxRssi);
    }

    private void saveToSharedPrefs(int minRssi, int maxRssi){
        sharedPreferences.edit().putInt(MIN_RSSI_TAG, minRssi).commit();
        sharedPreferences.edit().putInt(MAX_RSSI_TAG, maxRssi).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        utils.startRvTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bluetooth.stopScan();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetooth.stopScan();
    }


    private void updateAdapter(List<CustomScanResult> results){
        CustomScanResultDiffUtil customScanResultDiffUtilCallback = new CustomScanResultDiffUtil(mainRVAdapter.getResults(), results);
        DiffUtil.DiffResult customDiffUtilResult = DiffUtil.calculateDiff(customScanResultDiffUtilCallback);
        mainRVAdapter.setResults(results);
        customDiffUtilResult.dispatchUpdatesTo(mainRVAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_FINE_LOCATION:
                bluetooth.startScan();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivity result, request code: " + requestCode);
        switch (requestCode){
            case REQUEST_BT_ENABLE:
                bluetooth.startScan();
                break;
            case REQUEST_GPS_ENABLE:
                bluetooth.startScan();
                break;
        }
    }

    @Override
    public void updateScanningLabel() {
        String scanString = getString(bluetooth.isScanning()? R.string.scanningStarted : R.string.scanningStoped);
        this.scanningLabel.setText(scanString);
    }

    @Override
    public void onTaskCompleted(Bundle bundle) {
        Log.d("TAG", "onTaskCompleted bundle: " + bundle);
    }

    public static class SetRssiDialog extends DialogFragment{
        MainActivity mainActivity;
        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            this.mainActivity = (MainActivity) context;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.set_rssi));
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.set_rssi_dialog, null);
            builder.setView(v);
            EditText minRssiEt = v.findViewById(R.id.set_min_rssi);
            EditText maxRssiEt = v.findViewById(R.id.set_max_rssi);
            Button saveRssis = v.findViewById(R.id.save_rssis_btn);
            saveRssis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String minRssiStr = minRssiEt.getText().toString();
                    String maxRssiStr = maxRssiEt.getText().toString();
                    if(minRssiStr == null || minRssiStr.trim().isEmpty()){
                        minRssiStr = minRssiDefault + "";
                    }
                    if(maxRssiStr == null || maxRssiStr.trim().isEmpty()){
                        maxRssiStr = maxRssiDefault + "";
                    }
                    try {
                        int minRssiInt = Integer.parseInt(minRssiStr);
                        int maxRssiInt = Integer.parseInt(maxRssiStr);
                        mainActivity.saveToSharedPrefs(minRssiInt, maxRssiInt);
                        mainActivity.updateRssiTv(minRssiInt, maxRssiInt);
                        dismiss();
                    } catch (NumberFormatException e){
                        minRssiEt.setText("");
                        maxRssiEt.setText("");
                    }
                }
            });
            builder.setCancelable(true);
            return builder.create();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.set_min_max_rssi:
                SetRssiDialog dialog = new SetRssiDialog();
                dialog.show(fragmentHandler.getFragmentManager(), fragmentHandler.SET_RSSIS_DIALOG);
                return true;


            // TODO: 26.06.2021 add action to send the report
            case R.id.send_report:
                getReport();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<String> getReport(){
        List<String> report = new ArrayList<>();
        report.add("New report");
        report.add("Brand: " + App.get().getBrand());
        report.add("Model " + App.get().getModel());
        report.add("Package counter: " + bluetooth.getPackageCounter());
        report.add("Average rssi: " + bluetooth.getAllAvRssi());
        Log.d("TAG", report + "");
        return report;
    }
}
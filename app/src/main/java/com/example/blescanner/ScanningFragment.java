package com.example.blescanner;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ScanningFragment extends Fragment implements ScanningLabelUpdate{

    private RecyclerView recyclerView;
    private MainRVAdapter mainRVAdapter;
    private CustomViewModel viewModel;
    TextView tvMinRssi, tvMaxRssi;
    Spinner analysisTime;

    public static final int minRssiDefault = -200;
    public static final int maxRssiDefault = 200;

    public static final String MIN_RSSI_TAG = "min_rssi";
    public static final String MAX_RSSI_TAG = "max_rssi";

    Button startScanning;
    TextView scanningLabel;

    Context context;
    SharedPreferences sharedPreferences;
    Bluetooth bluetooth;
    FragmentHandler fragmentHandler;
    Utils utils;

    @Override
    public void onAttach(Context context) {
        try{
            this.context = context;
            bluetooth = ((MainActivity)context).getBluetooth();
            fragmentHandler = ((MainActivity)context).getFragmentHandler();
            utils = ((MainActivity)context).getUtils();
            sharedPreferences = ((MainActivity)context).getSharedPreferences();
            viewModel = ((MainActivity)context).getViewModel();
        } catch (ClassCastException e){
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_scanning, container, false);
        recyclerView = view.findViewById(R.id.main_rv);
        mainRVAdapter = new MainRVAdapter(new ArrayList<>());
        recyclerView.setAdapter(mainRVAdapter);
        viewModel.getResults().observe(this, this::updateAdapter);
        tvMinRssi = view.findViewById(R.id.tv_min_rssi);
        tvMaxRssi = view.findViewById(R.id.tv_max_rssi);

        int currentMinRssi = sharedPreferences.getInt(MIN_RSSI_TAG, minRssiDefault);
        int currentMaxRssi = sharedPreferences.getInt(MAX_RSSI_TAG, maxRssiDefault);
        updateRssiTv(currentMinRssi, currentMaxRssi);
        analysisTime = view.findViewById(R.id.spn_analysis);

        String str0 = getValues0();
        String[] values = {str0, "10", "30", "60", "180", "360"};
        ArrayAdapter<String> analysisTimeSpnAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, values);
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
        startScanning = view.findViewById(R.id.btn_start_scan);
        startScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.startScan();
//                utils.startRvTimer();
            }
        });
        scanningLabel = view.findViewById(R.id.scanningLabel);

        setHasOptionsMenu(true);
        bluetooth.setScanningTimeInSeconds(App.get().getAnalysisTime());

        return view;
    }

    private void updateAdapter(List<CustomScanResult> results){
        Log.d("TAG", "update scanning adapter, " + bluetooth.isScanning());
        if(bluetooth.isScanning()) {
            CustomScanResultDiffUtil customScanResultDiffUtilCallback = new CustomScanResultDiffUtil(mainRVAdapter.getResults(), results);
            DiffUtil.DiffResult customDiffUtilResult = DiffUtil.calculateDiff(customScanResultDiffUtilCallback);
            mainRVAdapter.setResults(results);
            customDiffUtilResult.dispatchUpdatesTo(mainRVAdapter);
        }
    }

    private void updateRssiTv(int minRssi, int maxRssi){
        tvMinRssi.setText(getString(R.string.current_min_rssi) + " " + minRssi);
        tvMaxRssi.setText(getString(R.string.current_max_rssi) + " " + maxRssi);
    }

    @Override
    public void updateScanningLabel() {
        String scanString = getString(bluetooth.isScanning()? R.string.scanningStarted : R.string.scanningStoped);
        this.scanningLabel.setText(scanString);
    }


    public static class SetRssiDialog extends DialogFragment {
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
                        ((MainActivity)getActivity()).saveToSharedPrefs(minRssiInt, maxRssiInt);
                        ((ScanningFragment)getParentFragment()).updateRssiTv(minRssiInt, maxRssiInt);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
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
                List<String> report = getReport();
                StringBuilder stringBuilder = new StringBuilder();
                report.forEach(it -> stringBuilder.append(it).append("\n"));
                new Thread(new SendReportToServer(stringBuilder.toString(), (MainActivity)context)).start();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<String> getReport(){
        List<String> report = new ArrayList<>();
        report.add("New report, date: " + bluetooth.getScanningTime());
        report.add("Analysis time: " + bluetooth.getScanningTimeInSeconds());
        report.add("Brand: " + App.get().getBrand());
        report.add("Model " + App.get().getModel());
        report.add("Package counter: " + bluetooth.getPackageCounter());
        report.add("Average rssi: " + bluetooth.getAllAvRssi());
        Log.d("TAG", report + "");
        return report;
    }

    private String getValues0(){
        return getString(R.string.analysis) + ": " + App.get().getAnalysisTime();
    }

    @Override
    public void onStart() {
        Log.d("TAG", "scanning on start");
        super.onStart();
        utils.startRvTimer();
//        bluetooth.clearResults();
        bluetooth.stopScan();
//        mainRVAdapter.clearAdapter();
    }

    @Override
    public void onPause() {
        super.onPause();
        bluetooth.stopScan();
    }

    @Override
    public void onStop() {
        super.onStop();
        bluetooth.stopScan();
    }

    @Override
    public void onResume() {
        Log.d("TAG", "scanning on resume");
//        bluetooth.clearResults();
//        utils.stopRvTimer();
        bluetooth.stopScan();
        scanningLabel.setText("");
        super.onResume();

    }
}
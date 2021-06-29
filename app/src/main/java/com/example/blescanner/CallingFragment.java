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

import java.util.ArrayList;
import java.util.List;

public class CallingFragment extends Fragment implements ScanningLabelUpdate {

    private RecyclerView recyclerView;
    private CallingAdapter callingAdapter;
    private CustomViewModel viewModel;

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
        View view =  inflater.inflate(R.layout.fragment_calling, container, false);
        recyclerView = view.findViewById(R.id.calling_rv);
        callingAdapter = new CallingAdapter(new ArrayList<>());
        recyclerView.setAdapter(callingAdapter);
        viewModel.getCallingResults().observe(this, this::updateAdapter);
        bluetooth.setScanningTimeInSeconds(Bluetooth.SCANNING_TIME_IN_SECONDS);
        return view;
    }

    private void updateAdapter(List<CustomScanResult> results){
        Log.d("TAG", "update calling adapter");
        CustomScanResultDiffUtil customScanResultDiffUtilCallback = new CustomScanResultDiffUtil(callingAdapter.getResults(), results);
        DiffUtil.DiffResult customDiffUtilResult = DiffUtil.calculateDiff(customScanResultDiffUtilCallback);
        callingAdapter.setResults(results);
        customDiffUtilResult.dispatchUpdatesTo(callingAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        utils.startRvTimer();
        bluetooth.startScan();
//        callingAdapter.clearAdapter();
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
//        utils.stopRvTimer();
    }

    @Override
    public void updateScanningLabel() {

    }
}
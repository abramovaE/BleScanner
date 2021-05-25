package com.example.blescanner;


import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class CustomScanResultDiffUtil extends DiffUtil.Callback {


    private final List<CustomScanResult> oldResults;
    private final List<CustomScanResult> newResults;

    public CustomScanResultDiffUtil(List<CustomScanResult> oldResults, List<CustomScanResult> newResults) {
        this.oldResults = oldResults;
        this.newResults = newResults;
    }

    public List<CustomScanResult> getOldResults() {
        return oldResults;
    }

    public List<CustomScanResult> getNewResults() {
        return newResults;
    }

    @Override
    public int getOldListSize() {
        return oldResults.size();
    }

    @Override
    public int getNewListSize() {
        return newResults.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        CustomScanResult oldCustomScanResult = oldResults.get(oldItemPosition);
        CustomScanResult newCustonScanResult = newResults.get(newItemPosition);
        return oldCustomScanResult.getBluetoothDeviceMac().equals(newCustonScanResult.getBluetoothDeviceMac());
    }

//    сравниваем только то, что видно в gui
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        CustomScanResult oldCustomScanResult = oldResults.get(oldItemPosition);
        CustomScanResult newCustonScanResult = newResults.get(newItemPosition);
        return oldCustomScanResult.getScanResult().getScanRecord().equals(newCustonScanResult.getScanResult().getScanRecord());
//                && oldCustomScanResult.getAppearanceTime() == newCustonScanResult.getAppearanceTime();
//        return oldCustomScanResult.getBluetoothDeviceMac().equals(newCustonScanResult.getBluetoothDeviceMac());
    }

//    @Nullable
//    @Override
//    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
//        CustomScanResult oldCustomScanResult = oldResults.get(oldItemPosition);
//        CustomScanResult newCustonScanResult = newResults.get(newItemPosition);
////         TODO: 01.03.2021 обработать изменения статуса дверей
//        if(!oldCustomScanResult.getScanResult().getDevice().equals(newCustonScanResult.getScanResult().getDevice())){
//            return newCustonScanResult;
//        }
//        return null;
//    }
}

package com.example.blescanner;

import android.bluetooth.le.ScanResult;

import java.util.Arrays;

public class CustomScanResult {

    private ScanResult scanResult;
    private long appearanceTime;
    private String bluetoothDeviceMac;
    private int counter;


    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public CustomScanResult(ScanResult result){
        this.scanResult = result;
        this.appearanceTime = System.currentTimeMillis();
        this.bluetoothDeviceMac = result.getDevice().getAddress();
        this.counter = 0;
    }

    public ScanResult getScanResult() {
        return scanResult;
    }

    public long getAppearanceTime() {
        return appearanceTime;
    }

    public String getBluetoothDeviceMac() {
        return bluetoothDeviceMac;
    }

    @Override
    public String toString() {
        return getSerial(scanResult);
    }

    private String getSerial(ScanResult result){
        String name = result.getScanRecord().getDeviceName();;
        byte[] data;
        if (name != null && name.startsWith("stp")) {
            byte[] pack1;
            byte[] pack2 = new byte[0];
            byte[] allBytes = result.getScanRecord().getBytes();
            pack1 = Arrays.copyOfRange(allBytes, 9, 31);
            if (allBytes.length > 35) {
                pack2 = Arrays.copyOfRange(allBytes, 35, allBytes.length);
            }
            data = new byte[pack1.length + pack2.length];
            System.arraycopy(pack1, 0, data, 0, pack1.length);
            System.arraycopy(pack2, 0, data, pack1.length, pack2.length);
            String deviceName = result.getScanRecord().getDeviceName();
            if(name.equals("stp")){
                int i = (((data[2] & 0xFF) << 16) + ((data[3] & 0xFF) << 8) + (data[4] & 0xFF));
                return  "stp" + String.format("%6s", i).replace(' ', '0');
            }
            else {
                return deviceName;
            }
        }
        return null;
    }

}

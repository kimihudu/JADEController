package lkphan.btcontroller.jadecontroller.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.support.annotation.NonNull;

import lkphan.btcontroller.jadecontroller.BuildConfig;

/**
 * Created by kimiboo on 2017-09-14.
 */

public class JadeBluetooth {




    static final String TAG = JadeBluetooth.class.getSimpleName();
    private static final boolean DBG = BuildConfig.DEBUG & true;

    // bluetooth adapter which provides access to bluetooth functionality.

//    private BluetoothAdapter mBTAdapter = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mBTDevice = null;
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path
    private JadeSocketThread mLocalSocketThread = null;
    private static java.util.UUID UUID_SPP = null; //java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private String dataCB;
    public String name;
    public String macAdd;
    public String uuid;
    private Boolean camStat = false;
    public Boolean modeRover = false;
    private int mState;


    public JadeBluetooth(@NonNull String bluetoothAddress,
                         @NonNull String uuid) {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            mBTDevice = mBluetoothAdapter.getRemoteDevice(bluetoothAddress); //"00:12:6F:2F:AA:CF"
            UUID_SPP = UUID.fromString(uuid);
        } catch (IllegalArgumentException i) {
            Log.i(TAG, "JadeBluetooth()" + i.getMessage());
        }

        // Make an RFCOMM binding.
        try {
//            act role: client
            mBTSocket = mBTDevice.createRfcommSocketToServiceRecord(UUID_SPP);
        } catch (Exception e1) {
            Log.i(TAG, "connect(): Failed to bind to RFCOMM by UUID. msg=" + e1.getMessage());
        }

//        jade property
        this.macAdd = mBTDevice.getAddress();
        this.uuid = mBTDevice.getUuids().toString();
        this.name = mBTDevice.getName();
    }


    public String getDataCB() {
        return this.dataCB;
    }

    public void start() {

        mLocalSocketThread = new JadeSocketThread(mBTSocket);
        mLocalSocketThread.start();
    }

    public void sendCommand(String comm) {

        switch (comm) {
            case CommandList.CAM_ON:
                camStat = true;
                break;
            case CommandList.CAM_OFF:
                camStat = false;
                break;
            case CommandList.R_ROVER_START:
                modeRover = true;
                break;

        }
        if (camStat && modeRover)
            mLocalSocketThread.setRoverMode(modeRover);
        mLocalSocketThread.write(comm);
        dataCB = mLocalSocketThread.getDataCB();

    }

    public void stop() {

        mLocalSocketThread.cancel();
        mLocalSocketThread = null;
    }


}

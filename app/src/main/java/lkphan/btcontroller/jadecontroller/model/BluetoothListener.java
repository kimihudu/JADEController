package lkphan.btcontroller.jadecontroller.model;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

/**
 * Created by kimiboo on 2017-10-06.
 */

public interface BluetoothListener {
    void onReceivedData(ArrayList data);

    void onConnected(BluetoothDevice device);

    void connectionFailed();

    void onLostConnection();

    void onGotCallback(String ack);
}

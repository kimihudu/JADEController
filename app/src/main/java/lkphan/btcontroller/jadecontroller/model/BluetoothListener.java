package lkphan.btcontroller.jadecontroller.model;

import android.bluetooth.BluetoothDevice;

/**
 * Created by kimiboo on 2017-10-06.
 */

public interface BluetoothListener {
    void onReceivedData(byte[] bytes);

    void onConnected(BluetoothDevice device);

    void connectionFailed();

    void onLostConnection();

    void onGotCallback(String ack);
}

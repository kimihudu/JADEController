package lkphan.btcontroller.jadecontroller.ultis;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by kimiboo on 2017-09-14.
 */

public class tmp {
    public static void copyArray(){
        int newBytes = 5;
        byte[] buffer = new byte[1024];
        buffer = Arrays.copyOfRange(buffer,0,buffer.length + newBytes);
    }
}

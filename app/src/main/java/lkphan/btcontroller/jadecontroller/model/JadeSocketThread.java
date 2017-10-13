package lkphan.btcontroller.jadecontroller.model;

/**
 * Created by kimiboo on 2017-09-14.
 */

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import lkphan.btcontroller.jadecontroller.BuildConfig;
import lkphan.btcontroller.jadecontroller.ultis.Ultis;

import static android.R.attr.mode;
import static android.media.CamcorderProfile.get;


public class JadeSocketThread extends Thread {

    static final String TAG = JadeSocketThread.class.getSimpleName();
    private static final boolean DBG = BuildConfig.DEBUG & true;

    private BluetoothSocket mmSocket = null;
    private InputStream rx = null; //mmInStream
    private OutputStream tx = null; //mmOutStream
    private String dataCB;
    private Boolean roverMode = false;

    public JadeSocketThread(BluetoothSocket socket) { //, Handler _handler) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        connectSocket();
        treamReading(tmpIn, tmpOut);
    }

    private void treamReading(InputStream tmpIn, OutputStream tmpOut) {
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = mmSocket.getInputStream();
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) {
            Log.i(TAG, "get input/output for temp: Exception thrown during stream: " + e.getMessage());
        }
        rx = tmpIn;
        tx = tmpOut;
    }

    //TODO: socket Connecting
    private void connectSocket() {
        try {
            mmSocket.connect();
            Log.i(TAG, "connect(): CONNECTED!");

        } catch (Exception e) {

            try {
                Log.i(TAG, "connect(): Exception thrown during connect: " + e.getMessage());
                mmSocket.close();
                return;
            } catch (IOException s) {
                Log.i(TAG, "close(): Exception thrown during close: " + s.getMessage());
            }
        }
    }

    //    TODO: set mode for jade
    public void setRoverMode(Boolean mode) {
        this.roverMode = mode;
    }

    //TODO: get dataCB from read stream
    public String getDataCB() {

//        Log.wtf(TAG,"before dataCB" + dataCB);
        this.dataCB = read();
        Log.i(TAG, "getDataCB():" + dataCB);
        return this.dataCB;
    }

    //TODO: read RX fro BT
    public String read() {


//        ArrayList dataCB = new ArrayList<>();

//        if (roverMode) {
//            try {
//                Thread.sleep(700);
//                buffer = new byte[mmSocket.getInputStream().available()];
//            } catch (Exception e) {
//            }
//        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bytes = 0;
        byte[] buffer;

        while (true) {
            try {

                Thread.sleep(700);
                // update rx after sent tx
                rx = mmSocket.getInputStream();
                Log.i(TAG, "get update rx after write tx:" + rx.toString());

                bytes = rx.available();
                buffer = new byte[bytes];
                Log.i(TAG, "new buffer: " + Integer.toString(buffer.length));
                if (bytes != 0) {
                    bytes += rx.read(buffer);
//                    bytes += rx.read(buffer, bytes, bytes);
//                    bytes += rx.read(buffer, bytes, buffer.length - bytes);
                    Log.i(TAG, "bytes read: " + Integer.toString(bytes));
                    byteArrayOutputStream.write(buffer);
                } else {
                    break;
                }
            } catch (Exception e) {
                Log.i(TAG, "read(): " + e.getMessage());
                cancel();
                break;
            }
        }

        final byte[] dataCB = byteArrayOutputStream.toByteArray();

        if (roverMode) {
//            dataCB.get(0);
            ArrayList k = Ultis.getDataPacket(dataCB);
            k.get(0);
            k.get(1);
            k.get(2);
            k.get(3);
            Ultis.convertArrayBufferToString((byte[])k.get(0));
            Ultis.byteArrayToImg((byte[])k.get(2));
            Log.i("rover mode","test");
        }
//            return dataCB after convert it

        return Ultis.convertArrayBufferToString(dataCB);
//        return "test"+"\\r";

    }

    //    TODO: send TX to BT
    public void write(String input) {
        byte[] bytes = Ultis.convertStringToArrayBuffer(input);
        try {
            tx.write(bytes);
            Log.i(TAG, "write(String input):" + bytes.toString());

        } catch (IOException e) {
            Log.i(TAG, "error write()" + e.getMessage());
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            dataCB = null;
            tx.close();
            rx.close();
            tx = null;
            rx = null;

            mmSocket.close();
            mmSocket = null;
            Log.i(TAG, "cancel()");
        } catch (IOException e) {
            Log.wtf(TAG, e.getMessage());
        }
    }


}




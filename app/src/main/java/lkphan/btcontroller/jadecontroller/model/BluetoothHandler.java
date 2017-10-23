package lkphan.btcontroller.jadecontroller.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.UUID;

import lkphan.btcontroller.jadecontroller.ultis.Ultis;

import static android.R.attr.data;
import static android.R.attr.handle;
import static android.support.v7.appcompat.R.id.up;

/**
 * Created by kimiboo on 2017-10-06.
 */

public class BluetoothHandler {

    private static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private boolean mIsRoverMode = false;
    private boolean mIsHangeOver;
    private int mState;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private BluetoothListener mBluetoothListener;
    private final String NAME = "BluetoothHandler";

    private final BluetoothAdapter mBluetoothAdapter;
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection

    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    public static final int STATE_RECONNECTED = 4;  // now Reconnected to a remote device
    private static final String TAG = "BluetoothHandler";
    private static final int SMALL_BUFF = 1024;
    private static final int BIG_BUFF = 1024 * 64;
    byte[] smallByte;
    byte[] bigByte;


    public BluetoothHandler(BluetoothListener bluetoothListener, boolean isRoverMode) {
        mIsRoverMode = isRoverMode;
        mBluetoothListener = bluetoothListener;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothHandler newInstance(BluetoothListener bluetoothListener, boolean isRoverMode) {
        return new BluetoothHandler(bluetoothListener, isRoverMode);
    }

    public Boolean getROVER_MODE() {
        return this.mIsRoverMode;
    }

    public void setROVER_MODE(Boolean toggle) {
        this.mIsRoverMode = toggle;
    }

    public synchronized void start() {
        setState(STATE_LISTEN);
        resetThreads(true, true, false);
//        if (mAcceptThread == null) {
//            mAcceptThread = new AcceptThread();
//            mAcceptThread.start();
//        }
    }

    public synchronized void connect(BluetoothDevice bluetoothDevice) {
        resetThreads(mState == STATE_CONNECTING, true, false);
        mConnectThread = new ConnectThread(bluetoothDevice);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    private void connected(BluetoothSocket bluetoothSocket, final BluetoothDevice remoteDevice) {
        ThreadHandler.getInstance().doInForground(new Runnable() {
            @Override
            public void run() {
                mBluetoothListener.onConnected(remoteDevice);
            }
        });

        setState(STATE_CONNECTED);
        resetThreads(true, true, true);
        if (mConnectedThread == null) {
            mConnectedThread = new ConnectedThread(bluetoothSocket);
            mConnectedThread.start();
        }
    }

    private void connectionFailed() {
        ThreadHandler.getInstance().doInForground(new Runnable() {
            @Override
            public void run() {
                mBluetoothListener.connectionFailed();
            }
        });
        start();
    }

    public synchronized void stop() {
        setState(STATE_NONE);
        resetThreads(true, true, true);
    }


    public synchronized void write(String strCmd) {

        byte[] cmdByte = Ultis.convertStringToArrayBuffer(strCmd);
        synchronized (this) {
            if (mState != STATE_CONNECTED) {
                return;
            }
            mConnectedThread.write(cmdByte);
        }
    }


    private void resetThreads(boolean connect, boolean connected, boolean accept) {
        if (connect && mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (connected && mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if (accept && mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
    }


    public void setState(int state) {
        mState = state;
    }

    public int getState() {
        return mState;
    }

    /**
     * This thread is to connect to a device
     */
    public class ConnectThread extends Thread {

        private BluetoothDevice mBluetoothDevice;
        private BluetoothSocket mBluetoothSocket;

        public ConnectThread(BluetoothDevice bluetoothDevice) {
            mBluetoothDevice = bluetoothDevice;
            try {
//                mBluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                /*UUID uuid = bluetoothDevice.getUuids()[0].getUuid();
                MY_UUID = uuid;*/
                mBluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try {
                mBluetoothSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    mBluetoothSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                connectionFailed();
                return;
            }

            synchronized (BluetoothHandler.this) {
                mConnectThread = null;
            }
            connected(mBluetoothSocket, mBluetoothDevice);
        }

        public void cancel() {
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * This thread works after establishing bluetooth connection
     */
    public class ConnectedThread extends Thread {

        private InputStream mInputStream;
        private OutputStream mOutputStream;
        private BluetoothSocket mBluetoothSocket;

        public ConnectedThread(BluetoothSocket socket) {
            mBluetoothSocket = socket;
            try {
                mInputStream = mBluetoothSocket.getInputStream();
                mOutputStream = mBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] data;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (mBluetoothSocket.isConnected()) {
                try {

                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {

                    }
                    int available = mInputStream.available();
                    if (available > 0) {
                        data = new byte[available];
                        mInputStream.read(data);
                        byteArrayOutputStream.write(data);
                        if (mIsRoverMode) {

                            if (Ultis.isEOI(byteArrayOutputStream.toByteArray())) {
                                try {
                                    byte[] streamData = Ultis.getStandardPacket(byteArrayOutputStream.toByteArray());
                                    mBluetoothListener.onReceivedData(streamData);
//                                    String savedFile = Ultis.testRenderBmpArray(streamData);
//                                    Log.i(TAG, "saved img " + savedFile);
                                } finally {
                                    byteArrayOutputStream.reset();
                                }
                            }
                        } else {
                            String receivedKey = Ultis.convertArrayBufferToString(data);
                            final String msg = receivedKey;
                            ThreadHandler.getInstance().doInForground(new Runnable() {
                                @Override
                                public void run() {
                                    mBluetoothListener.onGotCallback(msg);
                                }
                            });
//                            Log.i(TAG,"out of Rover sent to onGotCallback" + msg);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                    BluetoothHandler.this.start();
                    ThreadHandler.getInstance().doInForground(new Runnable() {
                        @Override
                        public void run() {
                            mBluetoothListener.onLostConnection();
                        }
                    });
                    break;
                } finally {
//                    data = null;
                }
            }
        }

        public void write(byte[] cmd) {
            try {
                mOutputStream.write(cmd);
                mOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
                mBluetoothListener.onLostConnection();
            }
        }

        public void cancel() {
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This thread will be listening to new connections
     */
    public class AcceptThread extends Thread {

        private BluetoothServerSocket mBluetoothServerSocket;

        public AcceptThread() {
            try {
                mBluetoothServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            BluetoothSocket socket = null;
            while (mState != STATE_CONNECTED) {
                try {
                    socket = mBluetoothServerSocket.accept();
                    Log.i(TAG, "Socket accepted");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (socket != null) {
                    switch (mState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            connected(socket, socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            Log.i(TAG, "Socket close failed");
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Could not close unwanted socket", e);
                            }
                            break;
                    }
                }
            }
        }

        public void cancel() {
            try {
                mBluetoothServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

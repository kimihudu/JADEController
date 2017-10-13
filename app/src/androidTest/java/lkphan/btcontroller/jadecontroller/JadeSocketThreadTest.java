package lkphan.btcontroller.jadecontroller;

import android.bluetooth.BluetoothSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;

import lkphan.btcontroller.jadecontroller.model.JadeSocketThread;

/**
 * Created by kimiboo on 2017-09-22.
 */
public class JadeSocketThreadTest {

    private JadeSocketThread testThread;
    private BluetoothSocket mmSocket = null;
    private InputStream rx = null; //mmInStream
    private OutputStream tx = null; //mmOutStream
    private String dataCB;


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getDataCB() throws Exception {

    }

    @Test
    public void read() throws Exception {

    }

    @Test
    public void write() throws Exception {

    }

    @Test
    public void cancel() throws Exception {

    }

}
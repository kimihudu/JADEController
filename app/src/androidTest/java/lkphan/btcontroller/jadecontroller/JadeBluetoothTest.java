package lkphan.btcontroller.jadecontroller;

import org.junit.Before;
import org.junit.Test;

import lkphan.btcontroller.jadecontroller.model.JadeBluetooth;

/**
 * Created by kimiboo on 2017-09-22.
 */
public class JadeBluetoothTest {

    private JadeBluetooth btJade;
    private String address  = "00:12:6F:2F:AA:CF";//"test";//
    private String strUUID  = "00001101-0000-1000-8000-00805f9b34fb";
    private String cmd      = "*\r";

    @Before
    public void setUp(){
        btJade = new JadeBluetooth(address,strUUID);
    }

    @Test
    public void getDataCB() throws Exception {
        btJade.start();
        btJade.getDataCB();
    }

    @Test
    public void start() throws Exception {
        btJade.start();
    }

    @Test
    public void sendCommand() throws Exception {
        btJade.start();
        btJade.sendCommand(cmd);
    }

    @Test
    public void stop() throws Exception {
        btJade.start();
        btJade.stop();
    }

}
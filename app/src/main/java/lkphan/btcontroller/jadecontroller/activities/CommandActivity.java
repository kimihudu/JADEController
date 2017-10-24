package lkphan.btcontroller.jadecontroller.activities;

import android.bluetooth.BluetoothDevice;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lkphan.btcontroller.jadecontroller.R;
import lkphan.btcontroller.jadecontroller.model.*;
import lkphan.btcontroller.jadecontroller.ultis.*;

import static android.media.CamcorderProfile.get;

public class CommandActivity extends AppCompatActivity {

    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection

    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    public static final int STATE_RECONNECTED = 4;  // now Reconnected to a remote device

    private BluetoothDevice mBluetoothDevice;
    private BluetoothHandler mBluetoothHandler;
    private LinearLayout left_side;
    private LinearLayout right_side;
    private LinearLayout number;
    private Spinner spinnerCmd;
    private Button btnSend;
    private Button btnClearLog;
    private Button btnGui;
    private CheckBox cbxToggleConnect;
    private TextView mBluetoothStatus;
    String cmd = "";
    String deviceName = "";
    String mode = "debug";
    Boolean CAM_ON      = false;
    Boolean ROVER_ON    = false;
    Boolean isRoverMde  = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);

        left_side = (LinearLayout) findViewById(R.id.left_side);
        right_side = (LinearLayout) findViewById(R.id.right_side);
        number = (LinearLayout) findViewById(R.id.number);

        addItemsOnSpinner();
        addListenerOnSpinnerItemSelection();
//TODO: get btJade
        mBluetoothDevice = getIntent().getExtras().getParcelable("data");
//        TODO: create BluetoothHandler with selected btJade
        mBluetoothHandler = BluetoothHandler.newInstance(new BluetoothListener() {
            String datacb = "";

            //            TODO: get data callback from Rover Mode
            @Override
            public void onReceivedData(ArrayList data) {
                try {


                } catch (Exception e) {
                }

            }

            @Override
            public void onConnected(BluetoothDevice device) {
                Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void connectionFailed() {

            }

            @Override
            public void onLostConnection() {

            }

            //TODO: get data callback fro Cmd Mode
            @Override
            public void onGotCallback(String ack) {
//                Toast.makeText(getApplicationContext(),ack,Toast.LENGTH_LONG).show();
                addLog(cmd, ack);
//                Log.e("bluetoothHandler", ack);
//                if (ack.equals(BluetoothHandler.ACK_INIT)) {
//                    mInitAck = true;
//                    mBluetoothHandler.write(cmdByte);
//                    mBluetoothHandler.write(BluetoothHandler.DATA_END.getBytes());
//                } else if (ack.equals(BluetoothHandler.ACK_DATA_RECEIVED)) {
//                    mDataAck = true;
//                }
            }
        }, false);

        mBluetoothHandler.start();
        mBluetoothHandler.connect(mBluetoothDevice);

        cbxToggleConnect = (CheckBox) findViewById(R.id.cbxToggleConnect);
        cbxToggleConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Boolean toggle = cbxToggleConnect.isChecked(); //--> change status when clicked
                if (toggle) {

//                    reconnect bt device
                    mBluetoothHandler.start();
                    mBluetoothHandler.connect(mBluetoothDevice);

                    cbxToggleConnect.setText("Connected");
                    cbxToggleConnect.setChecked(true);
                } else {
                    mBluetoothHandler.stop();
                    cbxToggleConnect.setText("DisConnected");
                    cbxToggleConnect.setChecked(false);
                }
            }
        });

        btnGui = (Button) findViewById(R.id.btnGui);
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBluetoothHandler.stop();
                Ultis.gotoActivity(getApplicationContext(), GuiActivity.class, mBluetoothDevice);
            }
        });

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cmd != "") {

                    switch (cmd){
                        case CommandList.CAM_ON:
                            CAM_ON = true;
                            break;
                        case CommandList.R_ROVER_START:
                            ROVER_ON = true;
                            break;
                        case CommandList.R_ROVER_STOP:
                            ROVER_ON = false;
                            break;
                    }

                    if (CAM_ON && ROVER_ON){
                        isRoverMde = true;
                    }else{
                        isRoverMde = false;
//                        if (mBluetoothHandler.getState() == STATE_CONNECTED){
//                            mBluetoothHandler.write(CommandList.A_TERMINATE);
//                        }
                    }

                    mBluetoothHandler.setROVER_MODE(isRoverMde);
//                    TODO: send cmd
                    mBluetoothHandler.write(cmd);
                    Toast.makeText(getBaseContext(), "sending...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnClearLog = (Button) findViewById(R.id.btnClear);
        btnClearLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = left_side.getChildCount();
                for (int i = 1; i < count; i++) {
                    right_side.focusableViewAvailable(right_side.getChildAt(1));
                    left_side.focusableViewAvailable(left_side.getChildAt(1));
                    number.focusableViewAvailable(number.getChildAt(1));

                    right_side.removeViewAt(1);
                    left_side.removeViewAt(1);
                    number.removeViewAt(1);
                }

            }
        });
        mBluetoothStatus = (TextView) findViewById(R.id.btName);
        mBluetoothStatus.setText("Connected to Device: " + deviceName);
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {

        spinnerCmd = (Spinner) findViewById(R.id.spinnerCmd);
        List<String> list = new ArrayList<String>();

        list = Ultis.getFieldObj(CommandList.class);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.preference_category);
        spinnerCmd.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinnerCmd = (Spinner) findViewById(R.id.spinnerCmd);
        spinnerCmd.setOnItemSelectedListener(cmdClickListener);
    }

    private AdapterView.OnItemSelectedListener cmdClickListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();
            String selectedCmd = parent.getItemAtPosition(pos).toString();
//            get value cmd of object's field
            cmd = Ultis.getVoField(CommandList.class, selectedCmd, "String").toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };

    public void addLog(String tx, String rx) {

        TextView TX = new TextView(this);
        TextView RX = new TextView(this);
        TextView NUMBER = new TextView(this);

        int tmp = 0;
        if (left_side.getChildCount() > 0) {
            tmp = left_side.getChildCount();
        }

        NUMBER.setText(Integer.toString(tmp));
        if (tx.contains("\r")) {
            TX.setText(tx + "\\r");
            TX.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorBlue));
        }

        if (rx.contains("\r")) {
            RX.setText(rx + "\\r");
            RX.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorRed));
        }

        number.addView(NUMBER);
        left_side.addView(TX);
        right_side.addView(RX);
    }
}

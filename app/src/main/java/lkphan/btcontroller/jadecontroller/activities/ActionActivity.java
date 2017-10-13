package lkphan.btcontroller.jadecontroller.activities;

import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import lkphan.btcontroller.jadecontroller.model.CommandList;
import lkphan.btcontroller.jadecontroller.model.JadeBluetooth;
import lkphan.btcontroller.jadecontroller.ultis.Ultis;

public class ActionActivity extends AppCompatActivity {

    private BluetoothDevice mBluetoothDevice;
    private LinearLayout left_side;
    private LinearLayout right_side;
    private LinearLayout number;
    private Spinner spinnerCmd;
    private Button btnSend;
    private Button btnClearLog;
    private Button btnGui;
    private JadeBluetooth btJade;
    private CheckBox cbxToggleConnect;
    private TextView mBluetoothStatus;
    String cmd          = "";
    String macAdd       = "";
    String mUUID        = "";
    String deviceName   = "";
    String mode         = "debug";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        left_side   = (LinearLayout) findViewById(R.id.left_side);
        right_side  = (LinearLayout) findViewById(R.id.right_side);
        number      = (LinearLayout) findViewById(R.id.number);

        addItemsOnSpinner();
        addListenerOnSpinnerItemSelection();

//        TODO: get bt info from previous activity
        macAdd      = getIntent().getStringExtra("address");
        mUUID       = getIntent().getStringExtra("BTMODULEUUID");
        deviceName  = getIntent().getStringExtra("name");


//        TODO: create asynctask for connect bt

            JadeTask newConnect = new JadeTask();
            newConnect.execute(macAdd, mUUID);


        btnGui = (Button)findViewById(R.id.btnGui);
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ultis.gotoActivity(getApplicationContext(),GuiControl.class,mBluetoothDevice);
            }
        });

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cmd != "") {

//                    TODO: create asynctask for sending cmd
                    JadeCmdTask sendCmd = new JadeCmdTask();
                    sendCmd.execute(cmd);
                    Toast.makeText(getBaseContext(), "sending...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnClearLog = (Button) findViewById(R.id.btnClear);
        btnClearLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = left_side.getChildCount();
                for (int i = 1; i < count; i++){
                    right_side.focusableViewAvailable(right_side.getChildAt(1));
                    left_side.focusableViewAvailable(left_side.getChildAt(1));
                    number.focusableViewAvailable(number.getChildAt(1));

                    right_side.removeViewAt(1);
                    left_side.removeViewAt(1);
                    number.removeViewAt(1);
                }

            }
        });

        cbxToggleConnect = (CheckBox) findViewById(R.id.cbxToggleConnect);
        cbxToggleConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Boolean toggle = cbxToggleConnect.isChecked(); //--> change status when clicked
                if (toggle) {

//                    reconnect bt device
                    JadeTask newConnect = new JadeTask();
                    newConnect.execute(macAdd, mUUID);

                    cbxToggleConnect.setText("Connected");
                    cbxToggleConnect.setChecked(true);
                } else {
                    btJade.stop();
                    cbxToggleConnect.setText("DisConnected");
                    cbxToggleConnect.setChecked(false);
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
        if (tx.contains("\r")){
            TX.setText(tx + "\\r");
            TX.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorBlue));
        }

        if(rx.contains("\r")){
            RX.setText(rx + "\\r");
            RX.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorRed));
        }

        number.addView(NUMBER);
        left_side.addView(TX);
        right_side.addView(RX);
    }

    private class JadeTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            btJade = new JadeBluetooth(strings[0], strings[1]);
            btJade.start();
            return true;
        }
    }

    private class JadeCmdTask extends AsyncTask<String, String, Boolean> {


        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                btJade.sendCommand(strings[0]);
            }catch (Exception e){
                Log.i("JadeCmdTask","JadeCmdTask" + e.getMessage());
            }
            return true;
        }

//        waiting for doInBackground complete
        @Override
        protected void onPostExecute(final Boolean success) {


            /* terminate when script executing

            *if(btJade.getDataCB().contains("Script Executing")) {
                Toast.makeText(getBaseContext(),"Script Executing",Toast.LENGTH_LONG).show();
                return;
            }

            * */

            addLog(cmd,btJade.getDataCB());
        }
    }

}

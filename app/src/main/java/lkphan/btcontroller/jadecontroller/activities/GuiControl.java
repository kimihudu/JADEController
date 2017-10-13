package lkphan.btcontroller.jadecontroller.activities;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import lkphan.btcontroller.jadecontroller.R;
import lkphan.btcontroller.jadecontroller.model.CommandList;
import lkphan.btcontroller.jadecontroller.model.JadeBluetooth;
import lkphan.btcontroller.jadecontroller.ultis.Ultis;
import lkphan.btcontroller.jadecontroller.model.*;

import static android.R.attr.name;
import static android.R.id.toggle;

public class GuiControl extends AppCompatActivity {

    final String UP = CommandList.M_FORWARD;
    final String LEFT = CommandList.M_LEFT;
    final String RIGHT = CommandList.M_RIGHT;
    final String REV = CommandList.M_REVERSE;

    final String CAM_ON = CommandList.CAM_ON;
    final String CAM_OFF = CommandList.CAM_OFF;

    final String GRIP_ON = CommandList.SERVO_ON;
    final String GRIP_OFF = CommandList.SERVO_OFF;

    final String ROVER_ON = CommandList.R_ROVER_START;
    final String ROVER_OFF = CommandList.R_ROVER_STOP;

    JadeBluetooth btJade;
    String cmd = "";
    String macAdd = "";
    String mUUID = "";
    String deviceName = "";
    String mode = "GUI";
    Button btnLeft;
    Button btnRight;
    Button btnUp;
    Button btnDown;
    Button btnDebugMode;
    CheckBox cbxCamOp;
    CheckBox cbxGripOp;
    CheckBox cbxRoverOp;
    CheckBox cbxToggleConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_control);

        //        TODO: get bt info from previous activity
        macAdd = getIntent().getStringExtra("address");
        mUUID = getIntent().getStringExtra("BTMODULEUUID");
        deviceName = getIntent().getStringExtra("name");


//        TODO: create asynctask for connect bt
        JadeTask newConnect = new JadeTask();
        newConnect.execute(macAdd, mUUID);



        addControl();
    }

    private void addControl() {

        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnUp = (Button) findViewById(R.id.btnUp);
        btnDown = (Button) findViewById(R.id.btnDown);
        btnDebugMode = (Button) findViewById(R.id.btnDebugMode);
        btnDebugMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Ultis.gotoActivity(getApplicationContext(), ActionActivity.class, mBluetoothDevice);
            }
        });

        cbxCamOp = (CheckBox) findViewById(R.id.cbxCamOp);
        cbxGripOp = (CheckBox) findViewById(R.id.cbxGripOp);
        cbxRoverOp = (CheckBox) findViewById(R.id.cbxRoverOp);
        cbxToggleConnect = (CheckBox) findViewById(R.id.cbxToggleConnect);


        btnUp.setOnClickListener(cmdMoveListener);
        btnDown.setOnClickListener(cmdMoveListener);
        btnLeft.setOnClickListener(cmdMoveListener);
        btnRight.setOnClickListener(cmdMoveListener);
        cbxCamOp.setOnClickListener(cmdOptionListener);
        cbxGripOp.setOnClickListener(cmdOptionListener);
        cbxRoverOp.setOnClickListener(cmdOptionListener);
        cbxToggleConnect.setOnClickListener(cmdOptionListener);
    }

    private View.OnClickListener cmdMoveListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            String selectedControl = ((Button) view).getText().toString();
            switch (selectedControl) {
                case "up":
                    cmd = UP;
                    break;
                case "down":
                    cmd = REV;
                    break;
                case "left":
                    cmd = LEFT;
                    break;
                case "right":
                    cmd = RIGHT;
                    break;
            }

            JadeCmdTask sendCmd = new JadeCmdTask();
            Toast.makeText(getBaseContext(), cmd, Toast.LENGTH_LONG).show();
            sendCmd.execute(cmd);
        }
    };

    private View.OnClickListener cmdOptionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String selectedControl = ((CheckBox) view).getText().toString();
            Boolean state = ((CheckBox) view).isChecked();

            switch (selectedControl) {
                case "Cam On/Off":
                    if (state)
                        cmd = CAM_ON;
                    else
                        cmd = CAM_OFF;
                    break;
                case "Grip On/Off":
                    if (state)
                        cmd = GRIP_ON;
                    else
                        cmd = GRIP_OFF;
                    break;
                case "Rover On/Off":
                    if (state)
                        cmd = ROVER_ON;
                    else
                        cmd = ROVER_OFF;
                    break;
                case "DisConnected":


//                  reconnect bt device
                    JadeTask newConnect = new JadeTask();
                    newConnect.execute(macAdd, mUUID);
                    cbxToggleConnect.setText("Connected");
                    cbxToggleConnect.setChecked(true);
                    break;
                case "Connected":
                    btJade.stop();
                    cbxToggleConnect.setText("DisConnected");
                    cbxToggleConnect.setChecked(false);
                    break;
            }

//            JadeCmdTask sendCmd = new JadeCmdTask();
//            Toast.makeText(getBaseContext(), cmd, Toast.LENGTH_LONG).show();
//            sendCmd.execute(cmd);
        }
    };

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
            try {
                btJade.sendCommand(strings[0]);
            } catch (Exception e) {
                Log.i("JadeCmdTask", "JadeCmdTask" + e.getMessage());
            }
            return true;
        }

        //        waiting for doInBackground complete
        @Override
        protected void onPostExecute(final Boolean success) {

            /* terminate when script executing*/
            if (btJade.getDataCB().contains("Script Executing")) {
                Toast.makeText(getBaseContext(), "Script Executing", Toast.LENGTH_LONG).show();
                btJade.sendCommand("halt\r");
                return;
            }

            Log.i("onPostExecute",btJade.getDataCB());


        }
    }
}

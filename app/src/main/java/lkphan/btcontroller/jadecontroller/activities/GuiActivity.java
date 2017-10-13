package lkphan.btcontroller.jadecontroller.activities;

import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

import lkphan.btcontroller.jadecontroller.R;
import lkphan.btcontroller.jadecontroller.model.*;
import lkphan.btcontroller.jadecontroller.ultis.*;

import static android.R.attr.data;
import static android.media.CamcorderProfile.get;

public class GuiActivity extends AppCompatActivity {

    private static final String TAG = GuiActivity.class.getName();
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

    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaRecorder mMediaRecorder;

    private BluetoothDevice mBluetoothDevice;
    private BluetoothHandler mBluetoothHandler;
    Button btnLeft;
    Button btnRight;
    Button btnUp;
    Button btnDown;
    Button btnDebugMode;
    CheckBox cbxCamOp;
    CheckBox cbxGripOp;
    CheckBox cbxRoverOp;
    CheckBox cbxToggleConnect;
    ImageView mImageView;
    String cmd = "";
    Boolean isCamOn     = false;
    Boolean isRoverOn   = false;


    Boolean isHangeOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui);
        addControl();

        //TODO: get btJade
        mBluetoothDevice = getIntent().getExtras().getParcelable("data");
//        TODO: create BluetoothHandler with selected btJade
        mBluetoothHandler = BluetoothHandler.newInstance(new BluetoothListener() {
            String datacb = "";

            //            TODO: get data callback from Rover Mode
            @Override
            public void onReceivedData(byte[] bytes) {
                try {
                    ArrayList dataPacket = Ultis.getDataPacket(bytes);

                    String strPrefix = Ultis.convertArrayBufferToString((byte[])dataPacket.get(0));
                    if (strPrefix.contains(Constant.STAT_IMG)){
                        Bitmap bitmap = (Bitmap) dataPacket.get(2);
                        printBitmap(bitmap);

                    }


                } catch (Exception e) {
                    Log.wtf("onReceivedData", e.getMessage());
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
                Toast.makeText(getApplicationContext(), ack, Toast.LENGTH_LONG).show();
                if (ack.equals(CommandList.CURRENT_STAT)) {
                    mBluetoothHandler.write(CommandList.A_TERMINATE);
                }


//                Log.i("onGotCallback", ack);
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
                mBluetoothHandler.stop();
                Ultis.gotoActivity(getApplicationContext(), CommandActivity.class, mBluetoothDevice);
            }
        });

        cbxCamOp = (CheckBox) findViewById(R.id.cbxCamOp);
        cbxGripOp = (CheckBox) findViewById(R.id.cbxGripOp);
        cbxRoverOp = (CheckBox) findViewById(R.id.cbxRoverOp);
        cbxToggleConnect = (CheckBox) findViewById(R.id.cbxToggleConnect);
        mImageView = (ImageView) findViewById(R.id.video);

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

            ThreadHandler.getInstance().doInForground(new Runnable() {
                @Override
                public void run() {
                    mBluetoothHandler.write(cmd);
                }
            });

        }
    };

    private View.OnClickListener cmdOptionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Boolean isRoverMde = false;


            String selectedControl  = ((CheckBox) view).getText().toString();
            Boolean state           = ((CheckBox) view).isChecked();

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
                    mBluetoothHandler.start();
                    mBluetoothHandler.connect(mBluetoothDevice);

                    cbxToggleConnect.setText("Connected");
                    cbxToggleConnect.setChecked(true);
                    break;
                case "Connected":
                    mBluetoothHandler.stop();
                    cbxToggleConnect.setText("DisConnected");
                    cbxToggleConnect.setChecked(false);
                    break;
            }

            //check isRoverMode
            switch (cmd) {
                case CAM_ON:
                    isCamOn = true;
                    break;
                case CAM_OFF:
                    isCamOn = false;
                    break;
                case ROVER_ON:
                    isRoverOn = true;
                    break;
                case ROVER_OFF:
                    isRoverOn = false;
                    break;
            }

            if (isCamOn && isRoverOn) {
                isRoverMde = true;


            }

            mBluetoothHandler.setROVER_MODE(isRoverMde);
            ThreadHandler.getInstance().doInForground(new Runnable() {
                @Override
                public void run() {
                    mBluetoothHandler.write(cmd);
                }
            });
        }
    };

    private class LoadBitmap extends AsyncTask<Bitmap, Bitmap, Bitmap> {


        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            try {
                Thread.sleep(300);
            } catch (Exception e) {
                Log.i("LoadBitmap", "LoadBitmap" + e.getMessage());
            }
            return bitmaps[0];
        }

        //        waiting for doInBackground complete
        @Override
        protected void onPostExecute(final Bitmap bitmap) {

            printBitmap(bitmap);
        }
    }

    private void printBitmap(final Bitmap bitmap) {
        if (bitmap != null) {
            Log.e(TAG, "Bitmap:" + bitmap.getWidth() + " - " + bitmap.getHeight() + " - " + bitmap.getByteCount());
            ThreadHandler.getInstance().doInForground(new Runnable() {
                @Override
                public void run() {

                    mImageView.setImageBitmap(bitmap);
//                    mImageView.invalidate();
                }
            });
        }
    }
}

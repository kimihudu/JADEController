package lkphan.btcontroller.jadecontroller.activities;

import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import lkphan.btcontroller.jadecontroller.R;
import lkphan.btcontroller.jadecontroller.model.*;
import lkphan.btcontroller.jadecontroller.ultis.*;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.cacheColorHint;
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
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    Camera mCamera;
    private Spinner spinnerCmd;
    String cmd = "";
    Boolean isCamOn = false;
    Boolean isRoverOn = false;


    Boolean isHangeOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui);
        addControl();

//        setCamera();

        //TODO: get btJade
        mBluetoothDevice = getIntent().getExtras().getParcelable("data");
//        TODO: create BluetoothHandler with selected btJade
        BTListenerTask btListenerTask = new BTListenerTask();
        btListenerTask.execute(mBluetoothDevice);

//        mBluetoothHandler = BluetoothHandler.newInstance(new BluetoothListener() {
//            String datacb = "";
//
//            //            TODO: get data callback from Rover Mode
//            @Override
//            public void onReceivedData(final byte[] bytes) {
//                try {
//
//                    ThreadHandler.getInstance().doInForground(new Runnable() {
//                        @Override
//                        public void run() {
//                            ArrayList dataPacket = Ultis.getDataPacket(bytes);
//
//                            String strPrefix = Ultis.convertArrayBufferToString((byte[]) dataPacket.get(0));
//                            if (strPrefix.contains(Constant.STAT_IMG)) {
//                                final Bitmap bitmap = (Bitmap) dataPacket.get(2);
//                                printBitmap(bitmap);
////                        Canvas canvas = mSurfaceHolder.lockCanvas();
////                        canvas.drawColor(Color.BLACK);
////                        canvas.drawBitmap(bitmap, 0, 0, new Paint());
////                        mSurfaceHolder.unlockCanvasAndPost(canvas);
//
////                        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
////                            @Override
////                            public void onPreviewFrame(byte[] bytes, Camera camera) {
////                                previewCallBack(bitmap);
////                            }
////                        });
////                        mCamera.startPreview();
//
//                            }
//                        }
//                    });
//
//
//                } catch (Exception e) {
//                    Log.wtf("onReceivedData", e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onConnected(BluetoothDevice device) {
//                Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void connectionFailed() {
//
//            }
//
//            @Override
//            public void onLostConnection() {
//
//            }
//
//            //TODO: get data callback fro Cmd Mode
//            @Override
//            public void onGotCallback(String ack) {
//                Toast.makeText(getApplicationContext(), ack, Toast.LENGTH_LONG).show();
//                if (ack.equals(CommandList.CURRENT_STAT)) {
//                    mBluetoothHandler.write(CommandList.A_TERMINATE);
//                }
//
//
////                Log.i("onGotCallback", ack);
////                if (ack.equals(BluetoothHandler.ACK_INIT)) {
////                    mInitAck = true;
////                    mBluetoothHandler.write(cmdByte);
////                    mBluetoothHandler.write(BluetoothHandler.DATA_END.getBytes());
////                } else if (ack.equals(BluetoothHandler.ACK_DATA_RECEIVED)) {
////                    mDataAck = true;
////                }
//            }
//        }, false);
//        mBluetoothHandler.start();
//        mBluetoothHandler.connect(mBluetoothDevice);
    }

//    private void setCamera() {
//        mCamera = Camera.open();
//        if (mCamera != null) {
//            mCamera.setDisplayOrientation(90);
//
//            Camera.Parameters parameters = mCamera.getParameters();
////            previewFormat = ImageFormat.NV21;
//            parameters.setPreviewSize(320, 240);
////            mSurfaceHolder.setFixedSize(width, height);
//            parameters.setRotation(90);
//            mCamera.setParameters(parameters);
//        }
//
//    }

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
//        mSurfaceView = (SurfaceView)findViewById(R.id.surfaceView);
//        mSurfaceHolder = mSurfaceView.getHolder();


        btnUp.setOnClickListener(cmdMoveListener);
        btnDown.setOnClickListener(cmdMoveListener);
        btnLeft.setOnClickListener(cmdMoveListener);
        btnRight.setOnClickListener(cmdMoveListener);
        cbxCamOp.setOnClickListener(cmdOptionListener);
        cbxGripOp.setOnClickListener(cmdOptionListener);
        cbxRoverOp.setOnClickListener(cmdOptionListener);
        cbxToggleConnect.setOnClickListener(cmdOptionListener);

        addItemsOnSpinner();
        addListenerOnSpinnerItemSelection();

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

            mBluetoothHandler.write(cmd);

        }
    };

    private View.OnClickListener cmdOptionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Boolean isRoverMde = false;

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
                    mBluetoothHandler.start();
                    mBluetoothHandler.connect(mBluetoothDevice);

                    cbxToggleConnect.setText("Connected");
                    cbxToggleConnect.setChecked(true);
                    break;
                case "Connected":
                    mBluetoothHandler.stop();
                    cbxToggleConnect.setText("DisConnected");
                    cbxToggleConnect.setChecked(false);
                    cbxRoverOp.setChecked(false);
                    cbxCamOp.setChecked(false);
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

            mBluetoothHandler.write(RoverModeCmd.CAM_RES);
            mBluetoothHandler.setROVER_MODE(isRoverMde);
            mBluetoothHandler.write(cmd);
        }
    };

    // add items into spinner dynamically
    public void addItemsOnSpinner() {

        spinnerCmd = (Spinner) findViewById(R.id.spinnerCmd);
        List<String> list = new ArrayList<String>();

        list = Ultis.getFieldObj(RoverModeCmd.class);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.preference_category);
        spinnerCmd.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinnerCmd = (Spinner) findViewById(R.id.spinnerCmd);
        spinnerCmd.setOnItemSelectedListener(cmdClickListener);
    }

    //    TODO: select cmd and execute
    private AdapterView.OnItemSelectedListener cmdClickListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();
            String selectedCmd = parent.getItemAtPosition(pos).toString();
//            get value cmd of object's field
            String _cmd = Ultis.getVoField(RoverModeCmd.class, selectedCmd, "String").toString();
            if (!_cmd.equals(RoverModeCmd.ADEFAULT)){

                if (_cmd.equals(ROVER_OFF) || _cmd.equals(RoverModeCmd.A_TERMINATE)){
                    cbxRoverOp.setChecked(false);
                    mBluetoothHandler.setROVER_MODE(false);
                }else if (_cmd.equals(ROVER_ON)){
                    cbxRoverOp.setChecked(true);
                    mBluetoothHandler.setROVER_MODE(true);
                }

                mBluetoothHandler.write(_cmd);

            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };

    //TODO: load bitmap to Picasso for refreshing
    private void printBitmap(final Bitmap bitmap) {
        if (bitmap != null) {
            Log.e(TAG, "Bitmap:" + bitmap.getWidth() + " - " + bitmap.getHeight() + " - " + bitmap.getByteCount());
            ThreadHandler.getInstance().doInForground(new Runnable() {
                @Override
                public void run() {
//                    Uri file = Ultis.getImageUri(getApplicationContext(), bitmap);
//                    Picasso picasso = Picasso.with(getApplicationContext());
//                    picasso.invalidate(file);
//                    picasso.load(file).into(mImageView);

                    mImageView.setImageBitmap(bitmap);
                    mImageView.invalidate();

                }
            });
        }
    }

    private class BTListenerTask extends AsyncTask<BluetoothDevice, BluetoothDevice, Boolean> {

        BluetoothDevice bluetoothDevice;
        @Override
        protected Boolean doInBackground(BluetoothDevice... bluetoothDevices) {
            try {
                Thread.sleep(500);
            }catch (Exception e){}

            bluetoothDevice = bluetoothDevices[0];
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success){
            mBluetoothHandler = BluetoothHandler.newInstance(new BluetoothListener() {

                //            TODO: get data callback from Rover Mode
                @Override
                public void onReceivedData(final byte[] bytes) {
                    ArrayList dataPacket = Ultis.getDataPacket(bytes);
                    String prefix = Ultis.convertArrayBufferToString((byte[])dataPacket.get(0)) ;

                    switch (prefix){
                        case Constant.ROVER_IMG:
                            Bitmap bitmap = (Bitmap) dataPacket.get(2);
                            printBitmap(bitmap);
                            break;
                    }
                    Log.i("onReceivedData","prefix:" +prefix);

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
                }
            }, false);
            mBluetoothHandler.start();
            mBluetoothHandler.connect(bluetoothDevice);
        }
    }

}

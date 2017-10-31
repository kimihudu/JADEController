package lkphan.btcontroller.jadecontroller.activities;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.SyncStateContract;
import android.support.annotation.MainThread;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimingLogger;
import android.view.Choreographer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import lkphan.btcontroller.jadecontroller.R;
import lkphan.btcontroller.jadecontroller.model.*;
import lkphan.btcontroller.jadecontroller.ultis.*;

import static android.R.attr.bitmap;
import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.cacheColorHint;
import static android.R.attr.data;
import static android.R.attr.start;
import static android.R.attr.y;
import static android.media.CamcorderProfile.get;
import static android.support.v7.appcompat.R.id.image;

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
    ArrayList<String> listImg = new ArrayList<>();
    Button btnLeft;
    Button btnRight;
    Button btnUp;
    Button btnDown;
    Button btnDebugMode;
    Button btnTestFrame;
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
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    AnimationDrawable animationDrawable;
    Boolean isHangeOver = false;
    LinearLayout imgHolder;
    Choreographer mChoreographer;

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

    }

    private void addControl() {


        imgHolder = (LinearLayout) findViewById(R.id.img_holder);
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

        btnTestFrame = (Button) findViewById(R.id.btnTestFrame);
        btnTestFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBluetoothHandler.stop();
                Intent i = new Intent(getApplicationContext(), TestActivity.class);
                i.putExtra("listImg", listImg);
                startActivity(i);

            }
        });

        cbxCamOp = (CheckBox) findViewById(R.id.cbxCamOp);
        cbxGripOp = (CheckBox) findViewById(R.id.cbxGripOp);
        cbxRoverOp = (CheckBox) findViewById(R.id.cbxRoverOp);
        cbxToggleConnect = (CheckBox) findViewById(R.id.cbxToggleConnect);
        mImageView = (ImageView) findViewById(R.id.video);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.RGB_565);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mSurfaceView.setWillNotDraw(false);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });


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

        mChoreographer = Choreographer.getInstance();
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

            Boolean isRoverMde = true;

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

            if (!(isCamOn && isRoverOn)) {
                isRoverMde = false;
            }

            mBluetoothHandler.write(RoverModeCmd.CAM_RES);
            mBluetoothHandler.write(cmd);
            mBluetoothHandler.setROVER_MODE(isRoverMde);
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
            if (!_cmd.equals(RoverModeCmd.ADEFAULT)) {

                if (_cmd.equals(ROVER_OFF) || _cmd.equals(RoverModeCmd.A_TERMINATE)) {
                    cbxRoverOp.setChecked(false);
                } else if (_cmd.equals(ROVER_ON)) {
                    cbxRoverOp.setChecked(true);
                }

                mBluetoothHandler.write(RoverModeCmd.CAM_RES);
                mBluetoothHandler.write(_cmd);
                mBluetoothHandler.setROVER_MODE(cbxRoverOp.isChecked());

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
//            Log.e(TAG, "Bitmap:" + bitmap.getWidth() + " - " + bitmap.getHeight() + " - " + bitmap.getByteCount());

            mImageView.setImageBitmap(bitmap);
            mImageView.invalidate();
        }
    }

    private class BTListenerTask extends AsyncTask<BluetoothDevice, Bitmap, Boolean> {
        TimingLogger tm = new TimingLogger("fps", "UI thread");
        Bitmap img;

        protected void onProgressUpdate(final Bitmap... bitmaps) {
            super.onProgressUpdate(bitmaps);
//            img = (Bitmap) objects[0];
            mImageView.setImageBitmap(bitmaps[0]);
            mImageView.invalidate();
            tm.addSplit("done with main bmp");

            new CountDownTimer(300,100) {

                public void onTick(long millisUntilFinished) {
//                                            long secondUntilFinished = (long) (millisUntilFinished/1000);
//                                            long secondsPassed = (EndTime - secondUntilFinished);
//                                            long minutesPassed = (long) (secondsPassed/60);
//                                            secondsPassed = secondsPassed%60;
//                                            // So now at this point your time will be: minutesPassed:secondsPassed
//                                            mytextView.setText(String.format("%02d", minutesPassed) + ":" + String.format("%02d", secondsPassed));
//                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(img, Constant.IMG_WIDTH, Constant.IMG_HEIGHT, false);
//                    mImageView.setImageBitmap(scaledBitmap);
//                    mImageView.invalidate();
                }

                public void onFinish() {
//                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(img, Constant.IMG_WIDTH, Constant.IMG_HEIGHT, false);
                    mImageView.setImageBitmap(bitmaps[0]);
                    mImageView.invalidate();
                    tm.addSplit("done counter and display extra bmp");
                }
            }.start();




//            renderBitmap(bitmaps[0]);
//            printBitmap();

            tm.addSplit("done update UI");
            tm.dumpToLog();
        }

        @Override
        protected Boolean doInBackground(BluetoothDevice... bluetoothDevices) {

            try {
                mBluetoothHandler = BluetoothHandler.newInstance(new BluetoothListener() {

                    // TODO: get data callback from Rover Mode
                    @Override
                    public void onReceivedData(final ArrayList data) {
//                        movingEffect();
                        tm.addSplit("before get new datacallback");
//                        ArrayList dataPacket = Ultis.getDataPacket(bytes);
//                        String prefix = String.valueOf(data.get(0)) ;

                        img = (Bitmap) data.get(1);
                        tm.addSplit("done get datacallback from background thread");
                        publishProgress(img);
                        tm.addSplit("done push to progress");
//                        Log.i("onReceivedData", prefix);

//                        switch (prefix) {
//                            case Constant.ROVER_IMG:
//                                try {
//                                    img = (Bitmap) data.get(1);
////                                    String imgPath = data.get(1).toString();
//
////                                    tm.addSplit("done get file bmp");
//                                    publishProgress(img);
////                                    tm.addSplit("done push to progress");
//
//
////                                    tm.addSplit("updated a bitmap");
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                                break;
//                        }
                        tm.dumpToLog();
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
                mBluetoothHandler.connect(bluetoothDevices[0]);

//                atemplate to get data come from handler msg
//                Handler handlerUI = new Handler(Looper.getMainLooper()) {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        if (msg == null)
//                            return;
//                        Bundle tmp = msg.getData();
//                        ArrayList container = (ArrayList) tmp.get("dataCallback");
//                        img = (Bitmap) container.get(1);
//                        mImageView.setImageBitmap(img);
//                        mImageView.invalidate();
////                        publishProgress(img);
//                        tm.addSplit("done get msg from handler");
//                        tm.dumpToLog();
//
//                    }
//                };
//                mBluetoothHandler.setHandlerThread(handlerUI);
//                mBluetoothHandler.setHandlerThread(handlerUI);
            } catch (Exception e) {
            }
//            bluetoothDevice = bluetoothDevices[0];
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
        }
    }


    private void renderBitmap(final Bitmap bitmap) {

        if (bitmap == null)
            return;
        Canvas canvas = mSurfaceHolder.lockCanvas();
        try {
            canvas.drawBitmap(bitmap, 5, 5, new Paint());
        } finally {
            mSurfaceHolder.unlockCanvasAndPost(canvas);
            mSurfaceView.invalidate();
            bitmap.recycle();
        }
    }

    private void movingEffect() {
        GuiActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mImageView.setImageResource(R.mipmap.blur);
            }
        });
    }
}




package lkphan.btcontroller.jadecontroller.ultis;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lkphan.btcontroller.jadecontroller.model.*;


import static android.R.attr.data;
import static android.R.attr.offset;
import static android.R.id.mask;


/**
 * Created by kimiboo on 2017-09-20.
 */

public class Ultis {

    static final String TAG = Ultis.class.getSimpleName();

    //TODO: convert ArrayBuffer to String
    public static String convertArrayBufferToString(byte[] buff) {
        String strReturn = null;
        strReturn = new String(buff, StandardCharsets.UTF_8);
        return strReturn;
    }

    public static String byteArrayToString(byte[] data) {
        String response = Arrays.toString(data);

        String[] byteValues = response.substring(1, response.length() - 1).split(",");
        byte[] bytes = new byte[byteValues.length];

        for (int i = 0, len = bytes.length; i < len; i++) {
            bytes[i] = Byte.parseByte(byteValues[i].trim());
        }

        String str = new String(bytes);
        return str.toLowerCase();
    }

    //    TODO:Destructure datacallback then return the list
    public static ArrayList getDataPacket(byte[] data) {
        ArrayList dataCB = new ArrayList();
        String compare = convertArrayBufferToString(data);
        String _prefix = "";


        if (data == null || data.length == 0)
            return null;

        if (compare.contains(Constant.ROVER_IMG)) {
            _prefix = Constant.ROVER_IMG;
        } else if (compare.contains(Constant.ROVER_STAT)) {
            _prefix = Constant.ROVER_STAT;
        }

        byte[] datapacket = Arrays.copyOfRange(data, 8, data.length);
        String eoi = byte2Hex(datapacket[datapacket.length - 2]) + " " + byte2Hex(datapacket[datapacket.length - 1]);
        String soi = byte2Hex(datapacket[0]) + " " + byte2Hex(datapacket[1]) + " " + byte2Hex(datapacket[2]);

        dataCB.add(_prefix);

        if (soi.contains(Constant.SOI) && eoi.contains(Constant.EOI)) {
            dataCB.add(datapacket);
        } else
            dataCB.add(0);

        return dataCB;
    }

    public static Drawable byteArrayToImg(byte[] bytes) {
        Drawable d = Drawable.createFromStream(new ByteArrayInputStream(bytes), null);
        return d;
    }

    //TODO: convert String to ArrayBuffer
    public static byte[] convertStringToArrayBuffer(String msg) {
        return msg.getBytes();
    }

    //     TODO: get all fields inside class with filter
    public static List<String> getFieldObj(Class o) {

        List<String> list = new ArrayList<String>();

        for (Field field : o.getDeclaredFields()) {

            boolean isStatic = java.lang.reflect.Modifier.isStatic(field.getModifiers());
            boolean isFinal = java.lang.reflect.Modifier.isFinal(field.getModifiers());

            if (isFinal && isStatic) {
                list.add(field.getName());
            }
        }
        return list;
    }

    //    TODO: get all methods inside class
    public static List<String> getMethodObj(Class o) {

        List<String> list = new ArrayList<String>();

        for (Method method : o.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())
                    && method.getParameterTypes().length == 0
                    && method.getReturnType() != void.class
                    && (method.getName().startsWith("get") || method.getName().startsWith("is"))
                    ) {
                list.add(method.getName());
            }
        }
        return list;
    }

    //    TODO: get value of Field from object class
    public static Object getVoField(Class oClass, String _fieldName, String typeReturn) {

        Object oRturn = null;
//        inject String field to obj field
        try {
            Class _class = oClass;
            Field f = _class.getField(_fieldName);
            f.setAccessible(true);
            try {

                switch (typeReturn) {
                    case "String":
                        oRturn = f.get(_class).toString();
                        break;
                    //add other case for the other type field to return
                    default:
                        break;
                }

            } catch (IllegalAccessException i) {
                Log.e(TAG, "getVoField(Class oClass, String _fieldName, String typeReturn)" + i.getMessage());
            }

        } catch (NoSuchFieldException n) {
            Log.e(TAG, "getVoField(Class oClass, String _fieldName, String typeReturn)" + n.getMessage());
        }

        return oRturn;
    }

//    TODO:

    public static void waitTime(final long timer) {
        Timer timerObj = new Timer();
        TimerTask timerTaskObj = new TimerTask() {
            public void run() {
                //perform your action here
                Log.i(TAG, "waitTime " + Long.toString(timer));
            }
        };
        timerObj.schedule(timerTaskObj, 0, timer);
    }

    //TODO: go to activity with data
    public static void gotoActivity(Context context, Class actiClass, BluetoothDevice btJade) {
        try {
            Intent i = new Intent(context, actiClass);
            Bundle bundle = new Bundle();
            bundle.putParcelable("data", btJade);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            btJade.stop();
            context.startActivity(i);
        } catch (Exception e) {
            Log.wtf("gotoActivity", e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }


    }

    //    TODO: print out screen
    public static void printScreen(Context context, String string, int duration) {
        Toast.makeText(context, string, duration).show();
    }

    public static byte[] getByteArrayFromString(String data) {
        return Base64.decode(data, Base64.DEFAULT);
    }

    public static String getStringFromByteArray(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }


    public static int byteArray2Int(byte[] bytes, int offset) {

        int ret = 0;
        for (int i = 0; i < 4 && i + offset < bytes.length; i++) {
            ret <<= 8;
            ret |= (int) bytes[i] & 0xFF;
        }
        return ret;
    }

    public static String byte2Hex(byte _byte) {
        int byteValue = _byte & 0xff;
        return String.format("0x%02x", byteValue);
    }

    public static String byte2Hex(byte[] _bytes) {
        String strReturn = "";
        for (byte b : _bytes) {

            int byteValue = b & 0xff;
            strReturn += String.format("0x%02x", byteValue) + " ";
        }

        return strReturn;
    }

    //TODO: get standard packet
    public static byte[] getStandardPacket(byte[] bytes) {
        int pos = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == Constant.COLON) {
                pos = i;
                break;
            }
        }
        return Arrays.copyOfRange(bytes, pos - 4, bytes.length);
    }

    //TODO: check EOI
    public static Boolean isEOI(byte[] bytes) {
//        byte[] eoi = Arrays.copyOfRange(bytes, bytes.length - 2, bytes.length);
        String eoi = byte2Hex(bytes[bytes.length - 2]) + " " + byte2Hex(bytes[bytes.length - 1]);
        if (eoi.contains(Constant.EOI))
            return true;
        return false;
    }

    //TODO: check standard packet
    public static Boolean isStandardPacket(byte[] bytes) {
        String compare = Ultis.convertArrayBufferToString(bytes);
        if (compare.contains(Constant.ROVER_ACTIVE) && compare.contains(Constant.ROVER_CAM_ACTIVE))
            return true;
        return false;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static byte[] copyByteArray(byte[] bytes, int from, int to) {
        return null;

//
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//        try {
//            byteArrayOutputStream.write(bytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        byte[] streamData = byteArrayOutputStream.toByteArray();
//
//        return Arrays.copyOfRange(byteArrayOutputStream.toByteArray(), from, to);

    }

    public static Bitmap byteArray2Bitmap(byte[] bytes) {

        String soi = byte2Hex(bytes[0]) + " " + byte2Hex(bytes[1]) + " " + byte2Hex(bytes[2]);
        String eoi = byte2Hex(bytes[bytes.length - 2]) + " " + byte2Hex(bytes[bytes.length - 1]);

        if (!((soi.equals(Constant.SOI) && eoi.equals(Constant.EOI))))
            return null;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opts.inMutable = true;
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, Constant.IMG_WIDTH, Constant.IMG_HEIGHT, false);
            return scaledBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    public static Bitmap bmpDecoder(byte[] bytes, Bitmap initBmp) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        opts.inJustDecodeBounds = false;
        opts.inMutable = true;
//        opts.inTempStorage = new byte[16*1024];
        opts.inPurgeable = true;
        opts.inPreferQualityOverSpeed = false;
        opts.inDither = false;
        opts.inBitmap = initBmp;
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, Constant.IMG_WIDTH, Constant.IMG_HEIGHT, true);
            return scaledBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static File saveBitmap(Bitmap bmp) {
        try {
            String filename;

            filename = Long.toString(SystemClock.elapsedRealtime());

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + filename + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
            return f;
        } catch (Exception e) {
        }

        return null;
    }

    public static String testRenderBmpArray(byte[] bytes) {
        if (bytes == null)
            return null;

        ArrayList dataPacket = getDataPacket(bytes);
        Bitmap img = Ultis.byteArray2Bitmap((byte[]) dataPacket.get(1));
        File savedBmp = Ultis.saveBitmap(img);

        return savedBmp.getAbsolutePath();

    }

}

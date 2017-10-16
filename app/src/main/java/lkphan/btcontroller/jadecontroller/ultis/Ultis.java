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
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
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

        if (data == null || data.length == 0)
            return null;
//check for non-standard data callback
        byte[] prefix = Arrays.copyOfRange(data, 0, 6);
        byte[] dataSize = Arrays.copyOfRange(data, 6, 9);
        byte[] datapacket = Arrays.copyOfRange(data, 9, data.length);
        byte[] eoi = Arrays.copyOfRange(datapacket, datapacket.length - 2, datapacket.length);
        byte[] soi = Arrays.copyOfRange(datapacket, 0, 2);
        Bitmap bmp = null;

        if (byte2Hex(soi).contains(Constant.SOI) && byte2Hex(eoi).contains(Constant.EOI)) {

            Log.i(TAG, "img packet: " + Arrays.toString(datapacket));

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            // Decode bitmap with inSampleSize set
            opts.inJustDecodeBounds = false;
            opts.inBitmap =
            bmp = BitmapFactory.decodeByteArray(datapacket, 0, datapacket.length, opts);
            int size = bmp.getHeight() * bmp.getRowBytes();
            Log.i(TAG,String.valueOf(size));

        }

        dataCB.add(prefix);
        dataCB.add(dataSize);
        dataCB.add(bmp);
        return dataCB;
    }

    public static Drawable byteArrayToImg(byte[] bytes) {
        Drawable d = Drawable.createFromStream(new ByteArrayInputStream(bytes), null);
        return d;
    }

    //TODO: convert String to ArrayBuffer
    public static byte[] convertStringToArrayBuffer(String msg) {

        byte[] buff = msg.getBytes();
        return buff;
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


    public static int arrayBufferToInt(byte[] bytes, int offset) {

//        BigInteger ui = new BigInteger(b); // let BigInteger do the work
//        int i = ui.intValue();
//        return i;

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
        byte[] returnBytes = null;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == Constant.COLON) {
                pos = i;
                break;
            }
        }
        returnBytes = Arrays.copyOfRange(bytes, pos - 5, bytes.length);
        return returnBytes;
    }

    //TODO: check EOI
    public static Boolean isEOI(byte[] bytes) {
        byte[] eoi = Arrays.copyOfRange(bytes, bytes.length - 2, bytes.length);
        if (byte2Hex(eoi).contains(Constant.EOI))
            return true;
        return false;
    }

    //TODO: check standard packet
    public static Boolean isStandardPacket(byte[] bytes) {
        byte first = bytes[0];
        byte second = bytes[1];
        if (first == second && first == 13)
            return false;

        return true;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}

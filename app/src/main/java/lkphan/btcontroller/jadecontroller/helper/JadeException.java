package lkphan.btcontroller.jadecontroller.helper;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


/**
 * Created by kimiboo on 2017-09-28.
 */

public class JadeException extends Application{

    @Override
    public void onCreate() {

        super.onCreate();
//        Thread.setDefaultUncaughtExceptionHandler(
//                new Thread.UncaughtExceptionHandler() {
//                    @Override
//                    public void uncaughtException (Thread thread, Throwable e) {
//                        handleUncaughtException (thread, e);
//                    }
//                });
    }

    private void handleUncaughtException (Thread thread, Throwable e) {

        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        if(isUIThread()){
            invokeLogActivity();
        }else{
            //handle non UI thread throw uncaught exception
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    invokeLogActivity();
                }
            });
        }
    }

    private boolean isUIThread(){
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    private void invokeLogActivity(){
        Intent intent = new Intent ();
        intent.setAction ("android.intent.action.SEND_LOG"); //
        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity (intent);
        System.exit(1); // kill off the crashed app
    }
}


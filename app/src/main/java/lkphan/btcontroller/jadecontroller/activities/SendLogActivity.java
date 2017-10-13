package lkphan.btcontroller.jadecontroller.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import lkphan.btcontroller.jadecontroller.R;

public class SendLogActivity extends Activity{

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature (Window.FEATURE_NO_TITLE); // make a dialog without a titlebar
        setContentView(R.layout.activity_send_log);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_cus_dialog);
        dialog.show();

        TextView msg = (TextView)dialog.findViewById(R.id.text_dialog);
        msg.setText("Send report to Mimetics");

        Button btnOk = (Button)dialog.findViewById(R.id.btn_dialog);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendEmailTask sendEmailTask = new SendEmailTask();
                sendEmailTask.execute();
            }
        });

    }

    private String extractLogToFile() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {}
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        // Make file name - file must be saved to external storage or it wont be readable by
        // the email app.
        File path = Environment.getExternalStorageDirectory(); //+ "/" + "MyApp/";
//        String path = Environment.getDataDirectory() + "/";
//        File path = getFilesDir();
        String fullName = path + "/Jade.txt";
//        String[] tmp = fullName.split("/data");

        // Extract to file.
        File file = new File(fullName);
        InputStreamReader reader = null;
        FileWriter writer = null;
        try {
            // For Android 4.0 and earlier, you will get all app's log output, so filter it to
            // mostly limit it to your app's output.  In later versions, the filtering isn't needed.
            String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
                    "logcat -d -v time JadeApp:v dalvikvm:v System.err:v *:s" :
                    "logcat -d -v time";

            // get input stream
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new InputStreamReader(process.getInputStream());

            // write output stream
            writer = new FileWriter(file);
            writer.write("Android version: " + Build.VERSION.SDK_INT + "\n");
            writer.write("Device: " + model + "\n");
            writer.write("App version: " + (info == null ? "(null)" : info.versionCode) + "\n");

            char[] buffer = new char[10000];
            do {
                int n = reader.read(buffer, 0, buffer.length);
                if (n == -1)
                    break;
                writer.write(buffer, 0, n);
            } while (true);

            reader.close();
            writer.close();
        } catch (IOException e) {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e1) {}

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e1) {}

            // You might want to write a failure message to the log here.
            return null;
        }

        return fullName;
    }

    private void sendLogFile ()
    {
        String fullName = extractLogToFile();
        if (fullName == null)
            return;

        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType ("plain/text");
        intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"lekien.phan@mimetics.ca"});
        intent.putExtra (Intent.EXTRA_SUBJECT, "JadeController log file");
        intent.putExtra (Intent.EXTRA_STREAM, Uri.parse ("file://" + fullName));
        intent.putExtra (Intent.EXTRA_TEXT, "Log file attached."); // do this so some email clients don't complain about empty body.
        startActivity (intent);

    }

    private class SendEmailTask extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            sendLogFile();

            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            dialog.dismiss();
            System.exit(1);
        }
    }

}

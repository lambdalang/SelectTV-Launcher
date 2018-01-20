package com.selecttvlauncher.tools;

import android.content.Context;
import android.util.Log;

import com.selecttvlauncher.LauncherApplication;

/**
 * Created by Ocs pl-79(17.2.2016) on 9/19/2016.
 */
public class ExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    private final Context myContext;
    private final String LINE_SEPARATOR = "\n";


    public ExceptionHandler(Context context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        Log.d("exception::::", ":::un caught exception");
        if (exception.getClass().equals(OutOfMemoryError.class)) {
            exception.printStackTrace();
            Log.d("exception::::", ":::memory exception");
            try {
                //android.os.Debug.dumpHprofData("/sdcard/dump.hprof");
                System.gc();
                Log.d("exception::::", ":::memory cleared");
                LauncherApplication.getInstance().cleardata();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Log.d(":::", ":::Free mem::" + Runtime.getRuntime().freeMemory());


            }
        } else {
            Log.d("exception::::", ":::other exception");
            exception.printStackTrace();
            //LauncherApplication.getInstance().cleardata();
        }

        /*Intent intent = new Intent(myContext, LoginActivity.class);

        myContext.startActivity(intent);
        //for restarting the Activity
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);*/


        /*try {
            Intent errorActivity = new Intent(myContext,LoginActivity.class);//this has to match your intent filter
            errorActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(myContext, 22, errorActivity, 0);
            try {
                pendingIntent.send();
            }
            catch (PendingIntent.CanceledException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);*/



       /* android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);*/
    }
}
package com.selecttvlauncher.network;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.selecttvlauncher.R;

/**
 * Created by Ocs pl-79(17.2.2016) on 8/5/2016.
 */
public class Helper {
    public static void alertSetAsDefaultLauncher(final Context c) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View dialogview = inflater
                .inflate(R.layout.alert_defaultlauncher, null);
        final AlertDialog alert = new AlertDialog.Builder(c).create();

        Button bSet = (Button) dialogview.findViewById(R.id.button1);
        Button bFinish = (Button) dialogview.findViewById(R.id.button2);

        bSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String currentDefaultLauncher = Helper.getDefaultLauncher(c);
                if (currentDefaultLauncher.equals("android") // means no
                        // current
                        // default
                        // launcher
                        || currentDefaultLauncher.equals(c.getPackageName())) {

                    clearCurrentDefaultLauncher(c);

                } else {
                    showInstalledAppDetails(c, currentDefaultLauncher);
                    Toast.makeText(c,
                            "CLEAR DEFAULTS the current launcher first",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        bFinish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (Helper.getDefaultLauncher(c).equals(c.getPackageName())) {

                    alert.dismiss();
                }

                else
                    Toast.makeText(c,
                            "Please set this app as a default launcher first!",
                            Toast.LENGTH_LONG).show();
            }
        });

        alert.setCancelable(false);
        alert.setView(dialogview);
        alert.show();
    }



   /* public static void alertExit(final Context c) {

        LayoutInflater inflater = LayoutInflater.from(c);
        View dialogview = inflater.inflate(R.layout.alert_menu_exit, null);
        final AlertDialog alert = new AlertDialog.Builder(c).create();

        Button bYes = (Button) dialogview.findViewById(R.id.button1);
        Button bNo = (Button) dialogview.findViewById(R.id.button2);

        bYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                clearCurrentDefaultLauncher(c);

                alert.dismiss();
            }
        });

        bNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alert.dismiss();
            }
        });

        alert.setView(dialogview);
        alert.show();
    }*/


    public static String getDefaultLauncher(Context c) {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        ResolveInfo resolveInfo = c.getPackageManager().resolveActivity(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;
        return currentHomePackage;
    }

    public static void clearCurrentDefaultLauncher(Context c) {
        Log.d("clear launcher", "::::clear launcher");
        c.getPackageManager().clearPackagePreferredActivities(
                c.getPackageName());
        // clear default launcher
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(home);

    }

    private static void showInstalledAppDetails(Context context,
                                                String packageName) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // above 2.3
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", packageName, null);
            intent.setData(uri);
        } else { // below 2.3
            final String appPkgName = (apiLevel == 8 ? "pkg"
                    : "com.android.settings.ApplicationPkgName");
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings",
                    "com.android.settings.InstalledAppDetails");
            intent.putExtra(appPkgName, packageName);
        }
        context.startActivity(intent);
    }





}


package com.selecttvlauncher;

import android.app.Application;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.channels.ChannelCategoryList;
import com.selecttvlauncher.channels.ChannelScheduler;
import com.selecttvlauncher.channels.WebChannelService;
import com.selecttvlauncher.tools.ExceptionHandler;
import com.selecttvlauncher.ui.activities.HomeActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Ocs pl-79(17.2.2016) on 4/7/2016.
 */
public class LauncherApplication extends Application {
    private static LauncherApplication instance;
    private Tracker mTracker;
    private static WebChannelService mWebService;

    public HomeActivity gethAct() {
        return hAct;
    }

    public void sethAct(HomeActivity hAct) {
        this.hAct = hAct;
    }

    private HomeActivity hAct;

    public static LauncherApplication getInstance() {
        return instance;
    }

    HomeActivity homeActivity;

    public HomeActivity getHomeActivity() {
        return homeActivity;
    }

    public void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    public static WebChannelService getmWebService() {
        return mWebService;
    }

    public static void setmWebService(WebChannelService mWebService) {
        LauncherApplication.mWebService = mWebService;
    }


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Fabric.with(this, new Crashlytics());
        instance = this;
        MultiDex.install(getBaseContext());
        mWebService = new WebChannelService(this);

        try {

            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
            File customCacheDirectory = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + getString(R.string.app_name) + "/cache");
            Picasso.Builder builder = new Picasso.Builder(this);
            // builder.downloader(new OkHttpDownloader(customCacheDirectory, Integer.MAX_VALUE));
            Picasso built = builder.build();
//            if (BuildConfig.DEBUG)
//                built.setIndicatorsEnabled(true);
//            else
            built.setIndicatorsEnabled(false);

            built.setLoggingEnabled(true);
            Picasso.setSingletonInstance(built);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void cleardata() {
        try {
            if (gethAct() != null) {
                gethAct().clearCacheData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static ArrayList<ChannelCategoryList> getAllCategorylist() {
        return allCategorylist;
    }

    public static void setAllCategorylist(ArrayList<ChannelCategoryList> allCategorylist) {
        LauncherApplication.allCategorylist = allCategorylist;
    }

    public static ArrayList<ChannelCategoryList> allCategorylist;

    public static HashMap<String, ArrayList<ChannelScheduler>> getmChannelsList() {
        return mChannelsList;
    }

    public static void setmChannelsList(HashMap<String, ArrayList<ChannelScheduler>> mChannelsList) {
        LauncherApplication.mChannelsList = mChannelsList;
    }

    public static HashMap<String,ArrayList<ChannelScheduler>> mChannelsList=new HashMap<>();
}

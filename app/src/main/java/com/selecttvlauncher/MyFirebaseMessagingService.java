package com.selecttvlauncher;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.selecttvlauncher.ui.activities.LoginActivity;

import java.util.Map;

/**
 * Created by Ocs pl-79(17.2.2016) on 11/24/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    TaskStackBuilder stackBuilder;
    String mTitle;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    boolean is_noty = false;
    Handler mHandler;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From:::: " + remoteMessage.getFrom());


            //Handle notifications with data payload for your app
            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload::: " + remoteMessage.getData());

            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body:::: " + remoteMessage.getNotification().getBody());
            }

        Log.e("Msg", remoteMessage.getData().size()+"");
        //sendNotification(remoteMessage.getData());

        try {
            sendNotification(remoteMessage.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }





        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mHandler = new Handler();
    }

    private void sendNotification(Map<String, String> data) {

        // handle notification here
        /*
		 * types of notification 1. result update 2. circular update 3. student
		 * corner update 4. App custom update 5. Custom Message 6. Notice from
		 * College custom
		 */
        int num = ++NOTIFICATION_ID;
        Bundle msg = new Bundle();
        for (String key : data.keySet()) {
            Log.e(key, data.get(key));
            msg.putString(key, data.get(key));
        }


        pref = getSharedPreferences("UPDATE_INSTANCE", MODE_PRIVATE);
        edit = pref.edit();
        Intent backIntent;
        Intent intent = null;
        PendingIntent pendingIntent = null;
        backIntent = new Intent(getApplicationContext(), LoginActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        SharedPreferences sp;
        SharedPreferences.Editor editor;


        if (!is_noty) {
            mNotificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this);

            mBuilder.setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(msg.getString("title"))
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(msg
                                    .getString("msg").toString()))
                    .setAutoCancel(true)
                    .setContentText(msg.getString("msg"));

            if (Integer.parseInt(msg.getString("type")) != 1) {
                mBuilder.setContentIntent(pendingIntent);
            }

            mBuilder.setDefaults(Notification.DEFAULT_ALL);

            mNotificationManager.notify(++NOTIFICATION_ID, mBuilder.build());
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        this.stopForeground(true);
    }

}

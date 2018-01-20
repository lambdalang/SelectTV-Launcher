package com.selecttvlauncher;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.selecttvlauncher.push.DeviceUtils;
import com.selecttvlauncher.push.PushUser;
import com.selecttvlauncher.push.Server;
import com.selecttvlauncher.push.ServerConfig;
import com.selecttvlauncher.tools.PreferenceManager;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ocs pl-79(17.2.2016) on 11/24/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private RequestParams params;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token::::: " + refreshedToken);

        Log.e("Token", refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("global");
        FirebaseMessaging.getInstance().subscribeToTopic(ServerConfig.APP_TYPE);
        sendRegistrationIdToBackend(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     */
    private void sendRegistrationIdToBackend(String refreshedToken) {
        params = new RequestParams();
        if(!TextUtils.isEmpty(PreferenceManager.getemail(getApplicationContext()))){
            params.put(PushUser.EMAIL, PreferenceManager.getemail(getApplicationContext()));
        }

        params.put(PushUser.APP_TYPE, ServerConfig.APP_TYPE);
        params.put(PushUser.TOKEN, refreshedToken);
        params.put(PushUser.DEVICE_MODEL, DeviceUtils.getDeviceModel());
        params.put(PushUser.DEVICE_API, DeviceUtils.getDeviceAPILevel());
        params.put(PushUser.DEVICE_OS, DeviceUtils.getDeviceOS());
        params.put(PushUser.DEVICE_NAME, DeviceUtils.getDeviceName());
        params.put(PushUser.TIMEZONE, DeviceUtils.getDeviceTimeZone());
        params.put(PushUser.LAST_LAT, DeviceUtils.getLastlat(this) + "");
        params.put(PushUser.LAST_LONG, DeviceUtils.getLastLng(this) + "");
        params.put(PushUser.MEMORY, DeviceUtils.getDeviceMemory(this) + "");
        params.put(PushUser.DEVICE_ID, DeviceUtils.getDeviceId(this) + "");
        params.put(PushUser.PIN_CODE, DeviceUtils.getPinCode(this) + "");

        Log.e("param", params.toString());
        Server.postSync(ServerConfig.REGISTRATION_URL, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        Log.e("Advance finish","finish");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.e("Advance Push 61",response.toString());
                    }
                });
    }
}

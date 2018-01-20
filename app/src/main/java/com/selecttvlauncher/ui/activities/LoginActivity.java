package com.selecttvlauncher.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.Helper;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.PreferenceManager;
import com.selecttvlauncher.tools.Utilities;
import com.selecttvlauncher.ui.fragments.ExitDialogFragment;
import com.selecttvlauncher.ui.fragments.ExitDialogFragment.OnExitFragmentInteractionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OnExitFragmentInteractionListener {

    private Context mContext;
    private Button btnSignIn;
    private EditText txtEmailAddress, txtPassword;
    private TextView txtForgetPassword, btnCreateAccount;
    private String username, password;
    private JSONObject m_jsonArrayLogin, m_jsonUpdate;

    private ProgressDialog progressDialog;
    private LinearLayout exit_layout, help_layout;

    private Typeface OpenSans_Bold, OpenSans_Regular, OpenSans_Semibold, osl_ttf;
    String strVersion = "";
    PackageInfo packageInfo;
    private LinearLayout main, bottom_text_layout, wifi_layout;
    private Tracker mTracker;
    private ImageView imageview_wifi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;

//        NewRelic.withApplicationToken(
//                "AA86b2b12dbeedc0b1e819b90b87c79f48176c3bb9"
//        ).start(this.getApplication());

        mTracker = LauncherApplication.getInstance().getDefaultTracker();

        main = (LinearLayout) findViewById(R.id.main);
        bottom_text_layout = (LinearLayout) findViewById(R.id.bottom_text_layout);
        main.setVisibility(View.GONE);
        bottom_text_layout.setVisibility(View.GONE);
        exit_layout = (LinearLayout) findViewById(R.id.exit_layout);
        help_layout = (LinearLayout) findViewById(R.id.help_layout);
        wifi_layout = (LinearLayout) findViewById(R.id.wifi_layout);
        imageview_wifi = (ImageView) findViewById(R.id.imageview_wifi);

        exit_layout.setVisibility(View.GONE);

        Utilities.setAnalytics(mTracker, "Login");

        @SuppressLint("WifiManagerLeak") WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()) {
//wifi is enabled
            imageview_wifi.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.wifi_active));
        } else {
            imageview_wifi.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.wifi_inactive));
        }


        wifi_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 101);
               /* final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity( intent);*/
            }
        });

        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            strVersion = String.valueOf(packageInfo.versionCode);
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            System.out.println("---------versioncode==" + strVersion + "version name" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            strVersion = "Cannot load Version!";
        } catch (Exception e) {
            e.printStackTrace();
        }

        text_font_typeface();
        initComponents();
        addClickListiner();
        checkIfUserIsLoggedInOrNot();
        //checkUpdate();
        exit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FragmentManager fm = getSupportFragmentManager();
                    ExitDialogFragment dialogFragment = new ExitDialogFragment();
                    dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");

                    // finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        help_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = getString(R.string.support_url);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    if (i != null) {
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 101) {
                @SuppressLint("WifiManagerLeak") WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                if (wifi.isWifiEnabled()) {
                    //wifi is enabled
                    imageview_wifi.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.wifi_active));
                } else {
                    imageview_wifi.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.wifi_inactive));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkUpdate() {

        if (HomeActivity.isNetworkAvailable(mContext) || HomeActivity.isWifiAvailable(mContext)) {
            new CallUpdateAPI().execute();
        } else {
            checkIfUserIsLoggedInOrNot();
            /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("No Network Available");

            alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    checkUpdate();
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setCancelable(false);
            alertDialog.show();*/
        }

    }

    @Override
    public void onBackPressed() {

    }

    private void initComponents() {
        txtEmailAddress = (EditText) findViewById(R.id.email_editText);
        txtPassword = (EditText) findViewById(R.id.password_editText);

        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (HomeActivity.isNetworkAvailable(mContext) || HomeActivity.isWifiAvailable(mContext)) {
                        onActionUserLogin();
                    } else {
                        Toast.makeText(mContext, getString(R.string.no_network_txt), Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }
        });

        txtForgetPassword = (TextView) findViewById(R.id.forgot_password_textView);
        btnCreateAccount = (TextView) findViewById(R.id.signup_textView);
        btnSignIn = (Button) findViewById(R.id.login_button);

        txtEmailAddress.setTypeface(OpenSans_Regular);
        txtPassword.setTypeface(OpenSans_Regular);
        txtForgetPassword.setTypeface(OpenSans_Regular);
        btnCreateAccount.setTypeface(OpenSans_Regular);
        btnSignIn.setTypeface(OpenSans_Regular);
    }

    private void text_font_typeface() {
        try {
            OpenSans_Bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_1.ttf");
            OpenSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_1.ttf");
            OpenSans_Semibold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold_1.ttf");
            osl_ttf = Typeface.createFromAsset(getAssets(), "fonts/osl.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addClickListiner() {
        btnSignIn.setOnClickListener(this);
        txtForgetPassword.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnCreateAccount) {
            try {
                Utilities.setAnalytics(mTracker, "Registration");
                String url = getString(R.string.support_url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                if (i != null) {
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //showCreateAccountScreen();
        } else if (view == btnSignIn) {
            if (HomeActivity.isNetworkAvailable(mContext) || HomeActivity.isWifiAvailable(mContext)) {
                onActionUserLogin();
            } else {
                Toast.makeText(mContext, getString(R.string.no_network_txt), Toast.LENGTH_SHORT).show();
            }
        } else if (view == txtForgetPassword) {
            showForgetPasswordScreen();
        }

    }

    private void checkIfUserIsLoggedInOrNot() {

        boolean isUserLoggedIn = PreferenceManager.isLogin(mContext);
        if (isUserLoggedIn) {


                /*if (!Helper.getDefaultLauncher(LoginActivity.this).equals(
                        getPackageName())) {

                    Helper.alertSetAsDefaultLauncher(LoginActivity.this);
                }else{
                    showMainScreen();
                }*/
            showMainScreen();


        } else {
            PreferenceManager.setFirstLogin(true, mContext);
            main.setVisibility(View.VISIBLE);
            bottom_text_layout.setVisibility(View.VISIBLE);
            exit_layout.setVisibility(View.VISIBLE);

        }
    }

    private void showCreateAccountScreen() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 0);
    }

    private void showForgetPasswordScreen() {
        Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
        startActivity(intent);

    }

    private void showMainScreen() {
        if (PreferenceManager.isFirstLogin(mContext)) {
            Intent intent = new Intent(mContext, HomeActivity.class);
            intent.putExtra("welcome", true);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(mContext, HomeActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void onActionUserLogin() {
        try {
            Utilities.hideKeyBoard(mContext, txtPassword);
            txtEmailAddress.setError(null);
            txtPassword.setError(null);

            String szEmail = txtEmailAddress.getText().toString();
            String szPassword = txtPassword.getText().toString();
            if (szEmail.length() == 0) {
                txtEmailAddress.setError("Input Email Address!!!");
                return;
            }
            if (szPassword.length() == 0) {
                txtPassword.setError("Input Password");
                return;
            }
            username = szEmail;
            password = szPassword;
            new LoginAPI().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exitlauncher() {
        try {
            Helper.clearCurrentDefaultLauncher(mContext);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class LoginAPI extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            try {
                progressDialog = ProgressDialog.show(mContext, "Please wait", "Authenticating...", false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            m_jsonArrayLogin = JSONRPCAPI.getLogin(username, password);
            if (m_jsonArrayLogin == null) return null;
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                if (m_jsonArrayLogin == null) return;

                if (!m_jsonArrayLogin.isNull("error")) {
                    try {
                        String message = m_jsonArrayLogin.getString("error");
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    PreferenceManager.setFirstLogin(true, mContext);
                    Log.d("Login response:::", ":::" + m_jsonArrayLogin);
                    PreferenceManager.setLogin(true, mContext);
                    PreferenceManager.setAccessToken(m_jsonArrayLogin.getString("access_token"), mContext);
                    PreferenceManager.setFirstTime(true, mContext);
                    Log.d(":Acesstoken::", "::" + m_jsonArrayLogin.getString("access_token"));
                    if (m_jsonArrayLogin.has("subscriptions")) {
                        try {
                            JSONArray subscriptions_array = m_jsonArrayLogin.getJSONArray("subscriptions");
                            if (subscriptions_array != null && subscriptions_array.length() > 0) {
                                String[] list = new String[subscriptions_array.length()];
                                for (int i = 0; i < subscriptions_array.length(); i++) {
                                    JSONObject sub_object = subscriptions_array.getJSONObject(i);
                                    if (sub_object.getBoolean("subscribed")) {
                                        list[i] = sub_object.getString("code").toLowerCase();
                                    } else {
                                        list[i] = "";
                                    }

                                }
                                PreferenceManager.setSubscribedList(list, mContext);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    new getUserDetails().execute();
                   /* if (!Helper.getDefaultLauncher(LoginActivity.this).equals(
                            getPackageName())) {

                        Helper.alertSetAsDefaultLauncher(LoginActivity.this);
                    }else{
                        showMainScreen();
                    }*/
                    showMainScreen();

                }

            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                try {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class getUserDetails extends AsyncTask<String, Object, Object> {
        JSONObject m_response;

        @Override
        protected Object doInBackground(String... params) {
            try {
                Log.d("accesstoken::", "::" + PreferenceManager.getAccessToken(mContext));

                m_response = JSONRPCAPI.getUserProfile(PreferenceManager.getAccessToken(mContext));
                if (m_response == null) return null;
                Log.d("m_response::", "::" + m_response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if (m_response != null) {
                    if (m_response.has("username")) {
                        String username = m_response.getString("username");
                        PreferenceManager.setusername(username, mContext);
                    }
                    if (m_response.has("city")) {
                        String city = m_response.getString("city");
                        PreferenceManager.setcity(city, mContext);
                    }
                    if (m_response.has("first_name")) {
                        String first_name = m_response.getString("first_name");
                        PreferenceManager.setfirst_name(first_name, mContext);
                    }
                    if (m_response.has("last_name")) {
                        String last_name = m_response.getString("last_name");
                        PreferenceManager.setlast_name(last_name, mContext);
                    }
                    if (m_response.has("gender")) {
                        String gender = m_response.getString("gender");
                        PreferenceManager.setgender(gender, mContext);
                    }
                    if (m_response.has("email")) {
                        String email = m_response.getString("email");
                        PreferenceManager.setemail(email, mContext);
                    }
                    if (m_response.has("state")) {
                        String state = m_response.getString("state");
                        PreferenceManager.setstate(state, mContext);
                    }
                    if (m_response.has("date_of_birth")) {
                        String date_of_birth = m_response.getString("date_of_birth");
                        PreferenceManager.setdate_of_birth(date_of_birth, mContext);
                    }
                    if (m_response.has("last_login")) {
                        String last_login = m_response.getString("last_login");
                        PreferenceManager.setlast_login(last_login, mContext);
                    }
                    if (m_response.has("address_1")) {
                        String address_1 = m_response.getString("address_1");
                        PreferenceManager.setaddress_1(address_1, mContext);
                    }
                    if (m_response.has("address_2")) {
                        String address_2 = m_response.getString("address_2");
                        PreferenceManager.setaddress_2(address_2, mContext);

                    }
                    if (m_response.has("postal_code")) {
                        String postal_code = m_response.getString("postal_code");
                        PreferenceManager.setpostal_code(postal_code, mContext);
                    }
                    if (m_response.has("phone_number")) {
                        String phone_number = m_response.getString("phone_number");
                        PreferenceManager.setphone_number(phone_number, mContext);
                    }
                    if (m_response.has("id")) {
                        int id = m_response.getInt("id");
                        PreferenceManager.setid(id, mContext);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class CallUpdateAPI extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            try {
                /*progressDialog = ProgressDialog.show(mContext, "Please wait", "Checking Version...", false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();*/

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                m_jsonUpdate = JSONRPCAPI.getAppConfig();
                if (m_jsonUpdate == null) return null;
                Log.d("update response::", ":::" + m_jsonUpdate.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                if (m_jsonUpdate == null) checkIfUserIsLoggedInOrNot();

                if (!m_jsonUpdate.isNull("error")) {
                    try {
                        String message = m_jsonUpdate.getString("error");
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject version_obj = m_jsonUpdate.getJSONObject("version");

                    if (m_jsonUpdate.has("welcome-video")) {
                        String welcome_video = m_jsonUpdate.getString("welcome-video");
                        PreferenceManager.setwelcome_video(welcome_video, mContext);
                    }


                    Log.d("update response:::", ":::" + version_obj);
                    String releasetext = version_obj.getString("releasetext");
                    final String app_link = version_obj.getString("app_link");
                    int ver = version_obj.getInt("number");
                    Log.d("version:::", "::" + strVersion + "::" + ver + "::");
                    String number = "" + ver;
                    Log.d("version:::", "::" + strVersion + "::" + number + "::");
                    boolean forceupdate = version_obj.getBoolean("forceupdate");
                    if (number.equalsIgnoreCase(strVersion)) {
                        checkIfUserIsLoggedInOrNot();
                    } else {
                        if (forceupdate) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle(getResources().getString(R.string.app_name));
                            builder.setMessage(releasetext);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Update".toUpperCase(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(app_link));
                                        if (myIntent != null) {
                                            startActivity(myIntent);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle(getResources().getString(R.string.app_name));
                            builder.setMessage(releasetext);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(app_link));
                                        if (myIntent != null) {
                                            startActivity(myIntent);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            builder.setNegativeButton("Skip Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    checkIfUserIsLoggedInOrNot();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                    }


                }

            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                try {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    boolean isMyLauncherDefault() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);

        List<IntentFilter> filters = new ArrayList<IntentFilter>();
        filters.add(filter);

        final String myPackageName = getPackageName();
        List<ComponentName> activities = new ArrayList<ComponentName>();
        final PackageManager packageManager = (PackageManager) getPackageManager();

        // You can use name of your package here as third argument
        packageManager.getPreferredActivities(filters, activities, null);

        for (ComponentName activity : activities) {
            if (myPackageName.equals(activity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static void makePrefered(Context c) {
        PackageManager p = c.getPackageManager();
        ComponentName cN = new ComponentName(c, LoginActivity.class);
        p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        c.startActivity(selector);

        p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

}

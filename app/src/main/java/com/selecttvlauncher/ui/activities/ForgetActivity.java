package com.selecttvlauncher.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.selecttvlauncher.Interface.AppDialogClickListiner;
import com.selecttvlauncher.Interface.AppDialogUserActions;
import com.selecttvlauncher.LauncherApplication;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.tools.Utilities;

import org.json.JSONObject;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private EditText txtEmailAddress;
    private Button btnContinue;
    private String email;
    private JSONObject m_jsonArrayForget;
    private ProgressDialog progressDialog;

    private Typeface OpenSans_Bold, OpenSans_Regular, OpenSans_Semibold, osl_ttf;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        mContext = this;
        mTracker = LauncherApplication.getInstance().getDefaultTracker();
        Utilities.setAnalytics(mTracker, "Forgot Password");
        text_font_typeface();
        initComponent();
        setOnClickListiners();
        setFonts();
    }

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            onUserActionForgetPassword();
        }
    }

    @Override
    public void onBackPressed() {
        showLoginScreen();
        super.onBackPressed();
    }

    private void setFonts() {
        try {
            txtEmailAddress.setTypeface(OpenSans_Regular);
            btnContinue.setTypeface(OpenSans_Regular);
            ((TextView) findViewById(R.id.reset_textView)).setTypeface(OpenSans_Regular);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOnClickListiners() {
        btnContinue.setOnClickListener(this);
    }

    private void initComponent() {
        txtEmailAddress = (EditText) findViewById(R.id.email_editText);
        txtEmailAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onUserActionForgetPassword();
                }
                return false;
            }
        });
        btnContinue = (Button) findViewById(R.id.continue_button);
    }

    private void onUserActionForgetPassword() {
        try {
            txtEmailAddress.setError(null);
            Utilities.hideKeyBoard(mContext, txtEmailAddress);

            String szEmail = txtEmailAddress.getText().toString();
            if (szEmail.length() == 0) {
                txtEmailAddress.setError("Input Email Address");
                return;
            }
            String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+";
            if (!szEmail.matches(emailPattern)) {
                txtEmailAddress.setError("Invalid email address");
                return;
            }
            email = szEmail;
            if (HomeActivity.isNetworkAvailable(mContext) || HomeActivity.isWifiAvailable(mContext)) {
                new ForgetPasswordAPI().execute();
            } else {
                Toast.makeText(mContext, getString(R.string.no_network_txt), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ForgetPasswordAPI extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            try {
                progressDialog = ProgressDialog.show(mContext, "Please wait", "Processing...", false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            Log.d("email::::", ":::" + email);
            String slug = "";
            switch (getPackageName()) {
                case "com.arvig":
                    slug = "arvig-freecast-com";
                    break;
                case "com.broadviewlauncher":
                    slug = "broadview-live";
                    break;
                case "com.centurylink":
                    slug = "centurylinkdemo-freecast-com";
                    break;
                case "com.endeavor":
                    slug = "endeavor-freecast-com";
                    break;
                case "com.bucktv":
                    slug = "2bucktv-freecast-com";
                    break;
                case "com.evolution":
                    slug = "evolution-freecast-com";
                    break;
                case "com.etv":
                    slug = "etvanywhere-net";
                    break;
                case "com.smartcity":
                    slug = "smartcity-freecast-com";
                    break;
                case "com.wisetv":
                    slug = "wisetv-freecast-com";
                    break;
                case "com.rabbittvplus":
                    slug = "rabbittvplus-freecast-com";
                    break;
                case "com.selecttvlauncher":
                    slug = "selecttv-freecast-com";
                    break;
                case "com.hughesnet":
                    slug = "hughesnet-freecast-com";
                    break;
                case "com.klicktv":
                    slug = "selecttv-freecast-com";
                    break;
                case "com.mohawk":
                    slug = "mohawk-freecast-com";
                    break;
                case "com.demo":
                    slug = "demo-freecast-com";
                    break;
                case "com.mcg":
                    slug = "mcg-freecast-com";
                    break;
            }
            m_jsonArrayForget = JSONRPCAPI.getForget(email,slug);
            if (m_jsonArrayForget == null) return null;
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                if (m_jsonArrayForget == null) {
                    Toast.makeText(ForgetActivity.this, "Invalid email information", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    try {
                        if (m_jsonArrayForget.has("message")) {
                            String messaage = m_jsonArrayForget.getString("message");
                            Utilities.showDialog(mContext,
                                    "Password Reset Email Sent",
                                    messaage,
                                    "OK", null,
                                    new AppDialogClickListiner() {
                                        @Override
                                        public void onDialogClick(AppDialogUserActions which) {
                                            if (which == AppDialogUserActions.OK) {
                                                showLoginScreen();
                                            }
                                        }
                                    }
                            );
                        } else {
                            showDialogToUser();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showDialogToUser();
                    }
                    Log.d("forgot pswd::::", ":::" + m_jsonArrayForget);

                }

            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        }
    }

    private void showDialogToUser() {
        Utilities.showDialog(mContext,
                "Password Reset Email Sent",
                "An email containing your password reset link has been sent to your verified email address.",
                "OK", null,
                new AppDialogClickListiner() {
                    @Override
                    public void onDialogClick(AppDialogUserActions which) {
                        if (which == AppDialogUserActions.OK) {
                            showLoginScreen();
                        }
                    }
                }
        );
    }

    private void showLoginScreen() {
        ForgetActivity.this.finish();
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

}

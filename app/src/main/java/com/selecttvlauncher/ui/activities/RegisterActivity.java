package com.selecttvlauncher.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.selecttvlauncher.Adapter.AgeSpinnerAdapter;
import com.selecttvlauncher.R;
import com.selecttvlauncher.network.JSONRPCAPI;
import com.selecttvlauncher.ui.dialogs.PopupDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private Context mContext;
    private Button btnCreateAccount;
    private EditText txtEmailAddress, txtPassword, txtConfirmPassword, txtZipCode;
    private RadioButton btnMale, btnFemale;
    private Spinner spinnerAge;
    private ArrayList<String> items = new ArrayList();
    private String email, username, password;
    private JSONObject objData;
    private TextView txtTerm, txtPolicy;
    private JSONObject m_jsonArrayRegister;
    private TextView register_textView,reedem_textView;
    private LinearLayout reedem_layout,register_layout;

    private ProgressDialog progressDialog;
    private View register_cast_left,register_cast_top,register_cast_bottom,reedem_top,reedem_bottom,reedem_right;

    private Typeface OpenSans_Bold,OpenSans_Regular,OpenSans_Semibold,osl_ttf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;
        text_font_typeface();

        initComponents();
        setOnClickListiners();
        setFonts();

        setResult(RESULT_CANCELED);
    }

    private void initComponents() {
        btnCreateAccount = (Button) findViewById(R.id.create_button);
        btnMale = (RadioButton) findViewById(R.id.ui_register_btn_male);
        btnFemale = (RadioButton) findViewById(R.id.ui_register_btn_female);
        txtEmailAddress = (EditText) findViewById(R.id.username_editText);
        txtPassword = (EditText) findViewById(R.id.pswd_editText);
        txtConfirmPassword = (EditText) findViewById(R.id.confirm_pswd_editText);
        register_textView=(TextView)findViewById(R.id.register_textView);
        reedem_textView=(TextView)findViewById(R.id.reedem_textView);
        reedem_layout=(LinearLayout)findViewById(R.id.reedem_layout);
        register_layout=(LinearLayout)findViewById(R.id.register_layout);

        register_cast_left=(View)findViewById(R.id.register_cast_left);
        register_cast_top=(View)findViewById(R.id.register_cast_top);
        register_cast_bottom=(View)findViewById(R.id.register_cast_bottom);
        reedem_top=(View)findViewById(R.id.reedem_top);
        reedem_bottom=(View)findViewById(R.id.reedem_bottom);
        reedem_right=(View)findViewById(R.id.reedem_right);


        txtPassword.setTypeface(OpenSans_Regular);
        txtConfirmPassword.setTypeface(OpenSans_Regular);

        txtZipCode = (EditText) findViewById(R.id.postal_code_editText);
        txtZipCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onActionCreateAccount();
                }
                return false;
            }
        });
        txtTerm = (TextView) findViewById(R.id.txtTerm);
        txtPolicy = (TextView) findViewById(R.id.txtPolicy);

        spinnerAge = (Spinner) findViewById(R.id.spinnerAge);
        items.add("18-24");
        items.add("25-35");
        items.add("36-49");
        items.add("50+");
        AgeSpinnerAdapter adapter = new AgeSpinnerAdapter(this, R.layout.spinneritem, items);
        spinnerAge.setAdapter(adapter);

        reedem_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reedem_layout.setVisibility(View.VISIBLE);
                register_layout.setVisibility(View.GONE);

                register_cast_left.setVisibility(View.GONE);
                register_cast_top.setVisibility(View.GONE);
                register_cast_bottom.setVisibility(View.VISIBLE);
                reedem_top.setVisibility(View.VISIBLE);
                reedem_bottom.setVisibility(View.GONE);
                reedem_right.setVisibility(View.VISIBLE);
                register_textView.setTextColor(ContextCompat.getColor(mContext, R.color.text_lite_blue));
                reedem_textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            }
        });
        register_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reedem_layout.setVisibility(View.GONE);
                register_layout.setVisibility(View.VISIBLE);
                register_textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                reedem_textView.setTextColor(ContextCompat.getColor(mContext, R.color.text_lite_blue));

                register_cast_left.setVisibility(View.VISIBLE);
                register_cast_top.setVisibility(View.VISIBLE);
                register_cast_bottom.setVisibility(View.GONE);
                reedem_top.setVisibility(View.GONE);
                reedem_bottom.setVisibility(View.VISIBLE);
                reedem_right.setVisibility(View.GONE);
            }
        });
    }

    private void setOnClickListiners() {
        try {
            btnCreateAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onActionCreateAccount();
                }
            });
            btnMale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnFemale.setChecked(false);
                    btnMale.setChecked(true);
                }
            });
            btnFemale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnMale.setChecked(false);
                    btnFemale.setChecked(true);
                }
            });

            txtPolicy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
            txtTerm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFonts() {
        txtEmailAddress.setTypeface(OpenSans_Regular);
        txtPassword.setTypeface(OpenSans_Regular);
        txtConfirmPassword.setTypeface(OpenSans_Regular);
        txtZipCode.setTypeface(OpenSans_Regular);
        btnCreateAccount.setTypeface(OpenSans_Regular);
        btnCreateAccount.setTypeface(OpenSans_Regular);
        btnMale.setTypeface(OpenSans_Regular);
        btnFemale.setTypeface(OpenSans_Regular);

    }

    private void showDialog() {
        try {
            PopupDialog dlg = new PopupDialog(this);
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    (float) 30, getResources().getDisplayMetrics());
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int nWidth = displayMetrics.widthPixels;
            int nHeight = displayMetrics.heightPixels;
            dlg.showAtLocation(txtTerm, nWidth - 30, nHeight / 3 * 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onActionCreateAccount() {
        String szEmail, szPassword, szConfirmPassword, szUserName;
        szEmail = txtEmailAddress.getText().toString();
        szPassword = txtPassword.getText().toString();
        szConfirmPassword = txtConfirmPassword.getText().toString();
        szUserName = txtZipCode.getText().toString();

        txtEmailAddress.setError(null);
        txtPassword.setError(null);
        txtConfirmPassword.setError(null);
        txtZipCode.setError(null);

        if (szEmail.length() == 0) {
            txtEmailAddress.setError("Input Email Address");
            return;
        }
        if (szEmail.contains("@") == false) {
            txtEmailAddress.setError("Invalid email address");
            return;
        }

        if (szPassword.length() == 0) {
            txtPassword.setError("Input Password");
            return;
        }
        if (szConfirmPassword.length() == 0) {
            txtConfirmPassword.setError("Confirm Password");
            return;
        }
        if (!szPassword.equals(szConfirmPassword)) {
            txtPassword.setError("Password did not match with confirm password");
            return;
        }
        if (szUserName.length() == 0) {
            txtZipCode.setError("Input Postal Code");
            return;
        }
        email = szEmail;
        username = szUserName;
        password = szPassword;
        objData = new JSONObject();

        try {
            if (btnMale.isChecked())
                objData.put("gender", "M");
            else
                objData.put("gender", "F");
            objData.put("age_group", spinnerAge.getSelectedItemPosition() + 1);
            objData.put("zip_code", szUserName);
            new RegisterAPI().execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class RegisterAPI extends AsyncTask<Object, Object, Object> {
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
            m_jsonArrayRegister = JSONRPCAPI.getRegister(email, username, password, objData);
            if (m_jsonArrayRegister == null) return null;
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                if (m_jsonArrayRegister == null) return;
                if (!m_jsonArrayRegister.isNull("error")) {
                    try {
                        String message = m_jsonArrayRegister.getString("error");
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("register::::", ":::" + m_jsonArrayRegister);
                    onRegistrationCompleted();
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

    private void onRegistrationCompleted() {
        RegisterActivity.this.setResult(RESULT_OK);
        RegisterActivity.this.finish();
    }

    private void text_font_typeface() {
        try {
            OpenSans_Bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_1.ttf");
            OpenSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_1.ttf");
            OpenSans_Semibold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold_1.ttf");
            osl_ttf = Typeface.createFromAsset(getAssets(), "fonts/osl.ttf");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

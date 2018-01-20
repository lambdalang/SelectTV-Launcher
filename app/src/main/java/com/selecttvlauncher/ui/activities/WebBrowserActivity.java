package com.selecttvlauncher.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.selecttvlauncher.R;
import com.selecttvlauncher.ui.dialogs.ProgressHUD;

/**
 * Created by Ocs pl-79(17.2.2016) on 4/18/2016.
 */
public class WebBrowserActivity extends Activity implements View.OnClickListener {

    ImageView imageBack;
    WebView webView;
    ProgressHUD mProgressHUD;
    TextView txtTitle;

    String url;
    String ua = "Mozilla/5.0 (Android;) Gecko/20.0 Firefox/20.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webbrowser);

        try {
            imageBack = (ImageView) findViewById(R.id.imageBack);
            txtTitle = (TextView) findViewById(R.id.txtTitle);

            webView = (WebView) findViewById(R.id.webView);
//            webView.getSettings().setUserAgentString(ua);
//            webView.getSettings().setLoadWithOverviewMode(true);
//            webView.getSettings().setUseWideViewPort(true);
//            webView.getSettings().setLoadWithOverviewMode(true);
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.getSettings().setBuiltInZoomControls(true);
//            webView.getSettings().setSupportZoom(true);
//            webView.getSettings().setAllowFileAccess(true);
//            webView.setInitialScale(200);
//            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.setWebChromeClient(new WebChromeClient());

            webView.setHorizontalScrollBarEnabled(false);
            webView.setVerticalScrollBarEnabled(false);
            webView.setScrollbarFadingEnabled(false);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);


            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            final int height = displaymetrics.heightPixels;
            final int width = displaymetrics.widthPixels;


            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                    viewx.loadUrl(urlx);
                    return false;
                }

                public void onPageFinished(WebView view, String url) {
                    // do your stuff here
                    webView.scrollTo(width / 2, 0);
                    if (mProgressHUD.isShowing())
                        mProgressHUD.dismiss();
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    if (mProgressHUD.isShowing())
                        mProgressHUD.dismiss();
                    ;
                }
            });
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            url = bundle.getString("url");
            String name = bundle.getString("name");


            txtTitle.setText(name);
//            webView.loadUrl(url);
            String frameWidth = ((width / 100) * 53) + "";
            String frameVideo = "<html><head> <meta name=\"viewport\" content=\"width=" + frameWidth + ",user-scalable = no \" ></head> <body style=\"margin:0;padding:0;background: #00000000;\"><iframe allowtransparency=\"true\" style=\"background: #00000000;\" width=\"100%\" height=\"100%\" src=" + url + " frameborder=\"0\" allowfullscreen></iframe></body></html>";
            webView.loadData(frameVideo, "text/html", "utf-8");
            webView.requestFocus();
            mProgressHUD = ProgressHUD.show(this, "Loading ...", true, false, null);

            imageBack.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            webView.goBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private String extractePackageName(String appLink) {
        if (appLink.equalsIgnoreCase("none")) {
            return null;
        }
        String[] packageName = appLink.split("=");
        String[] refinedPackgNameList;
        String refinedPackgName = null;
        if (packageName.length > 1) {
            refinedPackgNameList = packageName[1].split("&");
            refinedPackgName = refinedPackgNameList[0];
        } else {
            /*if( packageName.length > 0 ) {
                refinedPackgName = packageName[0];
            }*/
            return null;
        }
        return refinedPackgName;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        try {
            if (v == imageBack) {
                WebBrowserActivity.this.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

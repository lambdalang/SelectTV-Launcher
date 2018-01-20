package com.selecttvlauncher.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.selecttvlauncher.R;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/25/2016.
 */
public class PopupDialog implements View.OnClickListener{
    public View parent;
    public PopupWindow popupWindow;


    Activity context;
    boolean bShowSetting;
    WebView webView;
    ImageView imgExit;

    public void setShowSetting(boolean bShow){
        this.bShowSetting = bShow;
    }
    public PopupDialog(Activity paramContext)
    {
        bShowSetting = true;
        context = paramContext;
        this.parent = ((LayoutInflater)paramContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.setting_popup, null);

        //this.parent.findViewBy)
        this.popupWindow = new PopupWindow(this.parent, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT,true);

        webView = (WebView)parent.findViewById(R.id.webView);
        imgExit = (ImageView)parent.findViewById(R.id.imgExit);
        imgExit.setOnClickListener(this);

        new MyTask().execute();

    }
    public	class MyTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //pDialog.hide();

        }

        @SuppressLint("SetJavaScriptEnabled") @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            context.runOnUiThread(new Runnable() {
                public void run() {
                    WebSettings set = webView.getSettings();
                    set.setSupportMultipleWindows(true);
                    webView.setWebViewClient(new TabWebViewClient());
                    webView.setWebChromeClient(new WebChromeClient());
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setDomStorageEnabled(true);
                    webView.getSettings().setAllowContentAccess(true);
                    webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                    webView.getSettings().setSupportMultipleWindows(true);
                    webView.loadUrl("http://freecast.s3.amazonaws.com/SelectTV/App/selecttv-tos.html");
                }

            });

            return null;
        }
    }
    private class TabWebViewClient extends WebViewClient {

        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            Toast.makeText(context, "Some Error :" + description, Toast.LENGTH_SHORT)
                    .show();
        }
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event){
            return false;


        }

    }
    public void showAtLocation(View pView,int width,int height)
    {

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.update();

        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        popupWindow.showAtLocation(pView, Gravity.CENTER, 0 , 0);
    }
    public void hide()
    {
        this.popupWindow.dismiss();
    }
    public boolean isVisible()
    {
        return this.popupWindow.isShowing();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.imgExit:
                popupWindow.dismiss();
                break;
        }
    }
}

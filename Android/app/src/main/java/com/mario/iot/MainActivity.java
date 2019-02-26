package com.mario.iot;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ConnectionChange{

    private WebView mWebView;
    private ProgressBar progressBar;
    private TextView iotText;
    CustomBroadcastReceiver customBroadcastReceiver;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FullScreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Check connection
        customBroadcastReceiver = new CustomBroadcastReceiver(this);

        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        iotText = (TextView) findViewById(R.id.iotText);
        iotText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));

        //Open links in same webview
        mWebView.setWebViewClient(new WebViewClient());

        //Enable javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Open Page
        mWebView.loadUrl(getResources().getString(R.string.full_url));
        mWebView.setWebViewClient(new IotWebViewClient(progressBar, mWebView, iotText, this));

    }


    // Prevent the back-button from closing the app
    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(customBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(customBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void connectionChange() {
        //mWebView.loadUrl(getResources().getString(R.string.full_url));
        WebBackForwardList mWebBackForwardList = mWebView.copyBackForwardList();
        if (mWebBackForwardList.getCurrentIndex() > 0){
            int urlCounter = 1;
            String url = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()-urlCounter).getUrl();
            while (url.equals(getResources().getString(R.string.error_url))){
                urlCounter++;
                url = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()-urlCounter).getUrl();
            }
            mWebView.loadUrl(url);
        }
    }
}

package com.mario.iot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class IotWebViewClient extends WebViewClient {

    private ProgressBar progressBar;
    private WebView webView;
    private TextView iotText;
    private Context context;


    public IotWebViewClient(ProgressBar progressBar, WebView webView, TextView iotText, Context context){
        this.progressBar = progressBar;
        this.webView = webView;
        this.iotText = iotText;
        this.context = context;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        progressBar.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        iotText.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        progressBar.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        iotText.setVisibility(View.GONE);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        if(!isOnline(context)){
            webView.loadUrl(context.getResources().getString(R.string.error_url));
        }
        super.onLoadResource(view, url);
    }

    private boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        webView.loadUrl(context.getResources().getString(R.string.error_url));
        super.onReceivedError(view, request, error);
    }

}

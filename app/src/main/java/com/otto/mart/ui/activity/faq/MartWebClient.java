package com.otto.mart.ui.activity.faq;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MartWebClient extends WebViewClient{

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (String.valueOf(request.getUrl()).contains("onepage/success")) {
//                listener.onSuccess();
//            } else if (String.valueOf(request.getUrl()).contains("onepage/failed")) {
//                listener.onFailed();
//            }
        }
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//        if (url.contains("onepage/success")) {
//            listener.onSuccess();
//        } else if (url.contains("onepage/failed")) {
//            listener.onFailed();
//        }
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri uri;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            uri = Uri.parse(String.valueOf(request.getUrl()));
            view.loadUrl(String.valueOf(uri));
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
//        listener.onLoaded();
    }
}

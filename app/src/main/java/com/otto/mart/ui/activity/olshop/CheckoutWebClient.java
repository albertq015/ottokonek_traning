package com.otto.mart.ui.activity.olshop;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CheckoutWebClient extends WebViewClient {

    private final String ottoPrefix = "otto://";
    private WebViewListener listener;
    private PageListener pageListener;

    public CheckoutWebClient(WebViewListener listener) {
        this.listener = listener;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (request.getUrl().toString().contains(ottoPrefix)) {
            callListener(request.getUrl().toString().replace(ottoPrefix, ""));
        }
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.contains(ottoPrefix)) {
            callListener(url.replace(ottoPrefix, ""));
        }
        return true;
    }

    private void callListener(String value) {
        if (listener != null) {
            listener.onIntercepted(value);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        pageListener.onPageFinishLoad(view, url);
        super.onPageFinished(view, url);
    }

    public void setOnPageFinishLoad(PageListener pageListener) {
        this.pageListener = pageListener;
    }

    public interface WebViewListener {
        void onIntercepted(String value);
    }

    public interface PageListener {
        void onPageFinishLoad(WebView view, String url);
    }
}

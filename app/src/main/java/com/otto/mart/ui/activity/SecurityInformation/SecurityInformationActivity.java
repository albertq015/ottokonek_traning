package com.otto.mart.ui.activity.SecurityInformation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.RTItemModel;

import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;

public class SecurityInformationActivity extends BaseActivity {

    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private WebView wv;
    private List<RTItemModel.FAQUModel> models;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snk);
        initComponent();
        initContent();
    }

    private void initComponent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        wv = findViewById(R.id.wb_snk);
        WebSettings webSettings = wv.getSettings();
        webSettings.setDefaultFontSize(12);
        webSettings.setMinimumFontSize(12);

        //HttpsTrustManager.allowAllSSL();

        wv.setWebViewClient(new WebViewClient() {
            public void onPageFinished() {

            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SecurityInformationActivity.this);

                String message = "SSL Certificate error.";

                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;

                }
                message += "Do you want to continue anyway?";

                builder.setTitle("SSL Certificate Error");
                builder.setMessage(message);

                builder.setPositiveButton("Continue", (dialog, id) -> handler.proceed());

                builder.setNegativeButton("Cancel", (dialog, id) -> finish());

                Dialog dialog = builder.create();
                dialog.show();
            }
        });

    }


    private void initContent() {
        tvToolbarTitle.setText(getString(R.string.profile_menu_security_information));

        btnToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        wv.loadUrl("file:///android_asset/html/security_information.html");
    }

}

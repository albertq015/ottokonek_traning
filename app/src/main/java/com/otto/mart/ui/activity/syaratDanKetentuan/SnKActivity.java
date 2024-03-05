package com.otto.mart.ui.activity.syaratDanKetentuan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.RTItemModel;
import com.otto.mart.ui.activity.olshop.CheckoutWebClient;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;

public class SnKActivity extends BaseActivity {

    public static final String KEY_IS_FROM_REGISTER = "is_from_register";
    public static final String KEY_URL_CONTENT = "url_register";
    public static final String KEY_IS_OTTOCASH = "is_ottocash";
    public static final String KEY_IS_USE_BTN_CONFIRM = "is_btn_confirm";

    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private WebView webView;
    private List<RTItemModel.FAQUModel> models;
    private CheckBox checkBox;
    private Button btnConfirm;

    private boolean isFromRegister = false;
    private SwipeRefreshLayout swipeRefresh;
    private boolean isWebFinishLoad;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snk);

        if (getIntent().hasExtra(KEY_IS_FROM_REGISTER)) {
            isFromRegister = getIntent().getBooleanExtra(KEY_IS_FROM_REGISTER, false);
        }

        initComponent();
        initContent();
    }

    private void initComponent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        checkBox = findViewById(R.id.checkbox);
        webView = findViewById(R.id.wb_snk);
        btnConfirm = findViewById(R.id.btnConfirm);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultFontSize(12);
        webSettings.setMinimumFontSize(12);
        webSettings.setJavaScriptEnabled(true);

        swipeRefresh.setEnabled(false);

        if (getIntent().getBooleanExtra(KEY_IS_USE_BTN_CONFIRM, false)) {
            btnConfirm.setVisibility(View.VISIBLE);
        }

        // HttpsTrustManager.allowAllSSL();

        webView.setWebViewClient(new WebViewClient() {

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SnKActivity.this);

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

        tvToolbarTitle.setText(getString(R.string.text_tnc));

        btnToolbarBack.setOnClickListener(v -> onBackPressed());

        if (isFromRegister) {
            checkBox.setText(Html.fromHtml(getString(R.string.activation_confirmation_tac_ottopay)));
            checkBox.setVisibility(View.VISIBLE);
        }

        String content = "file:///android_asset/html/tos.html";
        if (getIntent().hasExtra(KEY_URL_CONTENT)) {
            content = getIntent().getStringExtra(KEY_URL_CONTENT);

        }

        if (getIntent().getBooleanExtra(KEY_IS_OTTOCASH, false) && getIntent().hasExtra(KEY_IS_FROM_REGISTER)) {
            content = "file:///android_asset/html/tos_ottocash.html";
        }

        CheckoutWebClient webClient = new CheckoutWebClient(value -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(value));
            startActivity(intent);
        });
        webClient.setOnPageFinishLoad((view, url) -> {
            swipeRefresh.setRefreshing(false);
            isWebFinishLoad = true;
            setButtonNextEnable();
        });

        webView.setWebViewClient(webClient);
        webView.loadUrl(content);
        swipeRefresh.setRefreshing(true);

        checkBox.setOnClickListener(v -> {
            if (getIntent().getBooleanExtra(KEY_IS_USE_BTN_CONFIRM, false)) return;
            setResult();
        });

        checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            setButtonNextEnable();
        });

        btnConfirm.setOnClickListener(v -> setResult());
    }

    private void setButtonNextEnable() {
        btnConfirm.setEnabled(checkBox.isChecked() && isWebFinishLoad);
    }

    private void setResult() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void loadPlaceholderData() {
        models = new ArrayList<>();
        List<String> subcontent = new ArrayList<>();
        subcontent.add("bullet item");
        subcontent.add("bullet item");
        subcontent.add("bullet item");
        subcontent.add("bullet item");
        subcontent.add("bullet item");
        subcontent.add("bullet item");

        RTItemModel.FAQUModel item1 = new RTItemModel.FAQUModel();
        item1.setTitle("Title1");
        item1.setTopContent(getText(R.string.hipsum_1) + "");
        item1.setMidContent(subcontent);
        item1.setBottomContent(getText(R.string.hipsum_2) + "");

        RTItemModel.FAQUModel item2 = new RTItemModel.FAQUModel();
        item2.setTitle("Title2");
        item2.setTopContent(getText(R.string.hipsum_2) + "");
        item2.setMidContent(subcontent);
        item2.setBottomContent(getText(R.string.hipsum_1) + "");
        models.add(item1);
        models.add(item2);
    }
}
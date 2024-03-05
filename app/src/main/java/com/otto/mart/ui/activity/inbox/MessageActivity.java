package com.otto.mart.ui.activity.inbox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.inbox.Inbox;
import com.otto.mart.model.APIModel.Request.inbox.InboxReadRequest;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.localmodel.WebViewContent;
import com.otto.mart.support.util.ApiCallback;
import com.otto.mart.support.util.UIUtils;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.webView.WebViewActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_INBOX_READ;


public class MessageActivity extends AppActivity {

    private final String TAG = this.getClass().getSimpleName();

    public static final String KEY_INBOX_DATA = "inbox_data";

    private Window thiswindow;
    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private TextView titleSub;
    private TextView content;

    private Inbox mInbox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thiswindow = this.getWindow();
        setContentView(R.layout.activity_message_content);

        if (getIntent().getStringExtra(KEY_INBOX_DATA) != null) {
            String inboxDataJSON = getIntent().getStringExtra(KEY_INBOX_DATA);
            mInbox = new Gson().fromJson(inboxDataJSON, Inbox.class);
        }

        initComponent();
        initContent();

        if (mInbox != null) {
            displayInboxDetail();
        }
    }

    private void initComponent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        titleSub = findViewById(R.id.title_sub);
        content = findViewById(R.id.contentContainer);
    }

    private void initContent() {
        tvToolbarTitle.setText(getString(R.string.title_activity_inbox));

        btnToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void displayInboxDetail() {
        if (!mInbox.isRead()) {
            updateReadStatus(mInbox.getId());
        }

        titleSub.setText(mInbox.getTitle());
        UIUtils.setHtmlClickable(content, mInbox.getDescription(), data -> {
            WebViewContent webViewContent = new WebViewContent(mInbox.getTitle(), data.getString("url", ""));
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(AppKeys.KEY_WEBVIEW_CONTENT, webViewContent);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        finish();
    }

    private void updateReadStatus(int notificationId) {
        InboxReadRequest inboxReadRequest = new InboxReadRequest();
        inboxReadRequest.setNotification_id(notificationId);
        inboxReadRequest.setAction("read");

//        new InboxDao(this).inboxRead(inboxReadRequest, BaseDao.getInstance(this, API_KEY_INBOX_READ).callback);
        OttoKonekAPI.notificationAction(this, inboxReadRequest, new ApiCallback<BaseResponseModel>() {

            @Override
            public void onResponseSuccess(BaseResponseModel body) {
                dismissLoading();
                if (!isSuccess200) {
                    new ErrorDialog(MessageActivity.this, MessageActivity.this, false, false, body.getMeta().getMessage(), "").show();
                }
            }

            @Override
            public void onApiServiceFailed(Throwable throwable) {
                dismissLoading();
                onApiResponseError();
            }
        });
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case API_KEY_INBOX_READ:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        Log.d(TAG, "Update Read Status Success");
                    } else {
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                        dialog.show();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        ErrorDialog errorDialog = new ErrorDialog(this, this, false, false, message, message);
        errorDialog.show();
        onApiResponseError();
    }


    private void dismissLoading() {
        if (!isFinishing()) {
            ProgressDialogComponent.dismissProgressDialog(this);
        }
    }
}

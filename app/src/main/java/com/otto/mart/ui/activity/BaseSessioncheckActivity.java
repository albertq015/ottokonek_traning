package com.otto.mart.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.otto.mart.BuildConfig;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.CheckVersionResponseModel;
import com.otto.mart.model.APIModel.Response.inbox.InboxResponse;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Response;

import static com.otto.mart.keys.AppKeys.API_KEY_INBOX_LIST;

public class BaseSessioncheckActivity extends AppActivity {

    public OnGetInboxListener mOnGetInboxListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new EtcDao(this).versionCheckDao(BaseDao.getInstance(this, 9090).callback);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        new ProfileDao(this).getProfile(BaseDao.getInstance(this, 10001).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (response.isSuccessful()) {
            switch (responseCode) {
                case 10001:
                    if (((BaseResponseModel) br).getMeta().getCode() == 498) {
                        SessionManager.logout();
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        ErrorDialog dialog = new ErrorDialog(this, this, false, false, "Input Otp Login Gagal", ((BaseResponseModel) br).getMeta().getMessage());
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });
                        dialog.show();
                    }
                    break;
                case 9090:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        CheckVersionResponseModel model = null;
                        try {
                            model = (CheckVersionResponseModel) br;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (model != null) {
                            updateDialog(model.getData());
                        }
                    }
                    break;
                case API_KEY_INBOX_LIST:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        if (mOnGetInboxListener != null) {
                            mOnGetInboxListener.onGetInboxSuccess(((InboxResponse) br).getData().getUnread());
                        }
                    } else {

                    }
                    break;
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }

    public void updateDialog(CheckVersionResponseModel.CheckVersionResponseData data) {
        if (data.getVersion_api() > BuildConfig.VERSION_CODE) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            if (data.isForce_update()) {
                alertDialogBuilder
                        .setMessage("Please Update the Application")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                          gotoPlayStore(true);
                        });
            } else {
                alertDialogBuilder
                        .setNegativeButton("Later", (dialog, which) -> dialog.dismiss())
                        .setMessage("New Version Available on Play Store")
                        .setPositiveButton("Update", (dialogInterface, i) -> {
                            gotoPlayStore(false);
                        });

            }
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void gotoPlayStore(boolean isForceUpdate){
        final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://appgallery.huawei.com/#/app/C103366529")));

        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

        if (isForceUpdate) {
            SessionManager.logout();
            finish();
        }
    }

    public void setmOnGetInboxListener(OnGetInboxListener mOnGetInboxListener) {
        this.mOnGetInboxListener = mOnGetInboxListener;
    }

    public interface OnGetInboxListener {
        void onGetInboxSuccess(int unread);
    }
}

package com.otto.mart.ui.activity.ottopoint;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.otto.mart.keys.AppKeys;
import com.otto.mart.presenter.dao.OttoPointDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.LogHelper;
import com.otto.mart.ui.actionView.ResponseBalance;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.component.ProgressDialogComponent;

import static com.otto.mart.keys.AppKeys.KEY_BROADCAST_REFRESH_POINT;
import static com.otto.mart.keys.AppKeys.KEY_BROADCAST_REFRESH_POINT_TWO;
import static com.otto.mart.keys.AppKeys.KEY_BROADCAST_REFRESH_VIEW_DASHBOARD;
import static com.otto.mart.keys.AppKeys.KEY_EXTRA_POINT;

public class BaseActivityOttopoint extends BaseActivity {

    @SuppressLint("SetTextI18n")
    public void getBalanceOttoPoint(ResponseBalance callback){
        if(!SessionManager.isOttopointAuthExist()) return;

        OttoPointDao.getBalance(this, callback);
    }

    public void callToUpdatePoint(){
        Intent broadcast = new Intent();
        broadcast.setAction(KEY_BROADCAST_REFRESH_POINT);
        sendBroadcast(broadcast);
    }

    public static void callToUpdatePointGlobal(Context context){
        if(context == null ) return;

        Intent broadcast = new Intent();
        broadcast.setAction(KEY_BROADCAST_REFRESH_POINT);
        context.sendBroadcast(broadcast);
    }

    public void callToUpdatePointGlobalTwo(Context context, long point){
        Intent intent = new Intent(KEY_BROADCAST_REFRESH_POINT_TWO);
        intent.putExtra(KEY_EXTRA_POINT, point);
        context.sendBroadcast(intent);
    }

    public static void callToUpdate(Context context, String filterAction){
        if(context == null ) return;

        Intent broadcast = new Intent();
        broadcast.setAction(filterAction);
        context.sendBroadcast(broadcast);
    }

    public static void callToUpdate(Context context, String filterAction, String valueExtra){
        if(context == null ) return;

        Intent broadcast = new Intent();
        broadcast.setAction(filterAction);
        broadcast.putExtra(AppKeys.BROADCAST_VALUE_EXTRA, valueExtra);
        context.sendBroadcast(broadcast);
    }

    public void callToUpdateViewDashboard(){
        Intent broadcast = new Intent();
        broadcast.setAction(KEY_BROADCAST_REFRESH_VIEW_DASHBOARD);
        sendBroadcast(broadcast);
    }

    @SuppressLint("IntentReset")
    protected void pickContact(int requestNumber) {
//        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse(""));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, requestNumber);
    }

    public static void callToShowInfoEarnPoint(Context context, int point, String productName){
        if(context == null ) return;

        Log.e(BaseActivityOttopoint.class.getSimpleName(), "callToShowInfoEarnPoint: " + point + ", " + productName);

        Intent broadcast = new Intent(AppKeys.KEY_BROADCAST_GET_POINT);
        broadcast.putExtra(AppKeys.KEY_EXTRA_POINT, point);
        broadcast.putExtra(AppKeys.KEY_EXTRA_PRODUCT_NAME, productName);
        context.sendBroadcast(broadcast);
    }

    public void showProgress(Context context, boolean isShow){
        if(context == null) return;

        try {
            if(isShow) {
                ProgressDialog dialog = ProgressDialogComponent.showProgressDialog(context, null, false);
                if(!dialog.isShowing())
                    dialog.show();
            }else
                ProgressDialogComponent.dismissProgressDialog((BaseActivityOttopoint) context);
        }catch (Exception e){
            LogHelper.showError(BaseActivityOttopoint.class.getSimpleName(), e.getMessage());
        }
    }
}

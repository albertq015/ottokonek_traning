package com.otto.mart.support.util;

import android.content.Context;
import android.widget.Toast;

import com.otto.mart.R;
import com.otto.mart.ui.actionView.ActionDialog;
import com.otto.mart.ui.actionView.ActionDialogThree;
import com.otto.mart.ui.component.dialog.ComingSoonOttopointDialog;
import com.otto.mart.ui.component.dialog.InfoEligibleOttoPointDialog;


public class MessageHelper {

    public static void underConstructionMessage(Context context){
        if(context != null)
            defaultMessage(context, context.getString(R.string.text_under_construction));
    }

    public static void commingSoonKategoriDialog(Context context){
        if(context == null) return;

        ComingSoonOttopointDialog.showDialog(context);
    }

    public static void notFoundData(Context context){
        if(context != null)
            defaultMessage(context, context.getString(R.string.error_detail_id_empty));
    }

    public static void emptyFormInput(Context context){
        if(context != null)
            defaultMessage(context, context.getString(R.string.error_empty_input));
    }

    public static void userMessage(Context context, String message){
        defaultMessage(context, message);
    }

    public static void userMessageDialog(Context context, String message){
        if(context == null) return;

        InfoEligibleOttoPointDialog.showDialog(context, "", message, true, data -> {

        });
    }

    public static void userMessageDialog(Context context, String message, ActionDialog callback){
        if(context == null) return;

        InfoEligibleOttoPointDialog.showDialog(context, "", message, true, callback);
    }

    public static void userMessageDialog(Context context, String message, ActionDialogThree callback){
        if(context == null) return;

        InfoEligibleOttoPointDialog.showDialog(context, "", message, true, callback);
    }

    private static void defaultMessage(Context context, String message){
        if(context == null) return;

        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            LogHelper.showError(MessageHelper.class.getSimpleName(), e.getMessage());
        }
    }
}

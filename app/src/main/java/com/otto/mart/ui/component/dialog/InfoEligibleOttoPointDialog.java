package com.otto.mart.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.support.util.LogHelper;
import com.otto.mart.ui.actionView.ActionDialog;
import com.otto.mart.ui.actionView.ActionDialogThree;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoEligibleOttoPointDialog extends BaseBottomDialog {

    private static String TAG = InfoEligibleOttoPointDialog.class.getSimpleName();

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_desc)
    TextView tvDesc;

    private ActionDialog callback;
    private ActionDialogThree callbackThree;
    private String title = "";
    private String description = "";
    private boolean hideTitle;

    private InfoEligibleOttoPointDialog(@NonNull Context context, int themeResId, ActionDialog callback) {
        super(context, themeResId);

        this.callback = callback;
    }

    private InfoEligibleOttoPointDialog(@NonNull Context context, int themeResId, String title, String description, ActionDialog callback) {
        super(context, themeResId);

        this.callback = callback;
        this.title = title;
        this.description = description;
    }

    private InfoEligibleOttoPointDialog(@NonNull Context context, int themeResId, String title, String description, boolean hideTitle, ActionDialog callback) {
        super(context, themeResId);

        this.callback = callback;
        this.title = title;
        this.description = description;
        this.hideTitle = hideTitle;
    }

    private InfoEligibleOttoPointDialog(@NonNull Context context, int themeResId, String title, String description, boolean hideTitle, ActionDialogThree callback) {
        super(context, themeResId);

        this.callbackThree = callback;
        this.title = title;
        this.description = description;
        this.hideTitle = hideTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info_eligible_ottopoint_dialog);
        ButterKnife.bind(this);

        if(!title.isEmpty())
            tvTitle.setText(title);

        if(!description.isEmpty())
            tvDesc.setText(description);

        tvTitle.setVisibility(hideTitle ? View.GONE : View.VISIBLE);

        setOnCancelListener(dialog -> {
            if(callbackThree != null)
                callbackThree.closeDialog();
        });
    }

    @OnClick(R.id.view_back)
    public void closeDialog(){
        dismiss();
    }

    @OnClick(R.id.btn_ok)
    public void actionSubmit(){
        dismiss();

        if(callback != null){
            Bundle data = new Bundle();
            callback.submitAction(data);
        }

        if(callbackThree != null){
            Bundle data = new Bundle();
            callbackThree.submitAction(data);
        }
    }

    public static void showDialog(Context context, ActionDialog callback){
        try {
            new InfoEligibleOttoPointDialog(context, R.style.CustomDialog_Clear_FromBottom, callback).show();
        }catch (Exception e){
            LogHelper.showError(TAG, e.getMessage());
        }
    }

    public static void showDialog(Context context, String title, String description, ActionDialog callback){
        try {
            new InfoEligibleOttoPointDialog(context, R.style.CustomDialog_Clear_FromBottom, title, description, callback).show();
        }catch (Exception e){
            LogHelper.showError(TAG, e.getMessage());
        }
    }

    public static void showDialog(Context context, String title, String description, boolean hideTitle, ActionDialog callback){
        try {
            new InfoEligibleOttoPointDialog(context, R.style.CustomDialog_Clear_FromBottom, title, description, hideTitle, callback).show();
        }catch (Exception e){
            LogHelper.showError(TAG, e.getMessage());
        }
    }

    public static void showDialog(Context context, String title, String description, boolean hideTitle, ActionDialogThree callback){
        try {
            new InfoEligibleOttoPointDialog(context, R.style.CustomDialog_Clear_FromBottom, title, description, hideTitle, callback).show();
        }catch (Exception e){
            LogHelper.showError(TAG, e.getMessage());
        }
    }
}

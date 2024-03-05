package com.otto.mart.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.otto.mart.R;
import com.otto.mart.ui.actionView.ActionDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuccessUseVoucherDialog extends BaseBottomDialog {

    private String TAG = SuccessUseVoucherDialog.class.getSimpleName();

    private ActionDialog callback;

    private SuccessUseVoucherDialog(@NonNull Context context, int themeResId, ActionDialog callback) {
        super(context, themeResId);

        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_success_voucher);
        ButterKnife.bind(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        callback.submitAction(null);
    }

    @OnClick({R.id.btn_tutup, R.id.view_back})
    public void closeDialog(){
        dismiss();
    }

    public static void showDialog(Context context, ActionDialog callback){
        new SuccessUseVoucherDialog(context, R.style.CustomDialog_Clear_FromBottom, callback).show();
    }
}

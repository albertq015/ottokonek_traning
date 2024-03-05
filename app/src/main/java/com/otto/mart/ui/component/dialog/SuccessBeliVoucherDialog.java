package com.otto.mart.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.ui.actionView.ActionDialogTwo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuccessBeliVoucherDialog extends BaseBottomDialog {

    private final int ACTION_LIHAT_VOUCHER = 1;

    @BindView(R.id.btn_lihat_voucher)
    Button btnLihatVoucher;
    @BindView(R.id.tv_desc)
    TextView tvDesc;

    private ActionDialogTwo callback;
    private int actionDialog = -1;
    private boolean isShowButton = true;
    private String message = "";

    private SuccessBeliVoucherDialog(@NonNull Context context, int themeResId, ActionDialogTwo callback) {
        super(context, themeResId);

        this.callback = callback;
    }

    private SuccessBeliVoucherDialog(@NonNull Context context, int themeResId, boolean isShowButton, ActionDialogTwo callback) {
        super(context, themeResId);

        this.isShowButton = isShowButton;
        this.callback = callback;
    }

    private SuccessBeliVoucherDialog(@NonNull Context context, int themeResId, boolean isShowButton, String message, ActionDialogTwo callback) {
        super(context, themeResId);

        this.isShowButton = isShowButton;
        this.callback = callback;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_success_beli_voucher);
        ButterKnife.bind(this);

        if(message != null && !message.isEmpty())
            tvDesc.setText(message);

        btnLihatVoucher.setVisibility(isShowButton ? View.VISIBLE : View.GONE);

        btnLihatVoucher.setOnClickListener(view ->{
            setActionDialog(ACTION_LIHAT_VOUCHER);

            dismiss();
        });
    }

    @OnClick(R.id.view_back)
    public void closeDialog(){
        dismiss();
    }

    private void setActionDialog(int action){
        this.actionDialog = action;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        switch (actionDialog){
            case ACTION_LIHAT_VOUCHER:
                callback.submitAction(null);
                break;

            default:
                callback.dismisDialog();
                break;
        }
    }

    public static void showDialog(Context context, ActionDialogTwo callback){
        new SuccessBeliVoucherDialog(context, R.style.CustomDialog_Clear_FromBottom, callback).show();
    }

    public static void showDialog(Context context, boolean isShowButton, ActionDialogTwo callback){
        new SuccessBeliVoucherDialog(context, R.style.CustomDialog_Clear_FromBottom, isShowButton, callback).show();
    }

    public static void showDialog(Context context, boolean isShowButton, String message, ActionDialogTwo callback){
        new SuccessBeliVoucherDialog(context, R.style.CustomDialog_Clear_FromBottom, isShowButton, message, callback).show();
    }
}

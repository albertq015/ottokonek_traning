package com.otto.mart.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.otto.mart.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FailedTukarVoucherDialog extends BaseBottomDialog {

    private FailedTukarVoucherDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_failed_tukar_voucher);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.view_back, R.id.btn_tutup})
    public void closeDialog(){
        dismiss();
    }

    public static void showDialog(Context context){
        new FailedTukarVoucherDialog(context, R.style.CustomDialog_Clear_FromBottom).show();
    }
}

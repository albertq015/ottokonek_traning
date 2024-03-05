package com.otto.mart.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.otto.mart.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComingSoonOttopointDialog extends BaseBottomDialog {

    private String TAG = ComingSoonOttopointDialog.class.getSimpleName();

    private ComingSoonOttopointDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ottopoint_coming_soon);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_tutup, R.id.view_back})
    public void closePage(){
        dismiss();
    }

    public static void showDialog(Context context){
        new ComingSoonOttopointDialog(context, R.style.CustomDialog_Clear_FromBottom).show();
    }
}

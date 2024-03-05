package com.otto.mart.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.otto.mart.R;
import com.otto.mart.ui.actionView.ActionDialog;
import com.otto.mart.ui.component.ButtonToggleable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoiceDialog extends BaseBottomDialog {

    ActionDialog callBack;
    String title;
    String subtitle;
    @BindView((R.id.btn_no))
    Button btnNo;
    @BindView((R.id.btn_yes))
    Button btnYes;
    @BindView((R.id.tv_title))
    TextView tvTitle;
    @BindView((R.id.tv_sub_title))
    TextView tvSubTitle;

    private ChoiceDialog(@NonNull Context context, String title, String subTitle, int theme, ActionDialog callBack) {
        super(context, theme);
        this.callBack = callBack;
        this.title = title;
        this.subtitle = subTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_yes);
        ButterKnife.bind(this);


        tvTitle.setText(this.title);
        tvSubTitle.setText(this.subtitle);
        btnNo.setOnClickListener(
                view -> {
                    dismiss();
                }
        );

        btnYes.setOnClickListener(v -> {

            Bundle data = new Bundle();
            callBack.submitAction(data);
            //callBack.submitAction(null);
        });
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        callBack.submitAction(null);
    }

//    @OnClick({R.id.btn_no})
//    public void closePage() {
//        dismiss();
//    }
//
//    @OnClick({R.id.btn_yes})
//    public void submit() {
//        callBack.submitAction(null);
//    }
    // @OnClick({R.id.btn_yes, R.id.view_back})

    public static void showDialog(Context context, String title, String subTitle, ActionDialog callBack) {
        new ChoiceDialog(context, title, subTitle, R.style.CustomDialog_Clear_FromBottom, callBack).show();
    }
}

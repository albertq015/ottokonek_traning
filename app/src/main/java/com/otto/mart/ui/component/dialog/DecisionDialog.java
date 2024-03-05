package com.otto.mart.ui.component.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.ui.actionView.ActionDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DecisionDialog extends Dialog {

    private static final int CODE_TYPE_TUKAR_POINT = 1;
    private static final int CODE_TYPE_BELI_VOUCHER = 2;

    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.btn_no)
    Button btnNo;
    @BindView(R.id.btn_yes)
    Button btnYes;

    private int type = -1;
    private String price;
    private ActionDialog callback;

    private DecisionDialog(@NonNull Context context, int type, String price, ActionDialog callback) {
        super(context);

        this.type = type;
        this.price = price;
        this.callback = callback;
    }

    private DecisionDialog(@NonNull Context context,int type, ActionDialog callback) {
        super(context);

        this.type = type;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_decision);
        ButterKnife.bind(this);

        setView(type);

        // events

        btnYes.setOnClickListener(view -> {
            callback.submitAction(null);

            dismiss();
        });

        btnNo.setOnClickListener(view -> dismiss());
    }

    @SuppressLint("SetTextI18n")
    private void setView(int type){
        switch (type){
            case CODE_TYPE_TUKAR_POINT:
                tvDesc.setVisibility(View.VISIBLE);
                tvPrice.setVisibility(View.VISIBLE);

                tvDesc.setText(R.string.text_question_use_kupon);
                tvPrice.setText(price + "?");
                break;

            case CODE_TYPE_BELI_VOUCHER:
                tvDesc.setVisibility(View.VISIBLE);
                tvPrice.setVisibility(View.GONE);

                tvDesc.setText(R.string.text_question_buy_voucher);
                break;
        }
    }

    public static void showDecisionTukarPoint(Context context, String saldoOttopay, ActionDialog actionDialog){
        new DecisionDialog(context, CODE_TYPE_TUKAR_POINT, saldoOttopay, actionDialog).show();
    }

    public static void showDecisionBeliVoucher(Context context, ActionDialog actionDialog){
        new DecisionDialog(context, CODE_TYPE_BELI_VOUCHER, actionDialog).show();
    }
}

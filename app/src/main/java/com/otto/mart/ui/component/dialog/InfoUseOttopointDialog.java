package com.otto.mart.ui.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.ui.actionView.ActionDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoUseOttopointDialog extends Dialog {

    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.button_ok)
    Button buttonOk;

    private ActionDialog callback;

    private InfoUseOttopointDialog(@NonNull Context context, ActionDialog callback) {
        super(context);

        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info_use_ottopoint);
        ButterKnife.bind(this);

        // set view

        setViewDefault();

        String text = "Anda dapat menggunakan Otto Point untuk mendapatkan potongan hingga 80% dari total transaksi.<br/><br/><b>1</b> Otto Point senilai dengan <b>Rp 1</b>";
        tvText.setText(Html.fromHtml(text));

        // events

        buttonOk.setOnClickListener(v -> {
            callback.submitAction(null);

            dismiss();
        });
    }

    private void setViewDefault(){
        if(getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void showDialog(Context context, ActionDialog callback){
        new InfoUseOttopointDialog(context, callback).show();
    }
}

package com.otto.mart.ui.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.otto.mart.R;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuccesTukarKuponDialog extends Dialog {

    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.btn_back)
    Button btnBack;

    private String harga;

    public SuccesTukarKuponDialog(@NotNull Context context, String harga) {
        super(context);

        this.harga = harga;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_success_tukar_kupon);
        ButterKnife.bind(this);

        // set view
        tvDesc.setText(buildTextSuccess());

        // events

        btnBack.setOnClickListener(view -> dismiss());
    }

    private String buildTextSuccess(){
        return getContext().getString(R.string.text_success_kupon_1) +
                "\n" + getContext().getString(R.string.text_success_kupon_2) +
                 " " + harga;
    }
}

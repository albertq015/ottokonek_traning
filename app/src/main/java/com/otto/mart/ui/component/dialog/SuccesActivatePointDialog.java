package com.otto.mart.ui.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.widget.Button;

import com.otto.mart.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuccesActivatePointDialog extends Dialog {

    @BindView(R.id.btn_poin)
    Button btnPoin;

    public SuccesActivatePointDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_success_activate_point);
        ButterKnife.bind(this);

        // events

        btnPoin.setOnClickListener(view -> dismiss());
    }
}

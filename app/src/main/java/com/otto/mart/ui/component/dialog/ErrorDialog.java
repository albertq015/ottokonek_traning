package com.otto.mart.ui.component.dialog;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.ui.component.LazyDialog;


public class ErrorDialog extends LazyDialog {
    private View contentView;
    private TextView tvMessage;
    private Context mContext;
    private String title = "", text = "";

    public ErrorDialog(@NonNull Context context, Activity parent, Boolean isHideToolbar, Boolean hideCloseBtn, String title, String text) {
        super(context, parent, isHideToolbar, hideCloseBtn);
        mContext = context;
        this.text = text;
        this.title = title;
        initComponent();
        initContent();
        setPadding(16);
    }

    private void initComponent() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_error, null);
        this.setContainerView(contentView);
        this.getToolbarView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.ocean_blue));
        this.setToolbarDarkmode();
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        this.getToolbarView().setVisibility(View.GONE);

        TextView titleTv = this.getToolbarView().findViewById(R.id.title);
        tvMessage = contentView.findViewById(R.id.tvMessage);
        Button btnOK = contentView.findViewById(R.id.btnOK);

        titleTv.setText("");

        btnOK.setOnClickListener(v -> this.dismiss());
    }

    private void initContent() {
        tvMessage.setText(title);
    }
}

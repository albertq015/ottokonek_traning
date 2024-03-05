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
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.ui.component.LazyDialog;

public class DeleteConfirmationFaveDialog extends LazyDialog {
    private View contentView;
    private TextView tvMessage;
    private Context mContext;
    private ImageView icon;
    private String title = "", text = "" ,button ="";
    IConfimation listener;
    private String favorite;

    public DeleteConfirmationFaveDialog(@NonNull Context context, Activity parent,String favorite,IConfimation listener) {
        super(context, parent);
        mContext = context;
        this.text = text;
        this.title = title;
        this.button = button;
        this.listener = listener;
        this.favorite = favorite;
        initComponent();
        initContent();
        setPadding(16);
    }

    private void initComponent() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.confirmation_dialog, null);
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
        icon = contentView.findViewById(R.id.logo);
        Button btnOK = contentView.findViewById(R.id.btnOK);
        Button btnCancel = contentView.findViewById(R.id.btnCancel);
        icon.setVisibility(View.GONE);

        titleTv.setText("");



        //btnOK.setText(button);
        btnOK.setOnClickListener(
                v -> {
                    listener.onClick();
                    this.dismiss();
                });

        btnCancel.setOnClickListener(view -> this.dismiss());
    }

    public void setListener(IConfimation listener) {
        this.listener = listener;
    }

    private void initContent() {
        String text = "Hapus nomor @%@ dari favorite ini ?";
        text = text.replace("@%@",favorite);
        tvMessage.setText(text);
    }


}

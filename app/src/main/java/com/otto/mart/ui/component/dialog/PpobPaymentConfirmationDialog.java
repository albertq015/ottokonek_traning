package com.otto.mart.ui.component.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.otto.mart.OttoMartApp;
import com.otto.mart.R;
import com.otto.mart.support.util.Connectivity;
import com.otto.mart.ui.component.LazyDialog;

import java.util.Timer;
import java.util.TimerTask;

public class PpobPaymentConfirmationDialog extends LazyDialog {

    View contentView, qrloading, layout_actioncontainer, action, action2;
    TextView tv_payment, tv_message;
    Timer timer;
    int retries = 0;

    Context mContext;
    LazyDialog mDialog;

    private int retriesLimit = 5;
    private boolean mIsLostConnection = false;

    onActionClickListener listener;

    public PpobPaymentConfirmationDialog(@NonNull Context context, Activity parent, Boolean isHideToolbar, Boolean hideCloseBtn) {
        super(context, parent, isHideToolbar, hideCloseBtn);
        mContext = context;
        mDialog = this;
        initComponent();
    }

    public PpobPaymentConfirmationDialog(@NonNull Context context, Activity parent, Boolean isHideToolbar, Boolean hideCloseBtn, boolean isLostConnection) {
        super(context, parent, isHideToolbar, hideCloseBtn);
        mContext = context;
        mDialog = this;
        mIsLostConnection = isLostConnection;
        initComponent();

        if(mIsLostConnection){
            setupListener();
            hideLoading();
        }
    }

    public void setListener(onActionClickListener listener) {
        this.listener = listener;
        setupListener();
    }

    public void setTimerListener(onActionClickListener listener) {
        this.listener = listener;
        if(!mIsLostConnection){
            setupTimerListener();
        }
    }

    private void initComponent() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_cashpayment, null);
        this.setContainerView(contentView);
        this.getToolbarView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.ocean_blue));
        this.setToolbarDarkmode();
        setTitle("Pembayaran Cash");
        layout_actioncontainer = findViewById(R.id.layout_actioncontainer);
        action = findViewById(R.id.action);
        action2 = findViewById(R.id.action2);
        tv_payment = findViewById(R.id.tv_payment);
        qrloading = findViewById(R.id.qrloading);

        tv_message = findViewById(R.id.tv_message);
    }

    public void setupListener() {
        layout_actioncontainer.setVisibility(View.VISIBLE);
        action.setOnClickListener(v -> {
            if (listener != null) {
                if(Connectivity.isConnected(getContext())){
                    listener.onActionClick();
                    showLoading();
                } else {
                    Toast.makeText(getContext(), OttoMartApp.getContext().getString(R.string.msg_check_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
        action2.setOnClickListener(v -> mDialog.dismiss());
    }

    public void setupTimerListener() {
        layout_actioncontainer.setVisibility(View.GONE);
        if (retries > retriesLimit) {
            stopTimedListener();
            setupListener();
            return;
        }
        qrloading.setVisibility(View.VISIBLE);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();

        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null)
                                    listener.onActionClick();
                            }
                        });
                        retries++;

                    }
                },
                6000
        );
    }

    public void stopTimedListener() {
        qrloading.setVisibility(View.GONE);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void showLoading() {
        retries = 0;

        tv_payment.setText(R.string.ppob_msg_transaction_in_progress);

        qrloading.setVisibility(View.VISIBLE);
        tv_payment.setVisibility(View.VISIBLE);
        layout_actioncontainer.setVisibility(View.GONE);
    }

    private void hideLoading() {
        retries = retriesLimit + 1;

        qrloading.setVisibility(View.GONE);
        tv_payment.setVisibility(View.VISIBLE);
        layout_actioncontainer.setVisibility(View.VISIBLE);
    }

    public void setAmount(String cash) {
        //tv_payment.setText(cash);
    }

    public void  setReturnMessage(String msg){
        tv_message.setText(msg);
        tv_message.setVisibility(View.GONE);
        tv_payment.setText(msg);
    }

    public interface onActionClickListener {
        void onActionClick();
    }
}

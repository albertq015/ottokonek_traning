package com.otto.mart.ui.component.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.support.util.BitmapUtil;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.support.util.DeviceUtil;
import com.otto.mart.ui.component.LazyDialog;

import java.util.Timer;
import java.util.TimerTask;

public class QrDynamicDialog extends LazyDialog {
    View contentView, qrloading, layout_btmdecor, layout_actioncontainer, action, anchor1, qrloading2, action2;
    Context mContext;
    LazyDialog mDialog;
    LinearLayout anchor0;
    ImageView qrItem;
    TextView tv_qrpayment, tv_product;
    Timer timer;
    int retries = 0;

    onActionClickListener listener;

    public QrDynamicDialog(@NonNull Context context, Activity parent, Boolean hideCloseBtn) {
        super(context, parent, hideCloseBtn);
        mContext = context;
        mDialog = this;
        initComponent();
    }

    public void setListener(onActionClickListener listener) {
        this.listener = listener;
        setupListener();
    }

    public void setTimerListener(onActionClickListener listener) {
        this.listener = listener;
        setupTimerListener();
    }

    private void initComponent() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_qr_dynamic, null);
        this.setContainerView(contentView);
        this.getToolbarView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.ocean_blue));
        this.setToolbarDarkmode();

        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        TextView titleTv = this.getToolbarView().findViewById(R.id.title);
        titleTv.setText(getContext().getString(R.string.text_qr_code));

        qrloading = findViewById(R.id.qrloading);
        qrItem = findViewById(R.id.qritem);
        tv_qrpayment = findViewById(R.id.tv_qrpayment);
        tv_product = findViewById(R.id.tv_product);
        layout_btmdecor = findViewById(R.id.layout_btmdecor);
        anchor0 = findViewById(R.id.anchor0);
        layout_actioncontainer = findViewById(R.id.layout_actioncontainer);
        action = findViewById(R.id.action);
        anchor1 = findViewById(R.id.anchor1);
        action2 = findViewById(R.id.action2);

        qrloading2 = findViewById(R.id.qrloading2);
    }

    public void setProduct(String product, String cash, String qrs) {
        if (product != null) {
            anchor0.setVisibility(View.VISIBLE);
            tv_product.setText(product);
        }
        layout_btmdecor.setVisibility(View.GONE);
        layout_actioncontainer.setVisibility(View.VISIBLE);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onActionClick();
                }
            }
        });
        setAmount(cash);
        setQr(qrs);
    }

    public void setProductName(String product) {
        if (product != null) {
            anchor0.setVisibility(View.VISIBLE);
            tv_product.setText(product);
        }
    }

    public void setAmount(String cash) {
        if (cash != null) {
            if (!cash.equals("")) {
                tv_qrpayment.setText(DataUtil.convertCurrency(DataUtil.getInt(cash)));
                anchor1.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setupListener() {
        layout_actioncontainer.setVisibility(View.VISIBLE);
        action.setOnClickListener(v -> {
            if (listener != null) {
                listener.onActionClick();
            }
        });
        action2.setOnClickListener(v -> mDialog.dismiss());

    }

    public void setupTimerListener() {
        layout_actioncontainer.setVisibility(View.GONE);
        if (retries > 5) {
            stopTimedListener();
            setupListener();
            return;
        }
        qrloading2.setVisibility(View.VISIBLE);
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
        qrloading2.setVisibility(View.GONE);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void showAmount() {
        anchor1.setVisibility(View.VISIBLE);
    }

    public void hideAmount() {
        anchor1.setVisibility(View.GONE);
    }

    public void setQr(String qrString) {
        Bitmap bitmap = BitmapUtil.generateBitmap(
                qrString
                , 9, DeviceUtil.dpToPx(480), DeviceUtil.dpToPx(480));
        qrloading.setVisibility(View.GONE);
        qrItem.setImageBitmap(bitmap);
    }

    public void removeBottomDecor() {
        layout_btmdecor.setVisibility(View.GONE);
        layout_actioncontainer.setVisibility(View.VISIBLE);
    }

    public void hideActionLayout(){
        layout_actioncontainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void dismiss() {
        stopTimedListener();
        super.dismiss();
    }

    public interface onActionClickListener {
        void onActionClick();
    }

}

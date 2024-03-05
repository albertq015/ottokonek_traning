package com.otto.mart.ui.activity.ottopoint;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.goodiebag.pinview.Pinview;
import com.otto.mart.R;
import com.otto.mart.support.util.CommonHelper;
import com.otto.mart.ui.component.ActionbarOttopointWhite;

import app.beelabs.com.codebase.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivePointPinActivity extends BaseActivity {

    private String TAG = ActivePointPinActivity.class.getSimpleName();

    @BindView(R.id.tv_countdown)
    TextView tvCountdown;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.pinview)
    Pinview pinview;
    @BindView(R.id.view_actionbar)
    ActionbarOttopointWhite viewActionbar;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_point_pin);
        ButterKnife.bind(this);

        // set input type pin
        pinview.setInputType(Pinview.InputType.NUMBER);

        // set countdown timer
        countDownCode();

        // events

        viewActionbar.setActionMenuLeft(view -> onBackPressed());
        tvSendCode.setOnClickListener(view -> actionResendCode());

        pinview.setPinViewEventListener((pinview, fromUser) -> {
            if(pinview.getValue().length() == 4){
                // hide soft keyboard
                CommonHelper.hideKeyboard(ActivePointPinActivity.this);

                // do post data
                new Handler().postDelayed(() -> actionSubmit(pinview.getValue()), 1500);
            }
        });
    }

    private void showButtonResendCode(boolean isShow){
        if(isShow){
            tvCountdown.setVisibility(View.GONE);
            tvSendCode.setVisibility(View.VISIBLE);
        }else{
            tvCountdown.setVisibility(View.VISIBLE);
            tvSendCode.setVisibility(View.GONE);

            // restart countdown
            if(countDownTimer != null) countDownTimer.start();
        }
    }

    @SuppressLint("SetTextI18n")
    private void countDownCode(){
        countDownTimer = new CountDownTimer(60000, 1000) { // milisecond (1 minute = 60000)
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000)  / 60;
                int seconds = (int)((millisUntilFinished / 1000) % 60);

                String textMinutes = minutes < 10 ? "0" + minutes : Long.toString(minutes);
                String textSeconds = seconds < 10 ? "0" + seconds : Integer.toString(seconds);
                tvCountdown.setText(textMinutes + ":" + textSeconds);
            }

            public void onFinish() {
                showButtonResendCode(true);
            }
        };

        countDownTimer.start();
    }

    private void actionResendCode(){
        showButtonResendCode(false);
    }

    private void actionSubmit(String pinValue){
        // do action in here
        finish();

        // send broadcast
        sendBroadcast(new Intent().setAction(DetailPoinActivity.KEY_BROADCAST_SUCCESS_ACTIVATION_PEDE));
    }
}

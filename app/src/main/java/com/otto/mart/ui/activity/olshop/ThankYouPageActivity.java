package com.otto.mart.ui.activity.olshop;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.component.LazyTextview;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import github.nisrulz.screenshott.ScreenShott;

public class ThankYouPageActivity extends AppActivity {

    private final String TAG = this.getClass().getSimpleName();

    TextView dialogTitle, yourCredit, done,total;
    LazyTextview paymentNo, transactionDate, custName;
    View closeButton,shareButton;
    LinearLayout nsvParentSS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.white);
        setStatusBarLightMode();
        setContentView(R.layout.activity_thank_you_page);

        initComponent();
        initContent();

        //Play Notification Ringtone
        playNotificationTone();
    }

    @Override
    public void onBackPressed() {
        executeIntent(CatalogActivity.class);
    }

    private void initComponent() {
        dialogTitle = findViewById(R.id.dialogTitle);
        yourCredit = findViewById(R.id.yourCredit);
        total = findViewById(R.id.total);
        closeButton = findViewById(R.id.imgClose);
        done = findViewById(R.id.done);
        transactionDate = findViewById(R.id.transactionDate);
        paymentNo = findViewById(R.id.paymentNo);
        custName = findViewById(R.id.custName);
        nsvParentSS = findViewById(R.id.nsv_parent_ss);
        shareButton = findViewById(R.id.imgShare);
    }

    private void initContent() {
        Long data = getIntent().getLongExtra("total", 0);
        int paymentNumber = getIntent().getIntExtra("paymentId", -1);
        total.setText(DataUtil.convertCurrency(data));
        paymentNo.setText(String.valueOf(paymentNumber));
        custName.setText(getIntent().getStringExtra("custName"));
        transactionDate.setText(DataUtil.getDateString((Calendar.getInstance()).getTimeInMillis()));
        yourCredit.setOnClickListener(v -> executeIntent(OrderStatusActivity.class));
        closeButton.setOnClickListener(v -> executeIntent(null));
        done.setOnClickListener(v -> executeIntent(null));
        shareButton.setVisibility(View.VISIBLE);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareReceipt(new ArrayList<>());
            }
        });
    }

    private void executeIntent(Class clazz) {
        Intent intent = new Intent(ThankYouPageActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (clazz == null) {
            finish();
        } else {
            startActivity(new Intent(ThankYouPageActivity.this, clazz));
            finish();
        }
    }

    private void shareReceipt(List<WidgetBuilderModel> saleData) {
        // View with spaces as per constraints
        //Bitmap bitmapView = ScreenShott.getInstance().takeScreenShotOfView(nsvParentSS);
        Bitmap bitmapView = ScreenShott.getInstance().takeScreenShotOfRootView(nsvParentSS);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(uri);
            bitmapView.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, getString(R.string.ppob_msg_share_it)));
    }

    private void playNotificationTone() {
        MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.ottopay_notification_ringtone);

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                Log.i(TAG, "Silent mode");
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Log.i(TAG, "Vibrate mode");
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                Log.i(TAG, "Normal mode");
                if (mPlayer != null) {
                    mPlayer.start();
                }
                break;
        }
    }
}

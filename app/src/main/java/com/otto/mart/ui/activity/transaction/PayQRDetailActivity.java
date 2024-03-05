package com.otto.mart.ui.activity.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Request.FavoriteAddRequestModel;
import com.otto.mart.model.APIModel.Request.PpobReceiptRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpEarningPointPulsaResponseModel;
import com.otto.mart.presenter.dao.OttoPointDao;
import com.otto.mart.presenter.dao.PpobDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.ui.Partials.adapter.KeyValueListAdapter;
import com.otto.mart.ui.actionView.HandleResponseApi;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.dashboard.DashboardActivity;
import com.otto.mart.ui.activity.ottopoint.BaseActivityOttopoint;
import com.otto.mart.ui.activity.tokoOttopay.OrderHistoryActivity;
import com.otto.mart.ui.component.dialog.EarnPointDialog;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import github.nisrulz.screenshott.ScreenShott;
import retrofit2.Response;

public class PayQRDetailActivity extends AppActivity {

    private final String TAG = this.getClass().getSimpleName();

    public static final String KEY_IS_CONTAIN_KOMISI = "is_contain_komisi";
    public static final String KEY_PRODUCT_TYPE = "product_type";
    private final String KEY_IS_FROM_RECEIPT = "is_from_receipt";
    private final String KEY_KOMISI = "komisi";

    private NestedScrollView nsvParent;
    ImageView closeButton;
    LinearLayout shareButton;
    RecyclerView rv_asd;
    KeyValueListAdapter displayAdapter;
    NestedScrollView nsv_parent;
    LinearLayout nsvParentSS;
    LinearLayout tokoOttopayLayout;
    Button btnCheckStatus;
    Button btnDone;

    View header, container_save, tv_save;
    TextView tvHeader, tvFooter;

    private boolean isFromReceeipt = false;
    List<WidgetBuilderModel> saleData = new ArrayList();
    String headerFooter = "";
    private boolean isOrderPayOffSuccess = false;

    private String productType;
    private String productCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_qrdetail);

        if (getIntent().getBooleanExtra(AppKeys.KEY_ORDER_PAYMENT_SUCCESS, false)) {
            isOrderPayOffSuccess = getIntent().getBooleanExtra(AppKeys.KEY_ORDER_PAYMENT_SUCCESS, false);
        }

        initComponent();
        initContent();
        getDataIntent();
        callEarnOttopoint();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra("isBogasari", false)) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
    }

    private void initComponent() {
        closeButton = findViewById(R.id.closeButton);
        shareButton = findViewById(R.id.shareButton);
        rv_asd = findViewById(R.id.rv_asd);
        rv_asd.setFocusable(false);
        nsv_parent = findViewById(R.id.nsv_parent);
        header = findViewById(R.id.header);
        container_save = findViewById(R.id.container_save);
        tv_save = findViewById(R.id.tv_save);
        nsvParent = findViewById(R.id.nsv_parent);
        tvHeader = findViewById(R.id.tv_header);
        tvFooter = findViewById(R.id.tv_footer);
        nsvParentSS = findViewById(R.id.nsv_parent_ss);
        tokoOttopayLayout = findViewById(R.id.toko_ottopay_layout);
        btnCheckStatus = findViewById(R.id.btn_check_status);
        btnDone = findViewById(R.id.btn_done);
    }

    private void initContent() {
        header.requestFocus();

        if (isOrderPayOffSuccess) {
            tokoOttopayLayout.setVisibility(View.VISIBLE);
        }

        if (getIntent().hasExtra("data")) {
            saleData = getIntent().getParcelableArrayListExtra("data");
            if (getIntent().getStringExtra("uimsg") != null) {
                headerFooter = getIntent().getStringExtra("uimsg");
            }
            if (getIntent().hasExtra(KEY_IS_FROM_RECEIPT)) {
                isFromReceeipt = getIntent().getBooleanExtra(KEY_IS_FROM_RECEIPT, false);
            }
            displayAdapter = new KeyValueListAdapter(saleData, R.layout.cw_textview_c_letwrapper);
            rv_asd.setAdapter(displayAdapter);
            rv_asd.setLayoutManager(new LinearLayoutManager(this));
            displayAdapter.notifyDataSetChanged();
            shareButton.setVisibility(View.VISIBLE);
        }

        closeButton.setOnClickListener(v -> onBackPressed());

        shareButton.setOnClickListener(v -> {
            if (getIntent().hasExtra(KEY_IS_CONTAIN_KOMISI)) {

                List<WidgetBuilderModel> receiptList = new ArrayList();

                for (WidgetBuilderModel data : saleData) {
                    if (!data.getKey().equalsIgnoreCase(KEY_KOMISI)) {
                        receiptList.add(data);
                    }
                }

                gotoReceipt(receiptList);
            } else {
                shareReceipt(saleData);
            }
        });


        if (getIntent().hasExtra("productCode") && getIntent().hasExtra("custRef") && getIntent().hasExtra("productType")) {
            FavoriteAddRequestModel model = new FavoriteAddRequestModel();

            model.setCustomer_reference(getIntent().getStringExtra("custRef"));
            model.setProduct_code(getIntent().getStringExtra("productCode"));
            model.setProduct_type(getIntent().getStringExtra("productType"));

            container_save.setVisibility(View.GONE);
            tv_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callSaveFavoriteAPI(model);
                }
            });
        }

        //Display Header & Footer
        if (!headerFooter.equals("")) {
            headerFooter = headerFooter.replace("|", "#");
            String[] separatedheaderFooters = headerFooter.split("#");

            if (separatedheaderFooters.length > 0) {
                tvHeader.setText(separatedheaderFooters[0]);
                tvHeader.setVisibility(View.VISIBLE);
            }

            if (separatedheaderFooters.length > 1) {
                tvFooter.setText(separatedheaderFooters[1]);
                tvFooter.setVisibility(View.VISIBLE);
            }
        }

        btnCheckStatus.setOnClickListener(v -> {
            startActivity(new Intent(this, OrderHistoryActivity.class));
            finish();
        });

        btnDone.setOnClickListener(v -> {
            finish();
        });

        if (isFromReceeipt) {
            ProgressDialogComponent.showProgressDialog(this, "Mohon Menunggu", false);

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                ProgressDialogComponent.dismissProgressDialog(PayQRDetailActivity.this);
                shareReceipt(saleData);
                finish();
            }, 1000);
        } else {
            //Play Notification Ringtone
            playNotificationTone();
        }
    }

    private void gotoReceipt(List<WidgetBuilderModel> receiptList){
        Intent intent = new Intent(this, PayQRDetailActivity.class);
        intent.putExtra("data", (ArrayList<? extends Parcelable>) receiptList);
        intent.putExtra(KEY_IS_CONTAIN_KOMISI, true);
        intent.putExtra(KEY_IS_FROM_RECEIPT, true);
        startActivity(intent);
    }

    private void getReceipt(String pool) {
        ProgressDialogComponent.showProgressDialog(this, "Mohon Menunggu", false);

        String referenceNumber = "";
        for (WidgetBuilderModel widgetBuilderModel : saleData) {
            if (widgetBuilderModel.getKey().equalsIgnoreCase("no. referensi customer")) {
                referenceNumber = widgetBuilderModel.getValue();
            }
        }

        if (!referenceNumber.equals("")) {
            PpobReceiptRequestModel ppobReceiptRequestModel = new PpobReceiptRequestModel();
            ppobReceiptRequestModel.setReference_number(referenceNumber);

            new PpobDao(this).receipt(pool, ppobReceiptRequestModel, BaseDao.getInstance(this, AppKeys.API_KEY_PPOB_RECEIPT).callback);
        } else {
            showErrorMessage("Data tidak ditemukan");
        }
    }

    private void callSaveFavoriteAPI(FavoriteAddRequestModel requestModel) {
        ProgressDialogComponent.showProgressDialog(this, "Mohon Menunggu", false);
        PpobDao dao = new PpobDao(this);
        dao.saveFav(requestModel, BaseDao.getInstance(this, 100).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        if (response.isSuccessful()) {
            switch (responseCode) {
                case 100:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        Toast.makeText(this, "Data Telah Tersimpan", Toast.LENGTH_SHORT).show();
                        container_save.setVisibility(View.GONE);
                    } else
                        Toast.makeText(this, ((BaseResponseModel) br).getMeta().getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case AppKeys.API_KEY_PPOB_RECEIPT:
                    if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                        Intent intent = new Intent(this, PayQRDetailActivity.class);
                        intent.putExtra("data", (ArrayList<? extends Parcelable>) ((BasePaymentResponseModel) br).getData().getKeyValueList());
                        intent.putExtra(KEY_IS_CONTAIN_KOMISI, true);
                        intent.putExtra(KEY_IS_FROM_RECEIPT, true);
                        startActivity(intent);
                    } else {
                        showErrorMessage(((BaseResponseModel) br).getMeta().getMessage());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    protected void showErrorMessage(String message) {
        ErrorDialog dialog = new ErrorDialog(this, this, false,
                false, message, "");
        dialog.show();
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

    private void getDataIntent(){
        Intent intent = getIntent();
        if(intent != null){
            Bundle data = intent.getExtras();
            if(data != null){
                if(data.containsKey("productType"))
                    productType = data.getString("productType");

                if(data.containsKey("productCode"))
                    productCode = data.getString("productCode");
            }
        }
    }

    private void showProgress(boolean isShow){
        if(isShow)
            ProgressDialogComponent.showProgressDialog(this, "Mohon Menunggu", false);
        else
            ProgressDialogComponent.dismissProgressDialog(this);
    }

    private String getNameFromListTransaction(){
        String result = "pulsa";

        for (WidgetBuilderModel item: saleData) {
            if(item.getKey().equalsIgnoreCase("Harga")){
                result += " " + item.getValue();
                break;
            }
        }

        return result;
    }

    private void callEarnOttopoint(){
        // cek jika transaksi yang dilakukan adalah transaksi beli pulsa
        if(productType == null) return;
        if(productCode.isEmpty()) return;
        if(!SessionManager.isOttopointAuthExist()) return;

        if(productType.equalsIgnoreCase("phone-prepaid2") && productType.equalsIgnoreCase("phone-postpaid")) {
            showProgress(true);
            OttoPointDao.earningPointFromPulsa(PayQRDetailActivity.this, productCode, "00", new HandleResponseApi() {
                @Override
                public void resultApiSuccess(BaseResponse br, Response response) {
                    showProgress(false);

                    // check if response data is empty
                    if(br == null) return;

                    long point = 0;
                    if(br instanceof OpEarningPointPulsaResponseModel){
                        OpEarningPointPulsaResponseModel result = (OpEarningPointPulsaResponseModel) br;

                        point = result.getData().getPoints();
                    }

                    if(point < 1) return;

                    // do update point on page
                    //BaseActivityOttopoint.callToShowInfoEarnPoint(PayQRDetailActivity.this, point, productName);
                    BaseActivityOttopoint.callToUpdatePointGlobal(PayQRDetailActivity.this);

                    EarnPointDialog.showDialog(PayQRDetailActivity.this, point, getNameFromListTransaction(), data -> {
                        // do something in here
                    });
                }

                @Override
                public void resultApiFailed(String message, int responseCodeHttp) {
                    showProgress(false);
                }
            });
        }
    }
}

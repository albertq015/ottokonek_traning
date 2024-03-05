package com.otto.mart.ui.activity.qr.scan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.LoginQrResponseModel;
import com.otto.mart.presenter.dao.AuthDao;
import com.otto.mart.support.util.EmvcoUtil;
import com.otto.mart.ui.activity.AppActivity;

import java.util.Collections;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Response;

public class QRScanSignUpActivity extends AppActivity {

    private ZXingScannerView mScannerView;
    private boolean contentInitialized;
    private ViewGroup back;
    private String merchantId;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                w.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
            }
        }
        setContentView(R.layout.activity_qr_login);
        initPermissions();
        try {
            initComponent();
            initContent();
        } catch (Exception e) {

        }
    }

    private void initComponent() {
        mScannerView = findViewById(R.id.cam_view);
        back = findViewById(R.id.backhitbox);
    }

    private void initContent() {
        mScannerView.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        mScannerView.setResultHandler(scannerHandler);
        contentInitialized = true;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initPermissions() {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                initComponent();
                initContent();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                finish();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.cancelPermissionRequest();
                finish();
            }
        }).check();
    }

    ZXingScannerView.ResultHandler scannerHandler = new ZXingScannerView.ResultHandler() {
        @Override
        public void handleResult(Result result) {
            mScannerView.resumeCameraPreview(this);
            merchantId = EmvcoUtil.parseEMVCO(result.toString());
            Toast.makeText(QRScanSignUpActivity.this, merchantId, Toast.LENGTH_SHORT).show();
            if (getIntent().hasExtra("isSFARegister")) {
                Intent intent = new Intent();
                intent.putExtra("merchantId", merchantId);
                setResult(RESULT_OK, intent);
                finish();
            } else if (getIntent().hasExtra("isRegister") && getIntent().getBooleanExtra("isRegister", false))
                callGetPhoneApiFromSfa(merchantId);
            else
                callGetPhoneApi(merchantId);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (contentInitialized) {
            mScannerView.setResultHandler(scannerHandler);
            mScannerView.startCamera();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (contentInitialized) {
            mScannerView.stopCamera();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void callGetPhoneApi(String merchId) {
        new AuthDao(this).requestPhoneNumDao(merchId, BaseDao.getInstance(this, 111).callback);
    }

    private void callGetPhoneApiFromSfa(String merchId) {
        new AuthDao(this).requestPhoneNumDao(merchId, BaseDao.getInstance(this, 112).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(QRScanSignUpActivity.this);
        if (response.isSuccessful())
            switch (responseCode) {
                case 111:
                    if (((LoginQrResponseModel) br).getMeta().getCode() == 200) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("phonejink", ((LoginQrResponseModel) br).getPhone());
                        returnIntent.putExtra("merchantId", merchantId);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Toast.makeText(this, "Merchant tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 112:
                    if (((LoginQrResponseModel) br).getMeta().getCode() == 200) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("phonejink", ((LoginQrResponseModel) br).getPhone());
                        returnIntent.putExtra("merchantId", merchantId);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Toast.makeText(this, "Merchant tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:

                    break;
            }

    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }
}
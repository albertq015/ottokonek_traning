package com.otto.mart.ui.activity.deposit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.LoginQrResponseModel;
import com.otto.mart.presenter.dao.AuthDao;
import com.otto.mart.support.util.EmvcoUtil;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;

import java.util.Collections;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Response;

public class TransferSaldoQRActivity extends AppActivity {

    private ZXingScannerView mScannerView;
    private boolean contentInitialized;
    private ViewGroup backButton;
    private String merchantId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_saldo_qr);
        initPermissions();

        try {
            initComponent();
            initContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initComponent() {
        mScannerView = findViewById(R.id.cam_view);
        backButton = findViewById(R.id.backhitbox);
    }

    private void initContent() {
        mScannerView.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        mScannerView.setResultHandler(scannerHandler);
        contentInitialized = true;
        Intent intent = new Intent(TransferSaldoQRActivity.this, TransferSaldoQRDetailActivity.class);
//        startActivity(intent);
        backButton.setOnClickListener(new View.OnClickListener() {
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
//            startActivity(new Intent(TransferSaldoQRActivity.this, TransferSaldoQRDetailActivity.class));
            merchantId = EmvcoUtil.parseEMVCO(result.toString());
            callCheckMerchant();
        }

    };

    private void callCheckMerchant() {
        new AuthDao(TransferSaldoQRActivity.this).requestPhoneNumDao(merchantId, BaseDao.getInstance(this, 920).callback);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (responseCode == 920) {
            if (((BaseResponseModel) br).getMeta().getCode() == 200) {
                LoginQrResponseModel model = (LoginQrResponseModel) br;
                Intent intent = new Intent(this, TransferSaldoQRDetailActivity.class);
                intent.putExtra("phone", model.getPhone());
                startActivity(intent);
                finish();
            } else {
                ErrorDialog dialog = new ErrorDialog(this, this, false, false, ((BaseResponseModel) br).getMeta().getMessage(), "");
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mScannerView.setResultHandler(scannerHandler);
                        mScannerView.startCamera();
                    }
                });
                dialog.show();
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        // super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }

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
}

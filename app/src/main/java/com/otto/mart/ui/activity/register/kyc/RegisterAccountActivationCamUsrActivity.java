package com.otto.mart.ui.activity.register.kyc;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.Realm.ImageBase64StoreModel;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.ui.activity.AppActivity;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;

import java.io.ByteArrayOutputStream;

import app.beelabs.com.codebase.component.ProgressDialogComponent;
import io.realm.Realm;
//activity not in use
public class RegisterAccountActivationCamUsrActivity extends AppActivity {

    private CameraView mCameraView;
    private View v_capture, backhitbox;
    private FrameLayout layout_bgholder, layout_bgholder_ava;
    private ImageView imgv;
    private TextView tv_title, tv_desc, action;
    private int bg = R.drawable.scrim_userktp;
    private String textMain, textSub;
    private int state = -1, facing = 0;
    private boolean isShowTuts = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        if (getIntent().hasExtra("jenk")) {
            state = getIntent().getIntExtra("jenk", 0);
            switch (state) {
                case 1:
                    bg = R.color.transparent;
                    facing = 0;
                    textMain = getString(R.string.s_ain_2);
                    textSub = getString(R.string.s_sub_2);

                    break;
                case 2:
                    bg = R.drawable.scrim_userktp;
                    facing = 1;
                    textMain = getString(R.string.s_ain_1);
                    textSub = getString(R.string.s_sub_1);
                    isShowTuts = true;
                    break;
                case 3:
                    bg = R.color.transparent;
                    facing = 0;
                    textMain = getString(R.string.s_ain_3);
                    textSub = getString(R.string.s_sub_3);
                    break;
                default:
                    break;
            }
        }
        setContentView(R.layout.activity_captureimage);
        initPermissions();
        initComponent();
        initContent();
    }

    private void initComponent() {
        mCameraView = findViewById(R.id.cameraview);
        v_capture = findViewById(R.id.layout_cambtn);
        imgv = findViewById(R.id.imgv_swtchcam);
        layout_bgholder = findViewById(R.id.layout_bgholder);
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.qrscan_info);
        backhitbox = findViewById(R.id.backhitbox);
        if (bg != 0)
            layout_bgholder.setBackground(ContextCompat.getDrawable(this, bg));
        else
            layout_bgholder.setBackground(null);

        tv_title.setText(textMain);
        tv_desc.setText(textSub);

        layout_bgholder_ava = findViewById(R.id.layout_bgholder_ava);
        layout_bgholder_ava.setVisibility(View.GONE);
        action = findViewById(R.id.action);

        if (isShowTuts) {
            layout_bgholder_ava.setVisibility(View.VISIBLE);
        }
    }

    private void initContent() {

        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCameraView.getFacing() == CameraKit.Constants.FACING_FRONT) {
                    mCameraView.setFacing(CameraKit.Constants.FACING_BACK);
                } else {
                    mCameraView.setFacing(CameraKit.Constants.FACING_FRONT);
                }
            }
        });
        mCameraView.setFacing(facing);

        v_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialogComponent.showProgressDialog(RegisterAccountActivationCamUsrActivity.this, "Memproses gambar", false).show();
                mCameraView.captureImage(new CameraKitEventCallback<CameraKitImage>() {
                    @Override
                    public void callback(CameraKitImage cameraKitImage) {
                        sendOkAndFinish(cameraKitImage.getBitmap());
                    }
                });

            }
        });
        backhitbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_bgholder_ava.setVisibility(View.GONE);
            }
        });
    }

    private void initPermissions() {
//        Nammu.init(getApplicationContext());
//        if (!Nammu.checkPermission(Manifest.permission.CAMERA)) {
//            Nammu.askForPermission(this, Manifest.permission.CAMERA, new PermissionCallback() {
//                @Override
//                public void permissionGranted() {
//                    initComponent();
//                    initContent();
//                }
//
//                @Override
//                public void permissionRefused() {
//                    finish();
//                }
//            });
//        }
    }

    private void sendOkAndFinish(Bitmap bmp) {

        Intent jenk = new Intent();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 75, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        DataUtil.querryRealm(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ImageBase64StoreModel model = realm.where(ImageBase64StoreModel.class).findFirst();
                switch (state) {
                    case 1:
                        model.setKtp(encoded);
                        break;
                    case 2:
                        model.setKtporang(encoded);
                        break;
                    case 3:
                        model.setEtalase(encoded);
                        break;
                }
                realm.copyToRealmOrUpdate(model);
            }
        });
        setResult(this.RESULT_OK, jenk);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    public void finish() {
        if (mCameraView.isStarted())
            mCameraView.stop();
        ProgressDialogComponent.dismissProgressDialog(this);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

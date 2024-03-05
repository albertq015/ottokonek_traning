package com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.model.APIModel.Request.kyc.KycUploadImageRequest;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.presenter.dao.OCBIDao;
import com.otto.mart.support.util.ImageUtil;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity;
import com.otto.mart.ui.activity.register.kyc.KycSuccessActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.IOException;

import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import id.zelory.compressor.Compressor;
import retrofit2.Response;

public class ShowKTPActivity extends AppActivity {

    public static final String KEY_IS_FROM_WALLET = "is_from_wallet";
    public static final int KEY_UPLOAD_KYC = 921;
    public static final int KEY_PIN = 324;
    public static final int KEY_CONFIRM_PIN = 324;

    private TextView title;
    private TextView tvToolbar;
    private ImageView appBack;
    private ImageView ivKTP;
    private ImageView ivSelfie;
    private String imageKTP;
    private LinearLayout next;
    private LinearLayout again;
    private int request_Code = 1234;
    private String KTPImage = "ktpimage";
    private String selfieImage = "selfieimage";
    private TextView nextBtn;
    private CardView selfieCont;
    private CardView ktpCont;
    private String mSelfieResult = "";
    private String mKtpResult = "";
    private boolean fromWallet;
    private String ktpBase64, selfieBase64;
    private String openCameraType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ktp_activity);
        initiateComponent();

        fromWallet = getIntent().getBooleanExtra(KEY_IS_FROM_WALLET, false);

        next.setOnClickListener(view -> {
            if (nextBtn.getText().toString().equalsIgnoreCase("Foto dengan KTP sekarang")) {
                openCamera(selfieImage);
            } else if (nextBtn.getText().toString().equalsIgnoreCase("Kirim Sekarang")) {
                if (fromWallet) {
                    if (!mKtpResult.equals("") && (!mSelfieResult.equals(""))) {
                        Intent intent = new Intent(this, RegisterPinResetActivity.class);
                        intent.putExtra("confirm", true);
                        startActivityForResult(intent, KEY_CONFIRM_PIN);
                    }
                } else {
                    //Toast.makeText(this, "dari register", Toast.LENGTH_SHORT).show();
                }
            }
        });

        again.setOnClickListener(view -> {
            if (ktpCont.getVisibility() == View.GONE) {
                openCamera(selfieImage);
            } else {
                openCamera(KTPImage);
            }
        });

        appBack.setOnClickListener(viwe -> {
            if (ktpCont.getVisibility() == View.GONE) {
                selfieCont.setVisibility(View.GONE);
                ktpCont.setVisibility(View.VISIBLE);
                nextBtn.setText(getString(R.string.message_take_photo_with_id_card));
            } else {
                finish();
            }
        });

//        nextBtn.setOnClickListener(v -> {
//            startActivityForResult(new Intent(this, ActivationInputPinActivity.class), KEY_PIN)
//        });
    }

    @Override
    public void onBackPressed() {
        if (mSelfieResult.isEmpty()) {
            finish();
        } else {
            mSelfieResult = "";
            nextBtn.setText(getString(R.string.message_take_photo_with_id_card));
            selfieCont.setVisibility(View.GONE);
            ktpCont.setVisibility(View.VISIBLE);
        }
    }

    private void uploadImageKYC() {
        new OCBIDao(this).uploadImageKyc(new KycUploadImageRequest(ktpBase64, selfieBase64), BaseDao.getInstance(this, KEY_UPLOAD_KYC).callback);
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false);
    }

    private void initiateComponent() {
        appBack = findViewById(R.id.imgToolbarLeft);
        tvToolbar = findViewById(R.id.tvToolbarTitle);
        ivKTP = findViewById(R.id.imageKTP);
        next = findViewById(R.id.bottomUp);
        again = findViewById(R.id.bottom);
        selfieCont = findViewById(R.id.imageselfie_cont);
        ktpCont = findViewById(R.id.imagektp_cont);
        nextBtn = findViewById(R.id.tvNext);
        title = findViewById(R.id.title);
        ivSelfie = findViewById(R.id.imageSELFIE);
        openCamera(KTPImage);
        if (GLOBAL.isOttoCash) {
            tvToolbar.setText(getString(R.string.text_ottocash));
            title.setText(getString(R.string.text_upgrade_ottocash_plus));
        } else {
            tvToolbar.setText(getString(R.string.text_ottopay));
            title.setText(getString(R.string.text_upgrade_ottopay_plus));
        }
    }

    public void openCamera(String key) {
        openCameraType = key;
        Intent intent = new Intent(this, OpenCameraActivity.class);
        intent.putExtra("Key", key);
        startActivityForResult(intent, request_Code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == request_Code) {
            if (resultCode == RESULT_OK) {
                if (data.getStringExtra(KTPImage) != null) {
                    Uri uri = Uri.parse(data.getStringExtra(KTPImage));
                    File file = new File(uri.getPath());
                    MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
                    setImageCompressed(KTPImage, compressImage(this, file));
                } else if (data.getStringExtra(selfieImage) != null) {
                    Uri uri = Uri.parse(data.getStringExtra(selfieImage));
                    File file = new File(uri.getPath());
                    MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
                    setImageCompressed(selfieImage, compressImage(this, file));
                } else if (requestCode == KEY_CONFIRM_PIN) {
                    uploadImageKYC();
                }
            } else {
                if (openCameraType.equals(KTPImage)) {
                    finish();
                }
            }
        } else if (requestCode == KEY_CONFIRM_PIN && resultCode == RESULT_OK) {
            uploadImageKYC();
        }
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        if (((BaseResponseModel) br).getMeta().getCode() == 200) {
            startActivity(new Intent(this, KycSuccessActivity.class));
        } else {
            new ErrorDialog(this, this, true, true, ((BaseResponseModel) br).getMeta().getMessage(), "").show();
        }
    }

    private void setImageCompressed(String key, File file) {
        if (key.equalsIgnoreCase(KTPImage)) {
            if (file != null) {
                mKtpResult = file.getAbsolutePath();
                Glide.with(this).load(file).into(ivKTP);
                ktpBase64 = ImageUtil.getByteArrayFromImageURL(mKtpResult, this) != null ? ImageUtil.getByteArrayFromImageURL(mKtpResult, this) : mKtpResult;
            }
        } else if (key.equalsIgnoreCase(selfieImage)) {
            if (file != null) {
                mSelfieResult = file.getAbsolutePath();
                Glide.with(this).load(file).into(ivSelfie);
                selfieCont.setVisibility(View.VISIBLE);
                ktpCont.setVisibility(View.GONE);
                nextBtn.setText(getString(R.string.button_send_now));
                selfieBase64 = ImageUtil.getByteArrayFromImageURL(mSelfieResult, this) != null ? ImageUtil.getByteArrayFromImageURL(mSelfieResult, this) : mSelfieResult;
            }
        }
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.withAspectRatio(16, 9);
        //options.withMaxResultSize(720, 720);
        //options.setCompressionQuality(100);
        options.setHideBottomControls(true);
        //options.setFreeStyleCropEnabled(true);
        //options.useSourceImageAspectRatio();
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.NONE, UCropActivity.NONE);
        options.setToolbarColor(ContextCompat.getColor(this, R.color.cross_toolbar_color));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.cross_toolbar_color));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.cross_toolbar_text_color));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.cross_toolbar_text_color));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.white));
        //options.setMaxBitmapSize(4096);
        return uCrop.withOptions(options);
    }

    public File compressImage(Context context, File image) {
        File compressedImage = null;
        compressedImage = image;
        int quality = 50;
        if (image == null) {
            Log.d("Compress Error", "compressImage: image is empty");
        } else {
            while (true) {
                if (quality <= 0) {
                    break;
                }
                if ((compressedImage.length() / 1024) < 80) {
                    break;
                } else {
                    quality = quality - 5;
                    try {
                        compressedImage = new Compressor(context)
                                .setQuality(quality)
                                .compressToFile(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return compressedImage;
    }
}

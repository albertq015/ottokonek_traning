package com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Flash;
import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class OpenCameraActivity extends AppActivity {

    private int cameraFacing = 0;
    private CameraView cameraView;
    private FloatingActionButton captureBtn;
    private String KTPImage = "ktpimage";
    private String selfieImage = "selfieimage";
    private String profileImage = "profileimage";
    private FrameLayout cameraFrame;
    private LinearLayout btnToolbarBack;
    private static int frontCamera = 1;
    private static int backCamera = 0;
    Intent dataResult = new Intent();
    private String bundle;
    private FloatingActionButton flashBtn;
    private FloatingActionButton changeBtn;
    private Uri path;
    private Uri resultUri;
    private TextView tvToolbar;
    private boolean flashOn = false;
    private boolean isCropEnable = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getIntent().getIntExtra("customLayout", R.layout.activity_takephotoid));
        initComponent();
        initAllPermission();
        keyCheck();

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                if (bundle.equals(KTPImage) || bundle.equals(profileImage)) {
                    path = getImageUri(getApplicationContext(), picture, "KTPUser");
                    if (path == null) {
                        Toast.makeText(OpenCameraActivity.this, "Unable to connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                    UCrop uCrop = UCrop.of(path, Uri.fromFile(new File(getCacheDir(), "Test.jpg")));
                    uCrop = advancedConfig(uCrop);
                    uCrop.start(OpenCameraActivity.this);

                } else {
                    onPicture(picture);
                }

            }
        });

        btnToolbarBack.setOnClickListener(viwe -> {
            finish();
        });

        captureBtn.setOnClickListener(viwe -> {
            Toast.makeText(this, "Taking Picture", Toast.LENGTH_SHORT).show();
            cameraView.capturePicture();
        });

        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            flashBtn.setOnClickListener(view -> {
                if (flashOn) {
                    flashLightOff();
                    flashOn = false;
                } else {
                    flashLightOn();
                    flashOn = true;
                }
            });
        } else {
            flashBtn.setEnabled(false);
        }


    }

    public void initComponent() {
        cameraView = findViewById(R.id.camera);
        captureBtn = findViewById(R.id.captureFab);
        cameraFrame = findViewById(R.id.cameraFrame);
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        flashBtn = findViewById(R.id.flashFab);
        changeBtn = findViewById(R.id.changeFab);
        tvToolbar = findViewById(R.id.tvToolbarTitle);

        if (GLOBAL.isOttoCash) {
            tvToolbar.setText(getString(R.string.text_ottocash));
        } else {
            tvToolbar.setText(getString(R.string.text_ottopay));
        }

        if (getIntent().hasExtra("title")) {
            tvToolbar.setText(getIntent().getStringExtra("title"));
        }

        isCropEnable = getIntent().getBooleanExtra("isCropEnable", true);

        changeBtn.setOnClickListener(view -> {
            if (cameraFacing == 0) {
                cameraView.setFacing(Facing.FRONT);
                cameraFacing = frontCamera;
            } else {
                cameraView.setFacing(Facing.BACK);
                cameraFacing = backCamera;
            }
        });
    }

    private void flashLightOn() {
        cameraView.setFlash(Flash.TORCH);
    }

    private void flashLightOff() {
        cameraView.setFlash(Flash.OFF);
    }

    private UCrop advancedConfig(UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        if (isCropEnable) {
            if (bundle.equalsIgnoreCase(profileImage)) {
                options.setCircleDimmedLayer(true);
//            options.withAspectRatio(1, 1);
            } else options.withAspectRatio(16, 9);
        }

        //options.withMaxResultSize(720, 720);
        options.setCompressionQuality(60);
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

    private void keyCheck() {
        //cameraFacing = backCamera;
        bundle = (String) getIntent().getExtras().get("Key");
        if (bundle.equalsIgnoreCase(selfieImage)) {
            cameraFrame.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.scrim_userktp, null));
            cameraView.setFacing(Facing.FRONT);
            cameraFacing = frontCamera;
        } else if (bundle.equalsIgnoreCase(KTPImage)) {
            cameraFrame.setBackground(ContextCompat.getDrawable(this, R.drawable.background_ktp));
            cameraView.setFacing(Facing.BACK);
            cameraFacing = backCamera;
        } else if (bundle.equalsIgnoreCase(profileImage)) {
            cameraFrame.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        } else {
            cameraFrame.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.transparent, null));
            cameraView.setFacing(Facing.BACK);
        }
    }

    private void onPicture(byte[] jpeg) {
        CameraUtils.decodeBitmap(jpeg, 1600, 1600, new CameraUtils.BitmapCallback() {
            @SuppressLint("WrongThread")
            @Override
            public void onBitmapReady(Bitmap bitmap) {
                FileOutputStream outStream = null;
                //File sdCard = getExternalFilesDir()
                File sdCard = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                {
                    sdCard = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                }
                else
                {
                    sdCard = Environment.getExternalStorageDirectory();
                }
                File dir = new File(sdCard.getAbsolutePath() + "/OttoPay/media/");
                dir.mkdirs();
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                // Write to SD Card
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    outStream = new FileOutputStream(outFile);
                    outStream.write(byteArray);
                    outStream.flush();
                    outStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    dataResult.putExtra(bundle, outFile.getAbsolutePath());
                    dataResult.putExtra("camera", cameraFacing);
                    setResult(RESULT_OK, dataResult);
                    finish();
                }
            }
        });
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static Uri getImageUri(Context context, byte[] inImage, String imageName) {
        File sdCard = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            sdCard = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        else
        {
            sdCard = Environment.getExternalStorageDirectory();
        }
        File myNewFolder = new File(sdCard.getAbsolutePath() + "/OttoPay/media/");
        boolean success = myNewFolder.exists() ? true : myNewFolder.mkdirs();
        if (success) {
            File file = new File(sdCard.getAbsolutePath() + "/OttoPay/media/", imageName + ".jpeg");
            try {
                OutputStream imageFileOS = new FileOutputStream(file);
                imageFileOS.write(inImage);
                imageFileOS.flush();
                imageFileOS.close();
                return Uri.fromFile(file);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            resultUri = UCrop.getOutput(data);
            try {
                InputStream iStream = getContentResolver().openInputStream(resultUri);
                byte[] inputData = getBytes(iStream);
                onPicture(inputData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }
}
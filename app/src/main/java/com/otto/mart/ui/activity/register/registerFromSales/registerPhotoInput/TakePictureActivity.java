package com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otto.mart.GLOBAL;
import com.otto.mart.R;
import com.otto.mart.ui.activity.AppActivity;

import static com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput.ShowKTPActivity.KEY_IS_FROM_WALLET;

public class TakePictureActivity extends AppActivity {

    private TextView tvGuidance;
    private LinearLayout btnText;
    private ImageView appBack;
    private TextView tvToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_picture_layout);
        initComponent();
        /*openCamera(KTPImage);*/

        btnText.setOnClickListener(view->{
            Intent intent = new Intent(TakePictureActivity.this,ShowKTPActivity.class);
            intent.putExtra(KEY_IS_FROM_WALLET, getIntent().getBooleanExtra(KEY_IS_FROM_WALLET,false));
            startActivity(intent);
        });

        appBack.setOnClickListener(viwe ->{
            finish();
        });
    }


    public void initComponent(){
        btnText = findViewById(R.id.bottom);
        appBack = findViewById(R.id.imgToolbarLeft);
        tvToolbar = findViewById(R.id.tvToolbarTitle);
        tvGuidance = findViewById(R.id.wordingguidance);

        if(GLOBAL.isOttoCash){
            tvToolbar.setText(getString(R.string.text_ottocash));
            tvGuidance.setText(getString(R.string.text_guidance_upgrade_oc));
        }else{
            tvToolbar.setText(getString(R.string.text_ottopay));
            tvToolbar.setTextColor(Color.BLACK);
            tvGuidance.setText(getString(R.string.text_guidance_upgrade_op));
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == request_Code){
            if(resultCode==RESULT_OK){
                if (data.getStringExtra(KTPImage) != null) {
                    Uri uri = Uri.parse(data.getStringExtra(KTPImage));
                    File file = new File(uri.getPath());
                    MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
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
                }
            }
        }else if(requestCode == UCrop.REQUEST_CROP){
            Uri resultUri = UCrop.getOutput(data);
            File file = new File(resultUri.getPath());
            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
            setImageCompressed(KTPImage, compressImage(this, file));
        }
    }*/

    /*private void setImageCompressed(String key, File file){
        if(key.equalsIgnoreCase(KTPImage)){
            imageKTP = file.getAbsolutePath();
            Intent intent = new Intent(TakePictureActivity.this,ShowKTPActivity.class);
            intent.putExtra("imagektp",imageKTP);
            startActivity(intent);
        }
    }*/

    /*private UCrop advancedConfig(@NonNull UCrop uCrop) {
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
        int quality = 100;
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
    }*/

    /*public static String getByteArrayFromImageURL(String uri, Context context) {
        String headerBase64 = "data:image/jpg;base64,";
        try {
            String encodedImage = headerBase64 + ImageBase64
                    .with(context)
                    .noScale()
                    .encodeFile(uri);
            return encodedImage;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}

package com.otto.mart.ui.activity.profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.otto.mart.R;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.model.APIModel.Request.UpdateProfileRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;
import com.otto.mart.model.APIModel.Response.ProfileResponseModel;
import com.otto.mart.model.APIModel.Response.profile.ProfileData;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.ApiCallback;
import com.otto.mart.support.util.ImageUtil;
import com.otto.mart.support.util.formValidation.FormValidation;
import com.otto.mart.support.util.widget.dialog.ConfirmationDialogFragment;
import com.otto.mart.ui.activity.AppActivity;
import com.otto.mart.ui.activity.login.LoginActivity;
import com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput.OpenCameraActivity;
import com.otto.mart.ui.activity.settings.SettingActivity;
import com.otto.mart.ui.component.LazyDialog;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.dialog.Popup;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import id.zelory.compressor.Compressor;
import retrofit2.Response;

public class ProfileChangeActivity extends AppActivity implements DatePickerDialog.OnDateSetListener {

    private LinearLayout btnToolbarBack;
    private TextView tvToolbarTitle;
    private Button btnSubmit;
    private LazyDialog pinDialog;
    private ImageView avatar;
    private ViewGroup addPhone;
    private LazyDialog changesDialog;
    private LazyDialog messageDialog;
    private DatePickerDialog dateDialog;
    private String selectedDate;
    private UpdateProfileRequestModel model;
    private boolean isAvatarChanged = false;
    private ProfileData profile;
    private Uri mImageUri;
    private ImagePicker imagePicker2;
    private ViewAnimator viewAnimator;

    private EditText etName;
    private EditText etMerchantId;
    private EditText etPhone;
    private EditText etPhone2;
    private LinearLayout phone2Layout;
    private EditText etEmail;
    private EditText etDob;
    private EditText etNamaUsaha;
    private EditText etJenisUsaha;

    private FormValidation mFormValidation;
    private String profileImage = "profileimage";
    private String avatarBase64;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        initComponent();
        initContent();
    }

    private void initComponent() {
        btnToolbarBack = findViewById(R.id.btnToolbarBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        avatar = findViewById(R.id.avatar);
        addPhone = findViewById(R.id.addPhone);

        viewAnimator = findViewById(R.id.view_animator);
        etName = findViewById(R.id.etName);
        etMerchantId = findViewById(R.id.etMerchantId);
        etPhone = findViewById(R.id.etPhone);
        etPhone2 = findViewById(R.id.etPhone2);
        phone2Layout = findViewById(R.id.phone2Layout);
        etEmail = findViewById(R.id.etEmail);
        etDob = findViewById(R.id.etDob);
        etNamaUsaha = findViewById(R.id.etNamaUsaha);
        etJenisUsaha = findViewById(R.id.etJenisUsaha);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initContent() {
        tvToolbarTitle.setText(getString(R.string.button_edit_profile));

        buildDialog();
        buildChangesDialog();

        mFormValidation = new FormValidation(this);

        etPhone2.setInputType(InputType.TYPE_CLASS_PHONE);
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        if (getIntent().hasExtra(SettingActivity.Companion.getKEY_DATA_PROFILE())) {
            profile = new Gson().fromJson(getIntent().getExtras().getString(SettingActivity.Companion.getKEY_DATA_PROFILE()), ProfileData.class);
            if (profile != null) {

                etName.setText(profile.getStoreName());
                etNamaUsaha.setText(profile.getStoreName());
                etMerchantId.setText(profile.getMid());
                etPhone.setText(SessionManager.getPhone());
                etEmail.setText(profile.getEmail());
                if (profile.getEmail() != null && profile.getEmail().isEmpty()) {
                    etEmail.setHint("e.g mail@email.com");
                }

                if (profile.getProfilePictureUrl() != null && !profile.getProfilePictureUrl().equals(""))
                    Glide.with(this).load(profile.getProfilePictureUrl())
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .skipMemoryCache(true)).into(avatar);

                viewAnimator.setDisplayedChild(1);
                btnSubmit.setVisibility(View.VISIBLE);
            }
        }

        btnToolbarBack.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            if (validation())
                changesDialog.show();
        });

        imagePicker2 = ImagePicker.create(this)
                .single()
                .showCamera(false)
                .includeVideo(false)
                .folderMode(true)
                .theme(R.style.ef_CustomToolbarTheme)
                .toolbarFolderTitle("Choose Picture")
                .toolbarImageTitle("Choose Picture")
                .toolbarArrowColor(ContextCompat.getColor(this, R.color.blue_grey));

        avatar.setOnClickListener(v -> {
            ConfirmationDialogFragment chooser = new ConfirmationDialogFragment();
            chooser.setMessage("");
            chooser.setTitle("");
            chooser.setNegativeButton("Open Camera");
            chooser.setOnNegativeButton(() -> {
                Intent intent = new Intent(this, OpenCameraActivity.class);
                intent.putExtra("title", getString(R.string.take_profile_picture));
                intent.putExtra("Key", "profileimage");
                startActivityForResult(intent, 333);
                chooser.dismiss();
                return null;
            });

            chooser.setPositiveButton("Open Gallery");
            chooser.setOnPositiveButton(() -> {
                startActivityForResult(imagePicker2.getIntent(this), 9090);
                chooser.dismiss();
                return null;
            });

            chooser.show(getSupportFragmentManager(), "confirm");
        });

        etDob.setOnTouchListener((v, event) -> {
            dateDialog.show();
            return true;
        });
    }

    private Object onPositive() {
        return null;
    }

    private void setSelectedDOB(String dob) {
        //selectedDate = new Date(dob * 1000L);
        selectedDate = dob;
    }

    private void buildChangesDialog() {
        View changeView = LayoutInflater.from(this).inflate(R.layout.dialog_changeconfirm, null);
        changesDialog = new LazyDialog(this, this, true);
        changesDialog.setContainerView(changeView);
        changeView.findViewById(R.id.posijing).setOnClickListener(v -> {
            model = new UpdateProfileRequestModel();

            if (mFormValidation.isValidEmail(etEmail.getText().toString())) {
                if (!profile.getEmail().equals(etEmail.getText().toString()))
                    model.setEmail(etEmail.getText().toString());
            }

            if (isAvatarChanged && avatarBase64 != null) {
                model.setProfile_picture(avatarBase64);
            }

            if (isAvatarChanged && model.getProfile_picture() == null) {
                ErrorDialog dialog = new ErrorDialog(this, this, false, false, "Gagal mengambil foto, silahkan ulangi!", "");
                dialog.setOnDismissListener(dialog1 -> avatar.performClick());
                if (changesDialog.isShowing()) {
                    changesDialog.dismiss();
                }
                dialog.show();
            } else {
                showProgressDialog(R.string.loading_message);
                OttoKonekAPI.profileUpdate(this, model, new ApiCallback<BaseResponseModel>() {

                    @Override
                    public void onResponseSuccess(BaseResponseModel body) {
                        dismissProgressDialog();
                        if (isSuccess200) {
                            Popup.showInfo(getSupportFragmentManager(), "Update", "Profile has updated", () -> {
                                finish();
                                return null;
                            });

                        } else showErrorDialog(responseMessage);
                    }

                    @Override
                    public void onApiServiceFailed(Throwable throwable) {
                        dismissProgressDialog();
                        onApiResponseError();
                    }
                });

                if (changesDialog != null) {
                    changesDialog.dismiss();
                }
            }
        });
        changeView.findViewById(R.id.negajing).setOnClickListener(v -> finish());
        if (changesDialog.getWindow() != null) {
            changesDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    private boolean validation() {
        if (etPhone2.getText().length() > 0 && !mFormValidation.isValidHandphone(etPhone2.getText().toString())) {
            return false;
        }
        if (!etEmail.getText().toString().isEmpty() && !etEmail.getText().toString().equalsIgnoreCase(profile.getEmail()))
            if (!mFormValidation.isValidEmail(etEmail.getText().toString())) {
                Toast.makeText(this, "Wrong Email Format", Toast.LENGTH_SHORT).show();
                return false;
            }
        return true;
    }

    private void buildDialog() {
        TextView titleTv;
        View contentView;


        Window window;
        WindowManager.LayoutParams wlp;
        pinDialog = new LazyDialog(ProfileChangeActivity.this, ProfileChangeActivity.this);
        window = pinDialog.getWindow();
        wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        contentView = LayoutInflater.from(this).inflate(R.layout.dialog_inputotp, null);
        contentView.findViewById(R.id.action1).setOnClickListener(v -> finish());
        pinDialog.setContainerView(contentView);
        titleTv = pinDialog.getToolbarView().findViewById(R.id.title);
        titleTv.setText(getString(R.string.label_input_pin_code));
        pinDialog.getToolbarView().setBackgroundColor(ContextCompat.getColor(this, R.color.ocean_blue));
        pinDialog.setToolbarDarkmode();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 333 && data.getStringExtra(profileImage) != null) {
                handleTakeImageResult(Uri.parse(data.getStringExtra(profileImage)));
            } else if (requestCode == 9090 && ImagePicker.getFirstImageOrNull(data) != null) {
                Image image = ImagePicker.getFirstImageOrNull(data);
                UCrop uCrop = UCrop.of(Uri.parse("file://" + image.getPath()), Uri.fromFile(new File(getCacheDir(), "Test.jpg")));
                uCrop = advancedConfig(uCrop);
                uCrop.start(this);
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleTakeImageResult(UCrop.getOutput(data));
            }
        }
    }

    private void handleTakeImageResult(Uri data) {
        File file = new File(data.getPath());
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            }
        });
        setImageCompressed(compressImage(this, file));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onApiResponseCallback(BaseResponse br, int responseCode, Response response) {
        ProgressDialogComponent.dismissProgressDialog(this);
        super.onApiResponseCallback(br, responseCode, response);
        changesDialog.dismiss();
        if (responseCode == 99) {
            ProfileResponseModel model = (ProfileResponseModel) br;
            if (model != null && model.getMeta().getCode() == 200) {
                showMessageDialog("profile has been updated.", v -> finish());
            } else if (model.getMeta().getCode() == 498) {
                SessionManager.logout();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(this, "Sorry : " + ((BaseResponseModel) br).getMeta().getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError();
    }

    private void showMessageDialog(String message, View.OnClickListener onClickListener) {
        View messageView = LayoutInflater.from(this).inflate(R.layout.dialog_changeconfirm, null);
        messageDialog = new LazyDialog(this, this, true);
        messageDialog.setContainerView(messageView);
        ((TextView) messageView.findViewById(R.id.posijing)).setText(getString(R.string.text_ok));
        messageView.findViewById(R.id.negajing).setVisibility(View.GONE);
        ((TextView) messageView.findViewById(R.id.dialogTvTitle)).setText(message);
        messageView.findViewById(R.id.posijing).setOnClickListener(onClickListener);
        messageDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        messageDialog.show();
//        System.out.println();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar temp = Calendar.getInstance();
        temp.set(year, month, dayOfMonth);

        //selectedDate = temp.getTime();
        //dob.setContentText(new SimpleDateFormat("dd MMMM yyyy").format(selectedDate));

        String selectedDay = dayOfMonth > 9 ? "" + dayOfMonth : "0" + dayOfMonth;
        String selectedMonth = (month + 1) > 9 ? "" + (month + 1) : "0" + (month + 1);

        selectedDate = selectedDay + "-" + selectedMonth + "-" + year;
        etDob.setText(selectedDate);
    }

    private void setImageCompressed(File file) {
        String mKtpResult;
        if (file != null) {
            mKtpResult = file.getAbsolutePath();
            Glide.with(this).load(file).into(avatar);
            avatarBase64 = ImageUtil.getByteArrayFromImageURL(mKtpResult, this) != null ? ImageUtil.getByteArrayFromImageURL(mKtpResult, this) : mKtpResult;
            isAvatarChanged = true;
        }

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

    private UCrop advancedConfig(UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCompressionQuality(60);
        options.setHideBottomControls(true);
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.NONE, UCropActivity.NONE);
        options.setToolbarColor(ContextCompat.getColor(this, R.color.cross_toolbar_color));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.cross_toolbar_color));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.cross_toolbar_text_color));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.cross_toolbar_text_color));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.white));
        return uCrop.withOptions(options);
    }
}
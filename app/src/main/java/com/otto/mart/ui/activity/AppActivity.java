package com.otto.mart.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.otto.mart.BuildConfig;
import com.otto.mart.R;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.MyLastLocation;
import com.otto.mart.model.APIModel.Response.CheckVersionResponseModel;
import com.otto.mart.presenter.dao.EtcDao;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.service.Receiver_Sec;
import com.otto.mart.service.SecureServices;
import com.otto.mart.support.util.Connectivity;
import com.otto.mart.support.util.SystemUtil;
import com.otto.mart.support.util.pref.Pref;
import com.otto.mart.ui.component.dialog.ErrorDialog;
import com.otto.mart.ui.component.dialog.Popup;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


/**
 * Created by macari on 3/5/18.
 */

public class AppActivity extends BaseActivity {

    //add TSD Security
	/*Receiver_Sec receiver;
	IntentFilter filter1;*/

    private final String TAG = this.getClass().getSimpleName();

    private final int REQUEST_ACCESS_FINE_LOCATION = 1080;

    protected MyLastLocation myLastLocation = new MyLastLocation();

    private AlertDialog mLocationPermissionDialog = null;
    private AlertDialog mEnableGpsDialog = null;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String themeColor = AppKeys.DEFAULT_THEME_COLOR;

        if (SessionManager.getMerchantTheme() != null && SessionManager.getMerchantTheme().getThemeColor() != null) {
            if (SessionManager.getMerchantTheme().getThemeColor().contains("#")) {
                themeColor = SessionManager.getMerchantTheme().getThemeColor();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor(themeColor));
        }

        initFusedLocationClient();


        //add TSD Security
        /*receiver = new Receiver_Sec();
        filter1 = new IntentFilter("security.id.adb");
        registerReceiver(receiver,filter1);
        startService(new Intent(getApplicationContext(), SecureServices.class));*/

    }

    @Override
    protected void onResume() {
        if (SystemUtil.isRooted()) {
            ErrorDialog errorDialog = new ErrorDialog(this, this, false, false, getString(R.string.security_root_message), "");
            errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finishAffinity();
                }
            });
            errorDialog.show();
        }
        super.onResume();

        if (Pref.getPreference()
                .getBoolean(SessionManager.KEY_PREF_HIDE_PROMINENT_PERMISSION_DIALOG))
            startLocationUpdates();

    }

    @Override
    protected void onPause() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        stopLocationUpdates();
        super.onPause();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                getLastKnowLocation();
            }
        }
    }

    private void initFusedLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        myLastLocation.setLatitude(location.getLatitude());
                        myLastLocation.setLongitude(location.getLongitude());
                    }
                }
            }
        };

        /*
         * don't delete it
         * */

//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//
//        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
//        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
//
//        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
//            @Override
//            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                System.out.println();
//            }
//        });
//
//        task.addOnFailureListener(this, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println();
//
//            }
//        });

    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public void initToolbar(int toolbarId) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            assert getSupportActionBar() != null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(updateBaseContextLocale(ViewPumpContextWrapper.wrap(context)));
    }

    private Context updateBaseContextLocale(Context context) {
        String language = getLanguage();
        if (!context.getResources().getConfiguration().locale.getLanguage().equals(language)) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                return updateResourcesLocale(context, locale);
            }

            return updateResourcesLocaleLegacy(context, locale);
        }
        return context;
    }

    @NotNull
    private String getLanguage() {
        if (SessionManager.getLanguageCode() == null) {
            String sysLang;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sysLang = Resources.getSystem().getConfiguration().getLocales().get(0).getLanguage();
            } else {
                sysLang = Resources.getSystem().getConfiguration().locale.getLanguage();
            }
            SessionManager.setLanguageCode("en");
        }
        return SessionManager.getLanguageCode();
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = new Configuration(Resources.getSystem().getConfiguration());
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        overrideConfiguration.locale = new Locale(getLanguage());
        super.applyOverrideConfiguration(overrideConfiguration);
    }

    /**
     * Avoid user to take screeen capture
     */
    protected void disableScreenshot() {
        /*if (BuildConfig.FLAVOR.equals("production")) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }*/
    }

    public void getLastKnowLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        myLastLocation.setLatitude(location.getLatitude());
                        myLastLocation.setLongitude(location.getLongitude());
                    } else {
                        getLocationGPS();
                    }
                });
    }

    protected void getLocationGPS() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // request write permission
                //ActivityCompat.requestPermissions(this,
                // new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
                return;
            }
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            myLastLocation.setLatitude(location.getLatitude());
            myLastLocation.setLongitude(location.getLongitude());


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        } catch (Exception e) {
            e.printStackTrace();
            getLocationWithProvider();
        }
    }

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                myLastLocation.setLatitude(location.getLatitude());
                myLastLocation.setLongitude(location.getLongitude());
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void getLocationWithProvider() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // request write permission
                    //ActivityCompat.requestPermissions(this,
                    //        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
                    return;
                }
            }
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                myLastLocation.setLatitude(location.getLatitude());
                myLastLocation.setLongitude(location.getLongitude());
            } else {
                myLastLocation.setLatitude(0.0);
                myLastLocation.setLongitude(0.0);
            }
        }
    }

    public void checkVersion() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show();
        new EtcDao(this).versionCheckDao(BaseDao.getInstance(this, AppKeys.API_KEY_CHECK_VERSION).callback);
    }

    public MyLastLocation getMyLastLocation() {
        return myLastLocation;
    }

    //region Dialog: Error Message

    protected void dialogNoInternetConnection() {
        ErrorDialog dialog = new ErrorDialog(this, this, false,
                false, getString(R.string.error_ms_no_internet_connection), "");
        if (!isDestroyed() && !this.isFinishing() && dialog != null) {
            dialog.show();
        }
    }

    protected void dialogInternetConnectionLost() {
        ErrorDialog dialog = new ErrorDialog(this, this, false,
                false, getString(R.string.error_msg_internet_connection_lost), "");
        if (!isDestroyed() && !this.isFinishing() && dialog != null) {
            dialog.show();
        }
    }

    protected void dialogServerError(String errorMessage) {
        ErrorDialog dialog = new ErrorDialog(this, this, false,
                false,
                errorMessage == null ? getString(R.string.error_msg_response_error_title) : errorMessage,
                getString(R.string.error_msg_response_error));
        if (!isDestroyed() && !this.isFinishing() && dialog != null) {
            dialog.show();
        }
    }

    protected void dialogServiceNotAvailableError() {
        ErrorDialog dialog = new ErrorDialog(this, this, false,
                false, getString(R.string.error_msg_service_not_available), "");
        if (!isDestroyed() && !this.isFinishing() && dialog != null) {
            dialog.show();
        }
    }

    //endregion  Dialog: Error Message

    public void updateDialog(CheckVersionResponseModel.CheckVersionResponseData data) {
        if (data.getVersion_api() > BuildConfig.VERSION_CODE) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            if (data.isForce_update()) {
                alertDialogBuilder
                        .setMessage("Please Update the Application")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://appgallery.huawei.com/#/app/C103366529")));

                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                            finish();
                        });
            } else {
                alertDialogBuilder
                        .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setMessage("New Version Available on Play Store")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://appgallery.huawei.com/#/app/C103366529")));

                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }
                        });

            }
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onApiFailureCallback(String message, BaseActivity ac) {
        //super.onApiFailureCallback(message, ac);
        if (Connectivity.isConnected(this)) {
            dialogServerError(null);
        } else {
            dialogInternetConnectionLost();
        }
    }

    protected void onApiResponseError() {
        onApiResponseError(null);
    }

    protected void onApiResponseError(String errorMessage) {
        if (Connectivity.isConnected(this)) {
            dialogServerError(errorMessage);
        } else {
            dialogInternetConnectionLost();
        }
    }

    protected void validateApiResponse(BaseResponse br) {
        if (isFinishing()) {
            return;
        }

        if (br == null) {
            dialogServiceNotAvailableError();
            return;
        }
    }

    public void initAllPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA
                //Manifest.permission.READ_SMS
        };
        Dexter.withContext(this)
                .withPermissions(
                        permissions
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        getCurrentLocation();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }


                }).check();
    }

    public void initLocationPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                getCurrentLocation();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        });
    }

    public boolean getCurrentLocation() {
        boolean result = false;
        if (isGpsEnable()) {
            if (isLocationPermissionGranted()) {
                result = true;
                getLastKnowLocation();
            }
        }
        return result;
    }

    public boolean isGpsEnable() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        boolean result = true;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!gpsEnabled) {
            result = false;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage(R.string.dialog_msg_ask_enable_gps);
            builder.setPositiveButton(R.string.dialog_btn_enable_gps, (dialog, which) -> {
                if (mEnableGpsDialog.isShowing()) {
                    mEnableGpsDialog.dismiss();
                }

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });

            if (mEnableGpsDialog == null) {
                mEnableGpsDialog = builder.create();
            }

            if (mEnableGpsDialog != null) {
                if (!mEnableGpsDialog.isShowing()) {
                    mEnableGpsDialog.show();
                    mEnableGpsDialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                }
            }
        }

        return result;
    }

    public boolean isLocationPermissionGranted() {
        boolean result = true;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            result = false;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage(R.string.dialog_msg_ask_permission_location);
            builder.setPositiveButton(R.string.dialog_btn_permission_location, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // request write permission
                    if (mLocationPermissionDialog.isShowing()) {
                        mLocationPermissionDialog.dismiss();
                    }

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            if (mLocationPermissionDialog == null) {
                mLocationPermissionDialog = builder.create();
            }

            if (mLocationPermissionDialog != null) {
                if (!mLocationPermissionDialog.isShowing()) {
                    mLocationPermissionDialog.show();
                    mLocationPermissionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                }
            }
            return result;
        }
        return result;
    }

    public String getLongLat() {
        return getMyLastLocation().getLongitude() + "," + getMyLastLocation().getLatitude();
    }

    public void setStatusBarColor(int resourseColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), resourseColor));
        }

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(resourseColor)));
        }

    }

    public int setStatusBarLightMode() {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(this, false)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(getWindow(), false)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }


    public boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    public boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    protected void dismissProgressDialog() {
        if (!isFinishing() && !isDestroyed()) ProgressDialogComponent.dismissProgressDialog(this);
    }

    protected void showProgressDialog(String message) {
        if (!isFinishing() && !isDestroyed())
            ProgressDialogComponent.showProgressDialog(this, message, false);
    }

    protected void showProgressDialog(int message) {
        if (!isFinishing() && !isDestroyed())
            ProgressDialogComponent.showProgressDialog(this, getString(message), false);
    }

    protected ErrorDialog showErrorDialog(String message) {
        ErrorDialog errorDialog = new ErrorDialog(this, this, false, false, message, "");
        errorDialog.show();
        return errorDialog;
    }

    protected void comingSoonDialogBase() {

        Popup.showInfo(getSupportFragmentManager(), "Feature is coming soon", "", null);

    }

}

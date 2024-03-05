package com.otto.mart.support.util;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.otto.mart.OttoMartApp;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class DeviceUtil {


    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = OttoMartApp.getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(int px) {
        DisplayMetrics displayMetrics = OttoMartApp.getContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String getTodayFormattedDate(String format, int dayDeviation, int monthDeviation) {
        SimpleDateFormat formatter;
        Calendar now = Calendar.getInstance();
        try {
            formatter = new SimpleDateFormat(format);
            now.set(now.get(Calendar.YEAR),
                    (now.get(Calendar.MONTH) + 1) - monthDeviation,
                    now.get(Calendar.DAY_OF_MONTH) - dayDeviation);
            return formatter.format(now.getTime());
        } catch (Exception e) {
            return "invalid date format";
        }
    }

    /**
     * @param input put all numeric
     * @return valid digits of eas13 barcode
     */
    public static String generateEas13(String input) {
        int sum = 0;
        int e = 0;
        int o = 0;
        for (int i = 0; i < input.length(); i++) {
            if ((int) input.charAt(i) % 2 == 0) {
                e += (int) input.charAt(i);
            } else {
                o += (int) input.charAt(i);
            }
        }
        o = o * 3;
        int total = o + e;
        if (total % 10 == 0) {
            sum = 0;//ceksum 0
        } else {
            sum = 10 - (total % 10);
        }
        return input + sum;
    }


    public static void getLocationOn(final Activity target) {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(OttoMartApp.getContext()).checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);

                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        null,
                                        100);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.

                            break;
                    }
                }
            }
        });
    }

    /**
     * Check exceptional device
     *
     * @return exceptional device status
     */
    public static boolean isExceptionalDevice(){
        String[] exceptionalDevices = {"xiaomi", "meizu"};
        String manufacturer = Build.MANUFACTURER;

        boolean findMatchData = Arrays.asList(exceptionalDevices).contains(manufacturer.toLowerCase());

        return findMatchData;
    }

    public static boolean isAboveKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void setStatusBar(Activity act, int color) {
        if (isAboveKitkat()) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

}

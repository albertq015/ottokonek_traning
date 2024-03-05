package com.otto.mart.support.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.otto.mart.model.APIModel.Response.ottopoint.UrlPhoto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CommonHelper {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }

        if(imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Dialog dialog) {
        InputMethodManager imm = (InputMethodManager) dialog.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = dialog.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(dialog.getContext());
        }

        if(imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String currencyFormat(long number){
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number).replace(',', '.');
    }

    public static String currencyFormat(double number){
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number).replace(',', '.');
    }

    public static String removeCurrencyFormat(String value){
        return value.replace(".", "").replace(",", "");
    }

    @NonNull
    public static String getPhoneNumberFromActivityResult(@Nullable Context context, @Nullable Intent data){
        String result = "";
        if(context == null || data == null) return result;

        Uri uri = data.getData();
        if(uri == null) return result;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor contentCursor = contentResolver.query(uri, null, null,null, null);
        if(contentCursor == null) return result;

        if(contentCursor.moveToFirst()){
            String hasPhone = contentCursor.getString(contentCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if (hasPhone.equalsIgnoreCase("1")) {
                try {
                    result = contentCursor.getString(contentCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }catch (Exception e){
                    LogHelper.showError(CommonHelper.class.getSimpleName(), "error message: " + e.getMessage());
                }
            }
        }
        contentCursor.close();

        return cleanPhoneNumber(result);
    }

    public static String cleanPhoneNumber(String phoneNumber){
        if(phoneNumber == null) return "";
        if(phoneNumber.isEmpty()) return "";

        String result = phoneNumber.replace("-", "").replace(" ", "");

        if(result.substring(0, 1).equalsIgnoreCase("0"))
            result = result.substring(1);
        else if(result.length() >= 2 && result.substring(0, 2).equalsIgnoreCase("62"))
            result = result.substring(2);
        else if(result.length() >= 3 && result.substring(0, 3).equalsIgnoreCase("+62"))
            result = result.substring(3);

        //LogHelper.showError(CommonHelper.class.getSimpleName(), "clearPhoneNumber: " + result.substring(0, 2));

        return result;
    }

    public static String getUrlBannerVoucher(List<UrlPhoto> urlPhotos, int type){
        String urlBanner = "";
        String urlBannerDetail = "";
        if(urlPhotos != null && !urlPhotos.isEmpty()){
            if(urlPhotos.size() > 1){
                urlBanner = urlPhotos.get(0).getUrl_photo();
                urlBannerDetail = urlPhotos.get(1).getUrl_photo();
            }else{
                urlBanner = urlPhotos.get(0).getUrl_photo();
            }
        }

        // banner detail
        if(type == 1){
            return !urlBannerDetail.isEmpty() ? urlBannerDetail : urlBanner;
        }else{
            return urlBanner;
        }
    }

    public static void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Ottopoint");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

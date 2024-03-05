package com.otto.mart.support.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.otto.mart.GLOBAL;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocoderUtil implements GLOBAL {

    public static String getAddressFromlatlng(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getAddressLine(0);
            } else {
                return "error - geoccoder not found";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error - geoccoder not found";
        }
    }

    public static double[] getLatlngFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> jenk = geocoder.getFromLocationName(address, 1, LATITUDE_MIN, LONGITUDE_MIN, LATITUDE_MAX, LONGITUDE_MAX);
            double[] returnItem = {jenk.get(0).getLatitude(), jenk.get(0).getLongitude()};
            return returnItem;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

}

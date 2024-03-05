package com.otto.mart.support.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.otto.mart.GLOBAL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static okhttp3.internal.Util.UTC;

public class DateUtil {
    static String[] suffixes =
            {"A",
                    "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th",
                    "11th", "12th", "13th", "14th", "15th", "16th", "17th", "18th", "19th", "20th",
                    "21st", "22nd", "23rd", "24th", "25th", "26th", "27th", "28th", "29th", "30th",
                    "31st"};

    public static Date msToDate(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return calendar.getTime();
    }

    public static long getRangeOfDate(String endDate, String startDate) throws ParseException {

        Date start = DateUtil.convertFromString(startDate);
        Date end = DateUtil.convertFromString(endDate);
        long diff=end.getTime()-start.getTime();

        return  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateAsString(Date date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateAsStringWithOrdinal(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        int day = Integer.parseInt(formatter.format(date));
        formatter = new SimpleDateFormat("MMMM yyyy");
        String[] splitted = formatter.format(date).split(" ");
        return splitted[0] + " " + suffixes[day] + " " + splitted[1];
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateAsString(String date) {

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
        String finalStrDateDisplay = null;
        if (date != null) {
            try {
                finalStrDateDisplay = outputFormat.format(inputFormat.parse(date));
                Log.i("TAG", "Final Format Start Date" + finalStrDateDisplay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("TAG", "Start Pick Date" + finalStrDateDisplay);
        }

        return finalStrDateDisplay;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateAsStringFormat(String date, String inputDate, String outputDate) {

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputDate);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputDate);
        String finalStrDateDisplay = null;
        if (date != null) {
            try {
                finalStrDateDisplay = outputFormat.format(inputFormat.parse(date));

                Log.i("TAG", "Final Format Start Date" + finalStrDateDisplay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("TAG", "Start Pick Date" + finalStrDateDisplay);
        }

        return finalStrDateDisplay;
    }

    @SuppressLint("SimpleDateFormat")
    public static int getTheLastDay(String day) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setCalendar(calendar);
        calendar.setTime(sdf.parse(day));

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @SuppressLint("SimpleDateFormat")
    public static Date convertFromString(String dateString) throws ParseException {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(tz);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setCalendar(cal);
        cal.setTime(sdf.parse(dateString));
        Date date = cal.getTime();

        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public static String pickerString(int day, int month, int year){
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(date);
    }

    public static long convertToEpoch(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        return (cal.getTimeInMillis());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat(GLOBAL.FORMAT_DATE_TIME_DEFAULT_SERVER);
        sdf.setTimeZone(UTC);
        return sdf.format(Calendar.getInstance().getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate(String format){
        return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
    }

    public static String getCurrentTimestamp(){
        return Long.toString((System.currentTimeMillis()/1000));
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean isExpired(String dateTime){
        SimpleDateFormat sdf = new SimpleDateFormat(GLOBAL.FORMAT_DATE_TIME_DEFAULT_SERVER);

        boolean result = false;
        try {
            Date dateTarget = sdf.parse(dateTime);

            result = new Date().after(dateTarget);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getSimpleTime(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String time = sdf.format(new Date());
        return time;
    }

    public static String formatDate(String dateTime, String format, String originFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat(originFormat,Locale.ENGLISH);
        Date date = null;
        try {
            date = fmt.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat(format,Locale.ENGLISH);
        return fmtOut.format(date);
    }
}

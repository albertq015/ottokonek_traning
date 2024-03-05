package com.otto.mart.support.util;

import android.text.format.DateFormat;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import io.realm.Realm;

public class DataUtil {

    public static String DEFAULT_FORMAT = "dd MMMM yyyy";
    public static String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static void querryRealm(Realm.Transaction transaction) {
        Realm r = Realm.getDefaultInstance();
        try {
            r.executeTransaction(transaction);
        } finally {
            if (!r.isClosed()) {
                r.close();
            }
        }
    }

    public static String getFormattedDateStringFromServerResponse(String serverResponse, String format) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(serverResponse.split("//+")[0]);
        return new SimpleDateFormat(format).format(date);
    }

    public static String getFormattedDateStringFromServerResponse2(String serverResponse, String format) throws ParseException {
        Date date = new SimpleDateFormat("yyyy mm dd HH:mm").parse(serverResponse.split("//+")[0]);
        return new SimpleDateFormat(format).format(date);
    }

    public static void removeAllData() {
        try {
            Realm.deleteRealm(Realm.getDefaultConfiguration());
        } catch (Exception e) {
            Log.e("", "removeAllData:" + e.getMessage());
        }
    }

    public static String getDateString(long value) {
        return getDateString(value, DEFAULT_FORMAT);
    }


    public static String getDateString(long value, String format) {
        return getDateString(value, format, false, 7);
    }

    public static String getDateString(long value, String format, boolean isUnix, int gmt) {
        long unix = isUnix ? 1000L : 1L;
        Locale locale = new Locale("id", "ID");
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, locale);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+" + gmt));
        return dateFormat.format(new Date(value * unix));
    }

    public static String convertCurrency(Object value) {
        String v = String.valueOf(value);
        String price = "PHP ";
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        try {
            String temp = numberFormat.format(Double.parseDouble(v));
            price += temp.replace(",", ".");
            price = price.replace("-", "");
        } catch (Exception e) {
            e.printStackTrace();
            price += "0";
        }
        return price;
    }

    public static String convertCurrencyWithoutPhp(Object value) {
        String v = String.valueOf(value);
        String price = "";
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        try {
            String temp = numberFormat.format(Double.parseDouble(v));
            price += temp.replace(",", ".");
            price = price.replace("-", "");
        } catch (Exception e) {
            e.printStackTrace();
            price += "0";
        }
        return price;
    }

    public static String convertCurrencyPHP(Object value) {
        String v = String.valueOf(value);
        String price = "PHP ";
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        try {
            String temp = "" + numberFormat.format(Double.parseDouble(v));
            price += temp.replace(".", ",").replace("-", "");
        } catch (Exception e) {
            e.printStackTrace();
            price += "0";
        }
        return price;
    }

    public static long convertLongFromCurrency(String value) {
        if (value != null) {
            try {
                return Long.valueOf(value.replaceAll("\\D", ""));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static int getInt(String value) {
        if (value != null) {
            try {
                return Integer.valueOf(value.replaceAll("\\D", ""));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static Long getLong(String value) {
        if (value != null) {
            try {
                return Long.valueOf(value.replaceAll("\\D", ""));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0L;
            }
        }
        return 0L;
    }

    public static int convertIntFromCurrency(String value) {
        return convertIntFromCurrency(value, true);
    }

    public static int convertIntFromCurrency(String value, boolean isClearDecimal) {
        return convertIntFromCurrency(value, isClearDecimal, ".");
    }

    public static int convertIntFromCurrency(String value, boolean isClearDecimal, String separator) {
        if (value != null) {
            try {
                if (isClearDecimal) {
                    value = value.substring(0, value.lastIndexOf(separator));
                }
                return Integer.valueOf(value.replaceAll("\\D", ""));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static String cleanDigit(String value) {
        if (value != null) {
            try {
                return value.replaceAll("[\\d\\s]", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getDigit(String value) {
        if (value != null) {
            try {
                return value.replaceAll("[\\D]", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getDigitFloat(String value) {
        if (value != null) {
            try {
                return value.replaceAll("PHP", "").replaceAll(",", "").replaceAll(" ", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getXXXPhone(String phone) {
        return phone.substring(0, phone.length() - 7) + "XXXX" + phone.substring(phone.length() - 3, phone.length());
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(16);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    /**
     * Convert Timestamp to Formatted Date
     *
     * @param timestamp
     * @param format
     * @return formatted date [String]
     */
    public static String timeStampToFormattedDate(int timestamp, String format) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format(format, cal).toString();
        return date.toString();
    }

    public static String getInitialName(String fullname) {
        String initialName = "";

        if (fullname != null) {
            String[] splitNames = fullname.split(" ");

            for (int i = 0; i < splitNames.length; i++) {
                if (splitNames[i] != null && !splitNames[i].equals("")) {
                    initialName += splitNames[i].substring(0, 1);
                }
            }

            if (initialName.length() > 1) {
                initialName = initialName.substring(0, 2);
            }
        }
        return initialName;
    }

    public static boolean isPinValid(String pin) {
        if (pin.length() == 6) {
            if (!noSameCharacters(pin)) {
                return false;
            }

            if (pin.substring(0, 3).equals(pin.substring(3))) {
                return false;
            }

            StringBuilder incr = new StringBuilder();

            int firstPin = Integer.valueOf(pin.substring(0, 1));
            for (int i = firstPin; i <= firstPin + 5; i++) {
                incr.append(i);
            }
            if (!noSequenceCharacters(pin)) {
                return false;
            }
        } else return false;

        return true;
    }

    public static boolean noSequenceCharacters(String input) {
        String uppercaseInput = input.toUpperCase();

        int c1 = 0;
        int c2 = 0;
        int c3 = 0;

        for (int i = 0; i < uppercaseInput.length(); i++) {
            c1 = c2;
            c2 = c3;
            c3 = uppercaseInput.charAt(i);

            if (Character.isLetterOrDigit(c1) && Character.isLetterOrDigit(c2) && Character.isLetterOrDigit(c3)) {

                if ((c1 + 1) == c2 && (c2 + 1) == c3) {
                    return false;
                }

            }

        }

        return true;
    }

    public static boolean noSameCharacters(String input) {
        String uppercaseInput = input.toUpperCase();

        int c1 = 0;
        int c2 = 0;
        int c3 = 0;

        for (int i = 0; i < uppercaseInput.length(); i++) {
            c1 = c2;
            c2 = c3;
            c3 = uppercaseInput.charAt(i);

            if (Character.isLetterOrDigit(c1) && Character.isLetterOrDigit(c2) && Character.isLetterOrDigit(c3)) {

                if (c1 == c2 && c2 == c3) {
                    return false;
                }

            }

        }

        return true;
    }

    public static double CurrencyToDouble(@NotNull String amount) {
        if (amount != null) {
            String value = amount.replaceAll("[^\\d.,]", "");

            DecimalFormat numfor = (DecimalFormat) NumberFormat.getNumberInstance(new Locale("en", "PH"));
            DecimalFormat result = new DecimalFormat("#.##");
            result.setRoundingMode(RoundingMode.UP);
            try {
                return Double.parseDouble(result.format(numfor.parse(value)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0.0;
    }

    public static String InputDecimal(String amount) {
        String inputAmount = "0";
        String result = "0";
        inputAmount = amount.replaceAll("[^\\d.]", "");
        DecimalFormat df = new DecimalFormat("###.##");



        if (inputAmount.equals("")){
            inputAmount = " ";
        }else {
            inputAmount = df.format(Double.parseDouble(inputAmount)).replace(",", ".");

        }

        if (inputAmount.contains(".")) {
            String[] separated = inputAmount.split("\\.");
            String partDecimal = "";

            if (separated.length > 1) {
                if (separated[1] != null && !separated[1].isEmpty()) {
                    if (separated[1].length() > 2) {
                        partDecimal = separated[1].substring(0, 2);
                    } else {
                        partDecimal = separated[1];
                    }
                }
                inputAmount = separated[0];
            }
            result = DataUtil.convertCurrencyPHP(inputAmount);
            result += "." + partDecimal;
        } else if (amount.trim().equals("PHP")) {
            result = "";
        } else {
            result = DataUtil.convertCurrencyPHP(inputAmount);
        }

        return result;
    }

    public static Double FormattedCurrencyToDouble(String amount) {
        return Double.valueOf(amount.replaceAll("[^\\d.]", ""));
    }

    public static String setFormatAccountNumber(String data) {

        String result = "";
        if (data.length() <= 15) {
            String firstNumber = data.substring(0, 3);

            result = firstNumber + "***********";
        } else {
            String firstNumber = data.substring(0, 3);
            String secondNumber = data.substring(13, 16);
            result = firstNumber + "***********" + secondNumber;

        }
        return result;

    }
}
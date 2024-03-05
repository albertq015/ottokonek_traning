package com.otto.mart.support.util;

import android.util.Log;

/**
 * Created by agungg on 02/08/18.
 */

public class EmvcoUtil {

    public static String parseEMVCO(String data) {
        if (data.length() > 40) {
            int cursor = 0;
            String tag;
            String length;
            String value;

            try {
                while (cursor < data.length()) {
                    tag = data.substring(cursor, cursor + 2);
                    if (isNumeric(tag) && !tag.equals("62")) {
                        cursor = cursor + 2;
                        length = data.substring(cursor, cursor + 2);
                        if (isNumeric(length)) {
                            cursor = cursor + 2;
                            value = data.substring(cursor, cursor + Integer.parseInt(length));
                            cursor = cursor + Integer.parseInt(length);
                        } else {
                            return data;
                        }
                    } else if (isNumeric(tag) && tag.equals("62")) {
                        cursor = cursor + 2;
                        length = data.substring(cursor, cursor + 2);
                        if (isNumeric(length)) {
                            cursor = cursor + 2;
                            value = data.substring(cursor, cursor + Integer.parseInt(length));
                            int idCursor = 0;
                            tag = value.substring(idCursor, idCursor + 2);
                            if (isNumeric(tag) && tag.equals("05")) {
                                idCursor = idCursor + 2;
                                length = value.substring(idCursor, idCursor + 2);
                                if (isNumeric(length)) {
                                    idCursor = idCursor + 2;
                                    value = value.substring(idCursor, idCursor + Integer.parseInt(length));
                                    return value;
                                } else {
                                    return data;
                                }
                            } else {
                                return data;
                            }
                        } else {
                            return data;
                        }
                    } else {
                        return data;
                    }
                }
            } catch (Exception e) {
                Log.d("EMVCO Reader", "parseEMVCO: failed to parse emvco");
            }
            return data;
        } else return data;
    }

    public static int parseEMVCOtag01(String data) {
        int cursor = 0;
        String tag;
        String length;
        String value;

        try {
            while (cursor < data.length()) {
                tag = data.substring(cursor, cursor + 2);
                if (isNumeric(tag) && !tag.equals("01")) {
                    cursor = cursor + 2;
                    length = data.substring(cursor, cursor + 2);
                    if (isNumeric(length)) {
                        cursor = cursor + 2;
                        cursor = cursor + Integer.parseInt(length);
                    }
                } else if (isNumeric(tag) && tag.equals("01")) {
                    cursor = cursor + 2;
                    length = data.substring(cursor, cursor + 2);
                    if (isNumeric(length)) {
                        cursor = cursor + 2;
                        value = data.substring(cursor, cursor + Integer.parseInt(length));
                        int idCursor = 0;
                        tag = value.substring(idCursor, idCursor + 2);
                        if (tag.equals("11")) {
                            return 11;
                        } else if (tag.equals("12")) {
                            return 12;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            }
        } catch (Exception e) {
            Log.d("EMVCO Reader", "parseEMVCO: failed to parse emvco");
        }

        return 0;
    }

    public static String parseEMVCOtag54(String data) {
        if (data.length() > 40) {
            int cursor = 0;
            String tag;
            String length;
            String value;

            try {
                while (cursor < data.length()) {
                    tag = data.substring(cursor, cursor + 2);

                    if (isNumeric(tag) && !tag.equals("54")) {
                        cursor = cursor + 2;
                        length = data.substring(cursor, cursor + 2);
                        if (isNumeric(length)) {
                            cursor = cursor + 2;
                            value = data.substring(cursor, cursor + Integer.parseInt(length));
                            cursor = cursor + Integer.parseInt(length);
                        } else {
                            return "-1";
                        }
                    } else if (isNumeric(tag) && tag.equals("54")) {
                        cursor = cursor + 2;
                        length = data.substring(cursor, cursor + 2);
                        if (isNumeric(length)) {
                            cursor = cursor + 2;
                            value = data.substring(cursor, cursor + Integer.parseInt(length));
                            return value;
                        } else {
                            return "-1";
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("EMVCO Reader", "parseEMVCO: failed to parse emvco");
            }

            return "-1";
        } else return data;
    }

    public static String parseEMVCOtag62(String data) {
        int cursor = 0;
        String tag;
        String length;
        String value;

        try {
            while (cursor < data.length()) {
                tag = data.substring(cursor, cursor + 2);
                if (isNumeric(tag) && !tag.equals("62")) {
                    cursor = cursor + 2;
                    length = data.substring(cursor, cursor + 2);
                    if (isNumeric(length)) {
                        cursor = cursor + 2;
                        value = data.substring(cursor, cursor + Integer.parseInt(length));
                        cursor = cursor + Integer.parseInt(length);
                    } else {
                        return "";
                    }
                } else if (isNumeric(tag) && tag.equals("62")) {
                    cursor = cursor + 2;
                    length = data.substring(cursor, cursor + 2);
                    if (isNumeric(length)) {
                        cursor = cursor + 2;
                        value = data.substring(cursor, cursor + Integer.parseInt(length));

                        int idCursor = 0;
                        tag = value.substring(idCursor, idCursor + 2);
                        if (isNumeric(tag) && tag.equals("05")) {
                            idCursor = idCursor + 2;
                            length = value.substring(idCursor, idCursor + 2);
                            if (isNumeric(length)) {
                                idCursor = idCursor + 2;
                                value = value.substring(idCursor, idCursor + Integer.parseInt(length));
                                return value;
                            } else {
                                return "";
                            }
                        } else {
                            return "";
                        }
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            }
        } catch (Exception e) {
            Log.d("EMVCO Reader", "parseEMVCO: failed to parse emvco");
        }

        return "";
    }


    /**
     * parse by num DO NOT USE THIS TO PARSE TAG 62 due subtag
     * @param num tag num
     * @param data emvco string
     * @return
     */
    public static String parseEMVCOtagByNum(int num, String data) {
        if (data.length() > 40) {
            int cursor = 0;
            String tag;
            String length;
            String value;

            try {
                while (cursor < data.length()) {
                    tag = data.substring(cursor, cursor + 2);

                    if (isNumeric(tag) && !tag.equals(num+"")) {
                        cursor = cursor + 2;
                        length = data.substring(cursor, cursor + 2);
                        if (isNumeric(length)) {
                            cursor = cursor + 2;
                            value = data.substring(cursor, cursor + Integer.parseInt(length));
                            cursor = cursor + Integer.parseInt(length);
                        } else {
                            return "-1";
                        }
                    } else if (isNumeric(tag) && tag.equals(num+"")) {
                        cursor = cursor + 2;
                        length = data.substring(cursor, cursor + 2);
                        if (isNumeric(length)) {
                            cursor = cursor + 2;
                            value = data.substring(cursor, cursor + Integer.parseInt(length));
                            return value;
                        } else {
                            return "-1";
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("EMVCO Reader", "parseEMVCO: failed to parse emvco");
            }

            return "-1";
        } else return data;
    }



    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }


}
package com.otto.mart.keys;

public class AppKeys {

    public static final String FLAVOR_PRODUCTION = "production";
    public static final String FLAVOR_DEVELOPMENT = "development";

    //region Intent Key

    public static final int KEY_PICK_CONTACT_REQUEST = 1000;
    public static final int KEY_PIN_COFIRMATION = 1001;
    public static final int KEY_PPOB_CATEGORY_PICKER = 1002;
    public static final int KEY_INPUT_PIN = 1003;

    public static final String KEY_PIN_CODE = "pincode";
    public static final String KEY_ORDER_PAYMENT_SUCCESS = "order_payment_success";
    public static final String KEY_ORDER_NUMBER = "order_number";
    public static final String KEY_BANK_LIST_NOT_SELECTABLE = "bank_list_from_people";
    public static final String KEY_IS_UPDATE_STATUS = "is_update_status";
    public static final String KEY_PHONE_NUMBER = "phone_number";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_WEBVIEW_CONTENT = "webview_content";
    public static final String KEY_PPOB_CATEGORY_PICKER_DATA = "ppob_category_picker";
    public static final String KEY_PPOB_CATEGORY_PICKER_SEARCH_STATUS = "ppob_category_picker_search_status";
    public static final String KEY_WALLET_ID = "wallet_id";
    public static final String KEY_TAB_POSITION = "tab_position";
    public static final String KEY_INBOX_DATA = "inbox_data";
    public static final String KEY_INPUT_PIN_TYPE_CONFIRM = "confirm";
    public static final String KEY_INPUT_PIN_TYPE_LOGIN = "login";
    public static final String KEY_INPUT_PIN_TYPE_INPUT = "payment";
    public static final String KEY_INPUT_PIN_TYPE_INPUT_QR_CONFRIM = "paymentQr";
    public static final String KEY_INPUT_PIN_TYPE_PAYMENT = "payment";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_CONFRIM_TRANSFER = "confrim_transfer";


    //PPOB Intent
    public static final String KEY_PPOB_PRODUCT_TYPE_DATA = "ppob_product_type_data";
    public static final String KEY_PPOB_PROVIDER_DATA = "ppob_provider_data";
    public static final String KEY_PPOB_PAYMENT_DATA = "ppob_payment_data";
    public static final String KEY_PPOB_INQUIRY_DATA = "ppob_inquiry_data";
    public static final String KEY_PPOB_INQUIRY_OLD_DATA = "ppob_inquiry_old_data";
    public static final String KEY_PPOB_PAYMENT_SUCCESS_DATA = "ppob_payment_success_data";
    public static final String KEY_INQUIRY_QR = "inquiry_qr";
    public static final String KEY_PPOB_PRODUCT_TYPE_CODE = "ppob_product_type_code";
    public static final String KEY_PPOB_IS_FROM_INPUT_FORM = "ppob_is_from_input_form";
    public static final String KEY_PPOB_FAVORITE_DATA = "ppob_favorite_data";
    public static final String KEY_PPOB_DONASI_PRODUCT_DATA = "ppob_donasi_product_data";
    public static final String KEY_PPOB_DONASI_INQUIRY_DATA = "ppob_donasi_inquiry_data";
    public static final String KEY_PPOB_DONASI_PAYMENT_REQUEST_DATA = "ppob_donasi_payment_request_data";
    public static final String KEY_PPOB_GAME_DATA = "ppob_game_data";
    public static final String KEY_CONTACT_PAYMENT_SUCCESS_DATA = "contact_payment_success_data";

    //endregion

    // Broadcast key

    public static final String KEY_BROADCAST_REFRESH_POINT = "key_refresh_poin";
    public static final String KEY_BROADCAST_REFRESH_KUPON_SAYA = "key_refresh_kupon_saya";
    public static final String KEY_BROADCAST_REFRESH_VIEW_DASHBOARD = "key_refresh_view_dashboard";
    public static final String KEY_BROADCAST_REFRESH_POINT_TWO = "key_refresh_poin_two";
    public static final String KEY_BROADCAST_GET_POINT = "key_get_point";
    public static final String KEY_BROADCAST_REFRESH_PAGE_DETAIL_VOUCHER = "key_refresh_detail_voucher";
    public static final String KEY_BROADCAST_REFRESH_PAGE_VOUCHER_SAYA_BOTH = "key_refresh_voucher_saya_both";

    public static final String BROADCAST_VALUE_EXTRA = "valueExtraBroadcast";

    // extra intent key

    public static final String KEY_EXTRA_POINT = "key_extra_point";
    public static final String KEY_EXTRA_PRODUCT_NAME = "key_extra_product_name";

    // ottopoint
    public static final String KEY_OTTOPOINT_DEALS_TYPE = "key_ottopoint_deals_type";


    //region API Key

    public static final int API_KEY_CHECK_VERSION = 10;
    public static final int API_KEY_APP_CLONER_LIST = 11;

    //Profile
    public static final int API_KEY_GET_PROVINCE = 20;
    public static final int API_KEY_GET_CITY = 21;
    public static final int API_KEY_GET_DISTRICT = 22;
    public static final int API_KEY_GET_PROFILE = 111111111;
    public static final int API_KEY_UPDATE_ADDRESS = 111;
    public static final int API_KEY_GET_OMSET_HISTORY = 222;
    public static final int API_KEY_UPDATEE_STATUS = 223;
    public static final int API_KEY_MERCHANT_STATUS = 224;
    public static final String FULL_ADDRESS= "full_address";
    public static final String CITY_ID= "city_id";

    //Alfamart
    public static final int API_KEY_ALFAMART_PURCHASE = 100;
    public static final int API_KEY_ALFAMART_PURCHASE_STATUS = 101;
    public static final int API_KEY_ALFAMART_TOKEN = 102;

    //Toko Ottopay
    public static final int API_KEY_STORE_LIST = 200;
    public static final int API_KEY_REORDER_PRODUCT_LIST = 201;
    public static final int API_KEY_ADD_TO_CART = 202;
    public static final int API_KEY_REMOVE_FROM_CART = 203;
    public static final int API_KEY_CART = 204;
    public static final int API_KEY_ORDER_CONFIRM = 205;
    public static final int API_KEY_ORDER_PAYMENT = 206;
    public static final int API_KEY_ORDER_PAY_OFF = 207;
    public static final int API_KEY_ORDER_HISTORY = 208;
    public static final int API_KEY_ORDER_HISTORY_DETAIL = 209;

    //Inbox
    public static final int API_KEY_INBOX_LIST = 300;
    public static final int API_KEY_INBOX_READ = 301;
    public static final int API_KEY_INBOX_READ_BULK = 302;
    public static final int API_KEY_INBOX_DELETE_BULK = 303;

    //PPOB
    public static final int API_KEY_TRANSACTION_ADVICE = 400;
    public static final int API_KEY_PPOB_RECEIPT = 401;
    public static final int API_KEY_PPOB_PAYMENT = 402;
    public static final int API_KEY_PPOB_CHECK_STATUS_QR_PAYMENT = 403;
    public static final int API_KEY_PPOB_ADVICE = 404;
    public static final int API_KEY_PPOB_FAVORITE = 405;

    //Donasi
    public static final int API_KEY_DONASI_PRODUCT_LIST = 500;
    public static final int API_KEY_DONASI_INQUIRY = 501;
    public static final int API_KEY_DONASI_QR_STRING = 502;
    public static final int API_KEY_DONASI_PAYMENT = 503;
    public static final int API_KEY_DONASI_CHECK_STATUS_QR_PAYMENT = 504;

    //Emoney
    public static final int API_KEY_WALLET_INFO = 600;
    public static final int API_KEY_WALLET_HISTORY = 601;

    //Oasis
    public static final int API_KEY_GET_GROSIR_LIST = 700;
    public static final int API_KEY_GET_CHECK_OASIS_STATUS = 701;
    public static final int API_KEY_GET_GROSIR_CHECK_SUPPLIER = 702;
    public static final int API_KEY_GET_GROSIR_REGISTER_SUPPLIER = 703;
    public static final int API_KEY_GET_GROSIR_LIST_CATEGORY = 707;
    public static final int API_KEY_APPROVED_ORDER = 708;
    public static final int API_KEY_ORDER = 709;

    //OCBI
    public static final int API_KEY_GET_OC_STATUS=928;
    public static final int API_KEY_GET_UPDATE_OC_STATUS=897;

    //Toko Online - elevania
    public static final int API_KEY_GET_SPECIAL_EVENT_PRODUCT = 713;

    //endregion API Key



    /**
     * PPOB Product Types
     */

    public static final String PPOB_TYPE_PULSA = "PULSA";
    public static final String PPOB_TYPE_PULSA_PASCABAYAR = "TAGIHANPULSA";
    public static final String PPOB_TYPE_PAKET_DATA = "PAKETDATA";
    public static final String PPOB_TYPE_ELECTRICITY_TOKEN = "PLNTOKEN";
    public static final String PPOB_TYPE_ELECTRICITY_BILL = "TAGIHANLISTRIK";
    public static final String PPOB_TYPE_AIR = "PDAM";
    public static final String PPOB_TYPE_INTERNET = "CBN";
    public static final String PPOB_TYPE_ASURANSI = "insurance";
    public static final String PPOB_TYPE_TELKOM = "TELKOM";
    public static final String PPOB_TYPE_TELKOM_INTERNET = "SPEEDY";
    public static final String PPOB_TYPE_PENDIDIKAN = "PENDIDIKAN";
    public static final String PPOB_TYPE_TOP_UP = "ISIDOMPET";
    public static final String PPOB_TYPE_CICILAN = "CICILAN";
    public static final String PPOB_TYPE_BPJS = "BPJS";
    public static final String PPOB_TYPE_DONASI = "donasi";
    public static final String PPOB_TYPE_GAME = "GAMEDENOM";
    public static final String PPOB_TYPE_VOUCHER = "ENTERTAINMENT";


    /**
     * Preference Keys
     */
    public static final String PREF_LAST_OMZET = "pref_last_omzet";
    public static final String PREF_LAST_BALANCE = "pref_last_balance";
    public static final String PREF_CONTACT_US = "pref_contact_us";
    public static final String PREF_CONTACT_US_TIME = "pref_contact_us_time";
    public static final String PREF_MERCHANT_THEME = "pref_merchannt_theme";
    public static final String PREF_FEATURE_PRODUCT = "pref_feature_product";
    public static final String PREF_LANGUAGE_CODE = "pref_language_code";

    /**
     * Session
     */
    public static final String PREF_LOGIN = "prefLogin";

    /**
     * Themes
     */
    public static final String DEFAULT_THEME_COLOR = "#2B6447";

}
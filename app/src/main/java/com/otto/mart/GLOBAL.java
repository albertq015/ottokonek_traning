package com.otto.mart;

public interface GLOBAL {

    //Version  v2.2.2
//    final boolean PULSA_BY_OTTOAG = true;
//    final boolean PULSA_POSTPAID_BY_OTTOAG = false;
//    final boolean PAKET_DATA_BY_OTTOAG = true;
//    final boolean TELKOM_BY_OTTOAG = false;
//    final boolean AIR_BY_OTTOAG = false;
//    final boolean LISTRIK_BY_OTTOAG = false;
//    final boolean PTR_VERSION = false;
//    final boolean TOKO_OTTOPAY = true;

    //Version  v2.3.0
    final boolean PULSA_BY_OTTOAG = true;
    final boolean PULSA_POSTPAID_BY_OTTOAG = true;
    final boolean PAKET_DATA_BY_OTTOAG = true;
    final boolean TELKOM_BY_OTTOAG = true;
    final boolean AIR_BY_OTTOAG = true;
    final boolean LISTRIK_BY_OTTOAG = true;
    final boolean PTR_VERSION = false;
    final boolean TOKO_OTTOPAY = false;

    final boolean PPOB_QR_PAYMENT_ENABLE = true;

    //Server Base Url
    String dev_server_endpoint = "http://toko-online.clappingape.com/";
//    String dev_server_endpoint = "http://ottopay2-api.clappingape.com/";
    //String production_server_endpoint = "https://ottopay2.ottopay.id/";
    String dev_tokol_server_endpoint = "http://toko-online.clappingape.com/";
    String production_server_endpoint = "https://tokoonline.ottopay.id/"; // New
    String prod_tokol_server_endpoint = "https://tokoonline.ottopay.idd/";

    //API Version
    String dev_server_version = "v1/";
    String production_server_version = "v1/";

    //Product Name
    String product_server = "ottopay-mart/";
    String product_serverz = "indomarco/";

    boolean isOttoCash = true;

    //Server Url
    String dev_server = dev_server_endpoint + dev_server_version + product_server;
    String dev_tokol_server = dev_tokol_server_endpoint + dev_server_version + product_server;
    String dev_serverz = dev_server_endpoint + dev_server_version + product_serverz;
    String production_server = production_server_endpoint + production_server_version + product_server;
    String production_tokol_server = prod_tokol_server_endpoint + production_server_version + product_server;

    String GOOGLE_API_SERVICE_NAME = "GOOGLE-API-SERVICE";
    String LOCATION_SERVICE_NAME = "LOCATION-SERVICE";
    int LOCATION_INTERVAL = 1800000;
    final float LOCATION_DISTANCE = 5f;

    int STATUS_RUNNING = 331;
    int STATUS_ERROR = 332;
    int STATUS_DONE = 333;

    int COUNTRY_ID = 2;
    double LONGITUDE_MIN = 95.010;
    double LONGITUDE_MAX = 141.030;
    double LATITUDE_MIN = -10.990;
    double LATITUDE_MAX = 5.910;

    double[] COUNTRY_LOCAL_MAP = {LONGITUDE_MIN, LONGITUDE_MAX, LATITUDE_MIN, LATITUDE_MAX};

    double[] PLACEHOLDER_LOCAL_MAP = {106.84513000, -6.21462000};

    int API_KEY_GET_PROFILE = 111111111;

    int API_KEY_GET_PROVINCE = 20;
    int API_KEY_GET_CITY = 21;
    int API_KEY_GET_DISTRICT = 22;

    int API_KEY_UPDATE_ADDRESS = 111;

    int API_KEY_GET_OMSET_HISTORY = 222;
    int KEY_API_PAYMENT_JURNAL = 203;
    int KEY_API_ORDER_CONFIRMATION = 204;

    String PAN = "001000234";

    String A_APIKEY = "89SBHwBpYdKkGXlz8VU13O10wWcMUjOA";

    // Merchant
    int KEY_API_GET_NEARBY_MERCHANT = 800;

    //Response Code
    int CODE_SUCCESS = 200;
    int CODE_SERVER_ERROR = 500;
    int CODE_ACCESS_TOKEN_EXPIRED = 401;
    int CODE_OTTOCASH_EXPIRED = 418;
    int CODE_NEED_OTP_VERIFICATION = 100;
    int CODE_DATA_EMPTY = 402;
    int CODE_ACCOUNT_BLOCKED = 415;
    int CODE_PEDE_PLUS_REQUIRED = 512;

    String IMAGE_SOURCE = "http://ottopay-mart.clappingape.com";
    String IMAGE_SOURCE_PROD = "https://ottopay3.ottopay.id";
    String CART_COUNT = "cart_count";
    String CATEGORIES = "categories";

    // HTTP Code
    int HTTP_CODE_SUCCESS = 200;
    int HTTP_CODE_CREATED = 201;
    int HTTP_CODE_UNAUTHORIZE = 401;
    int HTTP_CODE_FORBIDEN = 403;
    int HTTP_CODE_NOTFOUND = 404;

    // type image banner
    int BANNER_VOUCHER_LIST = 1;
    int BANNER_VOUCHER_OTHERS = 0;

    // type voucher
    String VOUCHER_TYPE_PROMO_TERBARU = "voucher_promo_terbaru";
    String VOUCHER_TYPE_PROMO_LAINNYA = "voucher_promo_lainnya";

    // date & time
    String FORMAT_DATE_TIME_DEFAULT_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    String FORMAT_DATE_TIME_DEFAULT_SERVER_TWO = "yyyy-MM-dd'T'HH:mm:ss";
    String FORMAT_DATE_TIME_DATE_SERVER = "yyyy-MM-dd";
    String FORMAT_DATE_TIME_TIME_SERVER = "HH:mm:ss";
    String FORMAT_DATE_TIME_DATE_TIME_SERVER = "yyyy-MM-dd HH:mm:ss";
    String FORMAT_DATE_TIME_MONTH_SERVER = "MM";
    String FORMAT_DATE_TIME_MONTH_YEAR_SERVER = "MM-yyyy";
    String FORMAT_DATE_TIME_TIME = "HH:mm";
    String FORMAT_DATE_TIME_DATE_MONTH = "dd MM yyyy";
    String FORMAT_DATE_TIME_DATE_MONTH_TIME = "dd MMM yyyy, HH.mm";
    String FORMAT_DATE_TIME_DATE_MONTH_TEXT = "dd MMMM yyyy";
    String FORMAT_DATE_TIME_DATE_MONTH_TEXT_MIN = "dd MMM yyyy";
    String FORMAT_DATE_TIME_DAY_MONTH_TEXT_MIN = "dd MMM";
    String FORMAT_DATE_TIME_FULL = "MMM, dd/MM/yyyy, HH:mm";
    String FORMAT_DATE_TIME_WITHOUT_SPACE = "yyyyMMddHHmmss";
    String FORMAT_DATE_FULL_MONTH = "MMMM";
    String FORMAT_DATE_FULL_MONTH_YEAR = "MMMM yyyy";
    String FORMAT_DATE_DAY_NUMBER = "dd";
}

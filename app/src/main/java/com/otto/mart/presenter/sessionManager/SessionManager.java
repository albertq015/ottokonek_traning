package com.otto.mart.presenter.sessionManager;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.otto.mart.OttoMartApp;
import com.otto.mart.keys.AppKeys;
import com.otto.mart.model.APIModel.Misc.AuthDataModel;
import com.otto.mart.model.APIModel.Misc.olshop.ShippingAddressData;
import com.otto.mart.model.APIModel.Response.merchant.featureProduct.FeatureProductData;
import com.otto.mart.model.APIModel.Response.merchant.theme.MerchantTheme;
import com.otto.mart.model.localmodel.Realm.LoginDatastoreModel;
import com.otto.mart.presenter.dao.AuthDao;
import com.otto.mart.support.util.DataUtil;
import com.otto.mart.support.util.pref.Pref;

public class SessionManager {

    private final String TAG = this.getClass().getSimpleName();

    final static String KEY_IS_LOGIN_KEY = "LOGINKEY_021394gruehbfjkowp20ir39ug";
    final static String KEY_FIREBASE_TOKEN = "firebase_token_564865885484848";
    final static String KEY_IS_REEMEMBER_PHONE_KEY = "REMEMBER_PHONE_KEY_35e26375675647";
    final static String KEY_USERNAME = "USERNAME_j23u489eijrwokqpi2u43hreknf";
    final static String KEY_NAME = "PAN_012398uiehrwkjip123908fgdjh";
    final static String KEY_STORENAME = "PAYEE_pio23rhuelwxpq[oijfkndlm";
    final static String KEY_USERID = "PROMO_3245rtegfjdweqr32t423453th";
    final static String KEY_WALLETID = "PROMO_3245rtegfjdweqr32t423453wl";
    final static String KEY_MERCHID = "STD_129308u4ihtbjwefknlqdk2ruhteig";
    final static String KEY_ACCESS_TOKEN = "POS_023948uthiregfjwiep082uy3tier";
    final static String KEY_HASSOFTBUTTON = "TEMP_Derq3124t2r413gwe23gwbeg23geb";
    final static String KEY_PHONE = "PHONE_Derq3124t2r413gwe23gwbeg23geb";
    final static String KEY_STATUS = "STATUS_Derq3124t2r413gwe23afgfdgfdsgfdg";
    final static String KEY_QR_CONTENT = "QR_CONTENT_otqwr2rterertertertewe5t";
    final static String KEY_LAST_SEEN = "LAST_SEEN_dfgdfgdfgfgerertertertewe5t";
    final static String KEY_MAIL_ADDRESS = "MAIL_ADDRESS_3245rtegfjdweqr32t423453wl";
    final static String KEY_PROFILE_DATA = "PROFILE_DATA_3245rteg3453453454353453wl";
    final static String PAN = "001000234";
    final static String KEY_OTTOPOINT_ISEXIST = "OTTOPOINT_KEY_ISEXIST_Derq3124t2r413gwe23afgfdgfdsgfdg";
    final static String KEY_OTTOPOINT_TOKEN = "OTTOPOINT_KEY_Derq3124t2r413gwe23afgfdgfdsgfdg";
    final static String KEY_OTTOPOINT_CUSTOMER_ID = "CUSTOMER_ID_OTTOPOINT_Derq3124t2r413gwe23afgfdgfdsgfdg";
    final static String KEY_OTTOPOINT_POINT = "OTTOPOINT_POINT_Derq3124t2r413gwe23afgfdgfdsgfdg";
    final static String KEY_IS_LOGIN_ADDRESS = "LOGINKEY_021394gruehbfjkowp20ir39ugfhgfgd";
    final static String KEY_TNC_OCBI_PROGRESS = "TncProgress_021394gruehbfjdasioumndjasgfgd";
    final static String KEY_MPAN = "mpan_kikaldks324878fumndjasgfgd";
    final static String KEY_MID = "mid_sjkad987asmm9120djasgfgd";
    final static String KEY_NMID = "nmid_9d8sda9dasioumndjasgfgd";
    final static String KEY_TID = "tid_9d8sda9djdsas990jgfgfgd";
    final static String KEY_DEFAULT_ADDRESS = "defaultAddress_9kjsa94893284dsasfgd";
    final static String KEY_MERCHANT_NAME = "owner_9aj984320daskn984u3293284dsasfgd";
    final static String KEY_SAVE_ADDRESS = "save_address_9kjsa94893284dsasfgd";
    final static String KEY_PROFILE_CARD_BG = "profile_card_bg_werlkfsdflkjasfsd";
    final static String KEY_PROFILE_CARD_LOGO = "profile_card_logo_werlkfsdflkjasfsd";
    final public static String KEY_PREF_HIDE_LOCATION_PERMISSION_DIALOG = "pref_show_location_permission";
    final public static String KEY_PREF_HIDE_PROMINENT_PERMISSION_DIALOG = "pref_show_prominent_permission";
    final public static String KEY_PREF_HIDE_CONTACT_PERMISSION_DIALOG = "pref_show_contact_permission";

    Context mContext;

    public SessionManager() {
        this.mContext = OttoMartApp.getContext();
    }

    /**
     * Sof Key
     */
    public static void setSoftkeyIsExist(boolean bool) {
        Pref.getPreference().putBoolean(KEY_HASSOFTBUTTON, bool);
    }

    public static boolean isSoftKey() {
        boolean check = Pref.getPreference().getBoolean(KEY_HASSOFTBUTTON);
        return check;
    }

    /**
     * Login Status
     */
    public static boolean isLoggedIn() {
        return Pref.getPreference().getBoolean(KEY_IS_LOGIN_KEY);
    }

    public static void createLoginSession(int user_id, int wallet_id, String accessToken, String secondaryToken, AuthDataModel storeModel) {
        Pref.getPreference().putBoolean(KEY_IS_LOGIN_KEY, true);
        Pref.getPreference().putString(KEY_ACCESS_TOKEN, accessToken);
        Pref.getPreference().putString(KEY_USERID, String.valueOf(user_id));
        Pref.getPreference().putString(KEY_STORENAME, secondaryToken);
        Pref.getPreference().putString(KEY_WALLETID, String.valueOf(wallet_id));
        Pref.getPreference().putString(KEY_PHONE, storeModel.getPhone());
        Pref.getPreference().putString(KEY_STATUS, storeModel.getStatus());
        Pref.getPreference().putString(KEY_QR_CONTENT, "");
        Pref.getPreference().putString(KEY_NAME, storeModel.getMerchant_name());
        Pref.getPreference().putString(KEY_MAIL_ADDRESS, storeModel.getEmail());
        Pref.getPreference().putString(KEY_MID,storeModel.getMid());
        Pref.getPreference().putString(KEY_MPAN,storeModel.getMpan());
        Pref.getPreference().putString(KEY_NMID,storeModel.getNmid());
        Pref.getPreference().putString(KEY_TID,storeModel.getTid());
        Pref.getPreference().putString(KEY_DEFAULT_ADDRESS,storeModel.getAddress());
        Pref.getPreference().putString(KEY_MERCHANT_NAME,storeModel.getName());
        AuthDao.initSessionData(storeModel);
    }

    public static void updateUserDataRealm(AuthDataModel storeModel) {
        AuthDao.updateSessionData(storeModel);
    }

    /**
     * Access Token
     */
    public static String getAccessToken() {
        if (isLoggedIn()) {
            if (Pref.getPreference().getString(KEY_ACCESS_TOKEN) == null) {
                try {
                    Pref.getPreference().putString(KEY_ACCESS_TOKEN, SessionManager.getUserProfile().getAccess_token());
                    return  SessionManager.getUserProfile().getAccess_token();
                } catch (Exception e) {
                    Log.e("", "getAccessToken: " + e.getMessage());
                    return "TokenNaN";
                }
            } else {
                return Pref.getPreference().getString(KEY_ACCESS_TOKEN);
            }
        } else {
            return null;
        }
    }

    public static void setToken(String token){
        Pref.getPreference().putBoolean(KEY_IS_LOGIN_KEY, true);
        Pref.getPreference().putString(KEY_ACCESS_TOKEN,token);
    }

    public static void setAddressOasis(ShippingAddressData data){
        Pref.getPreference().putString(KEY_SAVE_ADDRESS,new Gson().toJson((ShippingAddressData) data));
    }

    public static String getAddressOasis(){
        return Pref.getPreference().getString(KEY_SAVE_ADDRESS);
    }


    /**
     * Secondary Token
     */
    public static void setSecondaryToken(String secondaryToken) {
        Pref.getPreference().putString(KEY_STORENAME, secondaryToken);
    }

    public static String getSecondaryToken() {
        return Pref.getPreference().getString(KEY_STORENAME);
    }


    /**
     * Wallet ID
     */
    public static int getWalletID() {
        if (isLoggedIn()) {
            if (Integer.valueOf(Pref.getPreference().getString(KEY_WALLETID)) == 0) {
                try {
                    int walletId = getUserProfile().getWallet_id();

                    Pref.getPreference().putString(KEY_WALLETID, String.valueOf(walletId));
                    return walletId;
                } catch (Exception e) {
                    Log.e("", "getAccessToken: " + e.getMessage());
                    return -1;
                }
            } else
                return Integer.valueOf(Pref.getPreference().getString(KEY_WALLETID));
        } else {
            return -1;
        }
    }


    /**
     * Logout
     */
    public static void logout() {
        try {
            Pref.getPreference().remove(KEY_IS_LOGIN_KEY);
            Pref.getPreference().remove(KEY_ACCESS_TOKEN);
            Pref.getPreference().remove(KEY_STORENAME);
            Pref.getPreference().remove(KEY_USERID);
            Pref.getPreference().remove(KEY_USERID);
            Pref.getPreference().remove(KEY_WALLETID);
            Pref.getPreference().remove(KEY_MAIL_ADDRESS);
            Pref.getPreference().remove(KEY_DEFAULT_ADDRESS);
            Pref.getPreference().remove(AppKeys.PREF_MERCHANT_THEME);
            Pref.getPreference().remove(AppKeys.PREF_FEATURE_PRODUCT);

            //OttoPoint
            Pref.getPreference().remove(KEY_OTTOPOINT_CUSTOMER_ID);
            Pref.getPreference().remove(KEY_OTTOPOINT_TOKEN);
            Pref.getPreference().remove(KEY_OTTOPOINT_ISEXIST);
            Pref.getPreference().remove(KEY_OTTOPOINT_POINT);

            //Oasis
            Pref.getPreference().remove(KEY_SAVE_ADDRESS);

            DataUtil.removeAllData();
        } catch (Exception e) {
            Log.e("", "Logout: Error");
        }
    }

    public static int getUserId() {
        return Integer.valueOf(Pref.getPreference().getString(KEY_USERID));
    }

    public static String getPhone() {
        return Pref.getPreference().getString(KEY_PHONE);
    }

    public static String getUserStatus() {
        return Pref.getPreference().getString(KEY_STATUS);
    }

    public static String getUserName() {
        return Pref.getPreference().getString(KEY_NAME);
    }

    public static String getMailAddress() {
        return Pref.getPreference().getString(KEY_MAIL_ADDRESS);
    }

    //QR Content
    public static void setQrContent(String qrContent) {
        Pref.getPreference().putString(KEY_QR_CONTENT, qrContent);
    }

    public static String getQrContent() {
        return Pref.getPreference().getString(KEY_QR_CONTENT);
    }

    //Last Seen
    public static void setLastSeen(long lastSeen) {
        Pref.getPreference().putString(KEY_LAST_SEEN, String.valueOf(lastSeen));
    }

    public static long getLastSeen() {
        return Long.valueOf(Pref.getPreference().getString(KEY_LAST_SEEN));
    }

    //Remember Phone Number
    public static void setIsRemeberPhone(boolean status) {
        Pref.getPreference().putBoolean(KEY_IS_REEMEMBER_PHONE_KEY, status);
    }

    public static void setOttopointCustomerId(String id) {
        Pref.getPreference().putString(KEY_OTTOPOINT_CUSTOMER_ID, id);
    }

    public static void setOttopointIsExist(boolean isExist) {
        Pref.getPreference().putBoolean(KEY_OTTOPOINT_ISEXIST, isExist);
    }

    public static boolean getIsRememberPhone() {
        return Pref.getPreference().getBoolean(KEY_IS_REEMEMBER_PHONE_KEY);
    }

    public static String getOttopointCustomerId() {
        return Pref.getPreference().getString(KEY_OTTOPOINT_CUSTOMER_ID);
    }

    public static boolean getOttopointIsExist() {
        return Pref.getPreference().getBoolean(KEY_OTTOPOINT_ISEXIST);
    }

    public static void setOttopointPoint(double point) {
        Pref.getPreference().putLong(KEY_OTTOPOINT_POINT, (long) point);
    }

    public static double getOttopointPoint() {
       return (double) Pref.getPreference().getLong(KEY_OTTOPOINT_POINT);
    }

    public static boolean isOttopointAuthExist() {
        return getOttopointIsExist();
    }

    public static void setUserProfile(LoginDatastoreModel loginDatastoreModel) {
        Pref.getPreference().putString(KEY_PROFILE_DATA, new Gson().toJson(loginDatastoreModel));
    }

    public static LoginDatastoreModel getUserProfile() {
        String profileDataJson = Pref.getPreference().getString(KEY_PROFILE_DATA);
        return new Gson().fromJson(profileDataJson, LoginDatastoreModel.class);
    }

    public static void setTncProgress(boolean isOnProgress){
        Pref.getPreference().putBoolean(KEY_TNC_OCBI_PROGRESS,isOnProgress);
    }

    public static boolean getTncProgress(){
        return Pref.getPreference().getBoolean(KEY_TNC_OCBI_PROGRESS);
    }

    public static String getMID(){
        return Pref.getPreference().getString(KEY_MID);
    }

    public static String getMPAN(){
        return Pref.getPreference().getString(KEY_MPAN);
    }

    public static String getNMID(){
        return Pref.getPreference().getString(KEY_NMID);
    }

    public static String getTID(){
        return Pref.getPreference().getString(KEY_TID);
    }

    public static void setDefaultAddress(String completeAddress) {
        Pref.getPreference().putString(KEY_DEFAULT_ADDRESS,completeAddress);
    }

    public static String getDefaultAddress(){
        return Pref.getPreference().getString(KEY_DEFAULT_ADDRESS);
    }

    public static String getMerchantName(){
        return Pref.getPreference().getString(KEY_MERCHANT_NAME);
    }


    //Firebase Token
    public static void setFirebaseToken(String token) {
        Pref.getPreference().putString(KEY_FIREBASE_TOKEN, token);
    }

    public static String getFirebaseToken() {
        return Pref.getPreference().getString(KEY_FIREBASE_TOKEN);
    }

    //Pref Login
    public static void setPrefLogin(AuthDataModel model){
        Pref.getPreference().putString(AppKeys.PREF_LOGIN, new Gson().toJson(model));
    }

    public static AuthDataModel getPrefLogin(){
        String profileData = Pref.getPreference().getString(AppKeys.PREF_LOGIN);
        return new Gson().fromJson( profileData, AuthDataModel.class);
    }

    //Merchant Theme
    public static void setMerchantTheme(MerchantTheme merchantTheme){
        Pref.getPreference().putString(AppKeys.PREF_MERCHANT_THEME, new Gson().toJson(merchantTheme));
    }

    public static MerchantTheme getMerchantTheme(){
        String merchantThemeJson = Pref.getPreference().getString(AppKeys.PREF_MERCHANT_THEME);
        return new Gson().fromJson(merchantThemeJson, MerchantTheme.class);
    }

    //Feature Product
    public static void setFeatureProduct(FeatureProductData featureProductData){
        Pref.getPreference().putString(AppKeys.PREF_FEATURE_PRODUCT, new Gson().toJson(featureProductData));
    }

    public static FeatureProductData getFeatureProduct(){
        String featureProductJson = Pref.getPreference().getString(AppKeys.PREF_FEATURE_PRODUCT);
        return new Gson().fromJson(featureProductJson, FeatureProductData.class);
    }

    //Language Code
    public static void setLanguageCode(String code) {
        Pref.getPreference().putString(AppKeys.PREF_LANGUAGE_CODE, code);
    }

    public static String getLanguageCode() {
        return Pref.getPreference().getString(AppKeys.PREF_LANGUAGE_CODE);
    }

    public static String getProfileLogo() {
        return Pref.getPreference().getString(KEY_PROFILE_CARD_LOGO);
    }

    public static void setProfileLogo(String dashboardLogo) {
        Pref.getPreference().putString(KEY_PROFILE_CARD_LOGO, dashboardLogo);
    }

    public static void setProfileCardBg(String profileBackgroundImage) {
        Pref.getPreference().putString(KEY_PROFILE_CARD_BG, profileBackgroundImage);
    }

    public static String getProfileCardBg() {
        return Pref.getPreference().getString(KEY_PROFILE_CARD_BG);
    }
}
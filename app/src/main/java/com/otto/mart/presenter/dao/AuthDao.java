package com.otto.mart.presenter.dao;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.API;
import com.otto.mart.api.OttoKonekAPI;
import com.otto.mart.api.OttofinAPI;
import com.otto.mart.model.APIModel.Misc.AuthDataModel;
import com.otto.mart.model.APIModel.Request.CheckMerchantIdRequestModel;
import com.otto.mart.model.APIModel.Request.LoginOtpRequest;
import com.otto.mart.model.APIModel.Request.LoginOtpRequestModel;
import com.otto.mart.model.APIModel.Request.LoginQrRequestModel;
import com.otto.mart.model.APIModel.Request.LoginRequestModel;
import com.otto.mart.model.APIModel.Request.RegisterOtpRequestModel;
import com.otto.mart.model.APIModel.Request.RegisterUpgradeAccountRequestModel;
import com.otto.mart.model.APIModel.Request.ResendOtpRegisterRequestModel;
import com.otto.mart.model.APIModel.Request.ResetOtpPinRequestModel;
import com.otto.mart.model.APIModel.Request.ResetPinRequestModel;
import com.otto.mart.model.APIModel.Request.SignupRequestModel;
import com.otto.mart.model.APIModel.Request.UpdateStatusRequest;
import com.otto.mart.model.APIModel.Request.UserSecurityQuestionRequestModel;
import com.otto.mart.model.APIModel.Request.setting.LanguageSettingRequest;
import com.otto.mart.model.localmodel.Realm.LoginDatastoreModel;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.DataUtil;

import app.beelabs.com.codebase.base.BaseDao;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthDao extends BaseDao {

    public AuthDao(Object obj) {
        super(obj);
    }

//    public void requestTokenDao(TokenRequestModel requestModel, Callback callback) {
//        API.RequestTokenAPI(IndomarcoMobileApps.getContext(), requestModel, callback);
//    }

//    public void refreshTokenDao(TokenRefreshRequestModel requestModel, Callback callback) {
//        API.RefreshTokenAPI(IndomarcoMobileApps.getContext(), requestModel, callback);
//    }

    public void appClonerList(Callback callback) {
        OttofinAPI.AppClonerList(OttoMartApp.getContext(), callback);
    }

    public void loginDao(LoginRequestModel requestModel, Callback callback) {
        API.LoginAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void signupDao(SignupRequestModel requestModel, Callback callback) {
        API.SignUpAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void signUpDao(SignupRequestModel requestModel, Callback callback) {
        OttofinAPI.signUp(OttoMartApp.getContext(), requestModel, callback);
    }

    public void resendOtpLoginDao(ResendOtpRegisterRequestModel requestModel, Callback callback) {
        API.ResendOtpLoginAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void resendOtpDao(ResendOtpRegisterRequestModel requestModel, Callback callback) {
        OttoKonekAPI.resendOtp(OttoMartApp.getContext(), requestModel, callback);
    }

    public void reSignupOtpDao(ResendOtpRegisterRequestModel requestModel, Callback callback) {
        API.RequestRegisterOtpAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void resetDAO(ResetPinRequestModel requestModel, Callback callback) {
        API.ResetAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void forgotPinResetDAO(ResetPinRequestModel requestModel, Callback callback) {
        OttoKonekAPI.forgotPinReset(OttoMartApp.getContext(), requestModel, callback);
    }

    public void checkMerchantidDao(CheckMerchantIdRequestModel requestModel, Callback callback) {
        API.CheckMerchantidAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void searchMerchant(CheckMerchantIdRequestModel requestModel, Callback callback) {
        OttofinAPI.searchMerchant(OttoMartApp.getContext(), requestModel, callback);
    }

    public void getSecurityQuestionDao(Callback callback) {
        API.SecurityQuestionAPI(OttoMartApp.getContext(), callback);
    }

    public void getPhoneSecurityQustionDao(UserSecurityQuestionRequestModel requestModel, Callback callback) {
        API.getUserSecurityQuestionAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void forgotPinSecuritytQuestionDao(UserSecurityQuestionRequestModel requestModel, Callback callback) {
        OttofinAPI.forgotPinSecuritytQuestion(OttoMartApp.getContext(), requestModel, callback);
    }

    public void loginOtpDao(LoginOtpRequestModel requestModel, Callback callback) {
        API.LoginOtpAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void loginOttofinDao(LoginRequestModel requestModel, Callback callback) {
        OttoKonekAPI.login(OttoMartApp.getContext(), requestModel, callback);
    }

    public void loginOtpOttofinDao(LoginOtpRequest requestModel, Callback callback) {
        OttoKonekAPI.loginOTP(OttoMartApp.getContext(), requestModel, callback);
    }

    public void registerOtpDao(RegisterOtpRequestModel requestModel, Callback callback) {
        API.RegisterOtpAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void signUpOtpDao(RegisterOtpRequestModel requestModel, Callback callback) {
        OttofinAPI.signUpOtp(OttoMartApp.getContext(), requestModel, callback);
    }

    public void updateStatusDao(UpdateStatusRequest requestModel, Callback callback) {
        //API.UpdateStatusAPI(OttoMartApp.getContext(), requestModel, callback);
        OttoKonekAPI.updateStatus(OttoMartApp.getContext(), requestModel, callback);
    }

    public void upgradeAccountDao(RegisterUpgradeAccountRequestModel requestModel, Callback callback) {
        API.UpgradeAccountAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void merchantTheme(Callback callback) {
        OttoKonekAPI.merchantTheme(OttoMartApp.getContext(), callback);
    }

    public void featureProduct(Callback callback) {
        OttoKonekAPI.featureProduct(OttoMartApp.getContext(), callback);
    }

    public void languageSetting(LanguageSettingRequest languageSettingRequest, Callback callback) {
        OttofinAPI.languageSetting(OttoMartApp.getContext(), languageSettingRequest, callback);
    }

    public void requestResetPinOtpDao(ResetOtpPinRequestModel requestModel, Callback callback) {
        API.getResetPinOtpAPI(OttoMartApp.getContext(), requestModel, callback);
    }

    public void forgotPinOTP(ResetOtpPinRequestModel requestModel, Callback callback) {
        OttoKonekAPI.forgotPinOTP(OttoMartApp.getContext(), requestModel, callback);
    }
    public void getRegional(Callback callback) {
        OttoKonekAPI.getRegional(OttoMartApp.getContext(), callback);
    }


    public void requestLogout() {
        API.Logout(OttoMartApp.getContext(), new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void logout() {
        OttofinAPI.logout(OttoMartApp.getContext(), new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void requestPhoneNumDao(String merchId, Callback callback) {
        LoginQrRequestModel requestModel = new LoginQrRequestModel();
        requestModel.setMerchant_id(merchId);
        API.getPhone(OttoMartApp.getContext(), requestModel, callback);
    }

    public static void initSessionData(final AuthDataModel model) {
        DataUtil.querryRealm(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LoginDatastoreModel dbModel = new LoginDatastoreModel();
                dbModel.setSessionKey(0);
                dbModel.setUser_id(model.getUser_id());
                dbModel.setMerchant_id(model.getMerchant_id());
                dbModel.setAccess_token(model.getAccess_token());
                dbModel.setName(model.getName());
                dbModel.setMerchant_name(model.getMerchant_name());
                dbModel.setEmail(model.getEmail());
                dbModel.setAvatar(model.getAvatar());
                dbModel.setAvatar_thumb(model.getAvatar_thumb());
                dbModel.setPhone(model.getPhone());
                dbModel.setOmset_balance(model.getOmset_balance());
                dbModel.setWallet_balance(model.getWallet_balance());
                dbModel.setDaily_omset(model.getDaily_omset());
                dbModel.setWallet_id(model.getWallet_id());
                dbModel.setBusiness_category_name(model.getBusiness_category_name());
                dbModel.setAddress(model.getAddress());
                dbModel.setAddressCity(model.getAddressCity());
                dbModel.setAddressDistrict(model.getAddressDistrict());
                dbModel.setAddressProvince(model.getAddressProvince());
                dbModel.setAddressVillage(model.getAddressVillage());
                dbModel.setMpan(model.getMpan());

                //SharePreferences
                SessionManager.setUserProfile(dbModel);
            }
        });
    }

    public static void updateSessionData(final AuthDataModel model) {
        DataUtil.querryRealm(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //SharePreferences
                LoginDatastoreModel userProfile = SessionManager.getUserProfile();
                userProfile.setName(model.getName());
                userProfile.setMerchant_name(model.getMerchant_name());
                userProfile.setEmail(model.getEmail());
                userProfile.setAvatar(model.getAvatar());
                userProfile.setAvatar_thumb(model.getAvatar_thumb());
                userProfile.setSecondary_phone(model.getSecondary_phone());
                userProfile.setBusiness_category_name(model.getBusiness_category_name());

                SessionManager.setUserProfile(userProfile);
            }
        });
    }
}
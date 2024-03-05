package com.otto.mart.presenter.dao;

import android.content.Context;

import androidx.annotation.Nullable;

import com.otto.mart.GLOBAL;
import com.otto.mart.OttoMartApp;
import com.otto.mart.R;
import com.otto.mart.api.OttoPointAPI;
import com.otto.mart.model.APIModel.Request.ottopoint.OpCheckEligibleRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpEarningPointPulsaRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpRedeemVoucherDealsRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpRedeemVoucherSayaRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpRegisterRequestModel;
import com.otto.mart.model.APIModel.Request.ottopoint.OpVoucherComulativeRequestModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpBalancePointResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpCheckEligibleResponseModel;
import com.otto.mart.model.APIModel.Response.ottopoint.OpRegisterResponseModel;
import com.otto.mart.model.localmodel.Realm.LoginDatastoreModel;
import com.otto.mart.presenter.sessionManager.SessionManager;
import com.otto.mart.support.util.LogHelper;
import com.otto.mart.ui.actionView.HandleResponseApi;
import com.otto.mart.ui.actionView.HandleResponseApiTwo;
import com.otto.mart.ui.actionView.HandleResponseCheckRegistered;
import com.otto.mart.ui.actionView.ResponseBalance;
//import com.otto.ottosdk.BuildConfig;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.base.BaseDao;
import app.beelabs.com.codebase.base.response.BaseResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OttoPointDao extends BaseDao {

    private static String TAG = OttoPointDao.class.getSimpleName();

    public static final int ALL_SUCCESS = 0;
    public static final int SOME_SUCCESS = 1;
    public static final int ALL_FAILED = 2;

    public static final String ALL_SUCCESS_RESPONSE = "00"; // semua berhasil
    public static final String SOME_SUCCESS_RESPONSE = "33"; // beberapa ada yang gagal & ada yang berhasil
    public static final String ALL_FAILED_RESPONSE = "01"; // semua gagal

    public OttoPointDao(Object obj) {
        super(obj);
    }

    private void postCreateCustomer(BaseActivity baseActivity, OpRegisterRequestModel model, HandleResponseApi callback) {
        OttoPointAPI.opRegister(baseActivity, model, generateCallback(callback));
    }

    private void getEarningPoint(BaseActivity baseActivity, String uuid, HandleResponseApi callback) {
        OttoPointAPI.opEarningPoint(baseActivity, uuid, generateCallback(callback));
    }

    private void getBalancePoint(Context context, String phone, HandleResponseApiTwo callback) {
        OttoPointAPI.opBalancePoint(context, phone, generateCallback(callback));
    }

    private void postEarningPointFromPulsa(Context context, OpEarningPointPulsaRequestModel model, HandleResponseApi callback) {
        OttoPointAPI.opEarningPointFromPulsa(context, model, generateCallback(callback));
    }

    private void postCheckEligible(BaseActivity baseActivity, OpCheckEligibleRequestModel model, HandleResponseApi callback) {
        OttoPointAPI.opCheckEligible(baseActivity, model, generateCallback(callback));
    }

    private void getHistoryTransaction(BaseActivity baseActivity, HandleResponseApi callback) {
        OttoPointAPI.opHistoryTransaction(baseActivity, generateCallback(callback));
    }

    private void getVoucherSayaActive(BaseActivity baseActivity, int page, String harga, String periode, HandleResponseApi callback) {
        OttoPointAPI.opVoucherSayaActive(baseActivity, page, harga, periode, generateCallback(callback));
    }

    private void getVoucherSayaHistory(BaseActivity baseActivity, int page, String harga, String periode, HandleResponseApi callback) {
        OttoPointAPI.opVoucherSayaHistory(baseActivity, page, harga, periode, generateCallback(callback));
    }

    private void getVoucherSayaDetail(BaseActivity baseActivity, String phone, HandleResponseApi callback) {
        OttoPointAPI.opVoucherSayaDetail(baseActivity, phone, generateCallback(callback));
    }

    private void getVoucherDeals(BaseActivity baseActivity, int page, String campaignName, String campaignType, String harga, String periode, String kadaluarsa, boolean isPromo, long minHarga, long maxHarga, HandleResponseApi callback) {
        OttoPointAPI.opVoucherDeals(baseActivity, page, campaignName, campaignType, harga, periode, kadaluarsa, isPromo, minHarga, maxHarga, generateCallback(callback));
    }

    private void getVoucherDeals(BaseActivity baseActivity, int page, String campaignName, String campaignType, String harga, String periode, String kadaluarsa, boolean isPromo, HandleResponseApi callback) {
        OttoPointAPI.opVoucherDeals(baseActivity, page, campaignName, campaignType, harga, periode, kadaluarsa, isPromo, generateCallback(callback));
    }

    private void getVoucherDealsPromo(BaseActivity baseActivity, int page, String campaignName, String campaignType, String harga, String periode, String kadaluarsa, boolean isPromo, long minHarga, long maxHarga, HandleResponseApi callback) {
        OttoPointAPI.opVoucherDealsPromo(baseActivity, page, campaignName, campaignType, harga, periode, kadaluarsa, isPromo, minHarga, maxHarga, generateCallback(callback));
    }

    private void getVoucherDealsPromo(BaseActivity baseActivity, int page, String campaignName, String campaignType, String harga, String periode, String kadaluarsa, boolean isPromo, HandleResponseApi callback) {
        OttoPointAPI.opVoucherDealsPromo(baseActivity, page, campaignName, campaignType, harga, periode, kadaluarsa, isPromo, generateCallback(callback));
    }

    private void getVoucherDealsDetail(BaseActivity baseActivity, String campaignId, HandleResponseApi callback) {
        OttoPointAPI.opVoucherDealsDetail(baseActivity, campaignId, generateCallback(callback));
    }

    private void postVoucherDealsRedeem(BaseActivity baseActivity, OpRedeemVoucherDealsRequestModel model, HandleResponseApi callback) {
        OttoPointAPI.opVoucherDealsRedeem(baseActivity, model, generateCallback(callback));
    }

    private void postVoucherSayaRedeem(BaseActivity baseActivity, OpRedeemVoucherSayaRequestModel model, HandleResponseApiTwo callback) {
        OttoPointAPI.opVoucherSayaRedeem(baseActivity, model, generateCallback(callback));
    }

    private void postVoucherComulative(BaseActivity baseActivity, OpVoucherComulativeRequestModel model, HandleResponseApi callback) {
        OttoPointAPI.opVoucherComulative(baseActivity, model, generateCallback(callback));
    }

    /*
        Public
     */

    public static void createAccountOttopoint(BaseActivity activity, HandleResponseApi callback) {
        LoginDatastoreModel profile = SessionManager.getUserProfile();
        if (profile == null) return;

        // prepare data
        String[] splitName = profile.getName().split(" ");
        String firstname = splitName[0];
        StringBuilder lastname = new StringBuilder();
        if (splitName.length > 1)
            for (int i = 1; i < splitName.length; i++)
                lastname.append(splitName[i]).append(" ");
        else
            lastname.append("-");

        // start action
        new OttoPointDao(activity)
                .postCreateCustomer(
                        activity,
                        new OpRegisterRequestModel(
                                firstname,
                                lastname.toString(),
                                "",
                                SessionManager.getPhone(),
                                ""
                        ),
                        new HandleResponseApi() {
                            @Override
                            public void resultApiSuccess(BaseResponse br, Response response) {
                                if (!handleResponse(response, br, callback)) return;

                                // set current customer id ottopoint
                                if (br instanceof OpRegisterResponseModel) {
                                    OpRegisterResponseModel result = (OpRegisterResponseModel) br;

                                    if (result.getData() == null || result.getData().getCustomerId() == null || (result.getData().getCustomerId() != null && result.getData().getCustomerId().isEmpty())) {
                                        callback.resultApiFailed("Customer id kosong!", response.code());
                                        return;
                                    }

                                    SessionManager.setOttopointIsExist(true);
                                }

                                callback.resultApiSuccess(br, response);
                            }

                            @Override
                            public void resultApiFailed(String message, int responseCodeHttp) {
                                String messageText = activity != null ? activity.getString(R.string.text_failed_registration) : message;
                                callback.resultApiFailed(messageText, responseCodeHttp);
                            }
                        }
                );
    }

    public static void earningPointFromPulsa(Context context, String productCode, String rc, HandleResponseApi callback) {
        new OttoPointDao(context)
                .postEarningPointFromPulsa(context, new OpEarningPointPulsaRequestModel(productCode, rc), callback);
    }

    public static void getBalance(Context context, ResponseBalance callback) {
        if (context == null) return;

        new OttoPointDao(context)
                .getBalancePoint(context, SessionManager.getPhone(), new HandleResponseApiTwo() {
                    @Override
                    public void resultApiSuccess(BaseResponse br, Response response) {
                        if (!handleResponse(response, br, callback)) return;

                        double point = 0;
                        if (br instanceof OpBalancePointResponseModel) {
                            OpBalancePointResponseModel result = (OpBalancePointResponseModel) br;

                            if (result.getData() != null)
                                point = Double.parseDouble(result.getData().getPoints());
                        }
                        SessionManager.setOttopointPoint(point);

                        callback.result(point, br.getBaseMeta().getCode());
                    }

                    @Override
                    public void resultApiFailed(String message, int responseCodeHttp, int metaCode) {
                        callback.result(0, metaCode);
                    }
                });
    }

    public static void checUserRegistered(Context context, HandleResponseCheckRegistered callback) {
        if (context == null) return;

        new OttoPointDao(context)
                .getBalancePoint(context, SessionManager.getPhone(), new HandleResponseApiTwo() {
                    @Override
                    public void resultApiSuccess(BaseResponse br, Response response) {
                        if (isResponseCodeFailed(response)) {
                            // do something when token is expired or wrong
                            LogHelper.showError(OttoPointDao.class.getSimpleName(), "handleResponse: " + generateErrorMessage(response.code()));

                            //SessionManager.setOttopointIsExist(false);
                            callback.resultCheck(SessionManager.isOttopointAuthExist(), -1);
                            return;
                        }

                        double point = 0;
                        boolean isRegistered = false;
                        boolean isServerError = false;
                        if (br instanceof OpBalancePointResponseModel) {
                            OpBalancePointResponseModel result = (OpBalancePointResponseModel) br;

                            if (result.getBaseMeta().getCode() == 200)
                                isRegistered = true;
                            else if (result.getBaseMeta().getCode() != 201) {
                                isRegistered = SessionManager.isOttopointAuthExist();
                                isServerError = true;
                            }

                            if (result.getData() != null)
                                point = Double.parseDouble(result.getData().getPoints());
                        }
                        SessionManager.setOttopointIsExist(isRegistered);

                        if (!isServerError && isRegistered)
                            SessionManager.setOttopointPoint(point);

                        callback.resultCheck(isRegistered, point);
                    }

                    @Override
                    public void resultApiFailed(String message, int responseCodeHttp, int metaCode) {
                        LogHelper.showError(TAG, message);
                        //SessionManager.setOttopointIsExist(false);
                        callback.resultCheck(SessionManager.isOttopointAuthExist(), -1);
                    }
                });
    }

    public static void checkEligiblePhone(BaseActivity baseActivity, HandleResponseApiTwo callback) {
        if (baseActivity == null) return;

        new OttoPointDao(baseActivity)
                .postCheckEligible(baseActivity, new OpCheckEligibleRequestModel(SessionManager.getPhone()), new HandleResponseApi() {
                    @Override
                    public void resultApiSuccess(BaseResponse br, Response response) {
                        if (!handleResponseTwo(response, br, callback)) return;

                        if (br instanceof OpCheckEligibleResponseModel) {
                            OpCheckEligibleResponseModel result = (OpCheckEligibleResponseModel) br;
                        }

                        callback.resultApiSuccess(br, response);
                    }

                    @Override
                    public void resultApiFailed(String message, int responseCodeHttp) {
                        callback.resultApiFailed(message, responseCodeHttp, responseCodeHttp);
                    }
                });
    }

    public static void historyTransaction(BaseActivity baseActivity, HandleResponseApi callback) {
        if (baseActivity == null) return;

        new OttoPointDao(baseActivity)
                .getHistoryTransaction(baseActivity, new HandleResponseApi() {
                    @Override
                    public void resultApiSuccess(BaseResponse br, Response response) {
                        if (!handleResponse(response, br, callback)) return;

                        callback.resultApiSuccess(br, response);
                    }

                    @Override
                    public void resultApiFailed(String message, int responseCodeHttp) {
                        callback.resultApiFailed(message, responseCodeHttp);
                    }
                });
    }

    public static void voucherSayaActive(BaseActivity baseActivity, int page, String sort, HandleResponseApi callback) {
        if (baseActivity == null) return;

        if (page == -1) page = 1;
        if (sort == null) sort = "";

        String harga = converValueOrderDeals(sort, 0);
        String periode = converValueOrderDeals(sort, 1);

        new OttoPointDao(baseActivity)
                .getVoucherSayaActive(baseActivity, page, harga, periode, new HandleResponseApi() {
                    @Override
                    public void resultApiSuccess(BaseResponse br, Response response) {
                        if (!handleResponse(response, br, callback)) return;

                        callback.resultApiSuccess(br, response);
                    }

                    @Override
                    public void resultApiFailed(String message, int responseCodeHttp) {
                        callback.resultApiFailed(message, responseCodeHttp);
                    }
                });
    }

    public static void voucherSayaHistory(BaseActivity baseActivity, int page, String sort, HandleResponseApi callback) {
        if (baseActivity == null) return;

        if (page == -1) page = 1;
        if (sort == null) sort = "";

        String harga = converValueOrderDeals(sort, 0);
        String periode = converValueOrderDeals(sort, 1);

        new OttoPointDao(baseActivity)
                .getVoucherSayaHistory(baseActivity, page, harga, periode, new HandleResponseApi() {
                    @Override
                    public void resultApiSuccess(BaseResponse br, Response response) {
                        if (!handleResponse(response, br, callback)) return;

                        callback.resultApiSuccess(br, response);
                    }

                    @Override
                    public void resultApiFailed(String message, int responseCodeHttp) {
                        callback.resultApiFailed(message, responseCodeHttp);
                    }
                });
    }

    public static void voucherSayaDetail(BaseActivity baseActivity, String campaignId, HandleResponseApi callback) {
        if (baseActivity == null) return;

        new OttoPointDao(baseActivity)
                .getVoucherSayaDetail(baseActivity, campaignId, new HandleResponseApi() {
                    @Override
                    public void resultApiSuccess(BaseResponse br, Response response) {
                        if (!handleResponse(response, br, callback)) return;

                        callback.resultApiSuccess(br, response);
                    }

                    @Override
                    public void resultApiFailed(String message, int responseCodeHttp) {
                        callback.resultApiFailed(message, responseCodeHttp);
                    }
                });
    }

    public static void voucherDeals(BaseActivity baseActivity, int page, String sort, String campaignName, String campaignType, boolean isPromo, long minHarga, long maxHarga, boolean isUseVoucherDealsPromo, HandleResponseApi callback) {
        if (baseActivity == null) return;

        if (page == -1) page = 1;
        if (sort == null) sort = "";

        String harga = converValueOrderDeals(sort, 0);
        String periode = converValueOrderDeals(sort, 1);
        String kadaluarsa = converValueOrderDeals(sort, 2);

        OttoPointDao dao = new OttoPointDao(baseActivity);
        HandleResponseApi handleResponseApi = new HandleResponseApi() {
            @Override
            public void resultApiSuccess(BaseResponse br, Response response) {
                if (!handleResponse(response, br, callback)) return;

                callback.resultApiSuccess(br, response);
            }

            @Override
            public void resultApiFailed(String message, int responseCodeHttp) {
                callback.resultApiFailed(message, responseCodeHttp);
            }
        };

        if (!isUseVoucherDealsPromo) {
            if (maxHarga == 0)
                dao.getVoucherDeals(baseActivity, page, campaignName, campaignType, harga, periode, kadaluarsa, isPromo, handleResponseApi);
            else
                dao.getVoucherDeals(baseActivity, page, campaignName, campaignType, harga, periode, kadaluarsa, isPromo, minHarga, maxHarga, handleResponseApi);
        } else {
            if (maxHarga == 0)
                dao.getVoucherDealsPromo(baseActivity, page, campaignName, campaignType, harga, periode, kadaluarsa, isPromo, handleResponseApi);
            else
                dao.getVoucherDealsPromo(baseActivity, page, campaignName, campaignType, harga, periode, kadaluarsa, isPromo, minHarga, maxHarga, handleResponseApi);
        }
    }

    public static void voucherDealsDetail(BaseActivity baseActivity, String campaignId, HandleResponseApi callback) {
        if (baseActivity == null) return;

        new OttoPointDao(baseActivity)
                .getVoucherDealsDetail(baseActivity, campaignId, new HandleResponseApi() {
                    @Override
                    public void resultApiSuccess(BaseResponse br, Response response) {
                        if (!handleResponse(response, br, callback)) return;

                        callback.resultApiSuccess(br, response);
                    }

                    @Override
                    public void resultApiFailed(String message, int responseCodeHttp) {
                        callback.resultApiFailed(message, responseCodeHttp);
                    }
                });
    }

    public static void voucherDealsRedeem(BaseActivity baseActivity, OpRedeemVoucherDealsRequestModel model, HandleResponseApi callback) {
        if (baseActivity == null) return;

        new OttoPointDao(baseActivity)
                .postVoucherDealsRedeem(baseActivity, model, new HandleResponseApi() {
                    @Override
                    public void resultApiSuccess(BaseResponse br, Response response) {
                        if (!handleResponse(response, br, callback)) return;

                        callback.resultApiSuccess(br, response);
                    }

                    @Override
                    public void resultApiFailed(String message, int responseCodeHttp) {
                        callback.resultApiFailed(message, responseCodeHttp);
                    }
                });
    }

    public static void voucherSayaRedeem(BaseActivity baseActivity, OpRedeemVoucherSayaRequestModel model, HandleResponseApiTwo callback) {
        if (baseActivity == null) return;

        new OttoPointDao(baseActivity)
                .postVoucherSayaRedeem(baseActivity, model, new HandleResponseApiTwo() {
                    @Override
                    public void resultApiSuccess(BaseResponse br, Response response) {
                        if (!handleResponseTwo(response, br, callback)) return;

                        callback.resultApiSuccess(br, response);
                    }

                    @Override
                    public void resultApiFailed(String message, int responseCodeHttp, int metaCode) {
                        callback.resultApiFailed(message, responseCodeHttp, metaCode);
                    }
                });
    }

    public static void voucherComulative(BaseActivity baseActivity, OpVoucherComulativeRequestModel model, HandleResponseApi callback) {
        if (baseActivity == null) return;

        new OttoPointDao(baseActivity)
                .postVoucherComulative(baseActivity, model, new HandleResponseApi() {
                    @Override
                    public void resultApiSuccess(BaseResponse br, Response response) {
                        if (!handleResponse(response, br, callback)) return;

                        callback.resultApiSuccess(br, response);
                    }

                    @Override
                    public void resultApiFailed(String message, int responseCodeHttp) {
                        callback.resultApiFailed(message, responseCodeHttp);
                    }
                });
    }

    public static int getStatusCodeBuyAndUse(String value) {
        switch (value) {
            case ALL_FAILED_RESPONSE:
                return ALL_FAILED;

            case ALL_SUCCESS_RESPONSE:
                return ALL_SUCCESS;

            case SOME_SUCCESS_RESPONSE:
                return SOME_SUCCESS;

            default:
                return -1;
        }
    }

    /*
        Private
     */

    private static boolean handleResponse(Response response, BaseResponse baseResponse, HandleResponseApi callback) {
        if (isResponseCodeFailed(response)) {
            // do something when token is expired or wrong
            if (response.code() == GLOBAL.HTTP_CODE_UNAUTHORIZE) {

            } else
                LogHelper.showError(OttoPointDao.class.getSimpleName(), "handleResponse 1: " + generateErrorMessage(response.code()));

            callback.resultApiFailed(buildDefaultMessage(response.message()), response.code());
            return false;
        }

        // check if meta status is false
        if (baseResponse != null) {
            if (!baseResponse.getBaseMeta().isStatus()) {
                String message = baseResponse.getBaseMeta().getMessage() != null ?
                        baseResponse.getBaseMeta().getMessage() :
                        OttoMartApp.getContext().getString(R.string.error_request);

                callback.resultApiFailed(message, response.code());
                return false;
            }
        } else
            callback.resultApiFailed(OttoMartApp.getContext().getString(R.string.error_request), response.code());

        return true;
    }

    private static boolean handleResponseTwo(Response response, BaseResponse baseResponse, HandleResponseApiTwo callback) {
        if (isResponseCodeFailed(response)) {
            // do something when token is expired or wrong
            if (response.code() == GLOBAL.HTTP_CODE_UNAUTHORIZE) {

            } else
                LogHelper.showError(OttoPointDao.class.getSimpleName(), "handleResponse 1: " + generateErrorMessage(response.code()));

            callback.resultApiFailed(buildDefaultMessage(response.message()), response.code(), response.code());
            return false;
        }

        // check if meta status is false
        if (baseResponse != null) {
            if (!baseResponse.getBaseMeta().isStatus()) {
                String message = baseResponse.getBaseMeta().getMessage() != null ?
                        baseResponse.getBaseMeta().getMessage() :
                        OttoMartApp.getContext().getString(R.string.error_request);

                callback.resultApiFailed(message, response.code(), baseResponse.getBaseMeta().getCode());
                return false;
            }
        } else
            callback.resultApiFailed(OttoMartApp.getContext().getString(R.string.error_request), response.code(), response.code());

        return true;
    }

    private static boolean handleResponse(Response response, BaseResponse baseResponse, ResponseBalance callback) {
        if (isResponseCodeFailed(response)) {
            // do something when token is expired or wrong
            if (response.code() == GLOBAL.HTTP_CODE_UNAUTHORIZE) {

            } else
                LogHelper.showError(OttoPointDao.class.getSimpleName(), "handleResponse 2: " + generateErrorMessage(response.code()));

            callback.result(0, baseResponse.getBaseMeta().getCode());
            return false;
        }

        // check if meta status is false
        if (baseResponse != null) {
            if (!baseResponse.getBaseMeta().isStatus()) {
                callback.result(0, baseResponse.getBaseMeta().getCode());
                return false;
            }
        }

        return true;
    }

    private static boolean isResponseCodeFailed(Response response) {
        return !response.isSuccessful() && response.code() != GLOBAL.HTTP_CODE_CREATED && response.code() != GLOBAL.HTTP_CODE_SUCCESS;
    }

    private static int generateErrorMessage(int responseCode) {
        int message;
        if (responseCode == GLOBAL.HTTP_CODE_UNAUTHORIZE) {
            message = R.string.error_unauthorize;
        } else if (responseCode == GLOBAL.HTTP_CODE_FORBIDEN) {
            message = R.string.error_forbiden;
        } else if (responseCode == GLOBAL.HTTP_CODE_NOTFOUND) {
            message = R.string.error_notfound;
        } else {
            message = R.string.error_request;
        }

        return message;
    }

    private static String converValueOrderDeals(String sort, int type) {
        String[] sorts = sort.split(" ");

        if (type == 0 && sort.contains("Point")) {
            return sorts.length > 1 ? sorts[1].replace("Terendah", "Termurah").replace("Tertinggi", "Termahal") : "";
        } else if (type == 1 && sorts.length == 1) {
            return sort;
        } else if (type == 2 && sort.contains("Kadaluarsa")) {
            return sorts.length > 1 ? sorts[1] : "";
        } else
            return "";
    }

    private static String buildDefaultMessage(String responseMessage) {
        String baseUrl = "https://apisfa3.ottopay.id/api/"
                .replace("https://", "")
                .replace("http://", "")
                .replace("/", "")
                .replace("api", "");

        return responseMessage.contains(baseUrl) || responseMessage.isEmpty() ? "Failed to connect with server" : responseMessage;
    }

    private Callback generateCallback(HandleResponseApi callbackUI) {
        return new Callback() {
            @Override
            public void onResponse(@Nullable Call call, @Nullable Response response) {
                BaseResponse baseResponse = response != null ? (BaseResponse) response.body() : null;
                callbackUI.resultApiSuccess(baseResponse, response);
            }

            @Override
            public void onFailure(@Nullable Call call, @Nullable Throwable t) {
                String message = t != null ? t.getMessage() : "Something went wrong!";
                callbackUI.resultApiFailed(buildDefaultMessage(message), -1);
            }
        };
    }

    private Callback generateCallback(HandleResponseApiTwo callbackUI) {
        return new Callback() {
            @Override
            public void onResponse(@Nullable Call call, @Nullable Response response) {
                if (response.isSuccessful() && response != null) {
                    BaseResponse baseResponse = response != null ? (BaseResponse) response.body() : null;
                    callbackUI.resultApiSuccess(baseResponse, response);
                }
            }

            @Override
            public void onFailure(@Nullable Call call, @Nullable Throwable t) {
                String message = t != null ? t.getMessage() : "Something went wrong!";
                callbackUI.resultApiFailed(buildDefaultMessage(message), -1, -1);
            }
        };
    }
}

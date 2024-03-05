package com.otto.mart.presenter.dao.refund;

import android.content.Context;

import com.otto.mart.OttoMartApp;
import com.otto.mart.api.OttofinAPI;
import com.otto.mart.model.APIModel.Request.refund.MerchantRefundRequest;

import app.beelabs.com.codebase.base.BaseDao;
import retrofit2.Callback;
import retrofit2.http.Body;

public class RefundDao extends BaseDao {

    private Context context;

    public RefundDao(Object obj) {
        super(obj);
    }

    public RefundDao(Object obj, Context context) {
        super(obj);
        this.context = context;
    }



    /**
     * Refund
     */

    //region Refund

    //Check Refund Status
    public void checkRefundStatus(@Body MerchantRefundRequest merchantRefundRequest, Callback callback) {
        OttofinAPI.checkRefundStatus(OttoMartApp.getContext(), merchantRefundRequest, callback);
    }

    //Merchant Refund
    public void merchantRefund(MerchantRefundRequest merchantRefundRequest, Callback callback) {
        OttofinAPI.merchantRefund(OttoMartApp.getContext(), merchantRefundRequest, callback);
    }

    //endregion Refund

}

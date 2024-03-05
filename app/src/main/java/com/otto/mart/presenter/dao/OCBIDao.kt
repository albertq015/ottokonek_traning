package com.otto.mart.presenter.dao

import app.beelabs.com.codebase.base.BaseDao
import com.otto.mart.OttoMartApp
import com.otto.mart.api.API
import com.otto.mart.api.OttofinAPI
import com.otto.mart.model.APIModel.Request.kyc.KycUploadImageRequest
import com.otto.mart.model.APIModel.Request.payment.AlfamartTokenRequest
import retrofit2.Callback

class OCBIDao(ac: Any) : BaseDao(ac) {

    fun getProfile(callback: Callback<*>?) {
        OttofinAPI.merchantStatus(OttoMartApp.getContext(), callback)
    }

    fun getBalance(callback: Callback<*>?) {
        OttofinAPI.getBalance(OttoMartApp.getContext(), callback)
    }

    fun uploadImageKyc(reqUploadImage: KycUploadImageRequest, callback: Callback<*>?) {
        OttofinAPI.uploadImageKyc(OttoMartApp.getContext(), reqUploadImage, callback)
    }

    fun getOttocashStatus(callback: Callback<*>?) {
        OttofinAPI.getOttocashStatus(OttoMartApp.getContext(), callback)
    }

    fun confirmTNCOCBI(callback: Callback<*>?) {
        OttofinAPI.confirmTNCOCBI(OttoMartApp.getContext(), callback)
    }

    fun alfamartToken(alfamartTokenRequest: AlfamartTokenRequest, callback: Callback<*>?) {
        OttofinAPI.alfamartToken(OttoMartApp.getContext(), alfamartTokenRequest, callback)
    }

    fun contactUs(callback: Callback<*>?) {
        OttofinAPI.contactUs(OttoMartApp.getContext(), callback)
    }
}
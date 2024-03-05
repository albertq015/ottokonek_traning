package com.otto.mart.presenter.dao.ottopoin

import android.util.Patterns
import com.google.gson.Gson
import com.otto.mart.model.localmodel.ottopoin.CheckBalanceOttoPoin
import com.otto.mart.model.localmodel.ottopoin.OttoPoinLogin
import com.otto.mart.model.localmodel.ottopoin.OttoPoinLoginResponse
import com.otto.mart.model.localmodel.ottopoin.OttoPoinRegister
import com.otto.mart.presenter.sessionManager.SessionManager


class OttoPoinDao {
//
//    companion object {
//        const val REGISTER_KEY = "eligible"
//        const val ACTIVATE_KEY = "unlinked"
//        const val NOT_ELIGIBLE_KEY = "not eligible"
//        const val START_SDK_KEY = "linked"
//    }
//
//    private var balanceOttoPoin: CheckBalanceOttoPoin? = null
//    private var poinLoginResponse: OttoPoinLoginResponse? = null
//    private lateinit var auth: OttoPoinLogin
//    private var sdk: OttoSDK? = null
//    private lateinit var listener: OttoSDKInterface
//
//    /*fun init(callback: OttoSDKInterface) {
//        *//*listener = callback
//        auth = OttoPoinLogin(
//                SessionManager.getPhone(),
//                "2h",
//                BuildConfig.POIN_INSTITUSION_ID,
//                "MOBILE",
//                BuildConfig.POIN_APP_ID,
//                SystemUtil.getDeviceId(OttoMartApp.getContext()),
//                "")
//
//        sdk = OttoSDK(OttoMartApp.getContext(), this, BuildConfig.POIN_KEY)*//*
//    }*/
//
//    fun callAction(latlong: String) {
//        val authData = auth.apply { geolocation = latlong }.getJson()
//        //sdk?.startSDK(getOttoPoinAction(), authData, getJsonRegister())
//    }
//
//    fun callAction(latlong: String, directAction: Int) {
//        val authData = auth.apply { geolocation = latlong }.getJson()
//        //sdk?.startSDK(directAction, authData, getJsonRegister())
//    }
//
//    private fun getOttoPoinAction(): Int {
//        poinLoginResponse?.let {
//            when {
//                it.accountStatus.equals(REGISTER_KEY, true) -> return OttoSDK.OTTOPOINT_SDK_REGISTER
//                it.accountStatus.equals(ACTIVATE_KEY, true) -> return OttoSDK.OTTOPOINT_SDK_ACTIVATE
//                it.accountStatus.equals(START_SDK_KEY, true) -> return OttoSDK.OTTOPOINT_SDK_ELIGIBLE
//                else -> -1
//            }
//        } ?: return OttoSDK.OTTOPOINT_SDK_BALANCE
//
//        return -1
//    }
//
//    private fun getJsonRegister(): String {
//        poinLoginResponse?.let {
//            if (it.accountStatus.equals(REGISTER_KEY, true)) {
//                return OttoPoinRegister(
//                        SessionManager.getMerchantName(),
//                        ".",
//                        SessionManager.getPhone(),
//                        getMerchantEmail(),
//                        "",
//                        "", "2h"
//                ).getJson()
//            }
//        }
//        return ""
//    }
//
//    private fun getMerchantEmail(): String {
//        if (SessionManager.getMailAddress() != null && SessionManager.getMailAddress().isNotEmpty()) {
//            if (Patterns.EMAIL_ADDRESS.matcher(SessionManager.getMailAddress()).matches()) {
//                return SessionManager.getMailAddress()
//            }
//        }
//        return ""
//    }
//
//    fun clear() {
//        sdk = null
//        poinLoginResponse = null
//    }
//
//    override fun callbackSDKClosed(response: String?) {
//        try {
//            poinLoginResponse = Gson().fromJson(response, OttoPoinLoginResponse::class.java)
//            poinLoginResponse?.let { listener.callbackSDKClosed(it.accountStatus) }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    override fun callbackBalance(response: String?) {
//        try {
//            balanceOttoPoin = Gson().fromJson(response, CheckBalanceOttoPoin::class.java)
//            balanceOttoPoin?.let { listener.callbackBalance(it.points) }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    override fun callbackEligible(response: String?) {
//
//    }
//    override fun callbackLoadData(response: String?) {
//        try {
//            balanceOttoPoin = Gson().fromJson(response, CheckBalanceOttoPoin::class.java)
//            balanceOttoPoin?.let { listener.callbackBalance(it.points) }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
}

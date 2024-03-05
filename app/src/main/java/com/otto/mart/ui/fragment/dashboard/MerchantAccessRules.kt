package com.otto.mart.ui.fragment.dashboard

import android.se.omapi.Session
import com.otto.mart.BuildConfig
import com.otto.mart.keys.AppKeys
import com.otto.mart.presenter.sessionManager.SessionManager

class MerchantAccessRules {

    companion object {
        val KEY_MERCHANT_GROUP_OP = "OP"
        val KEY_MERCHANT_GROUP_PGMI = "PGMI"

        val KEY_MERCHANT_LEVEL_GOLD = "Gold"
        val KEY_MERCHANT_LEVEL_SILVER = "Silver"
    }

    var mMerchantGroup = ""
    var mMerchantLevel = ""

//    Group Merchant OP
//    - Silver --> Menu Isi Ulang dan Tagihan, OttoPoint
//
//    Group Merchant PGMI
//    - Gold --> Menu Isi Ulang dan Tagihan, OttoPoint, Toko Online, Toko Grosir

    init {
        mMerchantGroup = SessionManager.getPrefLogin().category
        mMerchantLevel = SessionManager.getPrefLogin().memberType

        if (BuildConfig.FLAVOR.equals(AppKeys.FLAVOR_DEVELOPMENT)) {
            mMerchantGroup = "pgmi"
            mMerchantLevel = "gold"
        }
    }

    fun ottoPoint() : Boolean {
        var status = false

        //OP
        if (mMerchantGroup.equals(KEY_MERCHANT_GROUP_OP, true)) {
            if (mMerchantLevel.equals(KEY_MERCHANT_LEVEL_SILVER, true)) {
                status = true
            }
        }

        //PGMI
        if (mMerchantGroup.equals(KEY_MERCHANT_GROUP_PGMI, true)) {
            if (mMerchantLevel.equals(KEY_MERCHANT_LEVEL_GOLD, true)) {
                status = true
            }
        }

        return status
    }

//    fun ppob() : Boolean {
//        var status = false
//
//        //OP
//        if (mMerchantGroup.equals(KEY_MERCHANT_GROUP_OP, true)) {
//            if (mMerchantLevel.equals(KEY_MERCHANT_LEVEL_SILVER, true)) {
//                status = true
//            }
//        }
//
//        //PGMI
//        if (mMerchantGroup.equals(KEY_MERCHANT_GROUP_PGMI, true)) {
//            if (mMerchantLevel.equals(KEY_MERCHANT_LEVEL_GOLD, true)) {
//                status = true
//            }
//        }
//
//        return status
//    }

//    fun oasis() : Boolean {
//        var status = false
//
//        //PGMI
//        if (mMerchantGroup.equals(KEY_MERCHANT_GROUP_PGMI, true)) {
//            if (mMerchantLevel.equals(KEY_MERCHANT_LEVEL_GOLD, true)) {
//                status = true
//            }
//        }
//
//        return status
//    }

    fun tokoOnline() : Boolean {
        var status = false

        //PGMI
        if (mMerchantGroup.equals(KEY_MERCHANT_GROUP_PGMI, true)) {
            if (mMerchantLevel.equals(KEY_MERCHANT_LEVEL_GOLD, true)) {
                status = true
            }
        }

        return status
    }
}
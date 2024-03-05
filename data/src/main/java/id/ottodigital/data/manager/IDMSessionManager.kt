package id.ottodigital.data.manager

import id.ottodigital.data.pref.PrefHelper
import javax.inject.Inject

class IDMSessionManager @Inject constructor(val prefHelper: PrefHelper) {

    var idmToken: String? = "indomarcoAuth"
        set(value) = prefHelper.putString(field,value)
        get() = prefHelper.getString(field)

    var idmUserID: String? = "indomarcoUserId"
        set(value) = prefHelper.putString(field,value)
        get() = prefHelper.getString(field)

    var idmShippingCode: String? = "shippingCode"
        set(value) = prefHelper.putString(field,value)
        get() = prefHelper.getString(field)

    var idmPhone: String? = "indophone"
        set(value) = prefHelper.putString(field,value)
        get() = prefHelper.getString(field)

    var idmName: String? = "indoname"
        set(value) = prefHelper.putString(field,value)
        get() = prefHelper.getString(field)

    var idmLocationCode: String? = "locationCode"
        set(value) = prefHelper.putString(field,value)
        get() = prefHelper.getString(field)

    var idmCustId: String? = "indoId"
        set(value) = prefHelper.putString(field,value)
        get() = prefHelper.getString(field)
}
package id.ottodigital.data.helper

import id.ottodigital.core.DeviceModule
import id.ottodigital.data.pref.PrefHelper
import javax.inject.Inject
import javax.inject.Named

class AuthHeader @Inject constructor(
        val prefHelper: PrefHelper,
        @Named(DeviceModule.DEVICE_ID) val deviceId: String) {

    var authKey = ""

    val auth: String
        get() = prefHelper.getString(authKey) ?: ""

    val lang: String
        get() = prefHelper.getString("defLang")

}
package id.ottodigital.data.repo

import id.ottodigital.data.helper.AuthHeader
import javax.inject.Inject

abstract class BaseRepo {

    @Inject
    lateinit var authHeader: AuthHeader
    val auth: HashMap<String, String>
        get() {
            authHeader.authKey = getAuthKey()
            return hashMapOf<String, String>().apply {
                put("Authorization", "Bearer ${authHeader.auth}")
                put("Device-Id", authHeader.deviceId)
            }
        }

    abstract fun getAuthKey(): String
}
package id.ottodigital.data.manager

import id.ottodigital.data.pref.PrefHelper
import javax.inject.Inject

class OPSessionManager @Inject constructor(val prefHelper: PrefHelper) {

    var opToken: String? = "opToken"
        set(value) = prefHelper.putString(field, value)
        get() = prefHelper.getString(field)
}
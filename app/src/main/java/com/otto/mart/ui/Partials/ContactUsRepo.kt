package com.otto.mart.ui.Partials

import com.google.gson.Gson
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Response.ContactUsResponse
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.pref.Pref
import java.util.*

class ContactUsRepo {
    fun getContactUs(listener: (ContactUsResponse.ContactUsData?) -> Unit) {
        val data = Pref.getPreference().getString(AppKeys.PREF_CONTACT_US)
        if (!data.isNullOrBlank() && isNotExpired() && data != "null") {
            listener(Gson().fromJson(data, ContactUsResponse.ContactUsData::class.java))
        } else {
            getContactUsFromApi(listener)
        }
    }

    private fun isNotExpired(): Boolean {
        val expiredTimePref = Pref.getPreference().getLong(AppKeys.PREF_CONTACT_US_TIME)
        if (expiredTimePref > 0) {
            val exp = Calendar.getInstance().apply { timeInMillis = expiredTimePref }
            return Calendar.getInstance().before(exp)
        }
        return false
    }

    private fun getContactUsFromApi(listener: (ContactUsResponse.ContactUsData?) -> Unit) {
        OttoKonekAPI.contactUs(object : ApiCallback<ContactUsResponse>() {
            override fun onResponseSuccess(body: ContactUsResponse?) {
                listener(body?.data)
                body?.let { savePref(it) }
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                listener(null)
            }
        })
    }

    private fun savePref(response: ContactUsResponse) {
        Pref.getPreference().putString(AppKeys.PREF_CONTACT_US, Gson().toJson(response.data))

        val expired = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, 1)
        }
        Pref.getPreference().putLong(AppKeys.PREF_CONTACT_US_TIME, expired.timeInMillis)
    }
}
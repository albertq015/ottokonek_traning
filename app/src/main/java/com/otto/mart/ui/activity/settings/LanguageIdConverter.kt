package com.otto.mart.ui.activity.settings

import com.otto.mart.presenter.sessionManager.SessionManager

class LanguageIdConverter {

    fun getLanguageId(languageeId: String): String {
        var apiLanguageId = "eng"

        when (languageeId) {
            "en" -> {
                apiLanguageId = "eng"
            }
            "in" -> {
                apiLanguageId = "ind"
            }
        }

        return apiLanguageId
    }

    fun getLanguageId(): String {
        val languageeId = SessionManager.getLanguageCode()
        return getLanguageId(languageeId)
    }
}
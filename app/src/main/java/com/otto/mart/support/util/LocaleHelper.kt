package com.otto.mart.support.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.otto.mart.presenter.sessionManager.SessionManager
import java.util.*


class LocaleHelper {
    companion object {
        private val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

        fun getLanguage(context: Context): String {
            return SessionManager.getLanguageCode() ?: "in"
        }

        @JvmStatic
        fun setLocale(context: Context, language: String): Context? {
            SessionManager.setLanguageCode(language)
            return updateResourcesLegacy(context, language)
        }

        private fun updateResourcesLegacy(context: Context, language: String): Context? {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
//            configuration.setLocale(locale)
//            context.createConfigurationContext(configuration)
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
            return context
        }
    }
}
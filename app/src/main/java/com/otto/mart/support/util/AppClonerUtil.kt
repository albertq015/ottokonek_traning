package com.otto.mart.support.util

import android.content.Context

class AppClonerUtil {

    private val APP_PACKAGE_DOT_COUNT = 3
    private val DUAL_APP_ID_999 = "999"
    private val DOT = '.'

    fun isRunOnAppCloner(context: Context): Boolean {
        var result = false
        val path = context.filesDir.path
        if (path.contains(DUAL_APP_ID_999)) {
            result = true
        } else {
            val count = getDotCount(path)
            if (count > APP_PACKAGE_DOT_COUNT) {
                result = true
            }
        }
        return result
    }

    private fun getDotCount(path: String): Int {
        var count = 0
        for (i in 0 until path.length) {
            if (count > APP_PACKAGE_DOT_COUNT) {
                break
            }
            if (path[i] == DOT) {
                count++
            }
        }
        return count
    }
}
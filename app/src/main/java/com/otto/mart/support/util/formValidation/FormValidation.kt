package com.otto.mart.support.util.formValidation

import android.content.Context

class FormValidation(context: Context) {

    var mContext: Context? = null

    init {
        mContext = context
    }

    fun isRequired(value: String): Boolean {
        return value.trim().isNotEmpty()
    }

    fun isValidEmail(email: String): Boolean {
        val validEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return validEmail
    }

    fun isValidFullname(input: String): Boolean {
        val result = input.replace("[^a-zA-Z\\s]".toRegex(), "")

        val originalLeng = input.length
        val newLeng = result.length

        return (originalLeng == newLeng)
    }

    fun isValidFullnameLength(input: String): Boolean {
        return input.length > 1
    }

    fun isValidPassword(input: String): Boolean {
        return input.length > 5
    }

    fun isValidInputHandphone(input: String): Boolean {
        val result = input.replace("[^0-9\\s]".toRegex(), "")

        val originalLeng = input.length
        val newLeng = result.length

        return (originalLeng == newLeng) && (newLeng > 1)
    }

    fun isValidPhone(input: String): Boolean {
        return input.length > 5
    }

    fun isValidHandphone(input: String): Boolean {
        return input.length > 8
    }

    fun isValidMobilePhone(input: String): Boolean {
        if (input.length > 8) {
            if (input.length >= 2) {
                if (input.get(0).toString() == "0") {
                    return true
                } else if (input.substring(0, 2) == "63") {
                    return true
                }
            }
        } else {
            false
        }
        return false
    }

    fun isValidNoPdam(input: String): Boolean {
        return input.length > 4
    }

    fun isValidPostcode(input: String): Boolean {
        return input.length > 4
    }

    fun isValidBPJS(input: String): Boolean {
        return input.length > 10
    }
}
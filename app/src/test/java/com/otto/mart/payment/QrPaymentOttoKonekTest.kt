package com.otto.mart.ui.activity.qr.payment

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.otto.mart.support.util.formValidation.FormValidation
import org.junit.Test
import org.mockito.Mock

class QrPaymentOttoKonekTest {

    @Mock
    var mockContext: Context? = null

    @Test
    fun amountValidator_CorrectAmount_ReturnTrue() {
        assertThat(QrPaymentOttoKonekActivity().isValidAmount(2000)).isTrue()
    }

    @Test
    fun amountValidator_IncorrectAmount_ReturnFalse() {
        assertThat(QrPaymentOttoKonekActivity().isValidAmount(300)).isFalse()
        assertThat(QrPaymentOttoKonekActivity().isValidAmount(1200)).isFalse()
    }

    @Test
    fun amountValidator_IncorrectMultipleAmount_ReturnFalse() {
        assertThat(QrPaymentOttoKonekActivity().isValidAmount(1200)).isFalse()
    }

    @Test
    fun emailValidator_CorrectEmail_ReturnTrue() {
        mockContext?.let {
            assertThat(FormValidation(it).isValidEmail("name@email.com")).isTrue()
        }
    }

    @Test
    fun emailValidator_CorrectEmail_ReturnFalse() {
        mockContext?.let {
            assertThat(FormValidation(it).isValidEmail("sumpah_ini_email")).isFalse()
        }
    }
}
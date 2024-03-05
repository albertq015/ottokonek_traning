package com.otto.mart

import com.google.common.truth.Truth.assertThat
import com.otto.mart.support.util.DataUtil
import org.junit.Test

class DataUtilsTest {

    @Test
    fun currencyFormatTest() {
        val value = "php 3.000,23"

        assertThat(DataUtil.CurrencyToDouble(value)).isEqualTo(3000.23)
    }

    @Test
    fun currencyFormatTestWithoutDecimal() {
        val value = "php 3.000,0"

        assertThat(DataUtil.CurrencyToDouble(value)).isEqualTo(3000.0)
    }

    @Test
    fun currencyFormatTestHundred() {
        val value = "php 300"

        assertThat(DataUtil.CurrencyToDouble(value)).isEqualTo(300.0)
    }

    @Test
    fun currencyFormatTestThousandNoSeparator() {
        val value = "php 3000"

        assertThat(DataUtil.CurrencyToDouble(value)).isEqualTo(3000.0)
    }

    @Test
    fun currencyFormatTestEmpty() {
        val value = ""

        assertThat(DataUtil.CurrencyToDouble(value)).isEqualTo(0.0)
    }

    @Test
    fun currencyFormatTestnNull() {
        val value: String? = null

        assertThat(DataUtil.CurrencyToDouble(value!!)).isEqualTo(0.0)
    }

    @Test
    fun currencyFormatTestnMoreDecimal() {
        val value: String = "php 3000,955"

        assertThat(DataUtil.CurrencyToDouble(value)).isEqualTo(3000.96)
    }

    @Test
    fun currencyFormatTestnMoreDecimalDown() {
        val value: String = "php 3000,9554"

        assertThat(DataUtil.CurrencyToDouble(value)).isEqualTo(3000.96)
    }

    @Test
    fun currencyFormatTestnMoreDecimalEqual() {
        val value: String = "php 3000,9555"

        assertThat(DataUtil.CurrencyToDouble(value)).isEqualTo(3000.96)
    }

    @Test
    fun currencyFormatTestnMoreDecimalUp() {
        val value: String = "php 3000,9556"

        assertThat(DataUtil.CurrencyToDouble(value)).isEqualTo(3000.96)
    }
}
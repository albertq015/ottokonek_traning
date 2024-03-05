package com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class RegisterPhotoMerchantActivityUnitTest {

    @Mock
    var objectTest: RegisterPhotoMerchantActivity? = null

    @Before
    fun setup() {
        objectTest = RegisterPhotoMerchantActivity()
    }

    @Test
    fun shouldReturnFalseBothImageEmpty() {
        objectTest?.apply {
            idCardPath = ""
            storePath = ""
        }

        assertFalse("Both Empty", objectTest?.isImageComplete() ?: false)
    }

    @Test
    fun shouldReturnFalseEmptyIdCard() {
        objectTest?.apply {
            idCardPath = ""
            storePath = "path"
        }

        assertFalse("Both Empty", objectTest?.isImageComplete() ?: false)
    }

    @Test
    fun shouldReturnFalseEmptyStore() {
        objectTest?.apply {
            idCardPath = "path"
            storePath = ""
        }

        assertFalse("store Empty", objectTest?.isImageComplete() ?: false)
    }

    @Test
    fun shouldReturnTrue() {
        objectTest?.apply {
            idCardPath = "path"
            storePath = "path"
        }

        assertTrue("ready", objectTest?.isImageComplete() ?: false)
    }

    @Test
    fun shouldReturnFalseBothNull() {
        objectTest?.apply {
            idCardPath = null
            storePath = null
        }

        assertFalse("ready", objectTest?.isImageComplete() ?: false)
    }

    @Test
    fun shouldReturnFalseIdCardNull() {
        objectTest?.apply {
            idCardPath = null
            storePath = null
        }

        assertFalse("ready", objectTest?.isImageComplete() ?: false)
    }

    @Test
    fun shouldReturnFalseStorePathNull() {
        objectTest?.apply {
            idCardPath = null
            storePath = null
        }

        assertFalse("ready", objectTest?.isImageComplete() ?: false)
    }
}
package com.otto.mart

import com.otto.mart.support.util.DataUtil
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DataUtilPinUnitTest {
    @Test
    fun noSequenceCharacters821738True() {
        assertTrue("no sequence char", DataUtil.noSequenceCharacters("821738"))
    }

    @Test
    fun noSequenceCharacters123456False() {
        assertFalse("no sequence char", DataUtil.noSequenceCharacters("123456"))
    }

    @Test
    fun noSequenceCharacters012978False() {
        assertFalse("no sequence char", DataUtil.noSequenceCharacters("012978"))
    }

    @Test
    fun noSequenceCharacters013789False() {
        assertFalse("no sequence char", DataUtil.noSequenceCharacters("013789"))
    }

    @Test
    fun noSameCharacters111222False() {
        assertFalse("no sequence char", DataUtil.noSameCharacters("111222"))
    }

    @Test
    fun noSameCharacters111122False() {
        assertFalse("no sequence char", DataUtil.noSameCharacters("111122"))
    }

    @Test
    fun noSameCharacters911190False() {
        assertFalse("no sequence char", DataUtil.noSameCharacters("911190"))
    }

    @Test
    fun noSameCharacters942111False() {
        assertFalse("no sequence char", DataUtil.noSameCharacters("942111"))
    }

    @Test
    fun noSameCharacters983232True() {
        assertTrue("no sequence char", DataUtil.noSameCharacters("983232"))
    }
}

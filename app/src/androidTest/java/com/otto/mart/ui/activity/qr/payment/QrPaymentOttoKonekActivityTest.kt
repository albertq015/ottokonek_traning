package com.otto.mart.ui.activity.qr.payment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.otto.mart.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


@RunWith(AndroidJUnit4::class)
@LargeTest
class QrPaymentOttoKonekActivityTest {

    private lateinit var stringToBetyped: String

    @get:Rule
    var activityRule: ActivityScenarioRule<QrPaymentOttoKonekActivity> = ActivityScenarioRule(QrPaymentOttoKonekActivity::class.java)

    @Before
    fun initValidString() {
        // Specify a valid string.
        stringToBetyped = "Espresso"
        onView(withId(R.id.btnNext))
    }

    @Test
    fun changeText_sameActivity() {
        // Type text and then press the button.
        onView(withId(R.id.etAmount))
                .perform(typeText(stringToBetyped))
        onView(withId(R.id.btnNext)).perform(click())

        // Check that the text was changed.
        onView(withId(R.id.etAmount))
                .check(matches(withText(stringToBetyped)))
    }
}

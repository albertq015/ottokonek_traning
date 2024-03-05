package com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.otto.mart.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterPhotoMerchantActivityTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule<RegisterPhotoMerchantActivity>(RegisterPhotoMerchantActivity::class.java)

    @Test
    fun shouldShowTakeIDPhotoBtn() {
        onView(withId(R.id.iclBtnIdPhoto)).check(ViewAssertions.matches(isDisplayed()))
    }
}
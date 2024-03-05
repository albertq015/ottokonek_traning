package com.otto.mart.ui.activity.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Request.RegisterOtpRequestModel
import com.otto.mart.model.APIModel.Request.ResendOtpRegisterRequestModel
import com.otto.mart.model.APIModel.Request.ResetOtpPinRequestModel
import com.otto.mart.model.APIModel.Request.ResetPinRequestModel
import com.otto.mart.model.APIModel.Request.multibank.IssuerLinkedConfrim
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.SignupOtpResponseModel
import com.otto.mart.model.APIModel.Response.merchant.featureProduct.FeatureProductResponse
import com.otto.mart.model.APIModel.Response.merchant.theme.MerchantThemeResponse
import com.otto.mart.presenter.dao.AuthDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.*
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import com.otto.mart.ui.activity.register.registerFromSales.ActivationInputPinActivity
import com.otto.mart.ui.component.dialog.Popup
import kotlinx.android.synthetic.main.content_otp_verification.*
import java.util.concurrent.TimeUnit

class OtpVerificationActivity : AppActivity() {

    companion object {
        const val KEY_DATA_PHONE = "data_phone"
        const val KEY_DATA_USER_ID = "data_useer_id"
        const val KEY_DATA_OTP = "data_otp"
        const val KEY_FROM_REGISTER = "isFromRegister"
        const val KEY_REQ_ID = "request_id"
        const val KEY_REQ_ADD_BANK = "addBank"
        const val KEY_BIN = "bin"
        const val KEY_ACCOUNT_NUMBER = "accountNumber"
    }

    val KEY_API_RESEND_OTP = 100

    var mPhone = ""
    var mUserId = 0
    var isExpired = false
    var mBin = ""
    var mAccountNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        if (intent.hasExtra(KEY_DATA_PHONE)) {
            mPhone = intent?.getStringExtra(KEY_DATA_PHONE)!!
        }

        if (intent.hasExtra(KEY_BIN)) {
            mPhone = intent?.getStringExtra(KEY_DATA_PHONE)!!
            mBin = intent?.getStringExtra(KEY_BIN)!!
            mAccountNumber = intent?.getStringExtra(KEY_ACCOUNT_NUMBER)!!

        }

        if (intent.hasExtra(KEY_DATA_USER_ID)) {
            mUserId = intent.getIntExtra(KEY_DATA_USER_ID, 0)
        }



        initView()
    }

    private fun initView() {

        if (mUserId == 0) {
            tvTimer.gone()
        }
        tvPhoneInfo.text = getString(R.string.otp_verification_phone_info, mPhone)

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        resetForm()
        // btnNext.isEnabled = false

        et1.onChange {
            setInput(it, 1)
        }

        et2.onChange {
            setInput(it, 2)
        }

        et3.onChange {
            setInput(it, 3)
        }

        et4.onChange {
            setInput(it, 4)
        }

        et5.onChange {
            setInput(it, 5)
        }

        et6.onChange {
            setInput(it, 6)
        }

        btnNext.setOnClickListener {

            if (et1.text.toString().isBlank() || et2.text.toString()
                    .isBlank() || et3.text.toString().isBlank() || et4.text.toString()
                    .isBlank() || et5.text.toString().isBlank() || et6.text.toString().isBlank()
            ) {
                "Please Complete Your OTP Code".showToast(this@OtpVerificationActivity)
            } else if (intent.getBooleanExtra(KEY_FROM_REGISTER, false)) {
                    callSignUpOtp()
                    return@setOnClickListener
                } else if (intent.getBooleanExtra(KEY_REQ_ADD_BANK, false)) {
                    confrimAddBank()
                } else {
                    val intent = Intent()
                    intent.putExtra(ActivationInputPinActivity.KEY_DATA_PIN, getOtp())
                    intent.putExtra(KEY_DATA_OTP, getOtp())
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }


        }

        btnResend.setOnClickListener {
            isExpired = false
            btnResend.visibility = View.GONE
            startTimer()
            resetForm()
            if (intent.getBooleanExtra(KEY_FROM_REGISTER, false)) {
                resendOtp()

            } else {


                callResendOtpApi()
            }

            // callResendOtp()
        }

        startTimer()
    }

    private fun callResendOtpApi() {
        val requestModel = ResetOtpPinRequestModel()
        requestModel.answer = ""
        requestModel.latitude = getMyLastLocation().latitude
        requestModel.longitude = getMyLastLocation().longitude
        requestModel.security_question_id = 0
        requestModel.phone = mPhone
        val dao = AuthDao(this)
        ProgressDialogComponent.showProgressDialog(this@OtpVerificationActivity, "Loading", false)
            .show()
        dao.forgotPinOTP(requestModel, BaseDao.getInstance(this, KEY_API_RESEND_OTP).callback)
    }


    private fun startTimer() {
        //180000
        tvTimer.visibility = View.VISIBLE
        val countDownTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.setTextColor(
                    ContextCompat.getColor(
                        this@OtpVerificationActivity,
                        R.color.ocean_blue
                    )
                )
                tvTimer.text = String.format(
                    "%02d : %02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )
            }

            override fun onFinish() {
                btnResend.visibility = View.VISIBLE
                tvTimer.visibility = View.GONE
                if (mUserId != 0) {
                    tvTimer.visibility = View.GONE
                    //  tvTimer.text = getString(R.string.message_otp_exp)
                    //  tvTimer.setTextColor(ContextCompat.getColor(this@OtpVerificationActivity, R.color.cherry_red))
                    btnResend.visibility = View.VISIBLE

                    //disableSubmitButton()
                    isExpired = true

                }
            }
        }.start()
    }

    private fun setInput(input: String, position: Int) {
        if (input.isEmpty()) {
            moveBack(position)
        } else {
            moveForward(position)
        }
    }

    private fun moveForward(position: Int) {
        when (position) {
            1 -> {
                setFocus(et2)
            }
            2 -> {
                setFocus(et3)
            }
            3 -> {
                setFocus(et4)
            }
            4 -> {
                setFocus(et5)
            }
            5 -> {
                setFocus(et6)
            }
            6 -> {
                validateForm()
            }
        }
    }

    private fun moveBack(position: Int) {
        when (position) {
            1 -> {

            }
            2 -> {
                setFocus(et1)
            }
            3 -> {
                setFocus(et2)
            }
            4 -> {
                setFocus(et3)
            }
            5 -> {
                setFocus(et4)
            }
            6 -> {
                setFocus(et5)
            }
        }
    }

    private fun setFocus(editText: EditText?) {
        val handler = Handler()
        handler.postDelayed({
            editText?.requestFocus()
            editText?.setSelection(editText.text.length)
        }, 10)
    }

    private fun validateForm() {
        if ((!et1.text.toString().equals("")) && (!et2.text.toString()
                .equals("")) && (!et3.text.toString().equals(""))
            && (!et4.text.toString().equals("")) && (!et5.text.toString()
                .equals("")) && (!et6.text.toString().equals(""))
        ) {
            inputOtpComplete(getOtp())
        } else {
            if (et1.text.toString().equals("")) {
                setFocus(et1)
            } else if (et2.text.toString().equals("")) {
                setFocus(et2)
            } else if (et3.text.toString().equals("")) {
                setFocus(et3)
            } else if (et4.text.toString().equals("")) {
                setFocus(et4)
            } else if (et5.text.toString().equals("")) {
                setFocus(et5)
            } else if (et6.text.toString().equals("")) {
                setFocus(et6)
            }

            disableSubmitButton()
        }
    }

    private fun disableSubmitButton() {
        //  btnNext.isEnabled = false
        btnNext.background =
            ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
    }

    private fun enableSubmitButton() {
        //  btnNext.isEnabled = true
        btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
    }

    private fun getOtp(): String {
        return et1.text.toString() + et2.text.toString() + et3.text.toString() +
                et4.text.toString() + et5.text.toString() + et6.text.toString()
    }

    private fun inputOtpComplete(otp: String) {
        if (isExpired) disableSubmitButton()
        else enableSubmitButton()
    }

    private fun resetForm() {
        et1.setText("")
        et2.setText("")
        et3.setText("")
        et4.setText("")
        et5.setText("")
        et6.setText("")
        setFocus(et1)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL) {
            if (et6.isFocused) {
                setFocus(et5)
            } else if (et5.isFocused) {
                setFocus(et4)
            } else if (et4.isFocused) {
                setFocus(et3)
            } else if (et3.isFocused) {
                setFocus(et2)
            } else if (et2.isFocused) {
                setFocus(et1)
            }
        }
        return super.dispatchKeyEvent(event)
    }

    //region API Call


    private fun callResendOtp() {
        val resendOtpRegisterRequestModel = ResendOtpRegisterRequestModel()
        resendOtpRegisterRequestModel.setUser_id(mUserId)

        AuthDao(this).resendOtpDao(
            resendOtpRegisterRequestModel,
            BaseDao.getInstance(this, KEY_API_RESEND_OTP).callback
        )
    }

    private fun callSignUpOtp() {
        showProgressDialog(R.string.loading_message)
        val data = RegisterOtpRequestModel().apply {
            otp_code = getOtp()
            latitude = getMyLastLocation().latitude
            longitude = getMyLastLocation().longitude
            phone = intent.getStringExtra(KEY_DATA_PHONE)
            isRose = true
            request_id = intent.getStringExtra("data_reqId")
        }

        OttoKonekAPI.signUpOtp(this, data, object : ApiCallback<SignupOtpResponseModel>() {
            override fun onResponseSuccess(body: SignupOtpResponseModel?) {
                dismissProgressDialog()
                if (isSuccess200) {
                    body?.data?.let { data ->
                        SessionManager.createLoginSession(
                            data.user_id,
                            data.wallet_id,
                            data.access_token,
                            intent.getStringExtra("pin"),
                            data
                        )
                        SessionManager.setPrefLogin(data)
                    }
                    callMerchantTheme()
                } else body?.meta?.let { showErrorDialog(it.message) }
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }
        })
    }

    private fun callMerchantTheme() {
        showProgressDialog(R.string.loading_message)
        OttoKonekAPI.merchantTheme(this, object : ApiCallback<MerchantThemeResponse>() {
            override fun onResponseSuccess(body: MerchantThemeResponse?) {
                dismissProgressDialog()
                if (isSuccess200) {
                    body?.data?.let { theme ->
                        SessionManager.setMerchantTheme(theme)
                        callFeatureProduct()
                    }
                } else body?.meta?.let { showErrorDialog(it.message) }
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }

        })
    }

    private fun callFeatureProduct() {
        showProgressDialog(R.string.loading_message)
        OttoKonekAPI.featureProduct(this, object : ApiCallback<FeatureProductResponse>() {
            override fun onResponseSuccess(body: FeatureProductResponse?) {
                dismissProgressDialog()
                if (isSuccess200) {
                    body?.data?.let { feature ->
                        SessionManager.setFeatureProduct(feature)

                        showSuccessPopup(
                            "Registration Successful!",
                            "Your account has been successfully registered"
                        )
                    }
                } else body?.meta?.let { showErrorDialog(it.message) }
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }

        })
    }

    private fun showSuccessPopup(title: String, subtitle: String) {
        Popup.showInfo(supportFragmentManager, title, subtitle) {
            val intent = Intent(this@OtpVerificationActivity, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun resendOtp() {
        val resendOtpModel = ResendOtpRegisterRequestModel().apply {
            request_id = intent.getStringExtra(KEY_REQ_ID)
            user_id = mUserId
        }

        OttoKonekAPI.resendOtp(this, resendOtpModel, object : ApiCallback<BaseResponseModel>() {
            override fun onResponseSuccess(body: BaseResponseModel?) {
                dismissProgressDialog()
                btnResend.visibility = View.GONE
                if (isSuccess200) {
                    "Request sent, Please Wait".showToast(this@OtpVerificationActivity)
                } else showErrorDialog(body?.meta?.message)
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }
        })
    }

    //endregion Call API

    // call api confirm add bank
    private fun confrimAddBank() {
        showProgressDialog(getString(R.string.loading_message))
        val requestBody = IssuerLinkedConfrim()
        requestBody.accountNumber = mAccountNumber
        requestBody.bin = mBin
        requestBody.otp = getOtp()

        OttoKonekAPI.issuerLinkedConfrim(
            this,
            requestBody,
            object : ApiCallback<BasePaymentResponseModel>() {
                override fun onResponseSuccess(body: BasePaymentResponseModel?) {
                    dismissProgressDialog()
                    if (isSuccess200) {
                        showSuccessPopup(
                            "Account Linked Successfully",
                            "your phone has been successfuly linked for account number " + mAccountNumber
                        )
                    } else {
                        showErrorDialog(body?.meta?.message)
                    }

                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                    onApiResponseError()

                }

            })
    }

}

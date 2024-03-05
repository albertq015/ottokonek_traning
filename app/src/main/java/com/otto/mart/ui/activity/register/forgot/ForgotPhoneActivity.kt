package com.otto.mart.ui.activity.register.forgot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Request.ResetOtpPinRequestModel
import com.otto.mart.model.APIModel.Request.ResetPinRequestModel
import com.otto.mart.model.APIModel.Request.UserSecurityQuestionRequestModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.ResetOtpPinResponseModel
import com.otto.mart.model.APIModel.Response.ResetPinResponseModel
import com.otto.mart.presenter.dao.AuthDao
import com.otto.mart.support.util.gone
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.login.LoginActivity
import com.otto.mart.ui.activity.login.OtpVerificationActivity
import com.otto.mart.ui.component.LazyDialog
import com.otto.mart.ui.component.dialog.CustomDialog
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.activity_forgot_phone.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class ForgotPhoneActivity : AppActivity() {

    private val KEY_CODE_INPUT_PIN = 100
    private val KEY_CODE_INPUT_OTP = 101
    private val KEY_API_RESET_OTP = 200
    private val KEY_API_RESET = 201

    private val hundDialog: LazyDialog? = null
    private var mPhone = ""
    private var mPin = ""
    private var mOtp = ""
    private var mCustomerId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_phone)
        initContent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == KEY_CODE_INPUT_PIN) {
                if (data != null) {
                    mPin = data?.getStringExtra("pincode")!!
                    callForgetPinOtpApi()
                }
            } else if (requestCode == KEY_CODE_INPUT_OTP) {
                if (data != null) {
                    mOtp = data?.getStringExtra(OtpVerificationActivity.KEY_DATA_OTP)!!
                    callForgotPinApi(mOtp)
                }
            }
        }
    }

    private fun initContent() {
        toolbarLine.gone()
        tvToolbarTitle.text = ""

        etHandphone.setText(intent.getStringExtra(AppKeys.KEY_PHONE_NUMBER))

        btnToolbarBack.setOnClickListener { finish() }

        btnSubmit.setOnClickListener { v: View? ->
            mPhone = etHandphone.text.toString()
//            val intent = Intent(this@ForgotPhoneActivity, ActivationInputPinActivity::class.java)
//            intent.putExtra(ActivationInputPinActivity.KEY_DATA_TYPE, ActivationInputPinActivity.KEY_TYPE_RESET_PIN)
//            startActivityForResult(intent, KEY_CODE_INPUT_PIN)
            inputPin()
        }

        etHandphone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length > 9) {
                    btnSubmit.isEnabled = true
                    btnSubmit.background = ContextCompat.getDrawable(this@ForgotPhoneActivity, R.drawable.button_primary_selector)
                } else {
                    btnSubmit.isEnabled = false
                    btnSubmit.background = ContextCompat.getDrawable(this@ForgotPhoneActivity, R.drawable.button_white_grey_selected_bg)
                }
            }
        })
    }

    private fun inputPin() {
        val intent = Intent(this@ForgotPhoneActivity, RegisterPinResetActivity::class.java)
        intent.putExtra("register", true)
        startActivityForResult(intent, KEY_CODE_INPUT_PIN)
    }

    private fun gotoOtp() {
        val intent = Intent(this, OtpVerificationActivity::class.java)
        intent.putExtra(OtpVerificationActivity.KEY_DATA_PHONE, mPhone)
        startActivityForResult(intent, KEY_CODE_INPUT_OTP)
    }

    private fun callForgotPINSecurityQuestionRequestAPI(phone: String) {
        val requestModel = UserSecurityQuestionRequestModel()
        requestModel.phone = phone
        val dao = AuthDao(this)
        dao.forgotPinSecuritytQuestionDao(requestModel, BaseDao.getInstance(this, 444).callback)
    }

    private fun callForgetPinOtpApi() {
        val requestModel = ResetOtpPinRequestModel()
        requestModel.answer = ""
        requestModel.latitude = getMyLastLocation().latitude
        requestModel.longitude = getMyLastLocation().longitude
        requestModel.security_question_id = 0
        requestModel.phone = mPhone
        val dao = AuthDao(this)
        ProgressDialogComponent.showProgressDialog(this@ForgotPhoneActivity, "Loading", false).show()
        dao.forgotPinOTP(requestModel, BaseDao.getInstance(this, KEY_API_RESET_OTP).callback)
    }

    private fun callForgotPinApi(otp: String) {
        val resetPinRequestModel = ResetPinRequestModel()
        resetPinRequestModel.customerId = mCustomerId
        resetPinRequestModel.customer_id = mCustomerId
        resetPinRequestModel.phone = mPhone
        resetPinRequestModel.otp_code = otp
        resetPinRequestModel.new_pin = mPin
        val dao = AuthDao(this)
        ProgressDialogComponent.showProgressDialog(this@ForgotPhoneActivity, "Loading", false).show()
        dao.forgotPinResetDAO(resetPinRequestModel, BaseDao.getInstance(this, KEY_API_RESET).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse, responseCode: Int, response: Response<*>) {
        if (response.isSuccessful) {
            ProgressDialogComponent.dismissProgressDialog(this@ForgotPhoneActivity)
            when (responseCode) {
                KEY_API_RESET_OTP -> if ((br as ResetOtpPinResponseModel).meta.code == 200) {
                    mCustomerId = br.data.customerId
                    gotoOtp()
                } else {
                    val dialog = ErrorDialog(this, this, false, false, (br as BaseResponseModel).meta.message, " ")
                    dialog.show()
                }
                KEY_API_RESET -> if ((br as ResetPinResponseModel).meta.code == 200 && br.meta.status) {
                    val customDialog = CustomDialog(this, this, true, false, "Change Pin success, Please login using the New Pin", "OK")
                    customDialog.show()
                } else {
                    val dialog = ErrorDialog(this, this, false, false, (br as BaseResponseModel).meta.message, " ")
                    dialog.show()
                }
            }
        }
    }

    fun backToLogin() {
        val jenk = Intent(this@ForgotPhoneActivity, LoginActivity::class.java)
        jenk.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        jenk.putExtra(AppKeys.KEY_PHONE_NUMBER, mPhone)
        startActivity(jenk)
    }

    override fun onApiFailureCallback(message: String, ac: BaseActivity) {
        //super.onApiFailureCallback(message, ac);
        onApiResponseError()
    }
}
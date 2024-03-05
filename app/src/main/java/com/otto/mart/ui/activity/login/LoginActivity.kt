package com.otto.mart.ui.activity.login

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.otto.mart.BuildConfig
import com.otto.mart.OttoMartApp
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.AuthDataModel
import com.otto.mart.model.APIModel.Request.*
import com.otto.mart.model.APIModel.Response.*
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.merchant.featureProduct.FeatureProductResponse
import com.otto.mart.model.APIModel.Response.merchant.theme.MerchantThemeResponse
import com.otto.mart.presenter.dao.AuthDao
import com.otto.mart.presenter.dao.EtcDao
import com.otto.mart.presenter.dao.OCBIDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.*
import com.otto.mart.support.util.formValidation.FormValidation
import com.otto.mart.support.util.pref.Pref
import com.otto.mart.support.util.widget.dialog.AlertDialogBottomSheet
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import com.otto.mart.ui.activity.prominent.ProminentActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.activity.register.registerFromSales.ActivationConfirmationActivity
import com.otto.mart.ui.activity.register.registerFromSales.ActivationInputPinActivity
import com.otto.mart.ui.activity.register.registerFromSales.registerPhoneInput.RegisterPhoneInputActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.component.dialog.OtpDialog
import com.stfalcon.smsverifycatcher.OnSmsCatchListener
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Response

class LoginActivity : AppActivity() {

    companion object {
        const val KEY_IS_SESSION_EXPIRED = "is_session_expired"
    }

    private val TAG = this.javaClass.simpleName

    private val KEY_CODE_INPUT_CREATE_PIN = 100
    private val KEY_CODE_INPUT_OTP = 101

    private val KEY_API_LOGIN = 444
    private val KEY_API_LOGIN_OTP = 445
    private val KEY_API_MERCHANT_THEME = 446
    private val KEY_API_FEATURE_MERCHANT = 447
    private val KEY_OC_CONFIRM = 432
    private val KEY_API_FORGOT_PIN = 450
    private val KEY_API_RESPONSE_OTP = 451
    private val KEY_API_OTP = 452

    private val KEY_OTP_TYPE_LOGIN = "otp_type_login"
    private val KEY_OTP_TYPE_UPDATE_STATUS = "otp_type_update_status"

    private var mOtpType = KEY_OTP_TYPE_LOGIN

    private var otpDialog: OtpDialog? = null
    private var mUserId = 0
    private var perm1 = false
    private val perm3 = false
    private var smsVerifyCatcher: SmsVerifyCatcher? = null
    private val isCheckUserStatus = true
    private var mPin = ""
    private var mPhone = ""
    private var mFormValidation: FormValidation? = null
    private var mCustomerId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initData()
//        initAllPermission()
        displayProminentDialog()
        initSmsCatcher()
        initComponent()
        initContent()
        firebaseToken()
        checkVersion()
    }

    private fun initData() {
        if (intent.hasExtra(KEY_IS_SESSION_EXPIRED)) {
            var isSessionExpired = intent.getBooleanExtra(KEY_IS_SESSION_EXPIRED, false)

            if (isSessionExpired) {
                getString(R.string.msg_session_expired).showToast(this)
            }
        }
    }

    private fun initSmsCatcher() {
        smsVerifyCatcher = SmsVerifyCatcher(this, OnSmsCatchListener { message ->
            Log.d(TAG, "SMS Content: $message")
            if (message.contains("ottopay") || message.contains("OttoPay")) {
                val otp = message.replace("[^0-9\\s]".toRegex(), "").trim { it <= ' ' }
                if (otp.length == 6) {
                    Log.d(TAG, "OTP: $otp")
                    otpDialog!!.setOTP(otp)
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        smsVerifyCatcher!!.onStop()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                111 -> etPhone!!.setText(data!!.getStringExtra("phonejink"))
                AppKeys.KEY_INPUT_PIN -> {
                    mPin = data?.getStringExtra(AppKeys.KEY_PIN_CODE)!!
                    login(mPin)
                }
                KEY_CODE_INPUT_CREATE_PIN -> if (data != null) {
                    mPin = data?.getStringExtra(ActivationInputPinActivity.KEY_DATA_PIN)!!
                    gotoOtp(mPhone)
                }
                KEY_CODE_INPUT_OTP -> if (data != null) {
                    if (getCurrentLocation()) {
                        val otp = data.getStringExtra(OtpVerificationActivity.KEY_DATA_OTP)
                        if (mOtpType == KEY_OTP_TYPE_LOGIN) {
                            val request = LoginOtpRequest()
                            request.otp_code = otp
                            request.user_id = etPhone!!.text.toString()
                            request.latitude = myLastLocation.latitude
                            request.longitude = myLastLocation.longitude
                            request.firebase_token = SessionManager.getFirebaseToken()
                            request.version = BuildConfig.VERSION_CODE.toString()
                            callLoginOtpApi(request)
                        } else {
                            callApiUpdateStatus(otp!!)
                        }
                    }
                }
                KEY_OC_CONFIRM -> {
                    callUpdateTNCOCBI()
                }
                KEY_API_RESPONSE_OTP -> {
                    if (data != null) {
                        callForgotPinApi(data?.getStringExtra(OtpVerificationActivity.KEY_DATA_OTP)!!)
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun readOTP() {
        smsVerifyCatcher!!.onStart()
    }

    /**
     * need for Android 6 real time permissions
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        smsVerifyCatcher!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkIfLoggedIn() {
        if (SessionManager.isLoggedIn() && !SessionManager.getTncProgress()) {
            val loginIntent = Intent(this, DashboardActivity::class.java)
            loginIntent.putExtra("isLoggedIn", true)
            startActivity(loginIntent)
            finish()
        } else {
            viewAnimator.displayedChild = 1
        }
    }

    private fun initComponent() {
        otpDialog = OtpDialog(this, this, false)
    }

    private fun initContent() {
        // tvTncDesc!!.text = Html.fromHtml(getString(R.string.msg_tnc_confirmation))

        mFormValidation = FormValidation(this)

        addTextWatcher(etPhone)

        if (intent.getStringExtra(AppKeys.KEY_PHONE_NUMBER) != null) {
            etPhone!!.setText(intent.getStringExtra(AppKeys.KEY_PHONE_NUMBER))
            etPhone!!.setSelection(etPhone!!.text.length)
        }

        if (SessionManager.getIsRememberPhone()) {
            etPhone!!.setText(SessionManager.getPhone())
            etPhone!!.setSelection(etPhone!!.text.length)
        }

        otpDialog?.setOtpListener(object : OtpDialog.OtpDialogListener {
            override fun onOtpEntered(dialog: OtpDialog) {}
            override fun onActionPressed(dialog: OtpDialog) {
                if (mOtpType == KEY_OTP_TYPE_LOGIN) {
                    val request = LoginOtpRequest()
                    request.otp_code = dialog.otpCode
                    request.user_id = etPhone.text.toString()
                    request.latitude = myLastLocation.latitude
                    request.longitude = myLastLocation.longitude
                    request.firebase_token = SessionManager.getFirebaseToken()
                    request.version = BuildConfig.VERSION_CODE.toString()
                    callLoginOtpApi(request)
                    dialog.setLoadingState()
                } else {
                    callApiUpdateStatus(otpDialog!!.otpCode)
                }
            }

            override fun onBackPressed(dialog: OtpDialog) {
                //finish();
            }

            override fun onResendPressed(dialog: OtpDialog) {
                val resendOtpRegisterRequestModel = ResendOtpRegisterRequestModel()
                resendOtpRegisterRequestModel.user_id = mUserId
                AuthDao(this@LoginActivity).resendOtpDao(
                    resendOtpRegisterRequestModel,
                    BaseDao.getInstance(this@LoginActivity, 447).callback
                )
                otpDialog!!.clearOTP()
            }
        })

        btnSubmit.setOnClickListener { v: View? ->
            if (loginInputLayoout.isVisible()) {
                if (getCurrentLocation()) {
                    if (isValidFormLogin) {
                        inputPin()
                    }
                }
            } else {
                loginInputLayoout.visible()
                orLayout.gone()
                label_welcome_desc.gone()

                label_welcome.setText("Please enter your mobile number")
                btnSignUp.setText(getString(R.string.button_back))
            }
        }

        btnSignUp.setOnClickListener { v: View? ->
            if (loginInputLayoout.isVisible()) {
                loginInputLayoout.gone()
                orLayout.visible()
                label_welcome_desc.visible()

                label_welcome.setText(getString(R.string.label_logiin_msg_welcome))
                btnSignUp.setText(getString(R.string.label_register_now))
            } else {
                startActivity(Intent(this@LoginActivity, RegisterPhoneInputActivity::class.java))
            }
        }
    }

    private fun inputPin() {
        val intent = Intent(this@LoginActivity, RegisterPinResetActivity::class.java)
        intent.putExtra(AppKeys.KEY_PHONE_NUMBER, etPhone.text.toString())
        intent.putExtra(AppKeys.KEY_INPUT_PIN_TYPE_LOGIN, true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivityForResult(intent, AppKeys.KEY_INPUT_PIN)
    }

    fun addTextWatcher(input: EditText?) {
        input!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (isValidFormLogin) {
                    btnSubmit.background = ContextCompat.getDrawable(
                        this@LoginActivity,
                        R.drawable.button_primary_rounded_selector
                    )
                } else {
                    //btnSubmit.background = ContextCompat.getDrawable(this@LoginActivity, R.drawable.button_white_grey_selected_bg)
                }
            }
        })
    }

    private val isValidFormLogin: Boolean
        private get() {
            var isValid = false
            if (mFormValidation!!.isValidHandphone(etPhone!!.text.toString())) {
                isValid = true
            }
            return isValid
        }

    private fun gotoOtp(phone: String) {
        var model = ResetOtpPinRequestModel()
        model.latitude = OttoMartApp.getCoordinate().latitude
        model.longitude = OttoMartApp.getCoordinate().longitude
        model.phone = phone
        model.answer = ""
        callResetApi(model)
    }

    private fun gotoOtpAc(phone: String) {
        val intent = Intent(this, OtpVerificationActivity::class.java)
        intent.putExtra(OtpVerificationActivity.KEY_DATA_PHONE, phone)
        intent.putExtra(OtpVerificationActivity.KEY_DATA_USER_ID, mUserId)
        startActivityForResult(intent, KEY_CODE_INPUT_OTP)
    }

    override fun checkVersion() {
        ProgressDialogComponent.showProgressDialog(
            this@LoginActivity,
            getString(R.string.loading_message),
            false
        ).show()
        EtcDao(this).versionCheckDao(
            BaseDao.getInstance(
                this,
                AppKeys.API_KEY_CHECK_VERSION
            ).callback
        )
    }

    private fun callUpdateTNCOCBI() {
        OCBIDao(this).confirmTNCOCBI(
            BaseDao.getInstance(
                this,
                AppKeys.API_KEY_GET_UPDATE_OC_STATUS
            ).callback
        )
    }

    private fun callLoginApi(model: LoginRequestModel) {
        ProgressDialogComponent.showProgressDialog(
            this@LoginActivity,
            getString(R.string.loading_message),
            false
        ).show()
        val auth = AuthDao(this)
        auth.loginOttofinDao(model, BaseDao.getInstance(this, KEY_API_LOGIN).callback)
    }

    private fun callResetApi(model: ResetOtpPinRequestModel) {
        val requestModel = ResetOtpPinRequestModel()
        requestModel.answer = ""
        requestModel.latitude = getMyLastLocation().latitude
        requestModel.longitude = getMyLastLocation().longitude
        requestModel.security_question_id = 0
        requestModel.phone = mPhone
        val dao = AuthDao(this)
        ProgressDialogComponent.showProgressDialog(this@LoginActivity, "Loading", false).show()
        dao.forgotPinOTP(requestModel, BaseDao.getInstance(this, KEY_API_FORGOT_PIN).callback)
    }

    private fun callLoginOtpApi(model: LoginOtpRequest) {
        ProgressDialogComponent.showProgressDialog(
            this@LoginActivity,
            getString(R.string.loading_message),
            false
        ).show()
        val auth = AuthDao(this)
        auth.loginOtpOttofinDao(model, BaseDao.getInstance(this, KEY_API_LOGIN_OTP).callback)
    }

    private fun callForgotPinApi(otp: String) {
        val resetPinRequestModel = ResetPinRequestModel()
        resetPinRequestModel.customerId = mCustomerId
        resetPinRequestModel.customer_id = mCustomerId
        resetPinRequestModel.phone = mPhone
        resetPinRequestModel.otp_code = otp
        resetPinRequestModel.new_pin = mPin
        val dao = AuthDao(this)
        ProgressDialogComponent.showProgressDialog(this@LoginActivity, "Loading", false).show()
        dao.forgotPinResetDAO(resetPinRequestModel, BaseDao.getInstance(this, KEY_API_OTP).callback)
    }

    private fun callApiUpdateStatus(otp: String) {
        ProgressDialogComponent.showProgressDialog(
            this@LoginActivity,
            getString(R.string.text_loading),
            false
        ).show()

        val updateStatusRequest = UpdateStatusRequest()
        updateStatusRequest.otp_code = otp
        updateStatusRequest.new_pin = mPin
        updateStatusRequest.answer = ""
        updateStatusRequest.security_question_id = 0
        val auth = AuthDao(this)
        auth.updateStatusDao(
            updateStatusRequest,
            BaseDao.getInstance(this, AppKeys.API_KEY_UPDATEE_STATUS).callback
        )
    }

    private fun login(pin: String) {
        if (Connectivity.isConnected(this@LoginActivity)) {
            val model = LoginRequestModel()
            model.user_id = etPhone.text.toString()
            model.pin = pin
            model.latitude = myLastLocation.latitude
            model.longitude = myLastLocation.longitude
            model.firebase_token = SessionManager.getFirebaseToken()
            model.version = BuildConfig.VERSION_CODE.toString()
            model.lang = SessionManager.getLanguageCode()
            callLoginApi(model)
        } else {
            dialogNoInternetConnection()
        }
    }

    private fun firebaseToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result
                SessionManager.setFirebaseToken(task.result)

                Log.d("Firebase Token", token)
                SessionManager.setFirebaseToken(token)
            })
    }

    private fun checkUserStatus(status: String, isTnCAccepted: Boolean, phone: String) {
        /**
         * need to be confirm data for merchant badge
         */
        if (isCheckUserStatus) {
            if (status.equals("RR", ignoreCase = true) || status.equals("UR", ignoreCase = true)) {
                mOtpType = KEY_OTP_TYPE_UPDATE_STATUS
                val intent = Intent(this@LoginActivity, ActivationInputPinActivity::class.java)
                intent.putExtra(
                    ActivationInputPinActivity.KEY_DATA_TYPE,
                    ActivationInputPinActivity.KEY_TYPE_RESET_PIN
                )
                startActivityForResult(intent, KEY_CODE_INPUT_CREATE_PIN)
            } else {
                checkOCTnC(isTnCAccepted, phone)
            }
        } else {
            checkOCTnC(isTnCAccepted, phone)
        }
    }

    private fun checkOCTnC(isTnCAccepted: Boolean, phone: String) {
        if (isTnCAccepted) {
            val model = Gson().fromJson(
                Pref.getPreference().getString(AppKeys.PREF_LOGIN),
                AuthDataModel::class.java
            )
            SessionManager.createLoginSession(
                model.user_id,
                model.wallet_id,
                model.access_token,
                mPin,
                model
            )
            SessionManager.setTncProgress(false)
            getMerchantTheme()
        } else {
            val intent = Intent(this, ActivationConfirmationActivity::class.java)
            intent.putExtra(ActivationConfirmationActivity.KEY_OC_ONLY, true)
            intent.putExtra(ActivationConfirmationActivity.KEY_PHONE_DATA, phone)
            startActivityForResult(intent, KEY_OC_CONFIRM)
        }
    }

    override fun onBackPressed() {
        if (loginInputLayoout.isVisible()) {
            btnSignUp.performClick()
        } else {
            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.addCategory(Intent.CATEGORY_HOME)
                        startActivity(intent)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.text_close_app_confirmation))
                .setPositiveButton(getString(R.string.text_yes), dialogClickListener)
                .setNegativeButton(getString(R.string.text_no), dialogClickListener).show()
        }
    }


    private fun getMerchantTheme() {
        ProgressDialogComponent.showProgressDialog(
            this@LoginActivity,
            getString(R.string.loading_message),
            false
        ).show()
        AuthDao(this).merchantTheme(BaseDao.getInstance(this, KEY_API_MERCHANT_THEME).callback)
    }

    private fun getFeatureProduct() {
        ProgressDialogComponent.showProgressDialog(
            this@LoginActivity,
            getString(R.string.loading_message),
            false
        ).show()
        AuthDao(this).featureProduct(BaseDao.getInstance(this, KEY_API_FEATURE_MERCHANT).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse, responseCode: Int, response: Response<*>) {
        if (responseCode != KEY_API_LOGIN && responseCode != KEY_API_LOGIN_OTP &&
            responseCode != KEY_API_MERCHANT_THEME && responseCode != KEY_API_FEATURE_MERCHANT
        ) {
            ProgressDialogComponent.dismissProgressDialog(this)
        }
        validateApiResponse(br)
        if (response.body() != null && response.isSuccessful) {
            when (responseCode) {
                KEY_API_LOGIN -> if ((br as LoginResponseModel).meta.code == 200) {
                    mUserId = br.data.user_id
                    mPhone = br.data.phone
                    if (br.data.need_otp == 1) {
                        mOtpType = KEY_OTP_TYPE_LOGIN
                        //readOTP();
                        gotoOtpAc(br.data.phone)
                    } else {
                        SessionManager.setIsRemeberPhone(true)
                        SessionManager.setPrefLogin(br.data)
                        SessionManager.setToken(br.data.access_token)
                        SessionManager.setTncProgress(true)
                        val status = br.data.status
                        checkUserStatus(status, br.data.isAcceptOcbiTnc, br.data.phone)
                    }
                } else {
                    val dialog = ErrorDialog(
                        this,
                        this,
                        false,
                        false,
                        (br as BaseResponseModel).meta.message,
                        ""
                    )
                    dialog.show()
                }
                KEY_API_LOGIN_OTP -> {
                    //otpDialog!!.setNotLoadingState()
                    if ((br as LoginOtpResponseModel).meta.code == 200) {
                        SessionManager.setIsRemeberPhone(true)
                        SessionManager.setPrefLogin((br as LoginResponseModel).data)
                        SessionManager.setToken((br as LoginResponseModel).data.access_token)
                        SessionManager.setTncProgress(true)
                        val status = (br as LoginResponseModel).data.status
                        mUserId = br.data.user_id
                        checkUserStatus(
                            status,
                            (br as LoginResponseModel).data.isAcceptOcbiTnc,
                            (br as LoginResponseModel).data.phone
                        )
                    } else {
                        val dialog = ErrorDialog(
                            this,
                            this,
                            false,
                            false,
                            (br as BaseResponseModel).meta.message,
                            ""
                        )
                        dialog.show()
                    }
                }
                AppKeys.API_KEY_CHECK_VERSION -> {
                    if ((br as BaseResponseModel).meta.code == 200) {
                        var model: CheckVersionResponseModel? = null
                        try {
                            model = br as CheckVersionResponseModel
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        if (model != null) {
                            updateDialog(model.data)
                        }
                        checkIfLoggedIn()
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
                    }
                }
                AppKeys.API_KEY_UPDATEE_STATUS -> {
                    if ((br as BaseResponseModel).meta.code == 200) {
                        SessionManager.setSecondaryToken(mPin)
                        getMerchantTheme()
                    } else {
                        if (otpDialog!!.isShowing) {
                            otpDialog!!.dismiss()
                        }
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
                    }
                }
                AppKeys.API_KEY_GET_UPDATE_OC_STATUS -> {
                    if ((br as BaseResponseModel).meta.code == 200) {
                        checkOCTnC(true, "")
                    }
                }
                KEY_API_MERCHANT_THEME -> {
                    if ((br as BaseResponseModel).meta.code == 200) {
                        SessionManager.setMerchantTheme((br as MerchantThemeResponse).data)
                        getFeatureProduct()
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
                    }
                }
                KEY_API_FEATURE_MERCHANT -> {
                    if ((br as BaseResponseModel).meta.code == 200) {
                        SessionManager.setFeatureProduct((br as FeatureProductResponse).data)

                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
                    }
                }
                KEY_API_FORGOT_PIN -> {
                    if ((br as ResetOtpPinResponseModel).meta.code == 200) {
                        mCustomerId = br.data.customerId
                        val intent = Intent(this, OtpVerificationActivity::class.java)
                        intent.putExtra(OtpVerificationActivity.KEY_DATA_PHONE, mPhone)
                        startActivityForResult(intent, KEY_API_RESPONSE_OTP)
                    }
                }
                KEY_API_OTP -> if ((br as ResetPinResponseModel).meta.code == 200 && br.meta.status) {
                    login(mPin)
                } else {
                    val dialog = ErrorDialog(
                        this,
                        this,
                        false,
                        false,
                        (br as BaseResponseModel).meta.message,
                        " "
                    )
                    dialog.show()
                }
            }
        }
    }

    override fun onApiFailureCallback(message: String, ac: BaseActivity) {
        onApiResponseError()
    }

    var prominentActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Pref.getPreference()
                    .putBoolean(SessionManager.KEY_PREF_HIDE_PROMINENT_PERMISSION_DIALOG, true)
            } else {
                displayForceProminentDialog()
            }
        }


    private fun displayProminentDialog() {
        if (!Pref.getPreference()
                .getBoolean(SessionManager.KEY_PREF_HIDE_PROMINENT_PERMISSION_DIALOG)
        ) {
            val intent = Intent(this, ProminentActivity::class.java)
            prominentActivityLauncher.launch(intent)
        }
    }


    private fun displayForceProminentDialog() {
        val dialog = AlertDialogBottomSheet(
            this,
            supportFragmentManager
        )
        dialog.setIsShowIcon(true)
        dialog.setIcon(R.drawable.dda_logo)
        dialog.setTitle(getString(R.string.dialog_ask_prominent_title))
        dialog.setMessage(getString(R.string.dialog_ask_prominent_contentf))
        dialog.setButton(getString(R.string.dialog_ask_prominent_btnf))
        dialog.setIsCancelable(false)
        dialog.setActionButton(::displayProminentDialog)
        dialog.setActionOnDismiss(::displayProminentDialog)
        dialog.show()
    }
}
package com.otto.mart.ui.activity.qr.show

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Request.QrStringRequestModel
import com.otto.mart.model.APIModel.Request.qr.CheckStatusQrRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.QrStringResponseModel
import com.otto.mart.model.APIModel.Response.qr.CheckStatusQrResponse
import com.otto.mart.presenter.dao.BENIDAO
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.formValidation.FormValidation
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import com.otto.mart.ui.activity.transaction.balance.OmzetActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.component.dialog.Popup
import com.otto.mart.ui.fragment.ppob.PpobShowQrQrisPaymentFragment
import kotlinx.android.synthetic.main.content_show_dynamic_qr.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class ShowDynamicQrActivity : AppActivity() {

    val API_KEY_GET_STRING_QR = 100

    val mPpobShowQrQrisPaymentFragment = PpobShowQrQrisPaymentFragment()

    var mPin: String = ""
    var mRefLabel: String = ""
    private var mIsValidationEnable = false
    var mTextWatcher: TextWatcher? = null

    var isCheckQrPaymentSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        disableScreenshot()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_dynamic_qr)

        initView()
    }

    override fun onResume() {
        super.onResume()
        disableScreenshot()
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_show_dynamic_qr)

        etAmount.requestFocus()

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) { }

            override fun onTextChanged(charSequence: CharSequence, i:

            Int, i1: Int, i2: Int) {
                var amount = DataUtil.InputDecimal(charSequence.toString())

                etAmount.removeTextChangedListener(mTextWatcher)

                if (amount.equals("")) {
                    etAmount.setText("")
                } else {
                    etAmount.setText(amount)
                }
                etAmount.setSelection(etAmount.text.length)

                etAmount.addTextChangedListener(mTextWatcher)
                isFormValid
            }

            override fun afterTextChanged(editable: Editable) { }
        }

          etAmount.addTextChangedListener(mTextWatcher)

        btnNext.setOnClickListener {
            mIsValidationEnable = true
            if (isFormValid) {
                callGetQrString(
                    etAmount.text.toString().trim().replace(getString(R.string.text_currency), "")
                        .replace(",", "").toDouble()
                )
            }
        }
    }

    /**
     * method to validate Leziz NU form
     * */
    private val isFormValid: Boolean
        get() {
            if (!mIsValidationEnable) {
                return false
            }

            var status = true

            var amount = etAmount.text.toString()

            tvAmountError.gone()

            if (!FormValidation(this).isRequired(amount)) {
                tvAmountError.text = getString(R.string.text_total_peyment_mandatory)
                tvAmountError.visible()
                status = false
            } else if (amount.equals("PHP 0") || amount.equals("PHP 0.")) {
                tvAmountError.text = getString(R.string.text_total_peyment_mandatory)
                tvAmountError.visible()
                status = false
            }

            if (status) {
                btnNext.background =
                    ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
            } else {
                btnNext.background =
                    ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
            }

            return status
        }


    private fun getQrStringSucess(qrStringResponseModel: QrStringResponseModel) {
        mRefLabel = qrStringResponseModel.getRReferenceLabel()

        mPpobShowQrQrisPaymentFragment.mProductName =
            SessionManager.getUserProfile().business_category_name
        mPpobShowQrQrisPaymentFragment.mQrString = qrStringResponseModel.qr_string
        mPpobShowQrQrisPaymentFragment.mAmount = etAmount.text.toString()

        mPpobShowQrQrisPaymentFragment.show(
            supportFragmentManager,
            mPpobShowQrQrisPaymentFragment.getTag()
        )
    }

    private fun checkStatusQrPaymentResponse(title: String, message: String, isSuccess: Boolean) {
        isCheckQrPaymentSuccess = isSuccess

        val dialog = Popup()
        dialog.isHideBtnNegative = true
        dialog.isHideBtnPositive = false
        dialog.positiveButton = getString(R.string.oke)
        dialog.title = title
        dialog.message = message
        dialog.show(supportFragmentManager, "popupInfo")

        dialog.closeAction = ::checkStatusQrDialogClose
        dialog.positiveAction = ::checkStatusQrDialogClose
    }

    fun checkStatusQrDialogClose() {
        if (isCheckQrPaymentSuccess) {
            val intentHome = Intent(this, DashboardActivity::class.java)
            intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intentHome)

            val intent = Intent(this, OmzetActivity::class.java)
            intent.putExtra(OmzetActivity.KEY_IS_FROM_OMZET, true)
            startActivity(intent)
        } else {

        }
    }

    //region API Call

    private fun callGetQrString(amount: Double) {
        showProgressDialog(getString(R.string.loading_message))

        val requestModel = QrStringRequestModel()
        requestModel.amount = amount
        requestModel.feeAmount = 0
        requestModel.feePercentage = 0
        requestModel.storeLabel = SessionManager.getMerchantName()
        requestModel.bill_ref_num = ""

        BENIDAO(this).qrString(
            requestModel,
            BaseDao.getInstance(this, API_KEY_GET_STRING_QR).callback
        )
    }

    override fun onApiResponseCallback(
        br: BaseResponse?,
        responseCode: Int,
        response: Response<*>?
    ) {
        ProgressDialogComponent.dismissProgressDialog(this)
        super.onApiResponseCallback(br, responseCode, response)
        if (responseCode == API_KEY_GET_STRING_QR) {
            if ((br as BaseResponseModel).meta.code == 200) {
                getQrStringSucess((br as QrStringResponseModel))
            } else {
                val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                dialog.setOnDismissListener {
                    finish()
                }
                dialog.show()
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        onApiResponseError()
    }

    fun callCheckStatus() {
        isCheckQrPaymentSuccess = false
        showProgressDialog(getString(R.string.loading_message))

        OttoKonekAPI.checkStatusQrPayment(
            this,
            CheckStatusQrRequest(mRefLabel),
            object : ApiCallback<CheckStatusQrResponse>() {
                override fun onResponseSuccess(body: CheckStatusQrResponse?) {
                    dismissProgressDialog()
                    body?.let {
                        if (isSuccess200) {
                            if (it.data.code.equals("00")) {
                                checkStatusQrPaymentResponse(
                                    "Payment Success",
                                    "Your payment was successful",
                                    true
                                )
                            } else {
                                checkStatusQrPaymentResponse(
                                    "Awaiting Payment",
                                    "Waiting the customer to pay the bill",
                                    false
                                )
                            }
                        } else
                            ErrorDialog(
                                this@ShowDynamicQrActivity,
                                this@ShowDynamicQrActivity,
                                false,
                                false,
                                responseMessage,
                                ""
                            ).show()
                    }
                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                    onApiResponseError()
                }
            })
    }

    //endregion API Call

}

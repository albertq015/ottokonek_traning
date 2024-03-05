package com.otto.mart.ui.activity.qr.payment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.PaymentData
import com.otto.mart.model.APIModel.Request.PayQrInquiryRequestModel
import com.otto.mart.model.APIModel.Request.PaymentOfflineConfirmationRequestModel
import com.otto.mart.model.APIModel.Request.multibank.ValidateQrRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.WalletResponseModel
import com.otto.mart.model.APIModel.Response.payment.QrQrisInquiryResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobPaymentMethod
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.presenter.dao.TransactionDao
import com.otto.mart.presenter.dao.WalletDao
import com.otto.mart.support.util.*
import com.otto.mart.support.util.formValidation.FormValidation
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.activity.transaction.ProcessQRPaymentActivity.KEY_QR_CONTENT
import com.otto.mart.ui.activity.wallet.PaymentSuccessActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.fragment.qr.payment.QrPaymentMethodFragment
import com.otto.mart.ui.fragment.qr.payment.QrSelectNominalFragment
import kotlinx.android.synthetic.main.button_split_payment_select_nominal_.*
import kotlinx.android.synthetic.main.content_qr_payment.*
import kotlinx.android.synthetic.main.ppob_button_payment_method.btnPaymentMethod
import kotlinx.android.synthetic.main.ppob_button_payment_method.imgPaymentMethodIcon
import kotlinx.android.synthetic.main.ppob_button_payment_method.selectLayout
import kotlinx.android.synthetic.main.ppob_button_payment_method.selectedLayout
import kotlinx.android.synthetic.main.ppob_button_payment_method.tvPaymentMeyhod
import kotlinx.android.synthetic.main.qr_button_payment_method.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response
import java.util.*

class QrPaymentActivity : AppActivity() {

    val API_KEY_OMZET_STAT = 100
    val API_KEY_OFFLINE_QR_INQUIRY = 101
    val API_KEY_OFFLINE_QR_PAYMENT = 102

    var mPin: String = ""
    private var mIsValidationEnable = false
    var mTextWatcher: TextWatcher? = null

    private var mQRContent = ""
    private var mWalletId = 0
    private var mPayableAmount: Int = 0
    private var mRrn: String = ""

    val mQrPaymentMethodFragment = QrPaymentMethodFragment()
    val mQrSelectNominalFragment = QrSelectNominalFragment()
    var mPaymentMethodList = mutableListOf<PpobPaymentMethod>()
    var mSelectedPaymentMethod: PpobPaymentMethod? = null
    private var loadingView: View? = null
    private var loading: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_qr_payment)

        if (intent.hasExtra(KEY_QR_CONTENT)) {
            mQRContent = intent?.getStringExtra(KEY_QR_CONTENT)!!
        }

        mPaymentMethodList = getPaymentMethod()

        initView()
        setupLoading()
        getOmzetStatApi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppKeys.KEY_INPUT_PIN) {
                if (data != null) {
                    mPin = data?.getStringExtra(AppKeys.KEY_PIN_CODE)!!
                    callOfflineQrPaymentApi()
                }
            }
        }
    }

    private fun initView() {
        tvToolbarTitle.text = "Pembayaran"
        tvToolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.dark_sky_blue))
        imgToolbarLeft.setImageResource(R.drawable.icon_close_blue)
        imgToolbarLeft.layoutParams = imgToolbarLeft.layoutParams.apply {
            height = 20.px()
            width = 20.px()
        }

        if (Build.VERSION.SDK_INT >= 24) {
            tvMessageTNC.text = Html.fromHtml(getString(R.string.msg_tnc_confirmation), Html.FROM_HTML_MODE_LEGACY)
        } else {
            tvMessageTNC.text = Html.fromHtml(getString(R.string.msg_tnc_confirmation))
        }

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        //Check Split Payment Status
//        val merchantData = EmvcoUtil.parseEMVCOtag62(mQRContent)
//        val qrContentSeparated = Arrays.asList(*merchantData.split("\\s*;;;;\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
//        if (qrContentSeparated[0].equals("RTSM", ignoreCase = true) ||
//                qrContentSeparated[0].equals("KFCO", ignoreCase = true)) {
//             btnPaymentMethod.visible()
//        } else {
//            btnPaymentMethod.gone()
//        }
        paymentMethodLayout.gone()

        mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var amount = DataUtil.getInt(etAmount.text.toString().replace(getString(R.string.text_currency), ""))

                etAmount.removeTextChangedListener(mTextWatcher)
                etAmount.setText(DataUtil.convertCurrency(amount))
                etAmount.setSelection(etAmount.text.length)

                if (amount == 0) {
                    etAmount.setText("")
                }

                isFormValid
                btnNext.isEnabled = etAmount.text.isNotEmpty()
                etAmount.addTextChangedListener(mTextWatcher)
            }

            override fun afterTextChanged(editable: Editable) {}
        }

        etAmount.addTextChangedListener(mTextWatcher)

        btnNext.setOnClickListener {
            mIsValidationEnable = true
            if (isFormValid) {
                inputPin()
            }
        }

        //Call Dynamic QR Payment
        if (mQRContent != "") {
            val requestModel = ValidateQrRequest()
            requestModel.qrData = mQRContent
            callOfflineQrInquiryApi(requestModel)
        }

        btnPaymentMethod.setOnClickListener {
            selectPaymentMethod()
        }

        btnSelectNominal.setOnClickListener {
            selectNominal()
        }
    }

    private fun inputPin() {
        val intent = Intent(this, RegisterPinResetActivity::class.java)
        intent.putExtra(AppKeys.KEY_INPUT_PIN_TYPE_INPUT, true)
        intent.putExtra(RegisterPinResetActivity.LAYOUT_ID, R.layout.activity_pinpad_oc)
        startActivityForResult(intent, AppKeys.KEY_INPUT_PIN)
    }

    fun getPaymentMethod(): MutableList<PpobPaymentMethod> {
        var paymentMethodList = mutableListOf<PpobPaymentMethod>()

        var cash = PpobPaymentMethod(
                "Cash",
                PpobPaymentMethod.DEPOSIT,
                R.drawable.ic_payment_method_cash,
                100000,
                true
        )
        paymentMethodList.add(cash)

        var qr = PpobPaymentMethod(
                "Kode QR",
                PpobPaymentMethod.QR,
                R.drawable.ic_payment_method_qr,
                100000,
                false
        )
        paymentMethodList.add(qr)

        var split = PpobPaymentMethod(
                "Split",
                PpobPaymentMethod.SPLIT,
                R.drawable.ic_payment_method_qr,
                null,
                false
        )
        paymentMethodList.add(split)

        return paymentMethodList
    }

    private fun selectPaymentMethod() {
        mQrPaymentMethodFragment.mPaymentMethodList = mPaymentMethodList
        mQrPaymentMethodFragment.show(supportFragmentManager, mQrPaymentMethodFragment.getTag())

        mQrPaymentMethodFragment.setItemSelectedClickListener(object : QrPaymentMethodFragment.OnPaymentMethodSelected {
            override fun onPaymeentMethodSelected(item: PpobPaymentMethod, position: Int) {
                mSelectedPaymentMethod = item
                mPaymentMethodList.get(position).selected = true
                tvPaymentMeyhod.text = item.name
                imgPaymentMethodIcon.setImageDrawable(item.icon?.let { ContextCompat.getDrawable(this@QrPaymentActivity, it) })

                if (item.balance != null) {
                    tvPaymentMethodBalance.text = DataUtil.convertCurrency(item.balance)
                    tvPaymentMethodBalance.visible()
                } else {
                    tvPaymentMethodBalance.gone()
                }

                if (item.code.equals(PpobPaymentMethod.SPLIT)) {
                    selectNominalLayout.visible()
                } else {
                    selectNominalLayout.gone()
                }

                selectLayout.gone()
                selectedLayout.visible()
            }
        })
    }

    private fun selectNominal() {
        mQrSelectNominalFragment.mTotalPayment = 300000
        mQrSelectNominalFragment.mTotalPoint = 100000
        mQrSelectNominalFragment.mTotalBalance = 2000000
        mQrSelectNominalFragment.funPointSelected = ::pointSelected

        mQrSelectNominalFragment.show(supportFragmentManager, mQrSelectNominalFragment.getTag())
    }

    fun pointSelected(point: Int) {
        val message = "Apply Point : " + point
        message.showToast(this)

        tvSplitNominal.text = "Point" + DataUtil.convertCurrency(point).replace(getString(R.string.text_currency), "") + " + " + "OttoCash Rp100.000"

        nominalSelectLayout.gone()
        nominalSelectedLayout.visible()
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
//                tvAmountError.text = getString(R.string.qr_payment_amount_is_required)
//                tvAmountError.visible()
                status = false
            }

//            if (status) {
//                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
//            } else {
//                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
//            }

            return status
        }


    private fun qrOfflineInquirySuccess(payQrInquiryResponse: QrQrisInquiryResponse) {
//        mRrn = payQrInquiryResponse.data.inquiryData.rrn
//        mPayableAmount = payQrInquiryResponse.data.inquiryData.amount
//        tvMerchantName.text = payQrInquiryResponse.data.inquiryData.merchant_name

        mRrn = payQrInquiryResponse.data.rrn
        mPayableAmount = payQrInquiryResponse.data.amount
        tvMerchantName.text = payQrInquiryResponse.data.merchantName
        tvAddress.text = payQrInquiryResponse.data.merchantAddress
        tvMPAN.text = "MPAN : ${payQrInquiryResponse.data.mpan}"

        if (mPayableAmount != 0) {
//            tvLabelAmount.text = "Jumlah Pembayaran"
            etAmount.setText(DataUtil.convertCurrency(mPayableAmount))
            etAmount.setKeyListener(null)
        } else {
//            tvLabelAmount.text = "Masukan nominal"
        }

        viewAnimator.displayedChild = 1
    }

    private fun qrOfflinePaymentSuccess(basePaymentResponseModel: BasePaymentResponseModel) {
        var ppobPaymentType = PpobProductType()
        ppobPaymentType.type = PpobProductType.QR_PAYMENT

        var ppobPayment = PpobPayment()
        ppobPayment.productName = "QR Payment"
        ppobPayment.ppobProductType = ppobPaymentType

        if (mPayableAmount == 0) {
            ppobPayment.totalPayment = DataUtil.getInt(etAmount.text.toString().replace(getString(R.string.text_currency), "")).toDouble()
        } else {
            ppobPayment.totalPayment = mPayableAmount.toDouble()
        }


        var ppobOttoagPaymentResponseModel = PpobOttoagPaymentResponseModel()
        var data = PaymentData()
        data.keyValueList = basePaymentResponseModel.data.keyValueList
        ppobOttoagPaymentResponseModel.data = data

        loadingView?.findViewById<View>(R.id.llProcess)?.gone()
        loadingView?.findViewById<View>(R.id.llSuccess)?.visible()

        val intent = Intent(this, PaymentSuccessActivity::class.java)
        intent.putExtra("merchantName", tvMerchantName.text.toString())
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA, Gson().toJson(ppobOttoagPaymentResponseModel))


        if (loadingView != null) {
            loadingView?.postDelayed({
                dismissLoading()
                startActivity(intent)
            }, 1000)
        } else {
            dismissLoading()
            startActivity(intent)
        }
    }


    //region API Call

    private fun getOmzetStatApi() {
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show()
        WalletDao(this).emoneySummary(BaseDao.getInstance(this, API_KEY_OMZET_STAT).callback)
    }

    private fun callOfflineQrInquiryApi(requestModel: ValidateQrRequest) {
        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show()
        TransactionDao(this).payQrValidate(requestModel, BaseDao.getInstance(this, API_KEY_OFFLINE_QR_INQUIRY).callback)
    }

    private fun callOfflineQrPaymentApi() {
        btnNext.isEnabled = false
//        ProgressDialogComponent.showProgressDialog(this, "Mohon menunggu", false).show()
        loading?.show()

        val requestModel = PaymentOfflineConfirmationRequestModel()
        requestModel.pin = mPin
        requestModel.rrn = mRrn
        requestModel.wallet_id = mWalletId

        if (mPayableAmount == 0) {
            requestModel.amount = DataUtil.getInt(etAmount.text.toString().replace(getString(R.string.text_currency), ""))
        } else {
            requestModel.amount = mPayableAmount
        }


        //Set Merchant ID (MVCO Tag 62)
        val tag62 = EmvcoUtil.parseEMVCOtag62(mQRContent)
        val merchantIdSeparated = Arrays.asList(*tag62.split("\\s*;;;;\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        if (merchantIdSeparated.size > 1) {
            requestModel.merchant_id = merchantIdSeparated[1]
        }

        TransactionDao(this).payQrPayment(requestModel, BaseDao.getInstance(this, API_KEY_OFFLINE_QR_PAYMENT).callback)
    }

    private fun setupLoading() {
        loadingView = LayoutInflater.from(this).inflate(R.layout.dialog_oc_loading, null)

        loadingView?.let {
            loading = Dialog(this, R.style.full_screen_dialog).apply {
                setContentView(it)
                window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
                setCancelable(false)
            }
        }
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        ProgressDialogComponent.dismissProgressDialog(this)
        validateApiResponse(br)
        super.onApiResponseCallback(br, responseCode, response)
        when (responseCode) {
            API_KEY_OMZET_STAT ->
                if ((br as BaseResponseModel).meta.code == 200) {
                    val walletList = (br as WalletResponseModel).data
                    if (walletList.size > 0) {
                        mWalletId = walletList.get(0).getId()
                    }
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.setOnDismissListener {
                        finish()
                    }
                    if (!this.isFinishing) {
                        dialog.show()
                    }
                }
            API_KEY_OFFLINE_QR_INQUIRY ->
                if ((br as BaseResponseModel).meta.code == 200) {
                    qrOfflineInquirySuccess(br as QrQrisInquiryResponse)
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.setOnDismissListener {
                        finish()
                    }
                    if (!this.isFinishing) {
                        dialog.show()
                    }
                }
            API_KEY_OFFLINE_QR_PAYMENT -> {
                if ((br as BaseResponseModel).meta.code == 200) {
                    qrOfflinePaymentSuccess((br as BasePaymentResponseModel))
                } else {
                    dismissLoading()
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.setOnDismissListener {
                        finish()
                    }
                    if (!this.isFinishing) {
                        dialog.show()
                    }
                }
                btnNext.isEnabled = true
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        dismissLoading()
        onApiResponseError()
    }

    private fun dismissLoading() {
        loading?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    //endregion API Call
}
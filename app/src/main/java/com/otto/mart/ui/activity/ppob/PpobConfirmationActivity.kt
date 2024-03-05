package com.otto.mart.ui.activity.ppob

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.keys.AppKeys.API_KEY_PPOB_ADVICE
import com.otto.mart.model.APIModel.Misc.*
import com.otto.mart.model.APIModel.Request.PpobOttoagPaymentRequestModel
import com.otto.mart.model.APIModel.Request.QrStringRequestModel
import com.otto.mart.model.APIModel.Request.donasi.DonasiPaymentRequest
import com.otto.mart.model.APIModel.Request.donasi.DonasiQrStringRequest
import com.otto.mart.model.APIModel.Request.ppob.PpobPaymentRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PPobPaymentQrResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagInquiryResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.QrStringResponseModel
import com.otto.mart.model.APIModel.Response.donasi.DonasiInquiryResponse
import com.otto.mart.model.APIModel.Response.donasi.DonasiQRPaymentResponse
import com.otto.mart.model.APIModel.Response.donasi.DonasiQrStringResponse
import com.otto.mart.model.APIModel.Response.ppob.PpobCheckStatusQRPaymentResponse
import com.otto.mart.model.APIModel.Response.ppob.PpobInquiryResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobPaymentMethod
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.presenter.dao.BENIDAO
import com.otto.mart.presenter.dao.DonasiDao
import com.otto.mart.presenter.dao.PpobDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.notNull
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.ppob.PpobPaymentDetailAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import com.otto.mart.ui.activity.ppob.setup.PpobConfirmationDataSetup
import com.otto.mart.ui.activity.ppob.setup.PpobPaymentMethodSetup
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.fragment.ppob.PpobPaymentMethodFragment
import com.otto.mart.ui.fragment.ppob.PpobShowQrPaymentFragment
import kotlinx.android.synthetic.main.content_ppob_confirmation.*
import kotlinx.android.synthetic.main.ppob_button_payment_method.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class PpobConfirmationActivity : AppActivity() {

    val API_KEY_PPOB_PAYMENT = 100
    val API_KEY_PPOB_PAYMENT_OLD = 200
    val API_KEY_PPOB_QR_PAYMENT_OLD = 201
    val API_KEY_PPOB_ADVICE_OLD = 202
    val API_KEY_PPOB_GENERATE_QR_STRING = 203

    val CODE_QR_PAYMENT_ON_PROGRESS = 202
    val CODE_ADVICE = 408

    var mPpobPayment: PpobPayment = PpobPayment()
    var mPpobInquiryResponse = PpobInquiryResponse()
    var mDonasiInquiryResponse = DonasiInquiryResponse()
    var mPpobInquiryOldResponse = PpobOttoagInquiryResponseModel()
    var mDonasiPaymentRequest = DonasiPaymentRequest()

    val mPpobShowQrPaymentFragment = PpobShowQrPaymentFragment()
    var mDonasiQrStringResponse: DonasiQrStringResponse? = null
    var mQrContentOld = ""
    val mPpobPaymentRequest = PpobOttoagPaymentRequestModel()

    val mPaymentMethodFragment = PpobPaymentMethodFragment()
    var mPaymentMethodList = mutableListOf<PpobPaymentMethod>()
    var mSelectedPaymentMethod: PpobPaymentMethod? = null

    var mIsCheckStatusQrDone = false
    var mPin: String = ""
    var mAdviceLimit = 8
    var mAdviceCount = 0
    var isAdviceOnProgress = false

    var isPaymentRequest = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_confirmation)

        initData()
        initView()
        initRecyclerView()
        displayPaymentInfo()

        if (mPpobPayment.ppobProductType?.code == AppKeys.PPOB_TYPE_DONASI) {
            paymentMethodSelected(PpobPaymentMethodSetup().getPaymentMethod()[0], 0)
        }
    }

    private fun initData() {
        //collect our intent
        if (intent.hasExtra(AppKeys.KEY_PPOB_PAYMENT_DATA)) {
            mPpobPayment = intent.getParcelableExtra<Parcelable>(AppKeys.KEY_PPOB_PAYMENT_DATA) as PpobPayment
        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_INQUIRY_DATA)) {
            mPpobInquiryResponse = Gson().fromJson(intent.getStringExtra(AppKeys.KEY_PPOB_INQUIRY_DATA), PpobInquiryResponse::class.java)
        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_DONASI_INQUIRY_DATA)) {
            mDonasiInquiryResponse = Gson().fromJson(intent.getStringExtra(AppKeys.KEY_PPOB_DONASI_INQUIRY_DATA), DonasiInquiryResponse::class.java)
        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_DONASI_PAYMENT_REQUEST_DATA)) {
            mDonasiPaymentRequest = Gson().fromJson(intent.getStringExtra(AppKeys.KEY_PPOB_DONASI_PAYMENT_REQUEST_DATA), DonasiPaymentRequest::class.java)
        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_INQUIRY_OLD_DATA)) {
            mPpobInquiryOldResponse = Gson().fromJson(intent.getStringExtra(AppKeys.KEY_PPOB_INQUIRY_OLD_DATA), PpobOttoagInquiryResponseModel::class.java)
        }

        mPaymentMethodList = PpobPaymentMethodSetup().getPaymentMethod()
    }

    override fun onDestroy() {
        mIsCheckStatusQrDone = true
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppKeys.KEY_INPUT_PIN) {
                if (data != null) {
                    mPin = data?.getStringExtra(AppKeys.KEY_PIN_CODE)!!
                }

                if (mPpobPayment.ppobPaymentMethod?.code.equals(PpobPaymentMethod.DEPOSIT)) {
                    if (mPpobPayment.ppobProductType?.type == PpobProductType.DONASI) {
                        ppobDonasiPayment()
                    } else {
                        if (mPpobPayment.ppobProductType?.isOldApi == true) ppobPaymentOld() else ppobPayment()
                    }
                } else {
                    if (mPpobPayment.ppobProductType?.isOldApi == true) {
                        if (mQrContentOld.equals("")) ppobQrPaymentOld() else qrPaymentOldSuccess()
                    } else {
                        if (mDonasiQrStringResponse == null) {
                            getQrString()
                        } else {
                            mDonasiQrStringResponse?.let { getQrStringSuccess() }
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (!mPin.equals("")) {
            backToHome()
        } else
            super.onBackPressed()
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_ppob_confirmation)

        if (mPpobPayment.ppobProductType?.type.equals(PpobProductType.PRABAYAR)) {
            tvLabelPrice.text = getString(R.string.ppob_label_product_price_prepaid)
        } else if (mPpobPayment.ppobProductType?.type.equals(PpobProductType.PASCABAYAR)) {
            tvLabelPrice.text = getString(R.string.ppob_label_product_price_postpaid)
        } else {
            tvLabelPrice.text = getString(R.string.ppob_label_product_price_prepaid)
        }

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        btnPaymentMethod.setOnClickListener {
            //selectPaymentMethod()
        }

        btnNext.setOnClickListener {
            if ((mPpobPayment.ppobProductType?.type == PpobProductType.DONASI) && (mSelectedPaymentMethod == null)) {
                selectPaymentMethod()
            } else {
                inputPin()
            }
        }
    }

    private fun updateButtonNext(isEnable: Boolean){
        btnNext.isEnabled = isEnable

        if (isEnable) {
            btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
        } else {
            btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
        }
    }

    private fun initRecyclerView() {
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    private fun inputPin() {
        val intent = Intent(this, RegisterPinResetActivity::class.java)
        intent.putExtra(AppKeys.KEY_INPUT_PIN_TYPE_INPUT, true)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivityForResult(intent, AppKeys.KEY_INPUT_PIN)
    }

    private fun displayPaymentInfo() {
        var confirmationDataList: MutableList<WidgetBuilderModel>

        when (mPpobPayment.ppobProductType?.code) {
            AppKeys.PPOB_TYPE_PULSA -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getPulsa(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_PULSA_PASCABAYAR -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getPulsaPsacabayar(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_PAKET_DATA -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getPaketData(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_ELECTRICITY_TOKEN -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getListrikToken(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_ELECTRICITY_BILL -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getListrikTagihan(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_AIR -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getAir(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_INTERNET -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getInternet(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_TELKOM -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getDefaultKeyValue(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_TELKOM_INTERNET -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getDefaultKeyValue(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_PENDIDIKAN -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getDefaultKeyValue(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_CICILAN -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getDefaultKeyValue(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_ASURANSI -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getAsuransi(mPpobInquiryOldResponse)
            }
            AppKeys.PPOB_TYPE_TOP_UP -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getTopUp(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_BPJS -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getDefaultKeyValue(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_GAME -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getDefaultKeyValue(mPpobInquiryResponse)
            }
            AppKeys.PPOB_TYPE_DONASI -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getDonasi(mPpobPayment.productName.toString(), mDonasiPaymentRequest)
            }
            AppKeys.PPOB_TYPE_VOUCHER -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getDefaultKeyValue(mPpobInquiryResponse)
            }
            else -> {
                confirmationDataList = PpobConfirmationDataSetup(this).getDefaultKeyValue(mPpobInquiryResponse)
            }
        }

        //Add Payment Method
        if (mPpobPayment.ppobProductType?.type == PpobProductType.DONASI) {
            paymentMethodLayout.visible()
            paymentDetailTopLayout.gone()
        } else {
            var paymentMethod = WidgetBuilderModel()
            paymentMethod.key = getString(R.string.ppob_label_payment_method)
            paymentMethod.value = mPpobPayment.ppobPaymentMethod?.name
            confirmationDataList.add(paymentMethod)
        }

        val adapter = PpobPaymentDetailAdapter(this, confirmationDataList)
        adapter.orientation = PpobPaymentDetailAdapter.VERTICAL
        recyclerview.adapter = adapter

        val komisi = mPpobPayment.komisi!!

        mPpobInquiryResponse.data.notNull {
            var total = DataUtil.getLong(
                    mPpobInquiryResponse.data.total
                            .replace(getString(R.string.text_currency), "")
                            .replace(",", "")) + komisi

            tvModal.text = DataUtil.convertCurrency(mPpobInquiryResponse.data.amount)
            tvKomisi.text = DataUtil.convertCurrency(mPpobPayment.komisi)
            tvTotal.text = DataUtil.convertCurrency(total)
            tvTotalAmount.text = DataUtil.convertCurrency(total)

            if ((mPpobInquiryResponse.data.cashback_omzet != null) && (!mPpobInquiryResponse.data.cashback_omzet.equals("0"))) {
                tvCashback.text = DataUtil.convertCurrency(Integer.valueOf((mPpobInquiryResponse.data.cashback_omzet)))
                cashbackLayout.visible()
            }
        }

        mPpobInquiryOldResponse.data.notNull {
            tvModal.text = DataUtil.convertCurrency(mPpobInquiryOldResponse.data.amount)
            tvKomisi.text = DataUtil.convertCurrency(mPpobPayment.komisi)
            tvTotal.text = DataUtil.convertCurrency(mPpobInquiryOldResponse.data.amount + komisi)
            tvTotalAmount.text = DataUtil.convertCurrency(mPpobInquiryOldResponse.data.amount + komisi)

            if ((mPpobInquiryOldResponse.data.cashback_omzet) != null && (!mPpobInquiryOldResponse.data.cashback_omzet.equals("0"))) {
                tvCashback.text = DataUtil.convertCurrency(Integer.valueOf((mPpobInquiryOldResponse.data.cashback_omzet)))
                cashbackLayout.visible()
            }
        }

        mDonasiInquiryResponse.data.notNull {
            tvTotal.text = DataUtil.convertCurrency(mDonasiInquiryResponse.data.inquiry_data.amount)
            tvTotalAmount.text = DataUtil.convertCurrency(mDonasiInquiryResponse.data.inquiry_data.amount)
        }
    }

    private fun selectPaymentMethod() {
        mPaymentMethodFragment.mPaymentMethodList = mPaymentMethodList
        mPaymentMethodFragment.show(supportFragmentManager, mPaymentMethodFragment.getTag())

        mPaymentMethodFragment.setItemSelectedClickListener(object : PpobPaymentMethodFragment.OnPaymentMethodSelected {
            override fun onPaymeentMethodSelected(item: PpobPaymentMethod, position: Int) {
                paymentMethodSelected(item, position)
            }
        })
    }

    private fun paymentMethodSelected(item: PpobPaymentMethod, position: Int) {
        mPpobPayment.ppobPaymentMethod = item
        mSelectedPaymentMethod = item
        mPaymentMethodList.get(position).selected = true
        tvPaymentMeyhod.text = "Tipe Pembayaran : " + item.name
        imgPaymentMethodIcon.setImageDrawable(item.icon?.let { ContextCompat.getDrawable(this@PpobConfirmationActivity, it) })
        selectLayout.gone()
        selectedLayout.visible()
    }

    private fun paymentSuccess(ppobOttoagPaymentResponseModel: PpobOttoagPaymentResponseModel) {
        val intent = Intent(this, PpobPaymentSuccessActivity::class.java)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, mPpobPayment)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA, Gson().toJson(ppobOttoagPaymentResponseModel))

        if (mPpobPayment.ppobProductType?.isOldApi == true) {
            intent.putExtra(AppKeys.KEY_PPOB_INQUIRY_OLD_DATA, Gson().toJson(mPpobInquiryOldResponse))
        } else {
            intent.putExtra(AppKeys.KEY_PPOB_INQUIRY_DATA, Gson().toJson(mPpobInquiryResponse))
        }

        if (mPpobPayment.ppobProductType?.type == PpobProductType.DONASI) {
            intent.putExtra(AppKeys.KEY_PPOB_DONASI_INQUIRY_DATA, Gson().toJson(mDonasiInquiryResponse))
        }

        startActivity(intent)
    }

    private fun getQrStringSuccess() {
        mIsCheckStatusQrDone = false

        mPpobShowQrPaymentFragment.mProductName = mPpobPayment.productName.toString()
        mPpobShowQrPaymentFragment.mQrString = mDonasiQrStringResponse?.data!!.qr_string

        if (mPpobPayment.ppobProductType?.type == PpobProductType.DONASI) {
            mPpobShowQrPaymentFragment.mAmount = DataUtil.convertCurrency(mDonasiInquiryResponse.data.inquiry_data.amount)
        } else {
            mPpobShowQrPaymentFragment.mAmount = mPpobInquiryResponse.data.total
        }

        mPpobShowQrPaymentFragment.show(supportFragmentManager, mPpobShowQrPaymentFragment.getTag())

        if (mPpobPayment.ppobProductType?.type == PpobProductType.DONASI) {
            callBillerCheckStatusQrPayment()
        } else {
            callCheckStatusQrPayment()
        }
    }

    private fun qrPaymentOldSuccess() {
        mIsCheckStatusQrDone = false

        mPpobShowQrPaymentFragment.mProductName = mPpobPayment.productName.toString()
        mPpobShowQrPaymentFragment.mAmount = DataUtil.convertCurrency(mPpobInquiryOldResponse.data.amount)
        mPpobShowQrPaymentFragment.mQrString = mQrContentOld
        mPpobShowQrPaymentFragment.show(supportFragmentManager, mPpobShowQrPaymentFragment.getTag())

        callAdviceOld()
    }

    private fun getQrStringOldSuccess(qrStringResponseModel: QrStringResponseModel) {
        mQrContentOld = qrStringResponseModel.qr_string

        mIsCheckStatusQrDone = false

        mPpobShowQrPaymentFragment.mProductName = mPpobPayment.productName.toString()
        mPpobShowQrPaymentFragment.mAmount = DataUtil.convertCurrency(mPpobInquiryOldResponse.data.amount + mPpobPayment.komisi!!)
        mPpobShowQrPaymentFragment.mQrString = mQrContentOld
        mPpobShowQrPaymentFragment.show(supportFragmentManager, mPpobShowQrPaymentFragment.getTag())

        callAdviceOld()
    }

    private fun checkStatusQrPaymentSuccess(ppobCheckStatusQRPaymentResponse: PpobCheckStatusQRPaymentResponse) {
        mIsCheckStatusQrDone = true
        mPpobShowQrPaymentFragment.dismiss()

        var ppobOttoagPaymentResponseModel = PpobOttoagPaymentResponseModel()
        var data = PaymentData()
        data.keyValueList = ppobCheckStatusQRPaymentResponse.data.key_value_list
        ppobOttoagPaymentResponseModel.data = data

        paymentSuccess(ppobOttoagPaymentResponseModel)
    }

    private fun checkDonasiStatusQrPaymentSuccess(donasiQRPaymentResponse: DonasiQRPaymentResponse) {
        mIsCheckStatusQrDone = true
        mPpobShowQrPaymentFragment.dismiss()

        var ppobOttoagPaymentResponseModel = PpobOttoagPaymentResponseModel()
        var data = PaymentData()
        data.keyValueList = donasiQRPaymentResponse.data.key_value_list
        ppobOttoagPaymentResponseModel.data = data

        paymentSuccess(ppobOttoagPaymentResponseModel)
    }

    private fun backToHome() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    //region API Call

    private fun ppobPayment() {
        updateButtonNext(false)
        ProgressDialogComponent.showProgressDialog(this@PpobConfirmationActivity, getString(R.string.loading_message), false).show()

        val ppobPaymentRequest = PpobPaymentRequest()
        ppobPaymentRequest.admin_fee = mPpobInquiryResponse.getData().getAdmin_fee()
        ppobPaymentRequest.amount = mPpobInquiryResponse.getData().getAmount()
        ppobPaymentRequest.commission = mPpobPayment.komisi!!
        ppobPaymentRequest.cust_id = mPpobInquiryResponse.getData().getCust_id()
        ppobPaymentRequest.email = mPpobPayment.email
        ppobPaymentRequest.product_code = mPpobInquiryResponse.getData().getProduct_code()
        ppobPaymentRequest.rc = mPpobInquiryResponse.getData().getRc()
        ppobPaymentRequest.rrn = mPpobInquiryResponse.getData().getRrn()
        ppobPaymentRequest.pin = mPin

        PpobDao(this).ppobPayment(ppobPaymentRequest, BaseDao.getInstance(this, API_KEY_PPOB_PAYMENT).callback)
        isPaymentRequest = true
    }

    private fun ppobDonasiPayment() {
        updateButtonNext(false)
        ProgressDialogComponent.showProgressDialog(this@PpobConfirmationActivity, getString(R.string.loading_message), false).show()

        DonasiDao(this@PpobConfirmationActivity)
                .billerPayment(mDonasiPaymentRequest,
                        BaseDao.getInstance(this@PpobConfirmationActivity, API_KEY_PPOB_PAYMENT).callback)
        isPaymentRequest = true
    }

    private fun ppobPaymentOld() {
        updateButtonNext(false)
        ProgressDialogComponent.showProgressDialog(this@PpobConfirmationActivity, getString(R.string.loading_message), false).show()

        val komisi = mPpobPayment.komisi!!

        val paymentPriceModel = PaymentPriceModel()
        paymentPriceModel.total = mPpobInquiryOldResponse.data.amount
        paymentPriceModel.komisi = komisi
        paymentPriceModel.modal = mPpobInquiryOldResponse.data.amount

        val pulsaPaketModel = PulsaPaketModel(mPpobPayment.custNumber, mPpobPayment.ppobProductType?.name)
        val confirmationModel = PaymentConfirmationModel()
        confirmationModel.paymentPriceModel = paymentPriceModel
        confirmationModel.pulsaPaketModel = pulsaPaketModel
        mPpobPaymentRequest.setConfirmationModel(confirmationModel)

        mPpobPaymentRequest.setAmount(mPpobInquiryOldResponse.data.amount)
        mPpobPaymentRequest.setProduct_code(mPpobInquiryOldResponse.data.productcode)
        mPpobPaymentRequest.setInquiry_data(mPpobInquiryOldResponse.data.rrn)
        mPpobPaymentRequest.setWallet_id(SessionManager.getWalletID())
        mPpobPaymentRequest.setAdmin_fee(mPpobInquiryOldResponse.data.adminfee)

        if (mPpobPayment.ppobProductType?.code.equals(AppKeys.PPOB_TYPE_BPJS)) {
            mPpobPaymentRequest.setCustomer_reference(mPpobPayment.custNumber)
        } else {
            mPpobPaymentRequest.setCustomer_reference(mPpobInquiryOldResponse.custid)
        }

        mPpobPaymentRequest.setSelling_price(mPpobInquiryOldResponse.data.amount)
        mPpobPaymentRequest.pin = mPin
        mPpobPaymentRequest.setProduct_type(mPpobPayment.ppobProductType?.code)

        if ((mPpobPayment.ppobProductType?.code == AppKeys.PPOB_TYPE_AIR) ||
                (mPpobPayment.ppobProductType?.code == AppKeys.PPOB_TYPE_GAME)) {
            mPpobPaymentRequest.setSub_amount(mPpobInquiryOldResponse.data.amount)
        } else {
            mPpobPaymentRequest.setSub_amount(mPpobInquiryOldResponse.data.amount - mPpobInquiryOldResponse.data.adminfee)
        }

        PpobDao(this).ppobPaymentOld(mPpobPayment.ppobProductType?.code, mPpobPaymentRequest, BaseDao.getInstance(this, API_KEY_PPOB_PAYMENT).callback)
        isPaymentRequest = true
    }

    private fun ppobQrPaymentOld() {
        ProgressDialogComponent.showProgressDialog(this@PpobConfirmationActivity, getString(R.string.loading_message), false).show()

        val komisi = mPpobPayment.komisi!!

        val paymentPriceModel = PaymentPriceModel()
        paymentPriceModel.total = mPpobInquiryOldResponse.data.amount
        paymentPriceModel.komisi = komisi
        paymentPriceModel.modal = mPpobInquiryOldResponse.data.amount

        val pulsaPaketModel = PulsaPaketModel(mPpobPayment.custNumber, mPpobPayment.ppobProductType?.name)
        val confirmationModel = PaymentConfirmationModel()
        confirmationModel.paymentPriceModel = paymentPriceModel
        confirmationModel.pulsaPaketModel = pulsaPaketModel
        mPpobPaymentRequest.setConfirmationModel(confirmationModel)

        mPpobPaymentRequest.setAmount(mPpobInquiryOldResponse.data.amount)
        mPpobPaymentRequest.setProduct_code(mPpobInquiryOldResponse.data.productcode)
        mPpobPaymentRequest.setInquiry_data(mPpobInquiryOldResponse.data.rrn)
        mPpobPaymentRequest.setWallet_id(SessionManager.getWalletID())
        mPpobPaymentRequest.setAdmin_fee(mPpobInquiryOldResponse.data.adminfee)
        mPpobPaymentRequest.setCustomer_reference(mPpobInquiryOldResponse.custid)
        mPpobPaymentRequest.setSelling_price(mPpobInquiryOldResponse.data.amount + komisi)
        mPpobPaymentRequest.pin = mPin
        mPpobPaymentRequest.setProduct_type(mPpobPayment.ppobProductType?.code)

        if ((mPpobPayment.ppobProductType?.code == AppKeys.PPOB_TYPE_AIR) ||
                (mPpobPayment.ppobProductType?.code == AppKeys.PPOB_TYPE_GAME)) {
            mPpobPaymentRequest.setSub_amount(mPpobInquiryOldResponse.data.amount)
        } else {
            mPpobPaymentRequest.setSub_amount(mPpobInquiryOldResponse.data.amount - mPpobInquiryOldResponse.data.adminfee)
        }

        PpobDao(this).ppobQrPaymentOld(mPpobPayment.ppobProductType?.code, mPpobPaymentRequest, BaseDao.getInstance(this, API_KEY_PPOB_QR_PAYMENT_OLD).callback)
        isPaymentRequest = true
    }

    private fun getQrString() {
        ProgressDialogComponent.showProgressDialog(this@PpobConfirmationActivity, getString(R.string.loading_message), false).show()

        val donasiQrStringRequest = DonasiQrStringRequest()

        if (mPpobPayment.ppobProductType?.type == PpobProductType.DONASI) {
            donasiQrStringRequest.amount = mDonasiInquiryResponse.data.inquiry_data.amount
            donasiQrStringRequest.bill_ref_num = mDonasiInquiryResponse.data.inquiry_data.rrn
        } else {
            donasiQrStringRequest.amount = mPpobInquiryResponse.data.amount
            donasiQrStringRequest.bill_ref_num = mPpobInquiryResponse.data.rrn
        }

        DonasiDao(this@PpobConfirmationActivity).billerQrString(donasiQrStringRequest, BaseDao.getInstance(this@PpobConfirmationActivity, AppKeys.API_KEY_DONASI_QR_STRING).callback)
    }

    private fun callCheckStatusQrPayment() {
        val ppobPaymentRequest = PpobPaymentRequest()
        ppobPaymentRequest.admin_fee = mPpobInquiryResponse.getData().getAdmin_fee()
        ppobPaymentRequest.amount = mPpobInquiryResponse.getData().getAmount()
        ppobPaymentRequest.commission = mPpobPayment.komisi!!
        ppobPaymentRequest.cust_id = mPpobInquiryResponse.getData().getCust_id()
        ppobPaymentRequest.email = mPpobPayment.email
        ppobPaymentRequest.product_code = mPpobInquiryResponse.getData().getProduct_code()
        ppobPaymentRequest.rc = mPpobInquiryResponse.getData().getRc()
        ppobPaymentRequest.rrn = mPpobInquiryResponse.getData().getRrn()
        ppobPaymentRequest.pin = mPin

        PpobDao(this@PpobConfirmationActivity).ppobCheckStatusQr(ppobPaymentRequest,
                BaseDao.getInstance(this@PpobConfirmationActivity, AppKeys.API_KEY_PPOB_CHECK_STATUS_QR_PAYMENT).callback)
    }

    private fun callBillerCheckStatusQrPayment() {
        DonasiDao(this@PpobConfirmationActivity).billerCheckStatusQrPayment(mDonasiPaymentRequest,
                BaseDao.getInstance(this@PpobConfirmationActivity, AppKeys.API_KEY_DONASI_CHECK_STATUS_QR_PAYMENT).callback)
    }

    private fun callAdvice() {
        ProgressDialogComponent.showProgressDialog(this@PpobConfirmationActivity, getString(R.string.loading_message), false).show()

        val ppobPaymentRequest = PpobPaymentRequest()
        ppobPaymentRequest.admin_fee = mPpobInquiryResponse.getData().getAdmin_fee()
        ppobPaymentRequest.amount = mPpobInquiryResponse.getData().getAmount()
        ppobPaymentRequest.commission = mPpobPayment.komisi!!
        ppobPaymentRequest.cust_id = mPpobInquiryResponse.getData().getCust_id()
        ppobPaymentRequest.email = mPpobPayment.email
        ppobPaymentRequest.product_code = mPpobInquiryResponse.getData().getProduct_code()
        ppobPaymentRequest.rc = mPpobInquiryResponse.getData().getRc()
        ppobPaymentRequest.rrn = mPpobInquiryResponse.getData().getRrn()
        ppobPaymentRequest.pin = mPin

        PpobDao(this@PpobConfirmationActivity).ppobAdvice(ppobPaymentRequest, BaseDao.getInstance(this@PpobConfirmationActivity, API_KEY_PPOB_ADVICE).callback)
    }

    private fun callAdviceOld() {
        ProgressDialogComponent.showProgressDialog(this@PpobConfirmationActivity, getString(R.string.loading_message), false).show()

        if (!mIsCheckStatusQrDone) {
            Handler().postDelayed({
                PpobDao(this@PpobConfirmationActivity).OttoagAdviceDao(mPpobPayment.ppobProductType?.code, mPpobPaymentRequest, BaseDao.getInstance(this@PpobConfirmationActivity, API_KEY_PPOB_ADVICE_OLD).callback)
            }, 2000)
        }
    }

    private fun callGenerateQrStringApi(data: PPobPaymentQrResponseModel.PpobPaymentQrDataModel) {
        ProgressDialogComponent.showProgressDialog(this@PpobConfirmationActivity, getString(R.string.loading_message), false).show()

        var komisi = 0.0
        mPpobPayment.komisi?.let {
            komisi = it.toDouble()
        }

        val requestModel = QrStringRequestModel()
        requestModel.bill_ref_num = data.billerRefNum
        requestModel.amount = mPpobInquiryOldResponse.amount.toDouble() + komisi

        BENIDAO(this).doGetQrStringOttoDAO(requestModel, BaseDao.getInstance(this, API_KEY_PPOB_GENERATE_QR_STRING).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        try {
            ProgressDialogComponent.dismissProgressDialog(this)
            super.onApiResponseCallback(br, responseCode, response)
            if (response != null) {
                if (response.isSuccessful) {
                    when (responseCode) {
                        API_KEY_PPOB_PAYMENT -> {
                            isPaymentRequest = false
                            if ((br as BaseResponseModel).meta.code == 200) {
                                paymentSuccess((br as PpobOttoagPaymentResponseModel))
                            } else if (br.meta.code == CODE_ADVICE) {
                                isAdviceOnProgress = true
                                if (mPpobPayment.ppobProductType?.isOldApi == true) {
                                    callAdviceOld()
                                } else {
                                    callAdvice()
                                }
                            } else {
                                if (!this.isFinishing) {
                                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                                    dialog.setOnDismissListener {
                                        backToHome()
                                    }
                                    dialog.show()
                                }
                            }
                            updateButtonNext(true)
                        }
                        AppKeys.API_KEY_DONASI_QR_STRING -> if ((br as BaseResponseModel).meta.code == 200) {
                            mDonasiQrStringResponse = br as DonasiQrStringResponse
                            getQrStringSuccess()
                        } else {
                            val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                            dialog.setOnDismissListener {
                                backToHome()
                            }
                            dialog.show()
                        }
                        AppKeys.API_KEY_PPOB_CHECK_STATUS_QR_PAYMENT -> if ((br as BaseResponseModel).meta.code == 200) {
                            checkStatusQrPaymentSuccess(br as PpobCheckStatusQRPaymentResponse)
                        } else if (br.meta.code == CODE_QR_PAYMENT_ON_PROGRESS) {
                            if (!mIsCheckStatusQrDone) {
                                Handler().postDelayed({ callCheckStatusQrPayment() }, 2000)
                            }
                        } else if (br.meta.code == CODE_ADVICE) {
                            isAdviceOnProgress = true
                            callAdvice()
                        } else {
                            val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                            dialog.setOnDismissListener {
                                backToHome()
                            }
                            dialog.show()
                        }
                        AppKeys.API_KEY_DONASI_CHECK_STATUS_QR_PAYMENT -> if ((br as BaseResponseModel).meta.code == 200) {
                            checkDonasiStatusQrPaymentSuccess(br as DonasiQRPaymentResponse)
                        } else if (br.meta.code == CODE_QR_PAYMENT_ON_PROGRESS) {
                            if (!mIsCheckStatusQrDone) {
                                Handler().postDelayed({ callBillerCheckStatusQrPayment() }, 2000)
                            }
                        } else if (br.meta.code == CODE_ADVICE) {
                            isAdviceOnProgress = true
                            callAdvice()
                        } else {
                            val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                            dialog.setOnDismissListener {
                                backToHome()
                            }
                            dialog.show()
                        }
                        API_KEY_PPOB_QR_PAYMENT_OLD -> if ((br as BaseResponseModel).meta.code == 200) {
                            isPaymentRequest = false
                            callGenerateQrStringApi((br as PPobPaymentQrResponseModel).data)
                        } else {
                            val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                            dialog.setOnDismissListener {
                                backToHome()
                            }
                            dialog.show()
                        }
                        API_KEY_PPOB_GENERATE_QR_STRING -> if ((br as BaseResponseModel).meta.code == 200) {
                            getQrStringOldSuccess((br as QrStringResponseModel))
                        } else {
                            val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                            dialog.setOnDismissListener {
                                backToHome()
                            }
                            dialog.show()
                        }
                        API_KEY_PPOB_ADVICE -> if ((br as BaseResponseModel).meta.code == 200) {
                            paymentSuccess((br as PpobOttoagPaymentResponseModel))
                        } else if (br.meta.code == CODE_ADVICE) {
                            mAdviceCount++
                            if (mAdviceCount <= mAdviceLimit) {
                                callAdvice()
                            } else {
                                if (!this.isFinishing) {
                                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                                    dialog.setOnDismissListener {
                                        backToHome()
                                    }
                                    dialog.show()
                                }
                            }
                        } else {
                            val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                            dialog.setOnDismissListener {
                                backToHome()
                            }
                            dialog.show()
                        }
                        API_KEY_PPOB_ADVICE_OLD -> if ((br as BaseResponseModel).meta.code == 200) {
                            mIsCheckStatusQrDone = true
                            paymentSuccess((br as PpobOttoagPaymentResponseModel))
                        } else {
                            if (isAdviceOnProgress) {
                                mAdviceCount++
                                if (mAdviceCount <= mAdviceLimit) {
                                    callAdviceOld()
                                }
                            } else {
                                if (!mIsCheckStatusQrDone) {
                                    Handler().postDelayed({ callAdviceOld() }, 2000)
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        if (isPaymentRequest) {
            isPaymentRequest = false
            onApiResponseError("Transaksi sedang diproses. Silakan periksa riwayat transaksi Anda dan periksa kembali ke pelanggan. Jika 1x24 Jam pembayaran anda belum masuk, silahkan klik hubungi kami di profile")
        } else onApiResponseError()
    }

    //endregion API Call
}

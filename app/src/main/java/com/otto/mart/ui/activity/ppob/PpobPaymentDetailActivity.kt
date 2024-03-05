package com.otto.mart.ui.activity.ppob

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.BuildConfig
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.keys.AppKeys
import com.otto.mart.keys.AppKeys.API_KEY_TRANSACTION_ADVICE
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Request.PpobTransactionAdviceModel
import com.otto.mart.model.APIModel.Request.history.HistoryDetailRequest
import com.otto.mart.model.APIModel.Request.refund.MerchantRefundRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.history.HistoryDetailResponse
import com.otto.mart.model.APIModel.Response.multibank.ReceiptAdressResponse
import com.otto.mart.model.APIModel.Response.refund.CheckRefundStatusResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.presenter.dao.PpobDao
import com.otto.mart.presenter.dao.WalletDao
import com.otto.mart.presenter.dao.refund.RefundDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.*
import com.otto.mart.ui.Partials.adapter.ppob.ContactReceiptAdapter
import com.otto.mart.ui.Partials.adapter.ppob.PpobPaymentDetailAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import com.otto.mart.ui.activity.transaction.refund.RefundConfirrmationActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_ppob_payment_detail.*
import kotlinx.android.synthetic.main.part_ppob_payment_detail.*
import kotlinx.android.synthetic.main.toolbar.btnToolbarBack
import kotlinx.android.synthetic.main.toolbar.btnToolbarRight
import kotlinx.android.synthetic.main.toolbar.tvToolbarTitle
import kotlinx.android.synthetic.main.toolbar_receipt.*
import retrofit2.Response
import java.io.OutputStream

class PpobPaymentDetailActivity : AppActivity() {

    companion object {
        const val KEY_IS_SHARE = "share"
        const val KEY_IS_FROM_RECEIPT = "from_receipt"
        const val KEY_IS_FROM_OMZET = "is_from_omzet"
        const val KEY_STATUS = "status"
        const val KEY_REFERENCE_NUMBER = "reference_number"
    }

    val API_KEY_CHECK_STATUS_REFUND = 100
    val API_KEY_MERCHANT_REFUND = 101
    val API_KEY_HISTORY_DETAIL = 102

    var mPpobOttoagPaymentResponseModel: PpobOttoagPaymentResponseModel? = null
    var mPpobPayment: PpobPayment = PpobPayment()
    var mContactReceipt: ReceiptAdressResponse? = null

    var mIsShare = false
    var mIsFromReceipt = false
    var mIsBackToHome = false
    var mStatus = ""
    var mReferenceNumber = ""
    var mIsFromOmzet = false
    var GOTO_HOME = "need_to_go_home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_payment_detail)
        initData()
        initHeaderFooterData()
        initView()
        initRecyclerView()

        if (mReferenceNumber.equals("")) {
            displayDetailHistory()
        } else {
//            if (mIsFromOmzet) {
//                getHistoryDetail()
//            } else {
//                displayDetailHistory()
//            }
            displayDetailHistory()
        }

        if (mPpobPayment.ppobProductType?.code.equals(AppKeys.PPOB_TYPE_AIR)) {
            mPpobOttoagPaymentResponseModel?.data?.uimsg?.let {
                displayHeaderFooter(it)
            }
        }

        if (mIsShare) {
            Handler().postDelayed({
                validateShareContent()
            }, 500)
        }

        if (mIsFromReceipt) {
            Handler().postDelayed({
                shareReceipt()
                finish()
            }, 500)
        }

        if (!mReferenceNumber.equals("")) {
            //checkRefundStatus()
        }
    }

    private fun initData() {
        if (intent.hasExtra(AppKeys.KEY_PPOB_PAYMENT_DATA)) {
            mPpobPayment =
                intent.getParcelableExtra<Parcelable>(AppKeys.KEY_PPOB_PAYMENT_DATA) as PpobPayment
        }

        if (intent.hasExtra(GOTO_HOME)) {
            mIsBackToHome = true
        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA)) {
            mPpobOttoagPaymentResponseModel = Gson().fromJson(
                intent.getStringExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA),
                PpobOttoagPaymentResponseModel::class.java
            )

            mContactReceipt = Gson().fromJson(
                intent.getStringExtra(AppKeys.KEY_CONTACT_PAYMENT_SUCCESS_DATA),
                ReceiptAdressResponse::class.java
            )
        }

        if (intent.hasExtra(KEY_IS_SHARE)) {
            mIsShare = intent.getBooleanExtra(KEY_IS_SHARE, false)
        }

        if (intent.hasExtra(KEY_IS_FROM_OMZET)) {
            mIsFromOmzet = intent.getBooleanExtra(KEY_IS_FROM_OMZET, false)
            mContactReceipt = Gson().fromJson(
                intent.getStringExtra(AppKeys.KEY_CONTACT_PAYMENT_SUCCESS_DATA),
                ReceiptAdressResponse::class.java
            )
        }

        if (intent.hasExtra(KEY_IS_FROM_RECEIPT)) {
            mIsFromReceipt = intent.getBooleanExtra(KEY_IS_FROM_RECEIPT, false)
        }

        if (intent.hasExtra(KEY_REFERENCE_NUMBER)) {
            mReferenceNumber = intent?.getStringExtra(KEY_REFERENCE_NUMBER)!!
        }

        if (intent.hasExtra(KEY_STATUS)) {
            mStatus = intent?.getStringExtra(KEY_STATUS)!!
        }
    }

    private fun initHeaderFooterData() {
        setValue(tvMerchantMID, SessionManager.getMID().replace("RTSM;;;;", ""))
        setValue(tvMerchantMPAN, SessionManager.getMPAN())
        setValue(tvMerchantNMID, SessionManager.getNMID())
        setValue(tvMerchantTID, SessionManager.getTID())
        setValue(tvMerchantAdrress, SessionManager.getDefaultAddress())
        setValue(tvMerchantName, SessionManager.getUserProfile().merchant_name)

        // call api contactUs v1
//        ContactUsRepo().getContactUs {
//            it?.let {
//                tvWA.text = it.telepon
//                tvEmail.text = it.email
//                tvWorkAvailability.text = it.support_hours
//            } ?: getString(R.string.text_error).showToast(this)
//        }

        tvVersion.text = "Ver ${BuildConfig.VERSION_NAME}"
    }

    private fun setValue(view: TextView, value: String?) {
        val newValue = value?.let { if (it.isNotEmpty()) it else "-" } ?: "-"
        view.text = newValue
    }

    override fun onResume() {
        super.onResume()
        if (mStatus.equals("")) {
            disableScreenshot()
        }
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_ppob_payment_detail)
        imgToolbarLeft.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close))

        btnToolbarRight2.gone()
        btnToolbarRight3.visible()

        if (mStatus.equals(getString(R.string.text_in_process), ignoreCase = true)) {
            btnCheckStatus.visible()
            bottomLayout.visible()
        }

        btnToolbarBack.setOnClickListener {
            if (mIsBackToHome) {
                backToHome()
            } else {
                backToHome()
            }
        }


        btnToolbarRight.setOnClickListener {
            gotoRefund()
        }

        btnToolbarRight2.setOnClickListener {
            print()
        }

        btnToolbarRight3.setOnClickListener {
            validateShareContent()
        }

        btnCheckStatus.setOnClickListener {
            checkStatus(mReferenceNumber)
        }
    }

    private fun initRecyclerView() {
        rvReceipt.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        rvReceipt.setLayoutManager(linearLayoutManager)

        //contact
        rvReceiptContact

        rvReceiptContact.setHasFixedSize(true)
        val linearLayoutManagerContact = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        rvReceiptContact.setLayoutManager(linearLayoutManagerContact)
    }

    private fun displayTransactionDetail(keyValueList: MutableList<WidgetBuilderModel>) {
        val adapter = PpobPaymentDetailAdapter(this, keyValueList)
        adapter.layout = R.layout.item_ppob_payment_detail_horizontal
        adapter.displayBarcode = ::displayBarcode
        rvReceipt.adapter = adapter
        viewAnimator.displayedChild = 1

        mPpobOttoagPaymentResponseModel?.data?.uimsg?.let {
            displayHeaderFooter(it)
        }


    }

    fun displayContactReceipt(keyValueList: MutableList<WidgetBuilderModel>) {
        val adapterContact = ContactReceiptAdapter(this, keyValueList)
        adapterContact.layout = R.layout.item_ppob_payment_detail_horizontal

        rvReceiptContact.adapter = adapterContact
        viewAnimator.displayedChild = 1
    }

    //Display Header & Footer PDAM Surabaya
    private fun displayHeaderFooter(uiMsg: String) {
        if (uiMsg != "" && uiMsg.contains("|")) {
            val uiMeessage = uiMsg?.replace("|", "#")
            val separatedHeaderFooters =
                uiMeessage?.split("#".toRegex())?.dropLastWhile({ it.isEmpty() })?.toTypedArray()

            separatedHeaderFooters?.let {
                if (it.isNotEmpty()) {
                    tvHeader.setText(separatedHeaderFooters?.get(0))
                    tvHeader.visibility = View.VISIBLE
                }

                if (it.size > 1) {
                    tvFooter.setText(separatedHeaderFooters?.get(1))
                    tvFooter.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun displayDetailHistory() {
        mPpobOttoagPaymentResponseModel?.let {
            displayTransactionDetail(it.data.keyValueList)
        }

        mContactReceipt?.let {
            displayContactReceipt(it.data.keyValueList)
        }


    }

    private fun displayBarcode(content: String) {
        tvBarcode.text = content

        val bitmap =
            BitmapUtil.generateBitmap(content, 8, DeviceUtil.dpToPx(180), DeviceUtil.dpToPx(60))
        ivBarcode.setImageBitmap(bitmap)
    }

    private fun validateShareContent() {
        var isContainKomisi = false

        mPpobOttoagPaymentResponseModel?.let {
            for (widgetBuilderModel in it.data.keyValueList) {
                if (!widgetBuilderModel.key.equals(getString(R.string.text_comission), true)) {
                    isContainKomisi = true
                }
            }
        }
        mContactReceipt?.let {
            for (widgetBuilderModel in it.data.keyValueList) {
                if (!widgetBuilderModel.key.equals(getString(R.string.text_comission), true)) {
                    isContainKomisi = true
                }
            }
        }

        if (!isContainKomisi) {
            shareReceipt()
            finish()
        } else {
            gotoTransactionDetail()
        }
    }

    override fun onBackPressed() {
        backToHome()
    }

    private fun gotoTransactionDetail() {
        var displayList = mutableListOf<WidgetBuilderModel>()
        var displayListContact = mutableListOf<WidgetBuilderModel>()


        mPpobOttoagPaymentResponseModel?.let {
            for (widgetBuilderModel in it.data.keyValueList) {
                if (!widgetBuilderModel.key.equals(getString(R.string.text_comission), true)) {
                    displayList.add(widgetBuilderModel)
                }
            }
        }
        mContactReceipt?.let {
            for (widgetBuilderModel in it.data.keyValueList) {
                if (!widgetBuilderModel.key.equals(getString(R.string.text_comission), true)) {
                    displayListContact.add(widgetBuilderModel)
                }
            }
        }


        mContactReceipt?.data?.keyValueList = displayListContact
        mPpobOttoagPaymentResponseModel?.data?.keyValueList = displayList

        val intent = Intent(this, PpobPaymentDetailActivity::class.java)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, mPpobPayment)
        intent.putExtra(
            AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA,
            Gson().toJson(mPpobOttoagPaymentResponseModel)
        )
        intent.putExtra(
            AppKeys.KEY_CONTACT_PAYMENT_SUCCESS_DATA,
            Gson().toJson(mContactReceipt)
        )
        intent.putExtra(KEY_IS_SHARE, false)
        intent.putExtra(KEY_IS_FROM_RECEIPT, true)
        startActivity(intent)
    }

    private fun gotoRefund() {
        val intent = Intent(this, RefundConfirrmationActivity::class.java)
        intent.putExtra(
            AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA,
            Gson().toJson(mPpobOttoagPaymentResponseModel)
        )
        intent.putExtra(RefundConfirrmationActivity.KEY_RRN, mReferenceNumber)
        startActivity(intent)
    }

    private fun backToHome() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun shareReceipt() {

        val bitmapView = BitmapUtil.getBitmapFromView(
            masterLayout,
            masterLayout.getChildAt(0).height,
            masterLayout.getChildAt(0).width
        )

        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val outstream: OutputStream?
        try {
            outstream = contentResolver.openOutputStream(uri!!)
            bitmapView.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
            outstream!!.close()
        } catch (e: Exception) {
            System.err.println(e.toString())
        }

        share.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(share, getString(R.string.ppob_msg_share_it)))
    }

    private fun print() {
        val dialog =
            ErrorDialog(this, this, false, false, getString(R.string.text_upcoming_feature), "")
        dialog.show()
    }

//region API Call

    private fun checkStatus(referenceNumber: String) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        val ppobTransactionAdviceModel = PpobTransactionAdviceModel()
        ppobTransactionAdviceModel.reference_number =
            CryptoUtil.encryptRSA(referenceNumber.toByteArray())

        val dao = PpobDao(this)
//        dao.ppobCheckPending(ppobTransactionAdviceModel, BaseDao.getInstance(this, API_KEY_TRANSACTION_ADVICE).callback)
        OttoKonekAPI.qrCheckStatus(
            this,
            ppobTransactionAdviceModel,
            object : ApiCallback<PpobOttoagPaymentResponseModel>() {
                override fun onResponseSuccess(body: PpobOttoagPaymentResponseModel?) {
                    if (!isFinishing) ProgressDialogComponent.dismissProgressDialog(this@PpobPaymentDetailActivity)
                    body?.let {
                        if (isSuccess200) {
                            if (it.getData().keyValueList.size > 0) {
                                btnCheckStatus.gone()
                                bottomLayout.gone()
                            } else {
                                val dialog = ErrorDialog(
                                    this@PpobPaymentDetailActivity,
                                    this@PpobPaymentDetailActivity,
                                    false,
                                    false,
                                    body.meta.message,
                                    ""
                                )
                                dialog.show()
                            }
                        } else
                            ErrorDialog(
                                this@PpobPaymentDetailActivity,
                                this@PpobPaymentDetailActivity,
                                false,
                                false,
                                body?.meta?.message,
                                ""
                            ).show()
                    }

                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    if (!isFinishing) ProgressDialogComponent.dismissProgressDialog(this@PpobPaymentDetailActivity)
                    onApiResponseError()
                }

            })
    }

    private fun checkRefundStatus() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        var merchantRefundRequest = MerchantRefundRequest()
        merchantRefundRequest.rrn = CryptoUtil.encryptRSA(mReferenceNumber.toByteArray())

        RefundDao(this).checkRefundStatus(
            merchantRefundRequest,
            BaseDao.getInstance(this, API_KEY_CHECK_STATUS_REFUND).callback
        )
    }

    private fun getHistoryDetail() {
        var historyDetailRequest = HistoryDetailRequest()
        historyDetailRequest.reference_number = mReferenceNumber

        WalletDao(this).historyDetail(
            historyDetailRequest,
            BaseDao.getInstance(this, API_KEY_HISTORY_DETAIL).callback
        )
    }

    override fun onApiResponseCallback(
        br: BaseResponse?,
        responseCode: Int,
        response: Response<*>?
    ) {
        if (br == null) {
            dialogServiceNotAvailableError()
            return
        }
        ProgressDialogComponent.dismissProgressDialog(this)
        when (responseCode) {
            API_KEY_TRANSACTION_ADVICE ->
                if ((br as BaseResponseModel).meta.code == 200) {
                    val model = br as PpobOttoagPaymentResponseModel?
                    if (model!!.getData().keyValueList.size > 0) {
                        btnCheckStatus.gone()
                        bottomLayout.gone()
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
                } else if (br.meta.code == 408) {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.show()
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.show()
                }
            API_KEY_CHECK_STATUS_REFUND ->
                if ((br as BaseResponseModel).meta.code == 200) {
                    val checkRefundStatusResponse = br as CheckRefundStatusResponse?
                    checkRefundStatusResponse?.let {
                        if (it.data.isRefundable) {
                            btnToolbarRight.visible()

                            if (it.data.refundRrn != null) {
                                if (!it.data.refundRrn.equals("")) {
                                    btnToolbarRight.gone()
                                }
                            }
                        }
                    }
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.show()
                }
            API_KEY_HISTORY_DETAIL ->
                if ((br as BaseResponseModel).meta.code == 200) {
                    val historyDetailResponse = br as HistoryDetailResponse?
                    historyDetailResponse?.let {
                        it.data?.let {
                            mPpobOttoagPaymentResponseModel?.data?.keyValueList = it.keyValueList
                            displayDetailHistory()
                        }
                    }
                } else {
                    displayDetailHistory()
                }
        }
    }

    override fun onApiFailureCallback(message: String, ac: BaseActivity) {
        onApiResponseError()
    }


//endregion API Call
}
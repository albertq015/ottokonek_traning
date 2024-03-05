package com.otto.mart.ui.activity.transaction.alfamart

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Parcelable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Request.payment.AlfamartPurchaseRequest
import com.otto.mart.model.APIModel.Request.payment.AlfamartStatusRequest
import com.otto.mart.model.APIModel.Request.payment.AlfamartTokenRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.payment.AlfamartPurchaseResponse
import com.otto.mart.model.APIModel.Response.payment.AlfamartTokenResponse
import com.otto.mart.model.localmodel.payment.TopUpGuide
import com.otto.mart.presenter.dao.OCBIDao
import com.otto.mart.presenter.dao.TransactionDao
import com.otto.mart.support.util.BitmapUtil
import com.otto.mart.support.util.DateUtil
import com.otto.mart.support.util.DeviceUtil
import com.otto.mart.ui.Partials.adapter.payment.TopUpGuideAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import com.otto.mart.ui.activity.qr.scan.ScanQrActivity
import com.otto.mart.ui.activity.qr.show.ShowQrActivity
import com.otto.mart.ui.activity.transaction.PayQRDetailActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_show_payment_code.*
import kotlinx.android.synthetic.main.content_show_payment_code.btnCheckStatus
import kotlinx.android.synthetic.main.ppob_tab_menu.*
import kotlinx.android.synthetic.main.qr_payment_tab_menu.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit

class AlfamartShowPaymentCodeActivity : AppActivity() {

    companion object {
        const val KEY_IS_OTTOCASH = "is_ottocash"
    }

    private val RC_INDOMARET_QR_PURCHASE = 2010
    private val RC_INDOMARET_QR_PURCHASE_CANCEL = 2011

    private val KEY_ALFAMART_QR_PURCHASE_SUCCESS_STATUS = "terbayar"

    private var mTimer: CountDownTimer? = null
    private var mReferenceNumber = ""

    private var mTopUpGuideList = mutableListOf<TopUpGuide>()

    private var isOttoCash = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_payment_code)

        if (intent.hasExtra(KEY_IS_OTTOCASH)) {
            isOttoCash = intent.getBooleanExtra(KEY_IS_OTTOCASH, false)
        }

        initView()
        initRecyclerview()
        initContent()
    }

    private fun initView() {
        if (isOttoCash) {
            tvToolbarTitle.setText(getString(R.string.text_ottocash_token))
            tvGuideTitle.setText(getString(R.string.text_ottocash_token))
            tvGuideDesc.text = getString(R.string.text_usability_oc_token_in_indomaret_alfamart)
            tvNote.text = getString(R.string.text_show_token_to_mini_market_staff)
        } else {
            tvToolbarTitle.setText(getString(R.string.text_op_token))
            tvGuideTitle.setText(getString(R.string.text_op_token))
            tvGuideDesc.text = getString(R.string.text_token_for_topup_deposit)
            tvNote.text = getString(R.string.text_show_op_token_to_mini_market_staff)
        }

        imgToolbarLeft.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close))

        tvTab1.setText(getString(R.string.label_qr_code))
        tvTab2.setText(getString(R.string.text_barcode_and_token))

        btnToolbarBack.setOnClickListener { v: View? ->
            finish()
        }

        btnCheckStatus.setOnClickListener { v: View? ->
            callAlfamartQrPurchaseStatus()
        }

        btnTab1.setOnClickListener { v: View? ->
            tabSelected(1)
        }

        btnTab2.setOnClickListener { v: View? ->
            tabSelected(2)
        }

        tabScanQr.setOnClickListener { v: View? ->
            startActivity(Intent(this, ScanQrActivity::class.java))
            finish()
        }

        tabQrCode.setOnClickListener { v: View? ->
            startActivity(Intent(this, ShowQrActivity::class.java))
            finish()
        }

        tabSelected(2)
    }

    private fun initRecyclerview() {
        rvGuide.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        rvGuide.setLayoutManager(linearLayoutManager)
    }

    private fun initContent() {
        tvTabToken!!.setTextColor(ContextCompat.getColor(this, R.color.white))
        tabToken!!.background = ContextCompat.getDrawable(this, R.drawable.blue_rounded_12_bg)
        displayGuide()
        callAlfamartQrPurchase()
    }

    private fun tabSelected(tabPosition: Int) {
        tabIndicator1!!.visibility = View.INVISIBLE
        tabIndicator2!!.visibility = View.INVISIBLE
        if (tabPosition == 1) {
            tabIndicator1!!.visibility = View.VISIBLE
            qr_layout!!.visibility = View.VISIBLE
            barcode_layout!!.visibility = View.GONE
        } else {
            tabIndicator2!!.visibility = View.VISIBLE
            qr_layout!!.visibility = View.GONE
            barcode_layout!!.visibility = View.VISIBLE
        }
    }

    private fun displayBarcode(qrContent: String) {
        val bitmap = BitmapUtil.generateBitmap(
                qrContent
                , 8, DeviceUtil.dpToPx(120), DeviceUtil.dpToPx(50))
        img_barcode!!.setImageBitmap(bitmap)
    }

    private fun displayQR(qrContent: String) {
        val bitmap = BitmapUtil.generateBitmap(
                qrContent
                , 9, DeviceUtil.dpToPx(512), DeviceUtil.dpToPx(512))
        img_qr!!.setImageBitmap(bitmap)
    }

    private fun displayGuide() {
        if (isOttoCash) {
            mTopUpGuideList = TopUpGuideData().getTopUpOttoCashGuide()
        } else {
            mTopUpGuideList = TopUpGuideData().getTopUpDepositGuide()
        }

        val adapter = TopUpGuideAdapter(this, mTopUpGuideList)
        adapter.itemSelected = ::guideSelected
        rvGuide.adapter = adapter
    }

    fun guideSelected(selectedTopUpGuide: TopUpGuide, position: Int) {
        var i = 0
        for (topUpGuide in mTopUpGuideList) {
            if (i == position) {
                mTopUpGuideList[position].isShow = !selectedTopUpGuide.isShow
            } else {
                mTopUpGuideList[i].isShow = false
            }
            i++
        }

        rvGuide.adapter?.let {
            it.notifyDataSetChanged()
        }
    }

    private fun callAlfamartQrPurchase() {
        var publiser = "ottocash"

        if (!isOttoCash) {
            publiser = "deposit"
        }

        val alfamartTokenRequest = AlfamartTokenRequest()
        alfamartTokenRequest.latitude = getMyLastLocation().latitude.toString()
        alfamartTokenRequest.longitude = getMyLastLocation().longitude.toString()
        alfamartTokenRequest.longitude = getMyLastLocation().longitude.toString()
        alfamartTokenRequest.publisher = publiser

        OCBIDao(this).alfamartToken(alfamartTokenRequest, BaseDao.getInstance(this, AppKeys.API_KEY_ALFAMART_TOKEN).callback)
    }

    private fun alfamartQrPurchase(data: AlfamartPurchaseResponse.Data) {
        mReferenceNumber = data.reference_number
        mTimer = object : CountDownTimer(180000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer!!.text = String.format("%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))
            }

            override fun onFinish() {
                finish()
            }
        }.start()

        tvCode!!.text = data.token
        displayBarcode(data.token)
        displayQR(data.token)
        view_animator!!.displayedChild = 1
    }

    private fun getAlfamartToken(data: AlfamartTokenResponse.Data) {
        data.let {
            mReferenceNumber = it.ref.toString()
            mTimer = object : CountDownTimer(180000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    tvTimer!!.text = String.format("%02d : %02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))
                }

                override fun onFinish() {
                    finish()
                }
            }.start()

            tvCode!!.text = it.token
            it.token?.let { it1 -> displayBarcode(it1) }
            it.token?.let { it1 -> displayQR(it1) }
            view_animator!!.displayedChild = 1
        }
    }

    private fun callAlfamartQrPurchaseStatus() {
        val alfamartStatusRequest = AlfamartStatusRequest()
        alfamartStatusRequest.reference_number = mReferenceNumber
        TransactionDao(this).getAlfamartQrPurchaseStatus(alfamartStatusRequest, BaseDao.getInstance(this, AppKeys.API_KEY_ALFAMART_PURCHASE_STATUS).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse, responseCode: Int, response: Response<*>) {
        ProgressDialogComponent.dismissProgressDialog(this)
        if (response.isSuccessful) {
            when (responseCode) {
                AppKeys.API_KEY_ALFAMART_PURCHASE -> if ((br as BaseResponseModel).meta.code == 200) {
                    alfamartQrPurchase((br as AlfamartPurchaseResponse).data)
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.show()
                }
                AppKeys.API_KEY_ALFAMART_TOKEN -> if ((br as BaseResponseModel).meta.code == 200) {
                    (br as AlfamartTokenResponse).data?.let { getAlfamartToken(it) }
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.show()
                }
                AppKeys.API_KEY_ALFAMART_PURCHASE_STATUS -> if ((br as BaseResponseModel).meta.code == 200) {
                    var status = ""
                    for (wbm in (br as BasePaymentResponseModel).getData().keyValueList) {
                        if (wbm.key.equals("status", ignoreCase = true)) {
                            status = wbm.value
                        }
                    }
                    if (status.equals(KEY_ALFAMART_QR_PURCHASE_SUCCESS_STATUS, ignoreCase = true)) {
                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        val sucessIntent = Intent(this, PayQRDetailActivity::class.java)
                        sucessIntent.putExtra("data", br.getData().keyValueList as ArrayList<out Parcelable?>)
                        startActivity(sucessIntent)
                        finish()
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, "Transaksi sedang diproses", "")
                        dialog.show()
                    }
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.show()
                }
            }
        }
    }

    override fun onApiFailureCallback(message: String, ac: BaseActivity) { //super.onApiFailureCallback(message, ac);
        onApiResponseError()
    }
}
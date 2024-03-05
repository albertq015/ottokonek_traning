package com.otto.mart.ui.fragment.wallet

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otto.mart.R
import com.otto.mart.model.APIModel.Request.payment.AlfamartStatusRequest
import com.otto.mart.model.APIModel.Request.payment.AlfamartTokenRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel
import com.otto.mart.model.APIModel.Response.payment.AlfamartPurchaseResponse
import com.otto.mart.model.APIModel.Response.payment.AlfamartTokenResponse
import com.otto.mart.model.localmodel.payment.TopUpGuide
import com.otto.mart.presenter.dao.OCBIDao
import com.otto.mart.presenter.dao.TransactionDao
import com.otto.mart.support.util.BitmapUtil
import com.otto.mart.support.util.DeviceUtil
import com.otto.mart.ui.Partials.adapter.payment.TopUpGuideAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import com.otto.mart.ui.activity.transaction.PayQRDetailActivity
import com.otto.mart.ui.activity.transaction.alfamart.TopUpGuideData
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.fragment.AppFragment
import kotlinx.android.synthetic.main.content_show_payment_code.*
import kotlinx.android.synthetic.main.ppob_tab_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit

class TokenFragment : AppFragment() {

    private var mReferenceNumber = ""
    private val KEY_ALFAMART_QR_PURCHASE_SUCCESS_STATUS = "terbayar"
    private var mTopUpGuideList = mutableListOf<TopUpGuide>()
    private val publiser = "ottocash"
    private var mTimer: CountDownTimer? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_show_payment_code, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRecyclerview()
        displayGuide()
        callAlfamartQrPurchase()
    }

    private fun initView() {

        btnCheckStatus.setOnClickListener { v: View? ->
            callAlfamartQrPurchaseStatus()
        }

        tabIndicator2!!.visibility = View.VISIBLE
        qr_layout!!.visibility = View.GONE
        barcode_layout!!.visibility = View.VISIBLE
    }

    private fun initRecyclerview() {
        rvGuide.setHasFixedSize(true)
        rvGuide.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
    }

    private fun displayGuide() {
        context?.let {
            mTopUpGuideList = TopUpGuideData().getTopUpOttoCashGuide()
            val adapter = TopUpGuideAdapter(it, mTopUpGuideList)
            adapter.itemSelected = ::guideSelected
            rvGuide.adapter = adapter
        }
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
        activity?.let { activity ->
            activity as AppActivity
            val alfamartTokenRequest = AlfamartTokenRequest()
            alfamartTokenRequest.latitude = activity.getMyLastLocation().latitude.toString()
            alfamartTokenRequest.longitude = activity.getMyLastLocation().longitude.toString()
            alfamartTokenRequest.longitude = activity.getMyLastLocation().longitude.toString()
            alfamartTokenRequest.publisher = publiser

            OCBIDao(this).alfamartToken(alfamartTokenRequest, object : Callback<AlfamartTokenResponse>{
                override fun onFailure(call: Call<AlfamartTokenResponse>, t: Throwable) {
                    onApiResponseError()
                }

                override fun onResponse(call: Call<AlfamartTokenResponse>, response: Response<AlfamartTokenResponse>) {
                    response.body()?.let {responseModel->
                        if (responseModel.meta.code==200) responseModel.data?.run { getAlfamartToken(this)}
                        else showErrorDialog(responseModel.meta.message)
                    }
                }

            })
        }
    }

    private fun getAlfamartToken(data: AlfamartTokenResponse.Data) {
        data.let {
            mReferenceNumber = it.ref.toString()
            mTimer = object : CountDownTimer(180000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    tvTimer?.text = "" + String.format("%02d : %02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))
                }

                override fun onFinish() {
                    activity?.finish()
                }
            }.start()

            tvCode!!.text = it.token
            it.token?.let { it1 -> displayBarcode(it1) }
            it.token?.let { it1 -> displayQR(it1) }
            view_animator!!.displayedChild = 1
        }
    }

    private fun alfamartQrPurchase(data: AlfamartPurchaseResponse.Data) {
        mReferenceNumber = data.reference_number
        mTimer = object : CountDownTimer(180000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer!!.text = "" + String.format("%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))
            }

            override fun onFinish() {
                activity?.finish()
            }
        }.start()

        tvCode!!.text = data.token
        displayBarcode(data.token)
        displayQR(data.token)
        view_animator!!.displayedChild = 1
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

    private fun callAlfamartQrPurchaseStatus() {
        val alfamartStatusRequest = AlfamartStatusRequest()
        alfamartStatusRequest.reference_number = mReferenceNumber
        TransactionDao(this).getAlfamartQrPurchaseStatus(alfamartStatusRequest, object : Callback<BasePaymentResponseModel> {
            override fun onFailure(call: Call<BasePaymentResponseModel>, t: Throwable) {
                onApiResponseError()
            }

            override fun onResponse(call: Call<BasePaymentResponseModel>, response: Response<BasePaymentResponseModel>) {
                context?.let { context ->
                    var status = ""
                    val data = response.body()
                    data?.takeIf { it.meta.code == 200 }?.apply {
                        for (wbm in getData().keyValueList) {
                            if (wbm.key.equals("status", ignoreCase = true)) {
                                status = wbm.value
                            }
                        }

                        if (status.equals(KEY_ALFAMART_QR_PURCHASE_SUCCESS_STATUS, ignoreCase = true)) {
                            val intent = Intent(context, DashboardActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            val sucessIntent = Intent(context, PayQRDetailActivity::class.java)
                            sucessIntent.putExtra("data", getData()?.keyValueList as ArrayList<out Parcelable?>)
                            startActivity(sucessIntent)
                            activity?.finish()
                        } else {
                            val dialog = ErrorDialog(context, activity as AppActivity, false, false, "Transaksi sedang diproses", "")
                            dialog.show()
                        }

                    }
                            ?: ErrorDialog(context, activity as AppActivity, false, false, data?.meta?.message, "").show()
                }
            }
        })
    }

    override fun onDestroy() {
        mTimer?.cancel()
        super.onDestroy()
    }
}
package com.otto.mart.ui.fragment.ppob

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Request.qr.CheckStatusQrRequest
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.BitmapUtil
import com.otto.mart.support.util.DeviceUtil
import com.otto.mart.support.util.gone
import com.otto.mart.ui.activity.qr.ShareQrActivity
import com.otto.mart.ui.activity.qr.show.ShowDynamicQrActivity
import com.otto.mart.ui.activity.qr.show.ShowQrActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_ppob_payment_detail.*
import kotlinx.android.synthetic.main.fragment_ppob_show_qr_payment.imgQR
import kotlinx.android.synthetic.main.fragment_ppob_show_qr_payment.tvAmount
import kotlinx.android.synthetic.main.fragment_ppob_show_qr_payment.tvProductName
import kotlinx.android.synthetic.main.fragment_ppob_show_qr_qris_payment.*
import kotlinx.android.synthetic.main.fragment_ppob_show_qr_qris_payment.btnCheckStatus
import kotlinx.android.synthetic.main.fragment_ppob_show_qr_qris_payment.tvMerchantName
import kotlinx.android.synthetic.main.part_ppob_payment_detail.*


/**
 * A simple [Fragment] subclass.
 */
class PpobShowQrQrisPaymentFragment : BottomSheetDialogFragment() {

    var mProductName = ""
    var mAmount = ""
    var mQrString = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ppob_show_qr_qris_payment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        displyaPaymentInfo()
        displayQr()
    }

    private fun initView() {
        btnShareQr.setOnClickListener {
            val intent = Intent(activity, ShareQrActivity::class.java)
            intent.putExtra(ShareQrActivity.KEY_QR_CONTENT, mQrString)
            intent.putExtra(ShareQrActivity.KEY_AMOUNT, mAmount)
            intent.putExtra(ShareQrActivity.KEY_ACTION_TYPE, ShareQrActivity.ACTION.SHARE)
            startActivity(intent)
        }

        btnUnduhQr.setOnClickListener {
            val intent = Intent(activity, ShareQrActivity::class.java)
            intent.putExtra(ShareQrActivity.KEY_QR_CONTENT, mQrString)
            intent.putExtra(ShareQrActivity.KEY_AMOUNT, mAmount)
            intent.putExtra(ShareQrActivity.KEY_ACTION_TYPE, ShareQrActivity.ACTION.DOWNLOAD)
            startActivity(intent)
        }

        btnCheckStatus.setOnClickListener {
            (activity as ShowDynamicQrActivity).callCheckStatus()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog

            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    private fun displyaPaymentInfo() {
        tvMPAN.gone()

        val merchantName = SessionManager.getUserProfile().merchant_name

        tvProductName.text = mProductName
        tvAmount.text =mAmount
        tvMerchantName.text = merchantName
        tvNMID.text = SessionManager.getPrefLogin().binName + " QR"
//        SessionManager.getNMID()?.takeIf { it.isNotEmpty() }?.run { tvNMID.text = "NMID : ${this}" }
        tvMPAN.text = SessionManager.getUserProfile().mpan
//        tvCode.text = SessionManager.getTID()
    }

    private fun displayQr() {
        val bitmap = BitmapUtil.generateBitmap(mQrString, 9, DeviceUtil.dpToPx(300), DeviceUtil.dpToPx(300))
        imgQR.setImageBitmap(bitmap)
    }
}

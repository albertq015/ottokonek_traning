package id.ottopay.oasis.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.bumptech.glide.Glide
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Request.grosir.GrosirItem
import com.otto.mart.model.APIModel.Request.grosir.OasisApprovedOrderRequest
import com.otto.mart.model.APIModel.Response.grosir.HistoryOasisOrderItem
import com.otto.mart.model.APIModel.Response.grosir.OasisApprovedOrderResponse
import com.otto.mart.presenter.dao.olshop.GrosirDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.DateUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.pref.Pref
import com.otto.mart.support.util.visible
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.component.dialog.Popup
import id.ottopay.oasis.R
import id.ottopay.oasis.adapters.GrosirChartAdapter
import kotlinx.android.synthetic.main.grosir_history_list_layout.*
import kotlinx.android.synthetic.main.oasis_toolbar.*
import retrofit2.Response

class GrosirDetailHistoryActivity : AppActivity() {
    var data: HistoryOasisOrderItem? = null
    private var collectedProduct = ArrayList<GrosirItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grosir_history_list_layout)
        tvToolbarTitle.setText(getString(com.otto.mart.R.string.text_order_detail))
        data = intent.getSerializableExtra("data") as HistoryOasisOrderItem?
        btnToolbarBack.setOnClickListener { View ->
            onBackPressed()
        }
        setLayoutData()
    }

    fun setLayoutData() {

        if (data?.payment_method?.name.equals("Pay at Delivery", true)) {
            ubah_kurir_oasis.visibility = View.GONE
            resi.visibility = View.GONE
            address.text = SessionManager.getDefaultAddress()
        } else {
            address.text = data?.shipment?.recipient_address_detail
        }

        if (data?.payment_status.equals("Paid", true)) {
            status_pembayaran.setTextColor(resources.getColor(com.otto.mart.R.color.green_dark_4))
        } else {
            status_pembayaran.setTextColor(resources.getColor(com.otto.mart.R.color.red_dark_oasis))
        }

        Log.d("hasilnyaa",data?.status_order.toString() + " asdasdasd")
        if (data?.status_order.equals("Order Processed", true)) {
            status_pemesanan.setTextColor(resources.getColor(com.otto.mart.R.color.blue_dark_oasis))
        } else if (data?.status_order.equals("Order In Transit", true)) {
            status_pemesanan.setTextColor(resources.getColor(com.otto.mart.R.color.orange_dark_oasis))
        } else if (data?.status_order.equals("Order Delivered", true)) {
            confirmation_button.visibility = View.VISIBLE
            status_pemesanan.setTextColor(resources.getColor(com.otto.mart.R.color.green_dark_order_oasis))
        } else if (data?.status_order.equals("Order Completed", true)) {
            status_pemesanan.setTextColor(resources.getColor(com.otto.mart.R.color.dark_blue_green))
        }else if (data?.status_order.equals("Order in Progress", true)) {
            status_pemesanan.setTextColor(resources.getColor(com.otto.mart.R.color.orange_dark_oasis))
        } else {
            status_pemesanan.setTextColor(resources.getColor(com.otto.mart.R.color.red_dark_oasis))
        }

        status_pemesanan.text = data?.status_order
        tanggal_pembelian.text = DateUtil.formatDate(data?.order_date, "dd-MMMM-yyyy", "yyyy-MM-dd")
        no_resi.text = data?.payment_reference
        no_pemesanan.text = data?.order_no
        status_pembayaran.text = data?.payment_status
        merchantName.text = SessionManager.getMerchantName()
        kurir_name.text = data?.shipment?.courier_name
        kurir_price.text =
            DataUtil.InputDecimal(data?.shipment?.courier_cost?.toBigDecimal().toString())

        //Glide.with(this).load()
        data?.product?.forEach {
            collectedProduct?.add(
                GrosirItem(
                    it.product_details?.product_code,
                    it.product_details?.sell_price,
                    it.quantity.toString(),
                    it.product_details?.name,
                    it.product_details?.photo,
                    it.product_details?.weight
                )
            )
        }

//        if (data?.order_no == null || data?.order_no.equals("")) {
//            no_resi_pengiriman.visibility = View.GONE
//        } else {
//            no_resi_pengiriman.text = data?.order_no
//        }

        if (data?.shipment?.courier_name.equals("") && data?.shipment?.recipient_address_detail.equals(
                ""
            )
        ) {
            ubah_kurir_oasis.gone()
            shipping_method.setText("Pick up at the store")
            address_merchant.gone()
        } else {
            address_merchant.visible()
            shipping_method.setText("Sent by courier")
            ubah_kurir_oasis.visible()
        }


        total_payment_row.text =
            DataUtil.InputDecimal(data?.total_amount?.toBigDecimal().toString())
        total_payment.text = DataUtil.InputDecimal(data?.total_amount?.toBigDecimal().toString())
        cost_kurir.text = DataUtil.InputDecimal(data?.courier_cost?.toBigDecimal().toString())
        total_payment_detail.text = DataUtil.InputDecimal(
            data?.total_amount?.toBigDecimal()?.plus(data?.courier_cost!!.toBigDecimal()).toString()
        )
        //var saldo = Pref.getPreference().getString(AppKeys.PREF_LAST_BALANCE).replace(".", "").replace(" ", "").toLong()
        saldo_tv.text = DataUtil.InputDecimal(
            data?.total_amount?.toBigDecimal()?.plus(data?.courier_cost!!.toBigDecimal()).toString()
        )
        tv_merchant_name.text = data?.supplier_name
        var adapter = GrosirChartAdapter(collectedProduct)
        productList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        productList.adapter = adapter

        if (data?.payment_type?.name.equals("Cash")){
            tv_bank_name.text = "Cash on delivery"
        }else{
            tv_bank_name.text = "Account balance"
        }

        tv_payment_method.text = data?.payment_method?.name

        if (data?.source_bank.equals("CARD BANK")) {
            logo.setImageResource(com.otto.mart.R.drawable.logo_card_bank)
        } else if (data?.source_bank.equals("CARD MRI")) {
            logo.setImageResource(com.otto.mart.R.drawable.logo_cardmri)
        } else {
            logo.setImageResource(com.otto.mart.R.drawable.iv_card_sme)
        }



        confirmation_button.setOnClickListener {
            var popup = Popup.getConfirmDialog(
                getString(com.otto.mart.R.string.konfirmasi_pesanan_diterima),
                getString(com.otto.mart.R.string.saya_telah_memastikan)
            );
            popup.isHideBtnNegative = true
            popup.positiveButton = getString(com.otto.mart.R.string.label_konfirmasi)
            popup.positiveAction = {
                callApiConfirmation(data?.order_no!!,data?.payment_type?.code!!,data?.reff_no!!)
            }
            popup.singleShow(supportFragmentManager, "popupconfirmation")
        }
    }


    fun callApiShowSuccessConfirmation() {
        var popup = Popup()
        popup.title = getString(com.otto.mart.R.string.pesanan_selesai)
        popup.message = getString(com.otto.mart.R.string.konfirmasi_penerimaan_barang_berhasil)
        popup.positiveButton = getString(com.otto.mart.R.string.oke)
        popup.positiveAction = {
            finish()
        }
        popup.isHideBtnNegative = true
        popup.singleShow(supportFragmentManager, "popupconfirmationafter")
    }


    override fun onApiResponseCallback(
        br: BaseResponse?,
        responseCode: Int,
        response: Response<*>?
    ) {
        super.onApiResponseCallback(br, responseCode, response)
        if (response!!.isSuccessful) {
            val response = br as OasisApprovedOrderResponse
            if (response.baseMeta.code == 200) {
                callApiShowSuccessConfirmation()
            } else {
                var errorDialog =
                    ErrorDialog(this, this, true, false, response.baseMeta.message, "")
                errorDialog.show()
            }
        }
    }

    fun callApiConfirmation(orderNo: String,paymentType :String,reffNo: String) {
        var request = OasisApprovedOrderRequest()
        request.orderNo = orderNo
        request.paymentType = paymentType
        request.reffNo = reffNo
        ProgressDialogComponent.showProgressDialog(
            this,
            getString(com.otto.mart.R.string.loading_message),
            false
        ).show()
        GrosirDao(this).getGrosirCheckStatusApproved(
            request,
            BaseDao.getInstance(this, AppKeys.API_KEY_APPROVED_ORDER).callback
        )
    }
}
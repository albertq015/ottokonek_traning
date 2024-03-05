package id.ottopay.oasis.ui

import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.otto.mart.model.APIModel.Request.grosir.GrosirItem
import com.otto.mart.model.APIModel.Request.grosir.ShipmentDetail
import com.otto.mart.model.APIModel.Response.grosir.DataCheckStatus
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.DataUtil
import com.otto.mart.ui.activity.AppActivity
import id.ottopay.oasis.R
import id.ottopay.oasis.adapters.GrosirChartAdapter
import kotlinx.android.synthetic.main.activity_grosir_order_detail.*
import kotlinx.android.synthetic.main.oasis_toolbar.*
import kotlinx.android.synthetic.main.part_grosir_show_product.totalPayment

class GrosirOrderDetail : AppActivity() {

    var model = DataCheckStatus()
    private var collectedProduct = ArrayList<GrosirItem>()
    private lateinit var shipment : ShipmentDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grosir_order_detail)
        tvToolbarTitle.setText(getString(com.otto.mart.R.string.text_order_detail))
        btnToolbarBack.setOnClickListener { View ->
            onBackPressed()
        }
        initContent()
    }

    fun initContent(){

        var string = intent.getStringExtra("dataProduct")

        val token = object : TypeToken<DataCheckStatus>() {

        }.type
        model = Gson().fromJson(string,token)

        totalPayment.text = DataUtil.convertCurrency(model.total_amount)
        address.text = SessionManager.getDefaultAddress()

        if(!model.shipment.courier_name.equals("") && !model.shipment.courier_description.equals("") && model.shipment.courier_cost > 0) {
            cont_delivery.visibility = View.VISIBLE
            kurir_name.text = model.shipment.courier_name
            kurir_service.text = model.shipment.courier_description
            kurir_price.text = DataUtil.convertCurrency(model.shipment.courier_cost)
            totalPayment.text = DataUtil.convertCurrency(model.total_amount + model.shipment.courier_cost)
            address.text = model.shipment.recipient_address_detail+ ", "+model.shipment.recipient_city_name+ ", "+model.shipment.recipient_district_name+ ", "+model.shipment.recipient_province_name+ ", "+model.shipment.recipient_zip_code
        }

        status.text = intent.getStringExtra("status")
        nomor.text = intent.getStringExtra("nomor")
        tanggal.text = intent.getStringExtra("date")
        metode.text = intent.getStringExtra("metode")

        //totalPayment.text = DataUtil.convertCurrency(model.total_amount)
        supplierName.text = intent.getStringExtra("name")
        merchantName.text = SessionManager.getUserProfile().merchant_name

        //address2.text = SessionManager.getUserProfile().addressCity +" - "+SessionManager.getUserProfile().addressDistrict+" - "+SessionManager.getUserProfile().addressProvince

        model.product.forEach {
            collectedProduct.add(GrosirItem(
                    it.product_details.product_code,
                    it.product_details.sell_price,
                    it.product_details.quantity.toString(),
                    it.product_details.name,
                    it.product_details.photo,0L
            ))
        }


        var adapter = GrosirChartAdapter(collectedProduct)
        productList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        productList.adapter = adapter
    }
}

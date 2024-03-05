package id.ottopay.oasis.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.AddressModel
import com.otto.mart.model.APIModel.Misc.AuthDataModel
import com.otto.mart.model.APIModel.Misc.PaymentData
import com.otto.mart.model.APIModel.Misc.olshop.ShippingAddressData
import com.otto.mart.model.APIModel.Request.grosir.*
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.bogasari.BogasariPaymentResponseModel
import com.otto.mart.model.APIModel.Response.grosir.DataResponseItemList
import com.otto.mart.model.APIModel.Response.grosir.GrosirAddressListResponse
import com.otto.mart.model.APIModel.Response.grosir.OasisListCourier
import com.otto.mart.model.APIModel.Response.grosir.ServiceCostShipment
import com.otto.mart.model.APIModel.Response.multibank.ReceiptAdressResponse
import com.otto.mart.model.APIModel.Response.ppob.PpobInquiryResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.presenter.dao.olshop.GrosirDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.*
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.multibank.ListLinkedBankAccountPresenter
import com.otto.mart.ui.activity.multibank.ListLinkedBankAccountPresenterImpl
import com.otto.mart.ui.activity.ppob.PpobPaymentDetailActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import id.ottopay.oasis.R
import id.ottopay.oasis.adapters.GrosirChartAdapter
import kotlinx.android.synthetic.main.activity_grosir_check_out_pa_o.*
import kotlinx.android.synthetic.main.oasis_toolbar.*
import kotlinx.android.synthetic.main.part_grosir_show_product.*
import retrofit2.Response

class GrosirCheckOutPaO : AppActivity() {

    private var collectedProduct = ArrayList<GrosirItem>()
    private lateinit var dataProduct: DataResponseItemList
    private var blPresenter: ListLinkedBankAccountPresenter =
        ListLinkedBankAccountPresenterImpl(this)
    private var isPickupMethod = false
    private val LIST_RC = 1
    private var delivery_method_name_text = ""

    private var radiotext = ""

    private var selectedbankpos = -1;

    private var hasSelectedPaymentMethod = false

    companion object {
        private const val RC_PIN = 1
        private const val RC_PAYMENT = 2
        private const val RC_COST = 3
    }

    var saldo = 0.0
    var cost = 0.0
    val mCourierMethodFragment = GrosirCourierDialog()
    var mCourierList = mutableListOf<ServiceCostShipment>()
    var shipment: OasisListCourier? = null
    var productSave: ShippingAddressData? = null
    var productChoice: ShippingAddressData? = null
    var addressModel: AddressModel? = null
    var addressLoginModel = AuthDataModel()
    var totalPaymentValue = 0.0
    var paymentMethod = ""
    var mPaymentType = ""

    var mAddresContact = ReceiptAdressResponse()

    var isItemSelectedLivedata = MutableLiveData<Boolean>()
    var isbankSelected = false
    var isCourierSelected = true
    var isCoD = false
    val presenter: ListLinkedBankAccountPresenter = ListLinkedBankAccountPresenterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grosir_check_out_pa_o)
        isItemSelectedLivedata.value = false
        setDisable()
        isItemSelectedLivedata.observe(this, Observer {
            if (it)
                setEnable()
            else
                setDisable()
        })
        getIntentData()
        initContent()
        if (!blPresenter.checkData())
            blPresenter.loadBanklistAPI()

        blPresenter.loadIssuerBalance()
    }

    fun getIntentData() {
        if (intent.hasExtra("totalPrice")) {
            totalPaymentValue = intent.extras?.getDouble("totalPrice")!!
        }

        dataProduct = Gson().fromJson(
            intent.extras?.getString("dataProduct"),
            DataResponseItemList::class.java
        )

//        try {
//
//        } catch (e: Exception) {
//            saldo = 0.0
//        }


        merchantName.text = SessionManager.getUserProfile().merchant_name
        minOrderTv.text =
            getString(com.otto.mart.R.string.text_minimum_order_mandatory) + " " + DataUtil.InputDecimal(
                intent.extras?.getDouble("minOrder", 0.0).toString()
            )
        total_payment_row.text = DataUtil.InputDecimal(totalPaymentValue.toString())
        var string = intent.extras?.getString("collectedProduct")
        val token = object : TypeToken<ArrayList<GrosirItem>>() {

        }.type
        collectedProduct = Gson().fromJson(string, token)

        collectedProduct.forEach {
            totalPaymentValue
        }

    }

    fun initContent() {
        tvToolbarTitle.setText(getString(com.otto.mart.R.string.checkout))
        textNext.setText(getString(com.otto.mart.R.string.pay_now))
        btnToolbarBack.setOnClickListener { View ->
            onBackPressed()
        }

        receipt_name.setContentText(SessionManager.getMerchantName())
        receipt_phone.setContentText(SessionManager.getPhone())

        pick_up.setOnClickListener { view ->
            top.visible()
            setEnable()
            isPickupMethod = true
            header_delivery.gone()
            email_pao.gone()
            receiptman_pao.gone()
            phone_pao.gone()
            address_header.text = "Store Address"
            change_shipment.text = "View Map"
            change_shipment.visible()
            ubah_kurir_oasis.gone()
            if (!dataProduct.name.isNullOrEmpty() && !dataProduct.alamat.isNullOrEmpty()) {
                merchantName.text = dataProduct.name
                address.text = dataProduct.alamat
            }
            change_shipment.setOnClickListener {
                try {
                    val uri =
                        "http://maps.google.com/maps?q=loc:" + dataProduct.warehouse.latitude.toFloat() + "," + dataProduct.warehouse.longitude.toFloat()
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    startActivity(intent)
                } catch (e: java.lang.Exception) {
                    Toast.makeText(this, "Cannot find the location", Toast.LENGTH_SHORT).show()
                }
            }
            cost_kurir.text = DataUtil.InputDecimal("0.0")
            total_payment.text = DataUtil.InputDecimal(totalPaymentValue.toString())
            totalPayment.text = DataUtil.InputDecimal(calculate(0.0, totalPaymentValue).toString())
        }

        delivery.setOnClickListener { view ->
            top.visible()
//            setEnable()
            isPickupMethod = false
            header_delivery.visible()
            email_pao.visible()
            receiptman_pao.visible()
            phone_pao.visible()
            address_header.text = "Delivery Address"
            change_shipment.text = "Change Address"
            change_shipment.visible()
            ubah_kurir_oasis.visible()
            if (dataProduct.courier.isEmpty()) {
                val dialog = ErrorDialog(
                    this,
                    this@GrosirCheckOutPaO,
                    false,
                    false,
                    getString(com.otto.mart.R.string.error_msg_something_wrong),
                    ""
                )
                dialog.setOnDismissListener {
                    finish()
                }
                dialog.show()
                return@setOnClickListener
            } else {
                if (dataProduct.courier.size > 1) {
                    ubah_kurir_oasis.setOnClickListener {
                        mCourierMethodFragment.mCourierList = dataProduct.courier
                        mCourierMethodFragment.show(
                            supportFragmentManager,
                            mCourierMethodFragment.tag
                        )

                        mCourierMethodFragment.setItemSelectedClickListener(object :
                            GrosirCourierDialog.OnCourierMethodSelected {
                            override fun onCourierMethodSelected(
                                item: OasisListCourier,
                                position: Int
                            ) {
                                kurir_name.text = item.name
                                kurir_price.text = DataUtil.InputDecimal(item.price.toString())
                                cost_kurir.text = DataUtil.InputDecimal(item.price.toString())
                                cost = calculate(item.price!!, totalPaymentValue)
                                total_payment.text =
                                    DataUtil.InputDecimal(cost.toBigDecimal().toString())
                                totalPayment.text =
                                    DataUtil.InputDecimal(cost.toBigDecimal().toString())
                                shipment = item
                                if (item.image_path!!.isNotEmpty()) {
                                    Glide.with(this@GrosirCheckOutPaO).load(item.image_path)
                                        .into(image_kurir)
                                } else {
                                    Glide.with(this@GrosirCheckOutPaO)
                                        .load(this@GrosirCheckOutPaO.resources.getDrawable(com.otto.mart.R.drawable.truck_1))
                                        .into(image_kurir)

                                }
                            }
                        })
                    }
                    kurir_name.text = dataProduct.courier[0].name
                    kurir_price.text =
                        DataUtil.InputDecimal(dataProduct.courier[0].price.toString())
                    cost_kurir.text = DataUtil.InputDecimal(dataProduct.courier[0].price.toString())
                    cost = calculate(dataProduct.courier[0].price!!, totalPaymentValue)
                    total_payment.text = DataUtil.InputDecimal(cost.toBigDecimal().toString())
                    totalPayment.text = DataUtil.InputDecimal(cost.toBigDecimal().toString())
                    shipment = dataProduct.courier[0]
                    if (dataProduct.courier[0].image_path!!.isNotEmpty()) {
                        Glide.with(this@GrosirCheckOutPaO).load(dataProduct.courier[0])
                            .into(image_kurir)
                    } else {
                        Glide.with(this@GrosirCheckOutPaO)
                            .load(this@GrosirCheckOutPaO.resources.getDrawable(com.otto.mart.R.drawable.truck_1))
                            .into(image_kurir)

                    }
                } else {
                    ubah_kurir_oasis.isEnabled = false
                    arrow.gone()
                    kurir_name.text = dataProduct.courier[0].name
                    kurir_price.text =
                        DataUtil.InputDecimal(dataProduct.courier[0].price.toString())
                    cost_kurir.text = DataUtil.InputDecimal(dataProduct.courier[0].price.toString())
                    cost = calculate(dataProduct.courier[0].price!!, totalPaymentValue)
                    total_payment.text = DataUtil.InputDecimal(cost.toBigDecimal().toString())
                    totalPayment.text = DataUtil.InputDecimal(cost.toBigDecimal().toString())
                    shipment = dataProduct.courier[0]
                    if (dataProduct.courier[0].image_path!!.isNotEmpty()) {
                        Glide.with(this@GrosirCheckOutPaO).load(dataProduct.courier[0])
                            .into(image_kurir)
                    } else {
                        Glide.with(this@GrosirCheckOutPaO)
                            .load(this@GrosirCheckOutPaO.resources.getDrawable(com.otto.mart.R.drawable.truck_1))
                            .into(image_kurir)

                    }
                }
                isCourierSelected = true
                isItemSelectedLivedata.value = isbankSelected && isCourierSelected
            }


            //total_payment.text = DataUtil.InputDecimal(totalPaymentValue.toString())
            //totalPayment.text = DataUtil.InputDecimal(calculate(dataProduct?.courier[0].price!!, totalPaymentValue).toString())
            isPickupMethod = false
            /*if (!SessionManager.getAddressOasis().isNullOrEmpty()) {
                productSave = Gson().fromJson(SessionManager.getAddressOasis(), ShippingAddressData::class.java)
                address.text = productSave?.detail + ", " + productSave?.city?.name + ", " + productSave?.province?.name + ", " + productSave?.district?.name + ", " + productSave?.zip_code
                merchantName.text = productSave?.name
                //getCostShipment(productSave?.city?.id!!.toLong(), productSave?.district_id!!)
            } else {*/
            addressLoginModel = SessionManager.getPrefLogin()
            //getCostShipment(addressLoginModel.addressCityId,addressLoginModel.addressDistrictId)
            address.text =
                addressLoginModel.address + ", " + addressLoginModel.addressRegion + ", " + addressLoginModel.addressProvince + ", " + addressLoginModel.addressMunicipality
            merchantName.text = SessionManager.getMerchantName()
            setDefaultAddress()

            change_shipment.setOnClickListener {
                val intent = Intent(this@GrosirCheckOutPaO, GrosirShippingAddress::class.java)
                startActivityForResult(intent, 1234)
            }
        }

        if (!dataProduct.delivery_method.isNullOrEmpty()) {
            when {
                dataProduct.delivery_method.size > 1 -> {
                    delivery_method.visibility = View.VISIBLE
                    pick_up.visibility = View.VISIBLE
                    delivery.visibility = View.VISIBLE

                    pick_up.setText(dataProduct.delivery_method[0].name)
                    delivery.setText(dataProduct.delivery_method[1].name)
                    delivery_choose.setOnCheckedChangeListener { group, i ->
                        delivery_method_name_text = if (i == R.id.pick_up) {
                            pick_up.performClick()
                            dataProduct.delivery_method[0].name.toString()
                        } else {
                            delivery.performClick()
                            dataProduct.delivery_method[1].name.toString()
                        }

                        radiotext = group.findViewById<RadioButton>(i).text.toString()

                    }
                    delivery.performClick()
                }

                dataProduct.delivery_method[0].name.equals("Pick up at the store", true) -> {
                    pick_up.visibility = View.VISIBLE
                    delivery.visibility = View.GONE
                    pick_up.performClick()
                    delivery_method_name_text = dataProduct.delivery_method[0].name.toString()
                    checkValidator()
                }

                else -> {
                    pick_up.visibility = View.GONE
                    delivery.visibility = View.VISIBLE
                    delivery.performClick()
                    delivery_method_name_text = dataProduct.delivery_method[1].name.toString()

                }
            }
        }

//        saldo_tv.text = DataUtil.InputDecimal(saldo.toString())

        if (Build.VERSION.SDK_INT >= 24) {
            tvTncDesc.text = Html.fromHtml(
                getString(com.otto.mart.R.string.msg_tnc_confirmation),
                Html.FROM_HTML_MODE_LEGACY
            )
        } else {
            tvTncDesc.text = Html.fromHtml(getString(com.otto.mart.R.string.msg_tnc_confirmation))
        }

        nextBtn.setOnClickListener {


            if (!isPickupMethod) {
                when {
                    receipt_name.textContent.isEmpty() -> {
                        (receipt_name.component as EditText).setError(getString(com.otto.mart.R.string.nama_penerima_wajib_diisi))
                    }

                    receipt_phone.textContent.isEmpty() -> {
                        (receipt_phone.component as EditText).setError(getString(com.otto.mart.R.string.nomor_handphone_wajib_diisi))
                    }

                    isCoD -> {
                        val intent =
                            Intent(this@GrosirCheckOutPaO, RegisterPinResetActivity::class.java)
                        intent.putExtra("confirm", true)
                        startActivityForResult(intent, RC_PIN)
                    }

                    cost > saldo -> {
                        Toast.makeText(
                            this,
                            getString(com.otto.mart.R.string.saldo_deposit_tidak_mencukupi),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    address.text.equals(getString(com.otto.mart.R.string.silahkan_pilih_alamat_pengiriman)) -> {
                        Toast.makeText(
                            this,
                            getString(com.otto.mart.R.string.alamat_pengiriman_wajib_diisi),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    kurir_name.text.equals("Please choose shipping method") -> {
                        Toast.makeText(
                            this,
                            getString(com.otto.mart.R.string.please_choose_shipping_method),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        val intent =
                            Intent(this@GrosirCheckOutPaO, RegisterPinResetActivity::class.java)
                        intent.putExtra("confirm", true)
                        startActivityForResult(intent, RC_PIN)
                    }
                }

            } else {
                when {
                    cost > saldo && !isCoD -> {
                        Toast.makeText(
                            this,
                            getString(com.otto.mart.R.string.saldo_deposit_tidak_mencukupi),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        val intent =
                            Intent(this@GrosirCheckOutPaO, RegisterPinResetActivity::class.java)
                        intent.putExtra("confirm", true)
                        startActivityForResult(intent, RC_PIN)
                    }
                }
            }
        }

        var adapter = GrosirChartAdapter(collectedProduct)
        productList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        productList.adapter = adapter

        rlPaymethod.setOnClickListener {
//            startActivityForResult(Intent(this, ListLinkedBankAccountActivity::class.java), 442)

            if (dataProduct.payment_method.size > 1) {

                var intent = Intent(this, GrosirPaymentMethod::class.java)
                intent.putExtra("dataProduct", Gson().toJson(dataProduct))
                startActivityForResult(intent, 442)
            } else {
                paymentMethod = dataProduct.payment_method.get(0).code!!

                var dialog = GrosirDialogPayment()
                dialog.presenter = ListLinkedBankAccountPresenterImpl(this)
                dialog.code = paymentMethod
                dialog.payment_list = dataProduct.payment_method[0].payment_type
                dialog.setOnClickPayment(object : GrosirDialogPayment.OnPaymentMethod {
                    override fun onPayment(
                        paymentType: String,
                        paymentMethod: String,
                        position: Int
                    ) {
                        //Toast.makeText(this@GrosirCheckOutPaO,paymentMethod+paymentType,Toast.LENGTH_SHORT).show()
                        hasSelectedPaymentMethod = true
                        if (paymentType == "LK000036") {
                            selectedbankpos = position
                            if (selectedbankpos >= 0) {
                                paymethodNoItem.visibility = View.GONE
                                tvBankdata1.visibility = View.VISIBLE
                                tvBankdata2.visibility = View.VISIBLE
                                tvBankdata3cont.visibility = View.VISIBLE
                                val dataitem1 =
                                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.binName?.toUpperCase()
                                val dataitem2 =
                                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.accountNumber
                                val dataitem3 =
                                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object2?.formatedBalance
                                val dataitem4 =
                                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.logo
                                val dataitem5 =
                                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.accountType

                                tvBankdata1.text =
                                    "$dataitem1 | " + DataUtil.setFormatAccountNumber(dataitem2)
                                tvBankdata2.text = "Balance : $dataitem3"
                                tvBankdata3.text = dataProduct.payment_method[0].name
                                Glide.with(this@GrosirCheckOutPaO)
                                    .load(dataitem4).error(com.otto.mart.R.drawable.icon_blank)
                                    .placeholder(com.otto.mart.R.drawable.icon_blank)
                                    .into(ivLogo)
                                ivLogo.visibility = View.VISIBLE

                                saldo =
                                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object2?.availableBalance
                                        ?: 0.0
                                isbankSelected = true
                                isItemSelectedLivedata.value = isbankSelected && isCourierSelected
                                mPaymentType = "LK000036"
                                isCoD = false
                            }
                        } else {
                            selectedbankpos = position
                            if (selectedbankpos >= 0) {
                                paymethodNoItem.visibility = View.GONE
                                tvBankdata1.visibility = View.VISIBLE
                                tvBankdata2.visibility = View.VISIBLE
                                tvBankdata3cont.visibility = View.VISIBLE
                                val dataitem1 =
                                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.binName?.toUpperCase()
                                val dataitem2 =
                                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.accountNumber
                                val dataitem3 =
                                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object2?.formatedBalance
                                val dataitem4 =
                                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.logo
                                val dataitem5 =
                                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.accountType

                                tvBankdata1.text =
                                    "$dataitem1 | " + DataUtil.setFormatAccountNumber(dataitem2)
                                tvBankdata2.text = "Balance : $dataitem3"
                                tvBankdata3.text = dataProduct.payment_method[0].name
                                Glide.with(this@GrosirCheckOutPaO)
                                    .load(dataitem4).error(com.otto.mart.R.drawable.icon_blank)
                                    .placeholder(com.otto.mart.R.drawable.icon_blank)
                                    .into(ivLogo)
                                ivLogo.visibility = View.VISIBLE

                                saldo =
                                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object2?.availableBalance
                                        ?: 0.0
                                isbankSelected = true
                                isItemSelectedLivedata.value = isbankSelected && isCourierSelected
                                setEnable()
                                mPaymentType = "LK000038"
                                isCoD = false
                            } else {
                                setEnable()
                                paymethodNoItem.visibility = View.GONE
                                tvBankdata1.visibility = View.VISIBLE
                                tvBankdata2.visibility = View.VISIBLE
                                tvBankdata3cont.visibility = View.VISIBLE
                                tvBankdata1.text = "Cash on Delivery"
                                tvBankdata2.gone()
                                tvBankdata3.text = dataProduct.payment_method[0].name
                                Glide.with(this@GrosirCheckOutPaO)
                                    .load(com.otto.mart.R.drawable.cod_icon)
                                    .error(com.otto.mart.R.drawable.icon_blank)
                                    .placeholder(com.otto.mart.R.drawable.icon_blank)
                                    .into(ivLogo)
                                ivLogo.visibility = View.VISIBLE
                                mPaymentType = "LK000037"
                                isCoD = true
                            }

                        }
                    }

                })
                dialog.show(supportFragmentManager, "payment_method")
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == RC_PIN) {
            callPaymentApi()
            callApiGetContactReceipt()
        } else if (resultCode == RESULT_OK && requestCode == 1234) {

            val product = data!!.getSerializableExtra("addressResult") as ShippingAddressData
            productSave = product
            productChoice = product
            address.text =
                product.detail + ", " + product.region.name + ", " + product.province.name + ", " + product.municipality.name
            (merchantName as TextView).visibility = View.VISIBLE
            merchantName.text = product.name
            //getCostShipment(product.city.id.toLong(),product.district.id.toLong())
        } else if (resultCode == RESULT_OK && requestCode == 442) {

            var name = data?.getStringExtra("name")
            paymentMethod = data?.getStringExtra("code")!!
            var dialog = GrosirDialogPayment()
            dialog.presenter = ListLinkedBankAccountPresenterImpl(this)
            dialog.code = data?.getStringExtra("code")
            dialog.payment_list =
                dataProduct.payment_method.get(data?.getIntExtra("pos", 0)!!.toInt()).payment_type
            dialog.setOnClickPayment(object : GrosirDialogPayment.OnPaymentMethod {
                override fun onPayment(paymentType: String, paymentMethod: String, position: Int) {
                    //Toast.makeText(this@GrosirCheckOutPaO,paymentMethod+paymentType,Toast.LENGTH_SHORT).show()
                    hasSelectedPaymentMethod = true

                    if (paymentType == "LK000034") {

                        selectedbankpos = position
                        if (selectedbankpos >= 0) {

                            paymethodNoItem.visibility = View.GONE
                            tvBankdata1.visibility = View.VISIBLE
                            tvBankdata2.visibility = View.VISIBLE
                            tvBankdata3cont.visibility = View.VISIBLE
                            val dataitem1 =
                                blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.binName?.toUpperCase()
                            val dataitem2 =
                                blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.accountNumber
                            val dataitem3 =
                                blPresenter.getDataAccount().value?.get(selectedbankpos)?.object2?.formatedBalance
                            val dataitem4 =
                                blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.logo
                            val dataitem5 =
                                blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.accountType

                            tvBankdata1.text =
                                "$dataitem1 | " + DataUtil.setFormatAccountNumber(dataitem2)
                            tvBankdata2.text = "Balance : $dataitem3"
                            tvBankdata3.text = "$name"
                            Glide.with(this@GrosirCheckOutPaO)
                                .load(dataitem4).error(com.otto.mart.R.drawable.icon_blank)
                                .placeholder(com.otto.mart.R.drawable.icon_blank)
                                .into(ivLogo)
                            ivLogo.visibility = View.VISIBLE

                            saldo =
                                blPresenter.getDataAccount().value?.get(selectedbankpos)?.object2?.availableBalance
                                    ?: 0.0
                            isbankSelected = true
                            isItemSelectedLivedata.value = isbankSelected && isCourierSelected
                            mPaymentType = "LK000038"
                            isCoD = false

                        }
                    } else {

                        selectedbankpos = position
                        if (selectedbankpos >= 0) {

                            paymethodNoItem.visibility = View.GONE
                            tvBankdata1.visibility = View.VISIBLE
                            tvBankdata2.visibility = View.VISIBLE
                            tvBankdata3cont.visibility = View.VISIBLE
                            val dataitem1 =
                                blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.binName?.toUpperCase()
                            val dataitem2 =
                                blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.accountNumber
                            val dataitem3 =
                                blPresenter.getDataAccount().value?.get(selectedbankpos)?.object2?.formatedBalance
                            val dataitem4 =
                                blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.logo
                            val dataitem5 =
                                blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.accountType

                            tvBankdata1.text =
                                "$dataitem1 | " + DataUtil.setFormatAccountNumber(dataitem2)
                            tvBankdata2.text = "Balance : $dataitem3"
                            tvBankdata3.text = "$name"
                            Glide.with(this@GrosirCheckOutPaO)
                                .load(dataitem4).error(com.otto.mart.R.drawable.icon_blank)
                                .placeholder(com.otto.mart.R.drawable.icon_blank)
                                .into(ivLogo)
                            ivLogo.visibility = View.VISIBLE

                            saldo =
                                blPresenter.getDataAccount().value?.get(selectedbankpos)?.object2?.availableBalance
                                    ?: 0.0
                            isbankSelected = true
                            isItemSelectedLivedata.value = isbankSelected && isCourierSelected

                            mPaymentType = if (name.equals("Pay at Order")) {
                                "LK000036"
                            } else {
                                "LK000038"
                            }
                            isCoD = false
                        } else {
                            paymethodNoItem.visibility = View.GONE
                            tvBankdata1.visibility = View.VISIBLE
                            tvBankdata2.visibility = View.VISIBLE
                            tvBankdata3cont.visibility = View.VISIBLE
                            tvBankdata1.text = "Cash on Delivery"
                            tvBankdata2.gone()
                            tvBankdata3.text = "$name"
                            Glide.with(this@GrosirCheckOutPaO)
                                .load(com.otto.mart.R.drawable.cod_icon)
                                .error(com.otto.mart.R.drawable.icon_blank)
                                .placeholder(com.otto.mart.R.drawable.icon_blank)
                                .into(ivLogo)
                            ivLogo.visibility = View.VISIBLE
                            setEnable()
                            mPaymentType = "LK000037"
                            saldo = 0.0
                            isCoD = true

                        }

                    }
                }

            })
            dialog.show(supportFragmentManager, "payment_method")


        }
    }

    public fun calculate(deliveryFee: Double, totalCost: Double): Double {
        return deliveryFee.plus(totalCost)
    }

    fun setDisable() {
        nextBtn.setCardBackgroundColor(
            ContextCompat.getColor(
                this,
                com.otto.mart.R.color.grey_soft
            )
        )
        textNext.setColor(com.otto.mart.R.color.white)
        nextBtn.isEnabled = false
    }

    fun setEnable() {
        if (hasSelectedPaymentMethod) {
            nextBtn.setCardBackgroundColor(
                ContextCompat.getColor(
                    this,
                    com.otto.mart.R.color.dark_blue_green
                )
            )
            textNext.setColor(com.otto.mart.R.color.white)
            nextBtn.isEnabled = true
        }
    }

    fun callPaymentApi() {
        var request = GrosirPostingRequestV2()
        var itemList = ArrayList<GrosirRequestItem>()
        collectedProduct.forEach {
            itemList.add(
                GrosirRequestItem(
                    it.product_code.toString(),
                    it.real_price,
                    it.quantity.toInt()
                )
            )
        }



        request.items = itemList
        request.supplier_id = intent.extras?.getInt("supplierId", 0)!!
        request.merchant_phone = SessionManager.getPhone()
        request.supplier_name = intent.extras?.getString("supplierName")
        request.payment_method = dataProduct.payment_type
        request.payment_source = "504192"
        request.shipment = ShipmentDetail()
        request.payment_method = paymentMethod
        request.payment_type = mPaymentType

        //  request.deliveryMethodName = radiotext

        if (isPickupMethod) {
            request.shipment?.courier_code = ""
            request.shipment?.recipient_name = ""
            request.shipment?.recipient_email = ""
            request.shipment?.recipient_phone = ""
            request.shipment?.courier_cost = 0.0
            request.shipment?.courier_description = ""
            request.shipment?.courier_name = ""
            request.shipment?.courier_service = ""
            request.delivery_method_name = delivery_method_name_text
            request.delivery_method = "LK000021"
            request.shipment?.courier_weight = 0
            request.shipment?.recipient_address_name = dataProduct.name
            request.shipment?.recipient_address_detail = address.text.toString()

            if (selectedbankpos >= 0) {
                request.payment = PayAccountDetail().apply {
                    accountNumber =
                        blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.accountNumber
                            ?: ""
                    bin = blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.bin
                        ?: ""
                    nameBin =
                        blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.binName
                            ?: ""
                }
                isbankSelected = true
                isItemSelectedLivedata.value = isbankSelected && isCourierSelected
                saldo =
                    blPresenter.getDataAccount().value?.get(selectedbankpos)?.object2?.availableBalance
                        ?: 0.0
            }

            ProgressDialogComponent.showProgressDialog(
                this,
                getString(com.otto.mart.R.string.loading_message),
                false
            )
            GrosirDao(this).postGrosirItemV2(
                request,
                BaseDao.getInstance(this, RC_PAYMENT).callback
            )

        } else {
            request.shipment?.courier_code = shipment?.code
            request.shipment?.recipient_name = receipt_name.textContent
            request.shipment?.recipient_email = receipt_email.textContent
            request.shipment?.recipient_phone = receipt_phone.textContent
            request.shipment?.courier_cost = shipment?.price!!
            request.shipment?.courier_description = ""
            request.delivery_method_name = delivery_method_name_text
            request.delivery_method = "LK000020"
            request.shipment?.courier_name = shipment?.name
            request.shipment?.courier_service = ""
            request.shipment?.courier_weight = 1000

            if (productSave != null) {
                request.shipment?.recipient_address_name = productSave?.name
                request.shipment?.recipient_address_detail = address.text.toString()
                request.shipment?.recipient_city_name = productSave?.province?.name
                request.shipment?.recipient_province_name = productSave?.region?.name
                request.shipment?.recipient_district_name = productSave?.municipality?.name
                request.shipment?.recipient_zip_code = productSave?.zip_code!!.toLong()
            } else {
                request.shipment?.recipient_address_name = addressLoginModel?.name
                request.shipment?.recipient_address_detail = address.text.toString()
                request.shipment?.recipient_city_name = addressLoginModel?.addressProvince
                request.shipment?.recipient_province_name = addressLoginModel?.addressRegion
                request.shipment?.recipient_district_name = addressLoginModel?.addressMunicipality
                //request.shipment?.recipient_zip_code = addressModel.
            }

            if (!request.shipment?.recipient_address_detail!!.isEmpty() &&
                !request.shipment?.recipient_city_name!!.isEmpty() &&
                !request.shipment?.recipient_district_name!!.isEmpty() &&
                !request.shipment?.recipient_province_name!!.isEmpty()
            ) {
                ProgressDialogComponent.showProgressDialog(
                    this,
                    getString(com.otto.mart.R.string.loading_message),
                    false
                )

                if (selectedbankpos >= 0) {
                    request.payment = PayAccountDetail().apply {
                        accountNumber =
                            blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.accountNumber
                                ?: ""
                        bin = blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.bin
                            ?: ""
                        nameBin =
                            blPresenter.getDataAccount().value?.get(selectedbankpos)?.object1?.binName
                                ?: ""
                    }
                }
                GrosirDao(this).postGrosirItemV2(
                    request,
                    BaseDao.getInstance(this, RC_PAYMENT).callback
                )
            } else {
                val dialog = ErrorDialog(
                    this,
                    this@GrosirCheckOutPaO,
                    false,
                    false,
                    getString(com.otto.mart.R.string.maaf_alamat_anda_belum_lengkap),
                    ""
                )
                dialog.show()
            }

        }

    }

    override fun onApiResponseCallback(
        br: BaseResponse?,
        responseCode: Int,
        response: Response<*>?
    ) {
        ProgressDialogComponent.dismissProgressDialog(this)

        if (response?.isSuccessful()!!) {
            br?.let {
                if (responseCode == RC_PAYMENT) {
                    val paymentResponse = br as BogasariPaymentResponseModel
                    if (paymentResponse.meta.code == 200) {
                        handlePayment(paymentResponse)
                        // callApiGetContactReceipt(paymentResponse)
                    } else {
                        val dialog = ErrorDialog(
                            this,
                            this@GrosirCheckOutPaO,
                            false,
                            false,
                            paymentResponse.meta.message,
                            "errorMessage"
                        )
                        dialog.show()
                    }
                } else if (responseCode == LIST_RC) {
                    var listAddress = br as GrosirAddressListResponse
                    if (listAddress.meta.code != 200 || listAddress.data.shipping_addresses.isNullOrEmpty()) {
                        addressLoginModel = SessionManager.getPrefLogin()
                        //getCostShipment(addressLoginModel.addressCityId,addressLoginModel.addressDistrictId)
                        address.text =
                            addressLoginModel.address + ", " + addressLoginModel.addressMunicipality + ", " + addressLoginModel.addressProvince + ", " + addressLoginModel.addressBarangay
                        merchantName.text = SessionManager.getMerchantName()
                        productChoice = null
                        productSave = null
                        return
                    }
                    listAddress.data.shipping_addresses.forEach {
                        if (it.isIs_primary) {
                            productSave = it
                            productChoice = it
                            address.text =
                                it.detail + ", " + it.region.name + ", " + it.province.name + ", " + it.municipality.name + ", " + it.zip_code
                            (merchantName as TextView).visibility = View.VISIBLE
                            merchantName.text = it.name
                            return
                        }

                    }

                } else {
                    var errorMessage = getString(com.otto.mart.R.string.error_msg_something_wrong)
                    br?.let {
                        if ((br as BaseResponseModel) != null) {
                            errorMessage = br.meta.message
                        }
                    }
                    val dialog = ErrorDialog(
                        this,
                        this@GrosirCheckOutPaO,
                        false,
                        false,
                        getString(com.otto.mart.R.string.error_msg_something_wrong),
                        errorMessage
                    )
                    dialog.setOnDismissListener {
                        finish()
                    }
                    dialog.show()
                    return
                }
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        super.onApiFailureCallback(message, ac)
    }

    private fun handlePayment(bogasariPaymentResponseModel: BogasariPaymentResponseModel) {

        val data = PpobOttoagPaymentResponseModel()
        data.data = PaymentData()
        data.data.keyValueList = bogasariPaymentResponseModel.data?.keyValueList

        val paymentData = PpobPayment()
        paymentData.totalPayment =
            DataUtil.FormattedCurrencyToDouble(totalPayment.text.toString()).toDouble()

        val response = PpobInquiryResponse()
        response.data = PpobInquiryResponse.Data()
        response.data.total = DataUtil.InputDecimal(
            DataUtil.FormattedCurrencyToDouble(totalPayment.text.toString()).toString()
        )

        val intent = Intent(this, PpobPaymentDetailActivity::class.java)
        intent.putExtra("need_to_go_home", true)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA, Gson().toJson(data))
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, paymentData)
        intent.putExtra(AppKeys.KEY_PPOB_INQUIRY_DATA, Gson().toJson(response))
        intent.putExtra(PpobPaymentDetailActivity.KEY_IS_FROM_OMZET, false)
        intent.putExtra(
            AppKeys.KEY_CONTACT_PAYMENT_SUCCESS_DATA,
            Gson().toJson(mAddresContact)
        )
        startActivity(intent)
        finish()

    }

    private fun setDefaultAddress() {
        ProgressDialogComponent.showProgressDialog(
            this,
            getString(com.otto.mart.R.string.loading_message),
            false
        )
        GrosirDao(this).getShippingAddressList(BaseDao.getInstance(this, LIST_RC).callback)
    }

    fun callApiGetContactReceipt() {


        OttoKonekAPI.getContactReceipt(this, object : ApiCallback<ReceiptAdressResponse?>() {

            override fun onResponseSuccess(body: ReceiptAdressResponse?) {


                if (isSuccess200) {
                    body?.let {
                        mAddresContact = it
                    }
                    // handlePayment(bogasariPaymentResponseModel)

                }
            }

            override fun onApiServiceFailed(throwable: Throwable) {

            }


        });


    }


    fun checkValidator() {

    }

}

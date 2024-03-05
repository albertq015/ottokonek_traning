package com.otto.mart.ui.activity.bogasari.catalog

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.keys.AppKeys.API_KEY_WALLET_INFO
import com.otto.mart.model.APIModel.Request.bogasari.BogasariInquiryProduct
import com.otto.mart.model.APIModel.Request.bogasari.BogasariInquiryRequestModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.WalletResponseModel
import com.otto.mart.model.APIModel.Response.bogasari.BogasariInquiryResponseModel
import com.otto.mart.model.APIModel.Response.bogasari.BogasariProduct
import com.otto.mart.model.APIModel.Response.bogasari.BogasariResponseModel
import com.otto.mart.presenter.dao.BogasariDao
import com.otto.mart.presenter.dao.WalletDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.*
import com.otto.mart.ui.Partials.RecyclerViewListener
import com.otto.mart.ui.Partials.decorator.SpaceDecorator
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.bogasari.cart.CartBogasariActivity
import com.otto.mart.ui.activity.deposit.ottocash.WalletOttocashActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.fragment.transaction.bogasari.BottomDIalogBogasari
import kotlinx.android.synthetic.main.activity_bogasari.*
import kotlinx.android.synthetic.main.part_total_catalog_bogasari.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response
import java.util.*

class CatalogBogasariActivity : AppActivity() {

    companion object {
        private const val RC_BOGASARI_PRODUCTS = 1
        private const val RC_BOGASARI_INQUIRY = 2
    }
    var fragmentListener : (BogasariProduct,Int)->Unit = this::openFragment

    private lateinit var adapter: CatalogBogasariAdapter
    private var collectedProduct: MutableList<BogasariInquiryProduct>?=null
    private var subTotal: Long? = null
    var amount = 0L;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bogasari)
        initContent()
    }


    private fun initContent() {
        tvToolbarTitle.text = "Bogasari"
        imgToolbarLeft.setOnClickListener { finish() }
        linear_bottom.setOnClickListener { mappingProduct() }

        saldo_otto.setOnClickListener {
            startActivity(Intent(this, WalletOttocashActivity::class.java))
        }

        add_btn.setOnClickListener {
            val fragment = BottomDIalogBogasari.newInstance()
            fragment.initViewBoga2(getString(R.string.text_another_product))
            fragment.show(supportFragmentManager, "add_to_album")
            fragment.setAmountBoga (::transferOmzet2)
        }

        otherProductPrice.isEnabled = false

        amount_btn.setOnClickListener {
            val fragment = BottomDIalogBogasari.newInstance(amount)
            fragment.initViewBoga2(getString(R.string.text_another_product))
            fragment.show(supportFragmentManager, "add_to_album")
            fragment.setAmountBoga (::transferOmzet2)
        }

        otherProductPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (s.isNotEmpty()) {
                        val price = DataUtil.getDigit(it.toString())
                        val newValue = DataUtil.convertCurrency(price)
                        otherProductPrice.removeTextChangedListener(this)
                        /*therProductPrice.setText(newValue.replace("Rp ", ""))*/
                        otherProductPrice.setSelection(otherProductPrice.length())
                        otherProductPrice.addTextChangedListener(this)

                        grandTotal.text = DataUtil.convertCurrency(
                                price.toLong().plus(subTotal ?: 0))

                        otherSubtotal.text=DataUtil.convertCurrency(price)
                    }else {
                        otherSubtotal.text = DataUtil.convertCurrency(0)
                    }
                }
            }

        })

        var recyclerviewListener = RecyclerViewListener { _, _, data ->
            subTotal = data as Long
            val otherPrice = if (otherProductPrice.text.isNotEmpty()) DataUtil.getDigit(otherProductPrice.text.toString()).toLong() else 0
            grandTotal.text = DataUtil.convertCurrency(subTotal?.plus(otherPrice))
        }

        adapter = CatalogBogasariAdapter(recyclerviewListener,fragmentListener)
        productList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        productList.addItemDecoration(SpaceDecorator(DeviceUtil.dpToPx(2), LinearLayout.VERTICAL))
        productList.adapter = adapter
        productList.isNestedScrollingEnabled = false

        callAPI(intent?.getStringExtra("merchantId")!!)
        getWalletInfo()
    }

    private fun getWalletInfo() {
        WalletDao(this).emoneySummary(BaseDao.getInstance(this@CatalogBogasariActivity, AppKeys.API_KEY_WALLET_INFO).callback)
    }

    private fun openFragment(product : BogasariProduct, position :Int){
        if(product.price != null && product.price!! >= 0L){
            val fragment = BottomDIalogBogasari.newInstance(product.price!!)
            fragment.initViewBoga(product)
            fragment.show(supportFragmentManager, "add_to_album")
            fragment.setAmountBoga(::transferOmzet)
        }else {
            val fragment = BottomDIalogBogasari.newInstance()
            fragment.initViewBoga(product)
            fragment.show(supportFragmentManager, "add_to_album")
            fragment.setAmountBoga(::transferOmzet)
        }
    }

    private fun transferOmzet(amount: Long){
        this.amount = amount
        adapter.setDataAmount(amount)
    }

    private fun transferOmzet2(amount: Long){
        otherProductPrice.setText(DataUtil.convertCurrency(amount.toString()))
        add_btn.gone()
        amount_btn.visible()
    }

    private fun mappingProduct() {
        collectedProduct = mutableListOf<BogasariInquiryProduct>()
        adapter.products.forEach {
            if (it.quantity ?: 0 > 0)
                collectedProduct?.add(BogasariInquiryProduct(
                        it.price,
                        it.name,
                        it.sku,
                        it.quantity
                ))
        }

        if (otherProductPrice.text.isNotEmpty()) {
            val price = DataUtil.getDigit(otherProductPrice.text.toString()).toLong()
            if (price > 0) {
                collectedProduct?.add(
                        BogasariInquiryProduct(
                                price,
                                getString(R.string.text_another_product),
                                "xxxxx",
                                0)
                )
            }
        }

        if (collectedProduct?.size == 0) {
            "Silakan Pilih Produk".showToast(this@CatalogBogasariActivity)
            return
        }

        callInquiryAPI(collectedProduct!!)
    }

    private fun callAPI(merchantId: String) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
        BogasariDao(this).getBogasariProducts(merchantId, BaseDao.getInstance(this, RC_BOGASARI_PRODUCTS).callback)
    }

    private fun callInquiryAPI(collectedProduct: MutableList<BogasariInquiryProduct>) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)

        val adminFee = 0L
        val model = BogasariInquiryRequestModel(
                SessionManager.getWalletID(),
                DataUtil.getDateString(Calendar.getInstance().timeInMillis, "yyMMddhhmmss"),
                DataUtil.getDigit(grandTotal.text.toString()).toLong().plus(adminFee),
                EmvcoUtil.parseEMVCOtag62(intent.getStringExtra("qrContent")),
                intent.getStringExtra("qrContent"),
                "${getMyLastLocation().longitude},${getMyLastLocation().latitude}",
                adminFee,
                DataUtil.getDigit(grandTotal.text.toString()).toLong(),
                collectedProduct
        )

        BogasariDao(this).getBogasariInquiry(model, BaseDao.getInstance(this, RC_BOGASARI_INQUIRY).callback)
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        ProgressDialogComponent.dismissProgressDialog(this)
        super.onApiFailureCallback(message, ac)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        ProgressDialogComponent.dismissProgressDialog(this)

        if (br != null && (br as BaseResponseModel).meta.code != 200) {
            val dialog = ErrorDialog(this, this@CatalogBogasariActivity, false, false, br.meta.message , "")
            dialog.show()
            return
        }

        if (br != null && (br as BaseResponseModel).meta.code == 200) {
            if (responseCode == RC_BOGASARI_PRODUCTS && br is BogasariResponseModel) {
                merchantName.text = br.data.merchantName
                handleProductsResponse(br.data.productList?.bogasari)
            } else if (responseCode == RC_BOGASARI_INQUIRY) {
                handleInquiry(br as BogasariInquiryResponseModel)
            }
        }

        if (br != null && (br as BaseResponseModel).meta.code == 200) {
            if (responseCode == API_KEY_WALLET_INFO && br is WalletResponseModel) {
                if (br.data.size > 0) {
                    tv_saldo.setText(br.data[0].balance
                            .replace("IDR", getString(R.string.text_currency))
                            .replace(",","."))
                }
            }
        }

    }

    private fun handleProductsResponse(products: List<BogasariProduct>?) {
        products?.let {
            if (it.isEmpty()) {
//                show empty list message
            } else {
                adapter.products = it as MutableList<BogasariProduct>
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun handleInquiry(bogasariInquiryResponseModel: BogasariInquiryResponseModel) {
        val intent = Intent(this@CatalogBogasariActivity, CartBogasariActivity::class.java)
        intent.putParcelableArrayListExtra("collectedProduct", collectedProduct as ArrayList<out Parcelable>)
        intent.putExtra("productCode", bogasariInquiryResponseModel.data?.inquiryData?.productCode)
        intent.putExtra("refNum", bogasariInquiryResponseModel.data?.inquiryData?.rrn)
        intent.putExtra("wallet",tv_saldo.text)
        startActivity(intent)
    }
}
package id.ottopay.oasis.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.bumptech.glide.Glide
import com.gaelmarhic.quadrant.QuadrantConstants
import com.google.gson.Gson
import com.jakewharton.rxbinding.widget.RxTextView
import com.otto.mart.OttoMartApp
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Request.grosir.GrosirItem
import com.otto.mart.model.APIModel.Request.grosir.GrosirRequestListProduct
import com.otto.mart.model.APIModel.Request.grosir.OasisListCategoryRequest
import com.otto.mart.model.APIModel.Response.grosir.*
import com.otto.mart.presenter.dao.olshop.GrosirDao
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.onChange
import com.otto.mart.support.util.setColor
import com.otto.mart.ui.Partials.LoadMoreListener
import com.otto.mart.ui.Partials.RecyclerViewListener
import com.otto.mart.ui.component.dialog.ErrorDialog
import id.ottopay.oasis.R
import id.ottopay.oasis.adapters.GrosirCategoryList
import id.ottopay.oasis.adapters.GrosirProductAdapter
import kotlinx.android.synthetic.main.activity_grosir_show_product.*
import kotlinx.android.synthetic.main.oasis_toolbar.btnToolbarBack
import kotlinx.android.synthetic.main.oasis_toolbar_search.*
import kotlinx.android.synthetic.main.part_grosir_show_product.*
import retrofit2.Response
import rx.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

class GrosirShowProduct : BaseActivity() {

    lateinit var model: DataResponseItemList
    lateinit var adapter: GrosirProductAdapter
    private var collectedProduct = ArrayList<GrosirItem>()
    var otherPrice = 0.0
    var page = 0
    var moredata = false
    var term = ""
    var categorySelected = ""
    var searchFirst = true
    var scanBar = false
    lateinit var modelProduct : GrosirResponseListProduct
    private var collectedProductSave = ArrayList<DataResponseProductItem>()
    var dialogCategory = OasisCategoryDialog()
    var requestScanBarCode = 1231
    var barcode = ""
    var minOrder = 0.0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grosir_show_product)
        model = Gson().fromJson(intent.getStringExtra("dataProduct"), DataResponseItemList::class.java)

        tvProductTitle.setText(model.name)
        kota.setText(model.kota)
        Glide.with(this).load(model.logo_path).into(product_image)
        btnToolbarBack.setOnClickListener { View ->
            onBackPressed()
        }
        //callApiGetList(model.id, "all")
        callApiGetCategory(model.id)
        initListCategory()
        checkValidation()
        totalPayment.onChange {
            checkValidation()
        }

        imgToolbarRight.setOnClickListener {
            scanBar = true
            searchFirst = true
            search_text.text.clear()
            category_selected.text = getString(com.otto.mart.R.string.text_category)
            val intent = Intent()
            intent.setClassName(this, QuadrantConstants.BAR_CODE_SCAN)
            startActivityForResult(intent, requestScanBarCode)

        }

        setDisable()

        close.setOnClickListener(View.OnClickListener {
            search_text.text.clear()
        })

        search_text.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!scanBar) {
                    scanBar = false
                    term = p0.toString()

                    if (p0.toString().length > 0) {
                        close.visibility = View.VISIBLE
                    } else {
                        close.visibility = View.GONE
                        page = 0
                        searchFirst = true
                        callApiGetList(model.id, categorySelected)
                    }

                }

            }

        })

            RxTextView.textChanges(search_text)
                    .debounce(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ textChanged ->
                        term = search_text.getText().toString()
                        if(term.isNotBlank()) {
                                page = 0
                                searchFirst = true
                                callApiGetList(model.id, categorySelected)
                        }
                    })

        search_text.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                term = search_text.getText().toString()
                if(term.isNotBlank()) {
                    page = 0
                    searchFirst = true
                    callApiGetList(model.id, categorySelected)
                }
                return@OnKeyListener true
            }
            false
        })


        var recyclerviewListener = RecyclerViewListener { opt, _, data ->
            val subTotal = data as Double
            if (opt == 1) {
                otherPrice = otherPrice.toBigDecimal().plus(subTotal.toBigDecimal()).toDouble()
            } else {
                otherPrice = otherPrice.toBigDecimal().minus(subTotal.toBigDecimal()).toDouble()
            }
            totalPayment.text = DataUtil.InputDecimal(otherPrice.toBigDecimal().toString())
        }
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_product)
        var linearLayout =  androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayout
        adapter = GrosirProductAdapter(recyclerviewListener, LoadMoreListener {
            if (moredata && !scanBar) {
                callApiGetList(model.id, categorySelected)
                searchFirst = false
            } else if (scanBar) {
                moredata = false
                scanBar = false
                //callApiBarCode(model.id)
                //searchFirst = false
            }
        }, recyclerView,linearLayout)
        adapter.isLoading = false
        recyclerView.adapter = adapter
    }

    fun setDisable(){
        /*nextBtn.setCardBackgroundColor(ContextCompat.getColor(this, com.otto.mart.R.color.grey_soft))
        textNext.setColor(com.otto.mart.R.color.black)
        nextBtn.isEnabled = false*/
        recycler_product.visibility = View.GONE
        not_found_layout.visibility = View.VISIBLE
    }

    fun setEnable(){
        /*nextBtn.setCardBackgroundColor(ContextCompat.getColor(this, com.otto.mart.R.color.dark_sky_blue))
        textNext.setColor(com.otto.mart.R.color.white)
        nextBtn.isEnabled = true*/
        recycler_product.visibility = View.VISIBLE
        not_found_layout.visibility = View.GONE
    }


    fun initListCategory() {



        minOrder = intent.getDoubleExtra("minOrder", 0.0)
        minOrderTv.text = getString(com.otto.mart.R.string.text_minimum_order_mandatory) +" "+ DataUtil.InputDecimal(minOrder.toString())


        val adapter = GrosirCategoryList(RecyclerViewListener { id, pos, data ->
            //collectedProductSave.add(modelProduct.data.get(pos))
        })
        adapter.categories = model
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_category)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        nextBtn.isEnabled = true
    }

    fun checkValidation() {
        if (!totalPayment.text.toString().equals("-") && DataUtil.FormattedCurrencyToDouble(totalPayment.text.toString()).toDouble() >= minOrder) {
            nextBtn.setCardBackgroundColor(ContextCompat.getColor(this, com.otto.mart.R.color.dark_blue_green))
            textNext.setColor(com.otto.mart.R.color.white)
            nextBtn.isEnabled = true
            nextBtn.setOnClickListener {
                nextBtn.isEnabled = false
                val intent = Intent(this, GrosirCheckOutPaO::class.java)
                mappingProduct()
                intent.putExtra("supplierId", model.id)
                intent.putExtra("dataProduct", Gson().toJson(model))
                intent.putExtra("collectedProduct", Gson().toJson(collectedProduct))
                intent.putExtra("supplierName", tvProductTitle.text.toString())
                intent.putExtra("minOrder", minOrder)
                intent.putExtra("totalPrice",otherPrice)
                startActivity(intent)

            }
        } else {
            nextBtn.setCardBackgroundColor(ContextCompat.getColor(this, com.otto.mart.R.color.grey_soft))
            textNext.setColor(com.otto.mart.R.color.white)
            nextBtn.isEnabled = false
        }
    }

    fun callApiGetList(id: Int, category: String) {
        page += 1
        val request = GrosirRequestListProduct()
        request.supplier_id = id
        request.category_id = category
        request.page = page
        request.count = 25
        request.name = term

        showApiProgressDialog(OttoMartApp.getAppComponent(),object  :GrosirDao(this){
            override fun call() {
                getSupplierListProduct(request, BaseDao.getInstance(this@GrosirShowProduct, AppKeys.API_KEY_GET_GROSIR_LIST).callback)
            }
        })
    }

    fun callApiBarCode(id: Int){
        val request = GrosirRequestListProduct()
        request.supplier_id = id
        request.barcode = barcode
        request.count = 25
        request.category_id = ""

        showApiProgressDialog(OttoMartApp.getAppComponent(),object  :GrosirDao(this){
            override fun call() {
                getSupplierListProductScan(request, BaseDao.getInstance(this@GrosirShowProduct, AppKeys.API_KEY_GET_GROSIR_LIST).callback)
            }
        })
    }


    fun callApiGetCategory(supplierId: Int) {
        scanBar = false
        val request = OasisListCategoryRequest()
        request.supplier_id = supplierId
        showApiProgressDialog(OttoMartApp.getAppComponent(),object  :GrosirDao(this){
            override fun call() {
                getSupplierListCategory(request, BaseDao.getInstance(this@GrosirShowProduct, AppKeys.API_KEY_GET_GROSIR_LIST_CATEGORY).callback)
            }
        })
    }


    private fun handleProductsResponse(products: List<DataResponseProductItem>?) {
        products?.let {
            if (it.isEmpty()) {
//                show empty list message
            } else {
                if(scanBar){
                    adapter.products = it as MutableList<DataResponseProductItem>
                    moredata = false
                }else if(!searchFirst){
                    var collection = it as MutableList<DataResponseProductItem>
                    if (collection.size > 0) {
                        if(!scanBar) {
                            moredata = true
                        }
                        collection.forEach {
                            adapter.products.add(it)
                        }
                        adapter.notifyItemInserted(collection.size - 1)
                    } else {
                        moredata = false
                    }
                }else{
                    part.visibility = View.VISIBLE
                    main_layout.visibility = View.VISIBLE
                    not_found_layout_search.visibility = View.GONE
                    imgToolbarRight.visibility = View.VISIBLE
                    adapter.products = it as MutableList<DataResponseProductItem>
                    moredata = true
                }
                adapter.notifyDataSetChanged()
                adapter.isLoading = false

            }
        }
    }

    private fun mappingProduct() {
        collectedProduct = ArrayList<GrosirItem>()
        adapter.productsCart.forEach {
            if (it.temp_qty ?: 0 > 0)
                collectedProduct?.add(GrosirItem(
                        it.product_code,
                        it.price,
                        it.temp_qty.toString(),
                        it.name,
                        it.photo,
                        it.weight
                ))
        }

    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        super.onApiResponseCallback(br, responseCode, response)
        ProgressDialogComponent.dismissProgressDialog(this)
        if (response!!.isSuccessful){
            if (responseCode == AppKeys.API_KEY_GET_GROSIR_LIST) {
                modelProduct = br as GrosirResponseListProduct
                if(modelProduct.meta.code == 200) {
                    if(searchFirst){
                        if(modelProduct.data.size < 1) {
                            part.visibility = View.GONE
                            main_layout.visibility = View.GONE
                            not_found_layout_search.visibility = View.VISIBLE
                        }else{
                            part.visibility = View.VISIBLE
                            main_layout.visibility = View.VISIBLE
                            not_found_layout_search.visibility = View.GONE
                            handleProductsResponse(modelProduct.data)
                            setEnable()
                        }
                    }else if (!searchFirst && modelProduct.data.size > 0) {
                        handleProductsResponse(modelProduct.data)
                        setEnable()
                    } else {
                        if (searchFirst) {
                            setDisable()
                        }else {
                            moredata = false
                        }
                    }
                } else {
                    var errorMessage = getString(com.otto.mart.R.string.error_msg_something_wrong)
                    br?.let {
                        if (modelProduct.meta != null) {
                            errorMessage = modelProduct.meta.message
                        }else {
                            errorMessage = getString(com.otto.mart.R.string.error_msg_something_wrong)
                        }
                    }
                    val dialog = ErrorDialog(this, this@GrosirShowProduct, false, false, errorMessage, errorMessage)
                    dialog.setOnDismissListener {
                        finish()
                    }
                    dialog.show()
                    return
                }
            } else if (responseCode == AppKeys.API_KEY_GET_GROSIR_LIST_CATEGORY){
                var responseCategory = br as OasisListCategoryResponse
                if(responseCategory.data?.size!! > 0) {
                    if(responseCategory.data?.get(0)?.total_product!! >0 ) {
                        buttonContainer.isEnabled = true
                        responseCategory.data?.get(0)?.let {
                            page = 0
                            searchFirst = true
                            callApiGetList(model.id, it!!.id!!)
                            categorySelected = it!!.id!!
                        }
                        buttonContainer.setOnClickListener {
                            dialogCategory.mCategoryList = responseCategory.data as MutableList<OasisItemCategoryResponse>
                            dialogCategory.show(supportFragmentManager, dialogCategory.tag)
                            dialogCategory.setItemSelectedClickListener(object : OasisCategoryDialog.OnCategoryMethodSelected {
                                override fun onCategoryMethodSelected(item: OasisItemCategoryResponse, position: Int) {
                                    page = 0
                                    searchFirst = true
                                    callApiGetList(model.id, item.id!!)
                                    categorySelected = item.id!!
                                    category_selected.text = item.name
                                }
                            })
                        }
                    }
                } else {
                    buttonContainer.isEnabled = false
                }
            }
        } else {
            var errorMessage = getString(com.otto.mart.R.string.error_msg_something_wrong)
            br?.let {
                if (model != null) {
                    errorMessage = modelProduct.meta.message
                }else {
                    errorMessage = getString(com.otto.mart.R.string.error_msg_something_wrong)
                }
            }
            val dialog = ErrorDialog(this, this@GrosirShowProduct, false, false, errorMessage, errorMessage)
            dialog.setOnDismissListener {
                finish()
            }
            dialog.show()
            return
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == requestScanBarCode) {
                barcode = data?.getStringExtra("dataResult") ?: ""
                callApiBarCode(model.id)
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        super.onApiFailureCallback(message, ac)
    }

}

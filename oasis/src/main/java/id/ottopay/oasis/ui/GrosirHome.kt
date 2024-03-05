package id.ottopay.oasis.ui

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Request.grosir.GrosirCheckSupplierRequest
import com.otto.mart.model.APIModel.Request.grosir.GrosirRegisterSupplierRequest
import com.otto.mart.model.APIModel.Request.grosir.GrosirRequestSupplier
import com.otto.mart.model.APIModel.Response.grosir.DataResponseItemList
import com.otto.mart.model.APIModel.Response.grosir.GrosirCheckSupplierRespond
import com.otto.mart.model.APIModel.Response.grosir.GrosirListSupplierResponse
import com.otto.mart.model.APIModel.Response.grosir.GrosirRegisterSupplierResponse
import com.otto.mart.presenter.dao.olshop.GrosirDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.ui.Partials.RecyclerViewListener
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import id.ottopay.oasis.R
import id.ottopay.oasis.adapters.GrosirListAdapter
import kotlinx.android.synthetic.main.oasis_toolbar.*
import retrofit2.Response

class GrosirHome : AppActivity(){

    private lateinit var dataProduct : DataResponseItemList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_grosir_home)
        tvToolbarTitle.setText(getString(com.otto.mart.R.string.text_shop_wholsale))
        btnToolbarBack.setOnClickListener { View ->
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        callApiGetList()
    }

    fun callApiGetList(){
//        val request = GrosirRequestSupplier()
//        request.merchant_phone = SessionManager.getPhone()
//        request.area_id = SessionManager.getPrefLogin().addressBarangayCode
        ProgressDialogComponent.showProgressDialog(this, getString(com.otto.mart.R.string.loading_message), false).show()
        GrosirDao(this).getSupplierList(SessionManager.getPrefLogin().addressBarangayCode,SessionManager.getPhone(), BaseDao.getInstance(this,AppKeys.API_KEY_GET_GROSIR_LIST).callback)
    }

    fun callApiCheckSupplier(supplierId : Int){
        val request = GrosirCheckSupplierRequest()
        request.supplier_id = supplierId
        ProgressDialogComponent.showProgressDialog(this, getString(com.otto.mart.R.string.loading_message), false).show()
        GrosirDao(this).getCheckSupplier(request, BaseDao.getInstance(this,AppKeys.API_KEY_GET_GROSIR_CHECK_SUPPLIER).callback)
    }


    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        super.onApiResponseCallback(br, responseCode, response)
        ProgressDialogComponent.dismissProgressDialog(this)
        if(response!!.isSuccessful){
            if(responseCode == AppKeys.API_KEY_GET_GROSIR_LIST){
                val model = br as GrosirListSupplierResponse
                if(model.meta.isStatus){
                    val adapter = GrosirListAdapter (RecyclerViewListener { id, pos, data ->
                        dataProduct = data as DataResponseItemList

//                        val intent = Intent(this, GrosirShowProduct::class.java)
//                        intent.putExtra("dataProduct", Gson().toJson(dataProduct))
//                        intent.putExtra("minOrder", dataProduct.minimum_order)
//                        startActivity(intent)
                        if(model.data.get(pos).payment_type.equals("Pay at Delivery")) {
                            val intent = Intent(this, GrosirShowProduct::class.java)
                            intent.putExtra("dataProduct", Gson().toJson(dataProduct))
                            intent.putExtra("minOrder", dataProduct.minimum_order)
                            startActivity(intent)
                        } else {
                            callApiCheckSupplier(model.data.get(pos).id)
                        }
                    },this)
                    adapter.categories = model
                    val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)
                    recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, LinearLayout.VERTICAL, false)
                    recyclerView.adapter = adapter
                }else{
                    var errorMessage = getString(com.otto.mart.R.string.error_msg_something_wrong)
                    br?.let {
                        if (model != null) {
                            errorMessage = model.meta.message
                        }
                    }
                    val dialog = ErrorDialog(this, this@GrosirHome, false, false, getString(com.otto.mart.R.string.error_msg_something_wrong), errorMessage)
                    dialog.setOnDismissListener {
                        finish()
                    }
                    dialog.show()
                    return
                }
            }else if(responseCode == AppKeys.API_KEY_GET_GROSIR_CHECK_SUPPLIER){
                val modelCheck = br as GrosirCheckSupplierRespond
                if(modelCheck.meta.isStatus && modelCheck.meta.code == 200){
                    if(modelCheck.data.isRegistered){
                        val intent = Intent(this, GrosirShowProduct::class.java)
                        intent.putExtra("dataProduct", Gson().toJson(dataProduct))
                        intent.putExtra("minOrder", dataProduct.minimum_order)
                        startActivity(intent)
                    }else{
                        val request = GrosirRegisterSupplierRequest()
                        request.supplier_id = dataProduct.id
                        request.address = SessionManager.getDefaultAddress()
                        request.mid = SessionManager.getUserProfile().merchant_id
                        request.contact_person = SessionManager.getUserProfile().name
                        request.merchant_name = SessionManager.getUserProfile().merchant_name
                        ProgressDialogComponent.showProgressDialog(this, getString(com.otto.mart.R.string.loading_message), false).show()
                        GrosirDao(this).getRegisterSupplier(request, BaseDao.getInstance(this,AppKeys.API_KEY_GET_GROSIR_REGISTER_SUPPLIER).callback)
                    }
                }else{
                    var errorMessage = getString(com.otto.mart.R.string.error_msg_something_wrong)
                    br.let {
                        if (modelCheck != null) {
                            errorMessage = modelCheck.meta.message
                        }
                    }
                    val dialog = ErrorDialog(this, this@GrosirHome, false, false, getString(com.otto.mart.R.string.error_msg_something_wrong), errorMessage)
                    dialog.setOnDismissListener {
                        finish()
                    }
                    dialog.show()
                    return
                }
            }else if (responseCode == AppKeys.API_KEY_GET_GROSIR_REGISTER_SUPPLIER){
                val model = br as GrosirRegisterSupplierResponse
                if(model.meta.isStatus && model.meta.code == 200){
                    val intent = Intent(this, GrosirShowProduct::class.java)
                    intent.putExtra("dataProduct", Gson().toJson(dataProduct))
                    intent.putExtra("minOrder", dataProduct.minimum_order)
                    startActivity(intent)
                }else{
                    var errorMessage = getString(com.otto.mart.R.string.error_msg_something_wrong)
                    br.let {
                        if (model != null) {
                            errorMessage = model.meta.message
                        }
                    }
                    val dialog = ErrorDialog(this, this@GrosirHome, false, false, getString(com.otto.mart.R.string.error_msg_something_wrong), errorMessage)
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


}
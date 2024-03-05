package id.ottopay.oasis.ui
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.otto.mart.model.APIModel.Request.grosir.GrosirItem
import com.otto.mart.model.APIModel.Request.grosir.GrosirPostingRequest
import com.otto.mart.model.APIModel.Request.grosir.GrosirRequestItem
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.presenter.dao.olshop.GrosirDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.DataUtil
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.component.dialog.Popup
import id.ottopay.oasis.R
import id.ottopay.oasis.adapters.GrosirChartAdapter
import kotlinx.android.synthetic.main.activity_grosir_check_out.*
import kotlinx.android.synthetic.main.oasis_toolbar.*
import kotlinx.android.synthetic.main.part_grosir_show_product.*
import retrofit2.Response


class GrosirCheckOut : AppActivity() {
    private var collectedProduct = ArrayList<GrosirItem>()
    companion object {
        private const val RC_PIN = 1
        private const val RC_PAYMENT = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grosir_check_out)

        tvToolbarTitle.setText(getString(com.otto.mart.R.string.pembayaran))
        btnToolbarBack.setOnClickListener { View ->
            onBackPressed()
        }
        initContent()
    }

    fun initContent(){
        minOrderTv.text = getString(com.otto.mart.R.string.text_minimum_order_mandatory)+" "+ DataUtil.convertCurrency(intent.getStringExtra("minOrder"))
        totalPayment.text = intent.getStringExtra("totalPrice")
        total_payment_row.text = intent.getStringExtra("totalPrice")
        merchantOwner.text = SessionManager.getPrefLogin().merchant_name
        textNext.text = getString(com.otto.mart.R.string.text_message).toUpperCase()
        supplierName.text = intent.getStringExtra("supplierName")
        //tvProductTitle.text = intent.getStringExtra("supplierName")
        merchantName.text = SessionManager.getPrefLogin().name
        address.text = SessionManager.getDefaultAddress()
        //address2.text = SessionManager.getUserProfile().addressCity +" - "+SessionManager.getUserProfile().addressDistrict+" - "+SessionManager.getUserProfile().addressProvince
        var string = intent.getStringExtra("collectedProduct")

        val token = object : TypeToken<ArrayList<GrosirItem>>() {

        }.type
        collectedProduct = Gson().fromJson(string,token)
        textNext.text = getString(com.otto.mart.R.string.pesan_sekarang)

        nextBtn.setOnClickListener {
            if(DataUtil.getDigit(totalPayment.text.toString())>= DataUtil.getDigit(minOrderTv.text.toString())) {
                val intent = Intent(this@GrosirCheckOut, RegisterPinResetActivity::class.java)
                intent.putExtra("confirm", true)
                startActivityForResult(intent, RC_PIN)
            }else{
                Toast.makeText(this,getString(com.otto.mart.R.string.text_not_reach_min_order),Toast.LENGTH_SHORT).show()
            }
        }

        var adapter = GrosirChartAdapter(collectedProduct)
        productList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        productList.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == RC_PIN) {
            callPaymentApi()
        }
    }

    fun callPaymentApi(){
        var request = GrosirPostingRequest()
        var itemList = ArrayList<GrosirRequestItem>()
        collectedProduct.forEach {
            itemList?.add(GrosirRequestItem(
                    it.product_code.toString(),
                    it.real_price,
                    it.quantity.toInt()
            ))
        }
        request.items = itemList
        request.supplier_id = intent.getIntExtra("supplierId",0)
        request.merchant_phone = SessionManager.getPhone()
        ProgressDialogComponent.showProgressDialog(this, getString(com.otto.mart.R.string.loading_message), false)
        GrosirDao(this).postGrosirItem(request, BaseDao.getInstance(this, RC_PAYMENT).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        ProgressDialogComponent.dismissProgressDialog(this)

        if ((!response?.isSuccessful()!!) || ((br as BaseResponseModel).meta.code != 200)) {
            var errorMessage = getString(com.otto.mart.R.string.error_msg_something_wrong)
            br?.let {
                if ((br as BaseResponseModel) != null) {
                    errorMessage = br.meta.message
                }
            }
            val dialog = ErrorDialog(this, this@GrosirCheckOut, false, false, getString(com.otto.mart.R.string.error_msg_something_wrong), errorMessage)
            dialog.setOnDismissListener {
                finish()
            }
            dialog.show()
            return
        }

        br?.let {
            if (responseCode == RC_PAYMENT) {
                //handlePayment(br as BogasariPaymentResponseModel)
                var popup = Popup()
                popup.title = getString(com.otto.mart.R.string.pesanan_diproses)
                popup.message = getString(com.otto.mart.R.string.pesanan_kamu_sedang_diproses)
                popup.positiveButton = getString(com.otto.mart.R.string.lanjut_belanja)
                popup.negativeButton = getString(com.otto.mart.R.string.lihat_riwayat)
                popup.negativeAction = {
                    gotodetail()
                }
                popup.isCancelable = false
                popup.positiveAction = {
                    buymore()
                }
                popup.singleShow(supportFragmentManager,"showpaoconfirmation")
            }
        }
    }

    fun gotodetail(){
        val intent = Intent(this, GrosirCheckStatusActivity::class.java)
        intent.putExtra("fromgrosir",true)
        startActivity(intent)
    }

    fun buymore(){
        val intent = Intent(this, GrosirHome::class.java)
        startActivity(intent)
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        super.onApiFailureCallback(message, ac)
    }

}

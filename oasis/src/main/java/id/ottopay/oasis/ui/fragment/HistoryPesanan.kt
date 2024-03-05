package id.ottopay.oasis.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Request.grosir.HistoryOasisOrderRequestModel
import com.otto.mart.model.APIModel.Request.grosir.OasisApprovedOrderRequest
import com.otto.mart.model.APIModel.Response.grosir.HistoryOasisOrderItem
import com.otto.mart.model.APIModel.Response.grosir.HistoryOasisOrderResponseModel
import com.otto.mart.model.APIModel.Response.grosir.OasisApprovedOrderResponse
import com.otto.mart.presenter.dao.olshop.GrosirDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.component.dialog.Popup
import com.otto.mart.ui.fragment.AppFragment
import id.ottopay.oasis.R
import id.ottopay.oasis.adapters.HistoryOasisFragmentAdapter
import id.ottopay.oasis.ui.GrosirDetailHistoryActivity
import kotlinx.android.synthetic.main.oasis_history_fragment.*
import retrofit2.Response

class HistoryPesanan() : AppFragment() {

    var mContext : Context? = null
    var mView : View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        message_order_oasis.visibility = View.VISIBLE
        callApiCheck()
    }

    private fun initRecyclerView() {
        recycler_view.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recycler_view.setLayoutManager(linearLayoutManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = context
        mView = inflater.inflate(R.layout.oasis_history_fragment, container, false)
        return mView
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        super.onApiResponseCallback(br, responseCode, response)
        if(response!!.isSuccessful){
            br?.let {
                if(responseCode == AppKeys.API_KEY_GET_GROSIR_LIST){
                    val historyRespond = br as HistoryOasisOrderResponseModel
                    if(historyRespond.baseMeta?.code == 200) {
                        if (historyRespond.data?.on_progress != null){
                            historyRespond.data?.on_progress.let {
                                handleList(it!!)
                            }
                        }



                    } else {
                        var errorDialog = ErrorDialog(mContext!!,activity,true,false,historyRespond.baseMeta.message,"")
                        errorDialog.show()
                    }
                }else if(responseCode == AppKeys.API_KEY_APPROVED_ORDER){
                    val approvedRespond = br as OasisApprovedOrderResponse
                    if(approvedRespond.baseMeta.code==200){
                        var popup = Popup()
                        popup.title = "Order Completed"
                        popup.message = "Confirm receive order successful"
                        popup.positiveButton = "Okay"
                        popup.positiveAction = {
                            callApiCheck()
                        }
                        popup.isHideBtnNegative = true
                        popup.singleShow(childFragmentManager,"popupconfirmationafter")
                    } else {
                        var errorDialog = ErrorDialog(mContext!!,activity,true,false,approvedRespond.baseMeta.message,"")
                        errorDialog.show()
                    }
                }
            }
        }

    }

    fun handleList(listHistoy : List<HistoryOasisOrderItem>){
        activity?.let {
            val adapter = HistoryOasisFragmentAdapter(it, listHistoy)
            recycler_view.adapter = adapter
            adapter?.setmOnViewClickListener(object : HistoryOasisFragmentAdapter.OnViewClickListener{
                override fun onViewClickListener(item: HistoryOasisOrderItem, position: Int) {

                    val intent = Intent(mContext,GrosirDetailHistoryActivity::class.java)
                    intent.putExtra("data",item)

                    startActivity(intent)
                }

            })
            adapter.setmOnConfirmClickListener(object : HistoryOasisFragmentAdapter.OnConfirmClickListener{
                override fun onConfirmClickListener(position: Int) {
                    var popup = Popup.getConfirmDialog("Confirm Receive Order","I have received the product with a good condition. I will not complaint or request a refund.");
                    popup.isHideBtnNegative = true
                    popup.positiveButton = "Confirm"
                    popup.positiveAction = {
                        callApiConfirmation(listHistoy.get(position).order_no!!,listHistoy.get(position).payment_type?.code!!,listHistoy.get(position).reff_no!!)
                    }
                    popup.singleShow(childFragmentManager,"popupconfirmation")
                }

            })
        }
    }

    fun callApiConfirmation(orderNo : String,paymentType :String,reffNo: String){
        var request = OasisApprovedOrderRequest()
        request.orderNo = orderNo
        request.paymentType = paymentType
        request.reffNo = reffNo
        ProgressDialogComponent.showProgressDialog(activity, getString(com.otto.mart.R.string.loading_message), false).show()
        GrosirDao(this).getGrosirCheckStatusApproved(request, BaseDao.getInstance(this, AppKeys.API_KEY_APPROVED_ORDER).callback)

    }


    override fun onApiFailureCallback(message: String?) {
        super.onApiFailureCallback(message)
    }

    fun callApiCheck(){

        ProgressDialogComponent.showProgressDialog(activity, getString(com.otto.mart.R.string.loading_message), false).show()
        GrosirDao(this).getGrosirCheckStatus("", BaseDao.getInstance(this, AppKeys.API_KEY_GET_GROSIR_LIST).callback)
    }
}
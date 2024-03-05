package id.ottopay.oasis.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import app.beelabs.com.codebase.component.ProgressDialogComponent.dismissProgressDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Response.grosir.PaymentMethodOasisList
import com.otto.mart.model.APIModel.Response.grosir.PaymentTypeResponse
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.support.util.ApiRoseCallback
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.multibank.LinkedAccountAdapter2
import com.otto.mart.ui.activity.multibank.DetailAccountActivity
import com.otto.mart.ui.activity.multibank.LLBAViewModel
import com.otto.mart.ui.activity.multibank.ListLinkedBankAccountPresenter
import id.ottopay.oasis.R
import kotlinx.android.synthetic.main.layout_grosir_payment.*
import java.util.ArrayList

class GrosirDialogPayment : BottomSheetDialogFragment() {

    var presenter: ListLinkedBankAccountPresenter? = null
    var payment_list : ArrayList<PaymentTypeResponse>? = null
    var code : String?= ""
    var onclickPayment : OnPaymentMethod? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_grosir_payment, container, false)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(code == "LK000017"){
            cash_cont.gone()
        }else {
            if(payment_list!!.size> 1) {
                cash_cont.visible()
            } else if(payment_list!!.size == 1 && payment_list!!.get(0).code=="LK000037"){
                cash_cont.visible()
                recycler_view.gone()
                non_cash.gone()
            } else {
                cash_cont.gone()
                recycler_view.visible()
            }
        }

        callListAccounBank()
        cash_btn.setOnClickListener {
            onclickPayment!!.onPayment(payment_list!!.get(0)!!.code!!,code!!,-1)
            dialog?.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.otto.mart.R.style. AppBottomSheetDialogTheme);
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog

            val bottomSheet = d.findViewById<View>(com.otto.mart.R.id.design_bottom_sheet) as FrameLayout?
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    @SuppressLint("NotifyDataSetChanged")
    fun listDataMVVM() {
        presenter!!.getDataAccount().observe(this,
            Observer<MutableList<LLBAViewModel>> {
                recycler_view.adapter?.notifyDataSetChanged()
            })

        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this.requireContext(),
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )

        val adapter = LinkedAccountAdapter2(presenter!!.getDataAccount().value!!)
        recycler_view.adapter = adapter
        adapter.setmOnViewClickListener(object : LinkedAccountAdapter2.OnViewClickListener {

            override fun onViewClickListener(
                position: Int,
                accountNumber: String,
                responCode: Int?
            ) {
                if (responCode != 498) {
                    //menuSelected(position, accountNumber)
                    if(payment_list!!.size > 1){
                        onclickPayment!!.onPayment(payment_list!!.get(1)!!.code!!,code!!,position)
                    } else {
                        onclickPayment!!.onPayment(payment_list!!.get(0)!!.code!!,code!!,position)
                    }
                    dialog?.dismiss()
                }
            }
        })
        adapter.setOnRetryRequestListener(object : LinkedAccountAdapter2.OnRetryViewCLickListener {
            override fun onRetryViewClickListener(pos: Int) {
                presenter!!.onRequestRetry(pos)
            }

        })


    }

    fun callListAccounBank() {
        OttoKonekAPI.getListAccountBank(this.requireContext(), object :
            ApiRoseCallback<AccountListResponse>() {

            override fun onResponseSuccess(body: AccountListResponse?) {
//                listData(body!!.data)
                presenter!!.setDataAccount(body?.data!!)
                listDataMVVM()
                presenter!!.loadIssuerBalance()
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                //showErrorDialog("Failed Network")
            }

        })

    }

    private fun menuSelected(position: Int, accountNumber: String) {
        val intent = Intent(this.requireContext(), DetailAccountActivity::class.java)
        intent.putExtra("pos", position)
        intent.putExtra("accountNumber", accountNumber)
        startActivity(intent)
    }

    fun setOnClickPayment(mOnViewClickListener : OnPaymentMethod){
        this.onclickPayment = mOnViewClickListener
    }

    interface OnPaymentMethod{
        fun onPayment(paymentType : String,paymentMethod : String, position: Int)
    }

}
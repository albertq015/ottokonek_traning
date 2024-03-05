package com.otto.mart.ui.fragment.ppob


import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.otto.mart.R
import com.otto.mart.model.localmodel.ppob.PpobPaymentMethod
import com.otto.mart.support.util.notNull
import com.otto.mart.ui.Partials.adapter.ppob.PpobPaymentMethodAdapter
import kotlinx.android.synthetic.main.fragment_payment_method.*

/**
 * A simple [Fragment] subclass.
 */
class PpobPaymentMethodFragment : BottomSheetDialogFragment() {

    var mPaymentMethodList = mutableListOf<PpobPaymentMethod>()
    var mSelectedPaymentMethod : PpobPaymentMethod? = null

    var mOnPaymeentMethodSelected : OnPaymentMethodSelected? = null
    var mSelectedPosition : Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_payment_method, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRecyclerView()
        displayPaymentMethod()
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

    private fun initView() {
        btnSelect.setOnClickListener {
            mSelectedPosition?.let { it1 -> mSelectedPaymentMethod?.let { it2 -> mOnPaymeentMethodSelected?.onPaymeentMethodSelected(it2, it1) } }
            dismiss()
        }
    }

    private fun initRecyclerView() {
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    private fun displayPaymentMethod() {
        mPaymentMethodList.notNull {
            val adapter = PpobPaymentMethodAdapter(this.context!!, mPaymentMethodList)
            recyclerview.adapter = adapter

            adapter.setmOnViewClickListener(object: PpobPaymentMethodAdapter.OnViewClickListener{
                override fun onViewClickListener(item: PpobPaymentMethod, position: Int) {
                    mSelectedPaymentMethod = item
                    mSelectedPosition = position
                    mPaymentMethodList.get(position).selected = true
                }
            })
        }
    }

    fun setItemSelectedClickListener(mOnViewClickListener: OnPaymentMethodSelected) {
        this.mOnPaymeentMethodSelected = mOnViewClickListener
    }

    interface OnPaymentMethodSelected {
        fun onPaymeentMethodSelected(item: PpobPaymentMethod, position: Int)
    }
}

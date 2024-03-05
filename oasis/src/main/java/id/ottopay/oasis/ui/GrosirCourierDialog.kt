package id.ottopay.oasis.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.model.APIModel.Response.grosir.OasisListCourier
import com.otto.mart.model.APIModel.Response.grosir.ServiceCostShipment
import com.otto.mart.support.util.notNull
import id.ottopay.oasis.R
import id.ottopay.oasis.adapters.GrosirCourierAdapter
import kotlinx.android.synthetic.main.layout_grosir_courier_dialog.*


class GrosirCourierDialog : BottomSheetDialogFragment() {

    var mCourierList = mutableListOf<OasisListCourier>()
    var mCourierSelected : OasisListCourier? = null
    var mCourierSelectedMethod : OnCourierMethodSelected? = null
    var mSelectedPosition : Int? = null
    var initClick = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_grosir_courier_dialog, container, false)

        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.otto.mart.R.style. AppBottomSheetDialogTheme);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        close.setOnClickListener{
            dismiss()
        }
        displayCourier()
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

    private fun displayCourier(){
        mCourierList.notNull {
            val adapter = GrosirCourierAdapter(this.context!!, mCourierList)
            recycler_view.adapter = adapter
            close.setOnClickListener {
                dismiss()
            }

            adapter.setmOnViewClickListener(object : GrosirCourierAdapter.OnViewClickListener{
                override fun onViewClickListener(item: OasisListCourier, position: Int) {
                    mCourierSelected = item
                    mSelectedPosition = position
                    mSelectedPosition?.let { it1->mCourierSelected?.let { it2 -> mCourierSelectedMethod?.onCourierMethodSelected(it2,it1) } }
                    dismiss()
                }

            })
        }
    }

    private fun initRecyclerView() {
        recycler_view.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recycler_view.setLayoutManager(linearLayoutManager)
    }

    fun setItemSelectedClickListener(mOnViewClickListener: OnCourierMethodSelected) {
        this.mCourierSelectedMethod = mOnViewClickListener
    }


    interface OnCourierMethodSelected {
        fun onCourierMethodSelected(item: OasisListCourier, position: Int)
    }
}
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
import com.otto.mart.model.APIModel.Response.grosir.OasisItemCategoryResponse
import com.otto.mart.support.util.notNull
import id.ottopay.oasis.R
import id.ottopay.oasis.adapters.OasisCategoryDialogAdapter
import kotlinx.android.synthetic.main.layout_grosir_courier_dialog.*

class OasisCategoryDialog : BottomSheetDialogFragment() {

    var mCategoryList = mutableListOf<OasisItemCategoryResponse>()
    var mCategorySelected: OasisItemCategoryResponse? = null
    var mCategorySelectedMethod: OnCategoryMethodSelected? = null
    var mSelectedPosition: Int? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.oasis_category_dialog, container, false)
        return view

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.otto.mart.R.style. AppBottomSheetDialogTheme);
    }

    private fun displayCourier() {
        mCategoryList.notNull {
            val adapter = OasisCategoryDialogAdapter(this.context!!, mCategoryList)
            recycler_view.adapter = adapter

            adapter.setmOnViewClickListener(object : OasisCategoryDialogAdapter.OnViewClickListener {
                override fun onViewClickListener(item: OasisItemCategoryResponse, position: Int) {
                    mCategorySelected= item
                    mSelectedPosition = position
                    mSelectedPosition?.let { it1 -> mCategorySelected?.let { it2 -> mCategorySelectedMethod?.onCategoryMethodSelected(it2, it1) } }
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

    fun setItemSelectedClickListener(mOnViewClickListener: OnCategoryMethodSelected) {
        this.mCategorySelectedMethod = mOnViewClickListener
    }


    interface OnCategoryMethodSelected {
        fun onCategoryMethodSelected(item: OasisItemCategoryResponse, position: Int)
    }
}
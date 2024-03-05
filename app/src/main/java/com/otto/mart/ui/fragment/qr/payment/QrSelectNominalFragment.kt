package com.otto.mart.ui.fragment.qr.payment


import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.otto.mart.R
import com.otto.mart.model.localmodel.ppob.PpobPaymentMethod
import com.otto.mart.support.util.DataUtil
import kotlinx.android.synthetic.main.fragment_qr_select_nominal.*

/**
 * A simple [Fragment] subclass.
 */
class QrSelectNominalFragment : BottomSheetDialogFragment() {

    var mSelectedPaymentMethod: PpobPaymentMethod? = null

    var mSelectedPosition: Int? = null

    var mTextWatcher: TextWatcher? = null

    lateinit var funPointSelected: (point: Int) -> Unit

    var mTotalPoint = 0
    var mTotalBalance = 0
    var mTotalPayment = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_qr_select_nominal, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        displayPaymentInfo()
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

        mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var point = DataUtil.getInt(etPoint.text.toString().replace(getString(R.string.text_currency), ""))

                etPoint.removeTextChangedListener(mTextWatcher)

                if (point > mTotalPayment) {
                    point = mTotalPayment
                }

                if (point > mTotalPoint) {
                    point = mTotalPoint
                }

                tvBalance.setText(DataUtil.convertCurrency(mTotalPayment - point))

                etPoint.setText(DataUtil.convertCurrency(point).replace(getString(R.string.text_currency), ""))
                etPoint.setSelection(etPoint.text.length)

                etPoint.addTextChangedListener(mTextWatcher)
            }

            override fun afterTextChanged(editable: Editable) {

            }
        }

        etPoint.addTextChangedListener(mTextWatcher)

        btnSelect.setOnClickListener {
            if (funPointSelected != null) {
                funPointSelected(DataUtil.getInt(etPoint.text.toString()))
            }
            dismiss()
        }
    }

    private fun displayPaymentInfo() {
        tvTotalPayment.text = DataUtil.convertCurrency(mTotalPayment)
        tvTotalBalance.text = DataUtil.convertCurrency(mTotalBalance)
        tvPoint.text = DataUtil.convertCurrency(mTotalPoint).replace(getString(R.string.text_currency), "")
        tvBalance.text = DataUtil.convertCurrency(0)
    }
}

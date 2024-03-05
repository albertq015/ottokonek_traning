package com.otto.mart.ui.fragment.transaction.balance


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.R
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.formValidation.FormValidation
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.ppob.PpobKomisiAdapter
import kotlinx.android.synthetic.main.fragment_payment_method.recyclerview
import kotlinx.android.synthetic.main.fragment_set_komisi.btnSubmit
import kotlinx.android.synthetic.main.fragment_transfer_omzet.*


/**
 * A simple [Fragment] subclass.
 */
class TransferOmzetFragment : BottomSheetDialogFragment() {

    var mOmzet = 0
    var mMinimumOmzet = 500

    var mTextWatcher: TextWatcher? = null

    private var mIsValidationEnable = false

    var funTransferOmzet: ((amount: Int) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transfer_omzet, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRecyclerView()
        displayOmzet()
        displayAmountOption()
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

    override fun onResume() {
        super.onResume()
        displayOmzet()
    }

    private fun initView() {

        mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) { }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var amount = DataUtil.getInt(etAmount.text.toString().replace(getString(R.string.text_currency), ""))

                etAmount.removeTextChangedListener(mTextWatcher)
                setAmount(amount)
                etAmount.setSelection(etAmount.text.length)

                if (amount == 0) {
                    etAmount.setText("")
                }

                isFormValid
                etAmount.addTextChangedListener(mTextWatcher)
            }

            override fun afterTextChanged(editable: Editable) { }
        }

        etAmount.addTextChangedListener(mTextWatcher)

        scAllIn.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                setAmount(mOmzet - mMinimumOmzet)
            }
        })


        btnSubmit.setOnClickListener {
            funTransferOmzet?.let { it1 -> it1(DataUtil.getInt(etAmount.text.toString())) }
            dismiss()
        }
    }

    private fun initRecyclerView() {
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    private fun displayOmzet() {
        tvOmzet.text = DataUtil.convertCurrency(mOmzet)
        etAmount.setText("")
    }

    private fun displayAmountOption() {
        var amountList = mutableListOf<Int>()
        amountList.add(100000)
        amountList.add(300000)
        amountList.add(500000)
        amountList.add(1000000)

        val adapter = this.context?.let { PpobKomisiAdapter(it, amountList) }
        recyclerview.adapter = adapter

        adapter?.setmOnViewClickListener(object : PpobKomisiAdapter.OnViewClickListener {
            override fun onViewClickListener(item: Int, position: Int) {
                if(item > (mOmzet - mMinimumOmzet)){
                    setAmount(mOmzet - mMinimumOmzet)
                } else {
                    setAmount(item)
                }

                scAllIn.isChecked = false
            }
        })
    }

    /**
     * method to validate Leziz NU form
     * */
    private val isFormValid: Boolean
        get() {
            if (!mIsValidationEnable) {
                return false
            }

            var status = true

            var amount = etAmount.text.toString()

            tvAmountError.gone()

            if (!FormValidation(activity as Context).isRequired(amount)) {
                tvAmountError.text = getString(R.string.donasi_msg_amount_required)
                tvAmountError.visible()
                status = false
            }

            if (status) {
                btnSubmit.background = ContextCompat.getDrawable(activity as Context, R.drawable.button_primary_selector)
            } else {
                btnSubmit.background = ContextCompat.getDrawable(activity as Context, R.drawable.button_white_grey_selected_bg)
            }

            return status
        }

    private fun setAmount(amount: Int) {
        var validAmount = 0

        if(amount > (mOmzet - mMinimumOmzet)){
            validAmount = mOmzet - mMinimumOmzet
        } else {
            validAmount = amount
        }

        etAmount.setText(DataUtil.convertCurrency(validAmount))
        etAmount.setSelection(etAmount.length())
    }

    fun setOmzet(omzet: Int) {
        mOmzet = omzet
    }

    fun setTransferOmzet(transferOmzet: (amount: Int) -> Unit) {
        funTransferOmzet = transferOmzet
    }
}

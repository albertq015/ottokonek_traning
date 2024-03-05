package com.otto.mart.ui.fragment.transaction.bogasari

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.bogasari.BogasariProduct
import com.otto.mart.support.util.DataUtil
import kotlinx.android.synthetic.main.dialog_bottom_bogasari.*
import kotlinx.android.synthetic.main.dialog_bottom_bogasari.view.*

class BottomDIalogBogasari : BottomSheetDialogFragment(){
    var funValue: ((amount: Long) -> Unit)? = null
    var product :BogasariProduct ?= null
    var label : String ?= null
    var  amount :Long ?= null
    companion object {
        fun newInstance(): BottomDIalogBogasari =
                BottomDIalogBogasari().apply {
                    arguments = Bundle().apply {
                    }
                }

        fun newInstance(amount : Long): BottomDIalogBogasari =
                BottomDIalogBogasari().apply {
                    arguments = Bundle().apply {
                    }
                    this.amount = amount
                }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.dialog_bottom_bogasari,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.linear_bottom.setOnClickListener {
            funValue?.let { it1 -> it1(DataUtil.getLong(amount_boga.text.toString())) }
            dismiss()
        }
        product?.let {
            textView19.text = it.name
        }

        label?.let {
            textView19.text = label
        }

        amount_boga.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.isNotEmpty()) {
                        val price = DataUtil.getDigit(it.toString())
                        val newValue = DataUtil.convertCurrency(price)
                        amount_boga.removeTextChangedListener(this)
                        amount_boga.setText(newValue.replace("Rp ", ""))
                        amount_boga.setSelection(amount_boga.text.length)
                        amount_boga.addTextChangedListener(this)

                    }
                }
            }
        })

        amount?.let {
            amount_boga.setText(amount.toString())
        }


    }

    fun setAmountBoga(transferOmzet: (amount: Long) -> Unit) {
        funValue = transferOmzet
    }
    fun setAmountBoga2(transferOmzet2: (amount: Long) -> Unit) {
        funValue = transferOmzet2
    }

    fun initViewBoga(product : BogasariProduct) {
        this.product = product
    }

    fun initViewBoga2(label :String) {
        this.label = label
    }
    }
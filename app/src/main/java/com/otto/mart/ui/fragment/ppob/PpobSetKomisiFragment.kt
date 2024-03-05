package com.otto.mart.ui.fragment.ppob


import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.otto.mart.R
import com.otto.mart.support.util.DataUtil
import com.otto.mart.ui.Partials.adapter.ppob.PpobKomisiAdapter
import kotlinx.android.synthetic.main.fragment_payment_method.recyclerview
import kotlinx.android.synthetic.main.fragment_set_komisi.*


/**
 * A simple [Fragment] subclass.
 */
class PpobSetKomisiFragment : BottomSheetDialogFragment() {

    var onKomisiSelectedListener: OnKomisiSelectedListener? = null

    var mPrice = 0L
    var mKomisi = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_set_komisi, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRecyclerView()
        displayKomisiOption()
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
        displayModal()
        setKomisi(0)
    }

    private fun initView() {
        imgMin.setOnClickListener {
            setKomisi((DataUtil.getInt(etKomisi.text.toString()) - mPrice) - 100)
        }

        imgPlus.setOnClickListener {
            setKomisi((DataUtil.getInt(etKomisi.text.toString()) - mPrice) + 100)
        }

        btnSubmit.setOnClickListener {
            onKomisiSelectedListener?.onKomisiSelected(mKomisi)
            dismiss()
        }
    }

    private fun initRecyclerView() {
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    private fun displayKomisiOption() {
        var komisiList = mutableListOf<Int>()
        komisiList.add(500)
        komisiList.add(1000)
        komisiList.add(1500)
        komisiList.add(2000)
        komisiList.add(2500)
        komisiList.add(3000)
        komisiList.add(3500)
        komisiList.add(4000)
        komisiList.add(4500)
        komisiList.add(5000)

        val adapter = this.context?.let { PpobKomisiAdapter(it, komisiList) }
        recyclerview.adapter = adapter

        adapter?.setmOnViewClickListener(object : PpobKomisiAdapter.OnViewClickListener {
            override fun onViewClickListener(item: Int, position: Int) {
                setKomisi(item.toLong())
            }
        })
    }

    private fun displayModal() {
        etKomisi.setText(DataUtil.convertCurrency(mPrice))
        etKomisi.setSelection(etKomisi.text.length)
        tvModal.text = DataUtil.convertCurrency(mPrice)
    }

    private fun setKomisi(komisi: Long) {
        mKomisi = komisi
        var newPrice = mPrice + komisi

        etKomisi.setText(DataUtil.convertCurrency(newPrice))
        etKomisi.setSelection(etKomisi.length())

        if (komisi < 0) {
            tvKomisi.text = "-" + DataUtil.convertCurrency(komisi)
            tvKomisi.setTextColor(ContextCompat.getColor(this.context!!, R.color.faded_red_old))
        } else {
            tvKomisi.text = DataUtil.convertCurrency(komisi)
            tvKomisi.setTextColor(ContextCompat.getColor(this.context!!, R.color.dark_sky_blue_two))
        }
    }

    fun setPrice(price: Long) {
        mPrice = price
    }

    fun setKomisiSelectedClickListener(selectedListener: OnKomisiSelectedListener) {
        this.onKomisiSelectedListener = selectedListener
    }

    interface OnKomisiSelectedListener {
        fun onKomisiSelected(komisi: Long)
    }
}

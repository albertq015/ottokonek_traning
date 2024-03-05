package com.otto.mart.ui.fragment.transaction.balance


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.R
import com.otto.mart.model.localmodel.omzet.OmzetFilter
import com.otto.mart.ui.Partials.adapter.omzet.OmzetFilterAdapter
import kotlinx.android.synthetic.main.fragment_deposit_filter.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class DepositFilterFragment : BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener {

    lateinit var applyFilterDeposit: (startDate: String, endDate: String) -> Unit

    private var startDateDialog: DatePickerDialog? = null
    private var endDateDialog: DatePickerDialog? = null

    private var isStartDate = false

    var transactionTypeList = mutableListOf<OmzetFilter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_deposit_filter, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initDatePicker()
        initRecyclerView()
        displayTransactionType()
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
    }

    private fun initView() {
        btnClose.setOnClickListener {
           dismiss()
        }

        btnStartDate.setOnClickListener {
            isStartDate = true
            startDateDialog?.show()
        }

        btnEndDate.setOnClickListener {
            isStartDate = false
            endDateDialog?.show()
        }

        btnSubmit.setOnClickListener {
            if (::applyFilterDeposit.isInitialized) {
                applyFilterDeposit(tvStartDate.text.toString(), tvEndDate.text.toString())
                dismiss()
            }
        }
    }

    private fun initRecyclerView() {
        rvTransactionType.setHasFixedSize(true)
        val llmTransactionType = androidx.recyclerview.widget.LinearLayoutManager(this.context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        rvTransactionType.setLayoutManager(llmTransactionType)
    }

    private fun initDatePicker() {
        Locale.setDefault(Locale.ENGLISH)

        val calender = Calendar.getInstance()
        var date = calender.get(Calendar.DATE)
        var month = calender.get(Calendar.MONTH)
        var year = calender.get(Calendar.YEAR)

        startDateDialog = activity?.let { DatePickerDialog(it, this, year, month, date) }
        endDateDialog = activity?.let { DatePickerDialog(it, this, year, month, date) }

        var finalDate = date
        var finalDateString = "$finalDate"

        if (date < 10) {
            finalDateString = "0$finalDate"
        }

        var finalMonth = month + 1
        var finalMonthString = "$finalMonth"

        if (finalMonth < 10) {
            finalMonthString = "0$finalMonth"
        }

        //tvStartDate.text = "$finalDateString-$finalMonthString-$year"
        //tvEndDate.text = "$finalDateString-$finalMonthString-$year"

        startDateDialog?.datePicker?.maxDate = System.currentTimeMillis()
        endDateDialog?.datePicker?.maxDate = System.currentTimeMillis()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var finalDate = dayOfMonth
        var finalDateString = "$finalDate"

        if (finalDate < 10) {
            finalDateString = "0$finalDate"
        }

        var finalMonth = month + 1
        var finalMonthString = "$finalMonth"

        if (finalMonth < 10) {
            finalMonthString = "0$finalMonth"
        }

        if (isStartDate) {
            tvStartDate.text = "$finalDateString-$finalMonthString-$year"
        } else {
            tvEndDate.text = "$finalDateString-$finalMonthString-$year"
        }
    }

    private fun displayTransactionType() {
        if (transactionTypeList.size == 0) {
            val all = OmzetFilter("All", "",true)
            transactionTypeList.add(all)

            val masuk = OmzetFilter("Cash In", "in",false)
            transactionTypeList.add(masuk)

            val keluar = OmzetFilter("Cash Out", "out",false)
            transactionTypeList.add(keluar)
        }

        activity?.let {
            val adapter = OmzetFilterAdapter(it, transactionTypeList)
            adapter.itemSelected = ::transactionTypeSelected
            rvTransactionType.adapter = adapter
        }
    }

    fun transactionTypeSelected(omzetFilter: OmzetFilter, position: Int) {
        var currentSelectedStatus = omzetFilter.isSelected
        omzetFilter.isSelected = !currentSelectedStatus
        transactionTypeList[position] = omzetFilter
        rvTransactionType.adapter?.notifyItemChanged(position)
    }
}

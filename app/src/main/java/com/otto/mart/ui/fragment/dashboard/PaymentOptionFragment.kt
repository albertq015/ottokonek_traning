package com.otto.mart.ui.fragment.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.R
import com.otto.mart.model.localmodel.ppob.PpobMenu
import com.otto.mart.ui.Partials.adapter.menu.PaymentOptionAdapter
import com.otto.mart.ui.activity.cashOut.CashOutMenuActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import com.otto.mart.ui.activity.qr.scan.ScanQrActivity
import com.otto.mart.ui.activity.qr.show.ShowQrActivity
import kotlinx.android.synthetic.main.fragment_payment_option.*

/**O
 * A simple [Fragment] subclass.
 */
class PaymentOptionFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_payment_option, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerview()
        initView()
        displayMoreMenu()
    }

    private fun initView() {
        ivClose.setOnClickListener {
            hidePaymentOptionMenu()
        }
    }

    private fun initRecyclerview() {
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    private fun displayMoreMenu() {

        activity?.let {
            val paymentOptionAdapter = PaymentOptionAdapter(it, getPaymentMenu())
            recyclerview.adapter = paymentOptionAdapter

            paymentOptionAdapter.setmOnViewClickListener(object : PaymentOptionAdapter.OnViewClickListener {
                override fun onViewClickListener(item: PpobMenu, position: Int) {
                    if (item.intent != null) {
                        startActivity(item.intent)
                        hidePaymentOptionMenu()
                    }
                }
            })
        }
    }

    private fun getPaymentMenu(): MutableList<PpobMenu> {
        val paymentMenuList = mutableListOf<PpobMenu>()

        val scanQr = PpobMenu(
                getString(R.string.label_payment_menu_scan_qr),
                R.drawable.ic_scan_qr,
                null,
                Intent(activity, ScanQrActivity::class.java)
        )
        paymentMenuList.add(scanQr)

        val showQr = PpobMenu(
                getString(R.string.label_payment_menu_show_qr),
                R.drawable.ic_show_qr,
                null,
                Intent(activity, ShowQrActivity::class.java)
        )
        paymentMenuList.add(showQr)

        val cashOut = PpobMenu(
                getString(R.string.label_payment_menu_cash_out),
                R.drawable.ic_cash_out,
                null,
                Intent(activity, CashOutMenuActivity::class.java)
        )
        paymentMenuList.add(cashOut)

        return paymentMenuList
    }

    private fun hidePaymentOptionMenu() {
        try {
            (activity as DashboardActivity).hidePaymentOptionFragment()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
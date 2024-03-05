package com.otto.mart.ui.fragment.ppob


import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.otto.mart.R
import com.otto.mart.support.util.BitmapUtil
import com.otto.mart.support.util.DeviceUtil
import kotlinx.android.synthetic.main.fragment_ppob_show_qr_payment.*


/**
 * A simple [Fragment] subclass.
 */
class PpobShowQrPaymentFragment : BottomSheetDialogFragment() {

    var mProductName = ""
    var mAmount = ""
    var mQrString = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ppob_show_qr_payment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displyaPaymentInfo()
        displayQr()
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

    private fun displyaPaymentInfo() {
       tvProductName.text = mProductName
       tvAmount.text = mAmount
    }

    private fun displayQr() {
        val bitmap = BitmapUtil.generateBitmap(mQrString, 9, DeviceUtil.dpToPx(204), DeviceUtil.dpToPx(204))
        imgQR.setImageBitmap(bitmap)
    }
}

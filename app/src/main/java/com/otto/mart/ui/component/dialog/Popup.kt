package com.otto.mart.ui.component.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.R
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import kotlinx.android.synthetic.main.dialog_dynamic_confirm.*
import kotlinx.android.synthetic.main.dialog_dynamic_info.*

class Popup : BottomSheetDialogFragment() {

    var positiveAction: (() -> Unit)? = null
    var negativeAction: (() -> Unit)? = null
    var closeAction: (() -> Unit)? = null

    var btnPositiveAction: Button? = null
    var btnNegativeAction: Button? = null
    var tvTitle: TextView? = null
    var tvMessage: TextView? = null

    var defaultLayout = R.layout.dialog_dynamic_info
    lateinit var title: String
    var message: String? = null
    var positiveButton: String? = null
    var negativeButton: String? = null
    var imagePopupLogo: Int = R.drawable.icon_check_green
    var isHideBtnNegative = false
    var isHideBtnPositive = false
    var isHideBtnClose = false
    var isHideMessage = false

    companion object {
        @JvmStatic
        fun showInfo(fragmentManager: FragmentManager, title: String, message: String, action: (() -> Unit)? = null) {
            val tag = "popupInfo"
            if ((fragmentManager.findFragmentByTag(tag) == null)) {
                Popup().apply {
                    positiveAction = action
                    this.title = title
                    this.message = message
                    isHideBtnNegative = true
                }.show(fragmentManager, tag)
            }
        }

//        fun showConfirm(fragmentManager: FragmentManager, title: String, message: String, positiveAction: () -> Unit, negativeAction: (() -> Unit)? = null, closeAction: (() -> Unit)? = null, isHideBtnClose: Boolean? = null) {
//            val tag = "popupConfirm"
//            if (fragmentManager.findFragmentByTag(tag) == null) {
//                Popup().apply {
//                    this.positiveAction = positiveAction
//                    this.negativeAction = negativeAction
//                    this.title = title
//                    this.message = message
//                    this.closeAction = closeAction
//                    defaultLayout = R.layout.dialog_dynamic_confirm
//                    this.isHideBtnClose = isHideBtnClose ?: false
//                }.show(fragmentManager, tag)
//            }
//        }

        @JvmStatic
        fun getConfirmDialog(title: String, message: String) =
                Popup().apply {
                    this.title = title
                    this.message = message
                    defaultLayout = R.layout.dialog_dynamic_confirm
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(defaultLayout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        positiveButton = positiveButton ?: getString(R.string.button_ok)
        negativeButton = negativeButton ?: getString(R.string.text_no)
        bindView(view)
    }

    private fun bindView(view: View) {
        btnPositiveAction = view.findViewById(R.id.btnPositive)
        btnNegativeAction = view.findViewById(R.id.btnNegative)

        tvTitle = view.findViewById(R.id.tvPopupTitle)
        tvMessage = view.findViewById(R.id.tvPopupMessage)

        btnPositiveAction?.setOnClickListener {
            dismiss()
            positiveAction?.invoke()
        }
        btnNegativeAction?.setOnClickListener {
            dismiss()
            negativeAction?.invoke()
        }
        btnCloseAction?.setOnClickListener {
            dismiss()
            closeAction?.invoke()
        }

        if (isHideBtnNegative) btnNegativeAction?.gone() else btnNegativeAction?.visible()
        if (isHideBtnPositive) btnPositiveAction?.gone() else btnPositiveAction?.visible()
        if (isHideBtnClose) btnCloseAction?.gone() else btnCloseAction?.visible()
        if (isHideMessage) tvMessage?.gone() else tvMessage?.visible()

        tvTitle?.text = title
        tvMessage?.text = message
        btnPositiveAction?.text = positiveButton
        btnNegativeAction?.text = negativeButton
        ivPopupLogo?.setImageResource(imagePopupLogo)
    }

    fun singleShow(fragmentManager: FragmentManager, tag: String) {
        if (fragmentManager.findFragmentByTag(tag) == null) {
            show(fragmentManager, tag)

        }
    }
}
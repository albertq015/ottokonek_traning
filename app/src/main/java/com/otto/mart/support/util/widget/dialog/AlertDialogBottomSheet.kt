package com.otto.mart.support.util.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.R
import kotlinx.android.synthetic.main.dialog_alert_bottom_sheet.*
import kotlinx.android.synthetic.main.dialog_alert_bottom_sheet.btnOK
import kotlinx.android.synthetic.main.dialog_alert_bottom_sheet.ivIcon
import kotlinx.android.synthetic.main.dialog_alert_bottom_sheet.tvMessage
import kotlinx.android.synthetic.main.dialog_alert_bottom_sheet.tvTitle
import kotlinx.android.synthetic.main.dialog_confirmation_bottom_sheet.*


class AlertDialogBottomSheet (context: Context, fragmentManager: FragmentManager) :
    BottomSheetDialogFragment() {

    private var actionButtonOK: (() -> Unit?)? = null
    private var actionOnDismiss: (() -> Unit?)? = null
    private var mContext = context
    private var mFragmentManager = fragmentManager
    private var mTitle: String? = null
    private var mMessage: String? = null
    private var mButton: String? = null
    private var mIcon: Int? = R.drawable.ic_succes_ottokonnek
    private var mIsShowIcon: Boolean = true
    private var mIsCancelable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.dialog_alert_bottom_sheet, container, false)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (null != bottomSheet) {
                val behavior: BottomSheetBehavior<*> =
                    BottomSheetBehavior.from(bottomSheet)
                behavior.isHideable = mIsCancelable
            }
        }
        return bottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(mIsCancelable)
        dialog?.setCanceledOnTouchOutside(mIsCancelable)

        initView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (actionOnDismiss != null) {
            actionOnDismiss?.let {
                    it1 -> it1()
            }
        }
    }

    private fun initView() {

        if (mIsShowIcon) {
            ivIcon.visibility = View.VISIBLE
        } else {
            ivIcon.visibility = View.GONE
        }

        mIcon?.let {
            ivIcon.setImageDrawable(ContextCompat.getDrawable(mContext, it))
        }

        mTitle?.let {
            if (it.isNotEmpty()) {
                tvTitle.text = mTitle
            } else {
                tvTitle.visibility = View.GONE
            }
        }

        mMessage?.let {
            if (it.isNotEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvMessage.text = Html.fromHtml(mMessage, HtmlCompat.FROM_HTML_MODE_LEGACY)
                }else
                    tvMessage.text = Html.fromHtml(mMessage)

            } else {
                tvMessage.visibility = View.GONE
            }
        }


        mButton?.let {
            if (it.isNotEmpty()) {
                btnOK.text = mButton
            } else {
                btnOK.text = "OK"
            }
        }

        btnOK.setOnClickListener {
            if (actionButtonOK != null) {
                actionButtonOK?.let { it1 -> it1() }
            }
            dismiss()
        }
    }

    fun setTitle(title: String) {
        mTitle = title
    }

    fun setMessage(message: String) {
        mMessage = message
    }

    fun setIcon(icon: Int) {
        mIcon = icon
    }

    fun setButton(button: String) {
        mButton = button
    }

    fun setActionButton(actionButton: () -> Unit) {
        actionButtonOK = actionButton
    }

    fun setActionOnDismiss(action: () -> Unit) {
        actionOnDismiss = action
    }

    fun setIsShowIcon(isShow: Boolean) {
        mIsShowIcon = isShow
    }

    fun setIsCancelable(isCancelable: Boolean) {
        mIsCancelable = isCancelable
    }

    fun show() {
        show(mFragmentManager, this.tag)
    }
}

package com.otto.mart.support.util.widget.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.R
import kotlinx.android.synthetic.main.activity_pay_qrdetail_failed.*
import kotlinx.android.synthetic.main.dialog_confirmation_bottom_sheet.*
import kotlinx.android.synthetic.main.dialog_confirmation_bottom_sheet.btnOK
import kotlinx.android.synthetic.main.dialog_confirmation_bottom_sheet.ivIcon
import kotlinx.android.synthetic.main.dialog_confirmation_bottom_sheet.tvMessage
import kotlinx.android.synthetic.main.dialog_confirmation_bottom_sheet.tvTitle
import java.util.*

class ConfirmationDialogBottomSheet(context: Context, fragmentManager: FragmentManager) :
    BottomSheetDialogFragment() {

    private var actionPositiveButton: (() -> Unit?)? = null
    private var actionNegativeButton: (() -> Unit?)? = null
    private var actionOnDismiss: (() -> Unit?)? = null
    private var mContext = context
    private var mFragmentManager = fragmentManager
    private var mTitle: String? = null
    private var mMessage: String? = null
    private var mMessageTC: String? = null
    private var mIcon: Int? = R.drawable.ic_succes_ottokonnek
    private var mPositiveButton: String? = null
    private var mNegativeButton: String? = null
    private var mIsShowIcon: Boolean = false
    private var mMessageGravity: Int = Gravity.CENTER
    private var flowdone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.dialog_confirmation_bottom_sheet, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!flowdone)
            if (actionOnDismiss != null) {
                actionOnDismiss?.let { it1 ->
                    it1()
                }
            }
    }

    private fun initView() {

        if (mIsShowIcon) {
            ivIcon.visibility = View.VISIBLE
        } else {
            ivIcon.visibility = View.GONE
        }

        tvTitle.gravity = mMessageGravity
        tvMessage.gravity = mMessageGravity

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

        mMessageTC?.let {

            val labelMessage =
                "In order to use OttoKonek application, we require your permission to access information from your device. To learn more about how we collect and use data, review our <html><font style='color:#006841'> <b><a href='https://otto-konek.s3-ap-southeast-1.amazonaws.com/tnc/otto_konek_privacy_policy.html'> Privacy Policy. </a></b></font></html>"

            if (it.isNotEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    tvMessage.text = Html.fromHtml(labelMessage, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    tvMessage.setMovementMethod(LinkMovementMethod.getInstance())

                } else


                    tvMessage.text = Html.fromHtml(labelMessage, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    tvMessage.setMovementMethod(LinkMovementMethod.getInstance())


            } else {
                tvMessage.visibility = View.GONE
            }
        }

        mMessage?.let {
            if (it.isNotEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvMessage.text = Html.fromHtml(mMessage, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    tvMessage.setMovementMethod(LinkMovementMethod.getInstance())
                } else
                    tvMessage.text = Html.fromHtml(mMessage, HtmlCompat.FROM_HTML_MODE_LEGACY)
                tvMessage.setMovementMethod(LinkMovementMethod.getInstance())

            } else {
                tvMessage.visibility = View.GONE
            }
        }

        mPositiveButton?.let {
            if (it.isNotEmpty()) {
                btnOK.text = mPositiveButton
            } else {
                btnOK.text = "OK"
            }
        }

        mNegativeButton?.let {
            if (it.isNotEmpty()) {
                btnCancel.text = mNegativeButton
            } else {
                btnCancel.text = "Cancel"
            }
        }

        btnOK.setOnClickListener {
            if (actionPositiveButton != null) {
                actionPositiveButton?.let { it1 -> it1() }
            }
            dismiss()
        }

        btnCancel.setOnClickListener {
            if (actionNegativeButton != null) {
                actionNegativeButton?.let { it1 -> it1() }
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

    fun setMessageTC(message: String) {
        mMessageTC = message
    }

    fun setIcon(icon: Int) {
        mIcon = icon
    }

    fun setIsShowIcon(isShow: Boolean) {
        mIsShowIcon = isShow
    }

    fun setPositiveButton(button: String) {
        mPositiveButton = button
    }

    fun setNegativeButton(button: String) {
        mNegativeButton = button
    }

    fun setPositiveAction(actionButton: () -> Unit) {
        flowdone = true
        actionPositiveButton = actionButton
    }

    fun setNegativeAction(actionButton: () -> Unit) {
        flowdone = true
        actionNegativeButton = actionButton
    }

    fun setActionOnDismiss(action: () -> Unit) {
        actionOnDismiss = action
    }

    fun setMessageGravity(messageGravity: Int) {
        mMessageGravity = messageGravity
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.add(this, tag).addToBackStack(null)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Log.e("IllegalStateException", "Exception", e)
        }
    }

    fun show(s: String) {
        show(mFragmentManager, s)
    }

    fun show() {
        show(mFragmentManager, UUID.randomUUID().toString())
    }
}
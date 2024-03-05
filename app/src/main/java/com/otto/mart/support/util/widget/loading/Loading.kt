package com.otto.mart.support.util.widget.loading

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import com.otto.mart.R
import com.otto.mart.support.util.visible
import kotlinx.android.synthetic.main.dialog_loading.*


class Loading {
    fun getDialog(context: Context, message: String?): Dialog {
        val dialog = Dialog(context, R.style.LoadingDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        if (message != null) {
            dialog.tvMessage.text = message
            dialog.tvMessage.visible()
        }
        return dialog
    }
}
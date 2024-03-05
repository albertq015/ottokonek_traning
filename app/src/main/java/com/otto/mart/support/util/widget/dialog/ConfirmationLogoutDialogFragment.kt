package com.otto.mart.support.util.widget.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.R
import kotlinx.android.synthetic.main.dialog_logout_fragment.*
import kotlinx.android.synthetic.main.fragment_confirmation_dialog.tvMessage
import kotlinx.android.synthetic.main.fragment_confirmation_dialog.tvTitle

class ConfirmationLogoutDialogFragment : BottomSheetDialogFragment() {

    var title = ""
    var message = ""
    var positiveButton = ""
    var negativeButton = ""

    lateinit var onPositiveButton: () -> Unit
    lateinit var onNegativeButton: () -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.dialog_logout_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        tvTitle.text = title
        tvMessage.text = message
        btnOK.text = positiveButton
        btnCancel.text = negativeButton

        btnOK.setOnClickListener {
            if (::onPositiveButton.isInitialized) {
                onPositiveButton()
            }
        }

        btnCancel.setOnClickListener {
            if (::onNegativeButton.isInitialized) {
                onNegativeButton()
            }
        }

    }
}
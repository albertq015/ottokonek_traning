package com.otto.mart.support.util.widget.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.R
import kotlinx.android.synthetic.main.fragment_confirmation_dialog.*

/**
 * A simple [Fragment] SUBCLASS.
 */
class ConfirmationDialogFragment : BottomSheetDialogFragment() {

    var title = ""
    var message = ""
    var positiveButton = "Batal"
    var negativeButton = "Ya"

    lateinit var onPositiveButton: () -> Unit
    lateinit var onNegativeButton: () -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_confirmation_dialog, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        tvTitle.text = title
        tvMessage.text = message
        btnPositive.text = positiveButton
        btnNegative.text = negativeButton

        btnPositive.setOnClickListener {
            if (::onPositiveButton.isInitialized) {
                onPositiveButton()
            }
        }

        btnNegative.setOnClickListener {
            if (::onNegativeButton.isInitialized) {
                onNegativeButton()
            }
        }

    }
}
package com.otto.mart.ui.activity.wallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import com.otto.mart.R
import com.otto.mart.support.util.FragmentDispatcher
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.fragment.wallet.ScanQRFragment
import com.otto.mart.ui.fragment.wallet.TokenFragment
import kotlinx.android.synthetic.main.activity_pay_wallet.*
import kotlinx.android.synthetic.main.toolbar.*

class PayWalletActivity : AppActivity() {

    private val RC_PIN_SHOW_QR = 214
    val fragmentDispatcher = FragmentDispatcher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_wallet)
        showScanQRFragment()

        initContent()
        registerObserver()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_PIN_SHOW_QR && resultCode == Activity.RESULT_OK)
            rbShow.isChecked = true
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initContent() {
        tvToolbarTitle.text = getString(R.string.text_scan_qr)
        btnToolbarBack.setOnClickListener { finish() }
        rgFragmentSwitch.setOnCheckedChangeListener { _, id ->
            if (id == R.id.rbScan) {
                showScanQRFragment()
            } else {
                showTokenFragment()
            }
        }

        rbShow.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                openPinConfirm()
            }
            return@setOnTouchListener true
        }
    }

    private fun registerObserver() {
        lifecycle.addObserver(fragmentDispatcher)
    }

    private fun showScanQRFragment() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        supportFragmentManager.beginTransaction().replace(R.id.flContainer, ScanQRFragment()).disallowAddToBackStack().commit()
    }

    private fun showTokenFragment() {
        fragmentDispatcher.dispatchFragment(TokenFragment())
    }

    private fun openPinConfirm() {
        val intent = Intent(this, RegisterPinResetActivity::class.java)
        intent.putExtra("confirm", true)
        intent.putExtra(RegisterPinResetActivity.LAYOUT_ID, R.layout.activity_pinpad_oc)
        startActivityForResult(intent, RC_PIN_SHOW_QR)
    }
}

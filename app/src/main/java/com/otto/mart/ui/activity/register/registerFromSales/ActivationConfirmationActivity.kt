package com.otto.mart.ui.activity.register.registerFromSales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.Html
import com.otto.mart.R
import com.otto.mart.support.util.*
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.syaratDanKetentuan.SnKActivity
import kotlinx.android.synthetic.main.content_activation_confirmation.*
import kotlinx.android.synthetic.main.toolbar.*

class ActivationConfirmationActivity : AppActivity() {

    val KEY_TAC_OTTOPAY = 100
    val KEY_TAC_OTTOCASH = 101

    var mPhone = ""
    var isOCOnly = false

    companion object {
        const val KEY_PHONE_DATA = "phone_data"
        const val KEY_OC_ONLY = "oconly"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation_confirmation)
        DeviceUtil.setStatusBar(this, ContextCompat.getColor(this, R.color.dark_blue_grey))

        if (intent.hasExtra(KEY_PHONE_DATA)) {
            mPhone = intent?.getStringExtra(KEY_PHONE_DATA)!!
        }

        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == KEY_TAC_OTTOPAY) {
                cbTacOttopay.isChecked = true
            } else if (requestCode == KEY_TAC_OTTOCASH) {
                cbTacOttocash.isChecked = true
            }
        }
    }

    private fun initView() {
        var confMessage = R.string.activation_confirmation_msg_conf_questtion
        isOCOnly = intent.getBooleanExtra(KEY_OC_ONLY, false)
        if (isOCOnly) {
            cbTacOttopay.gone()
            confMessage = R.string.activation_confirmation_msg_conf_oc
            ivLogo.setImageResource(R.drawable.logo_otto_cash)
        } else {
            tvMessage.text = getString(R.string.message_ask_connect_op_with_oc)
        }

        tvToolbarTitle.text = getString(R.string.activation_confirmation_tittle)
        tvConfQuestion.text = Html.fromHtml(getString(confMessage, DataUtil.getXXXPhone(mPhone)))
        cbTacOttopay.text = Html.fromHtml(getString(R.string.activation_confirmation_tac_ottopay))
        cbTacOttocash.text = Html.fromHtml(getString(R.string.activation_confirmation_tac_ottocash))

        /**
         * ottopay
         */
//        cbTacOttocash.isChecked = true
//        cbTacOttocash.gone()

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        cbTacOttopay.setOnClickListener {
            cbTacOttopay.isChecked = false
            val intent = Intent(this, SnKActivity::class.java)
            intent.putExtra(SnKActivity.KEY_IS_FROM_REGISTER, true)
            intent.putExtra(SnKActivity.KEY_URL_CONTENT, "https://opv3.s3-ap-southeast-1.amazonaws.com/tnc/TNC_PG_OP_310719_.html")
            startActivityForResult(intent, KEY_TAC_OTTOPAY)
        }

        cbTacOttocash.setOnClickListener {
            cbTacOttocash.isChecked = false
            val intent = Intent(this, SnKActivity::class.java)
            intent.putExtra(SnKActivity.KEY_IS_FROM_REGISTER, true)
            intent.putExtra(SnKActivity.KEY_URL_CONTENT, "https://opv3.s3-ap-southeast-1.amazonaws.com/oasis/Syarat+dan+Ketentuan+OttoCash.html")
            startActivityForResult(intent, KEY_TAC_OTTOCASH)
        }

        btnSubmit.setOnClickListener {
            if (isChecked()) {
//            if (cbTacOttopay.isChecked) {
//                val intent = Intent(this, RegisterFromsalesParentActivity::class.java)
//                intent.putExtra("merchantData", getIntent().getStringExtra("merchantData"))
//                startActivity(intent)
                setResult(Activity.RESULT_OK)
                finish()
            } else
                if (!cbTacOttopay.isChecked && !isOCOnly) {
                    getString(R.string.activation_confirmattion_msg_tac_ottopay_required).showToast(this)
                } else if (!cbTacOttocash.isChecked) {
                    getString(R.string.activation_confirmattion_msg_tac_ottocash_required).showToast(this)
                }
        }
    }

    private fun isChecked() =
            if (isOCOnly) cbTacOttocash.isChecked
            else cbTacOttopay.isChecked && cbTacOttocash.isChecked

}

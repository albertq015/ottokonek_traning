package com.otto.mart.ui.activity.register.registerFromSales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.otto.mart.R
import com.otto.mart.support.util.*
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_activation_input_pin.*
import kotlinx.android.synthetic.main.toolbar.*

class ActivationInputPinActivity : AppActivity() {

    companion object {
        const val KEY_TYPE_RESET_PIN = "type_reset_pin"
        const val KEY_TYPE_RESET_PIN_CONFIRMATION = "type_reset_pin_confirmation"

        const val KEY_DATA_TYPE = "data_type"
        const val KEY_DATA_PIN = "data_pindata_pin"

        const val KEY_HEADER_MESSAGE = "data_header_message"
    }

    var mType = ""
    var mPin = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation_input_pin)

        if (intent.hasExtra(KEY_DATA_TYPE)) {
            mType = intent?.getStringExtra(KEY_DATA_TYPE)!!
        }

        initView()
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_activation_input_pin)

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        resetForm()

        et1.onChange {
            setInput(it, 1)
        }

        et2.onChange {
            setInput(it, 2)
        }

        et3.onChange {
            setInput(it, 3)
        }

        et4.onChange {
            setInput(it, 4)
        }

        et5.onChange {
            setInput(it, 5)
        }

        et6.onChange {
            setInput(it, 6)
        }
    }

    private fun setInput(input: String, position: Int) {
        if (input.equals("")) {
            moveBack(position)
        } else {
            moveForward(position)
        }
    }

    private fun moveForward(position: Int) {
        when (position) {
            1 -> {
                setFocus(et2)
            }
            2 -> {
                setFocus(et3)
            }
            3 -> {
                setFocus(et4)
            }
            4 -> {
                setFocus(et5)
            }
            5 -> {
                setFocus(et6)
            }
            6 -> {
                validateForm()
            }
        }
    }

    private fun moveBack(position: Int) {
        when (position) {
            1 -> {

            }
            2 -> {
                setFocus(et1)
            }
            3 -> {
                setFocus(et2)
            }
            4 -> {
                setFocus(et3)
            }
            5 -> {
                setFocus(et4)
            }
            6 -> {
                setFocus(et5)
            }
        }
    }

    private fun setFocus(editText: EditText?) {
        val handler = Handler()
        handler.postDelayed({
            editText?.requestFocus()
            editText?.setSelection(editText.text.length)
        }, 10)
    }

    private fun validateForm() {
        if ((!et1.text.toString().equals("")) && (!et2.text.toString().equals("")) && (!et3.text.toString().equals(""))
                && (!et4.text.toString().equals("")) && (!et5.text.toString().equals("")) && (!et6.text.toString().equals(""))) {
            inputPinComplete(getPin())
        } else {
            if (et1.text.toString().equals("")) {
                setFocus(et1)
            } else if (et2.text.toString().equals("")) {
                setFocus(et2)
            } else if (et3.text.toString().equals("")) {
                setFocus(et3)
            } else if (et4.text.toString().equals("")) {
                setFocus(et4)
            } else if (et5.text.toString().equals("")) {
                setFocus(et5)
            } else if (et6.text.toString().equals("")) {
                setFocus(et6)
            }
        }
    }

    private fun getPin(): String {
        return et1.text.toString() + et2.text.toString() + et3.text.toString() +
                et4.text.toString() + et5.text.toString() + et6.text.toString()
    }

    private fun callApiRegister(pin: String) {
        var message = "Hit Registeerr " + pin
        message.showToast(this)
    }

    private fun inputPinComplete(pin: String) {
        if (mType.equals(KEY_TYPE_RESET_PIN)) {
            if (DataUtil.isPinValid(getPin())) {
                mPin = getPin()
                mType = KEY_TYPE_RESET_PIN_CONFIRMATION
            } else ErrorDialog(this, this, true, true, getString(R.string.message_pin_format_unaccaptable), "").show()
            resetForm()
        } else if (mType.equals(KEY_TYPE_RESET_PIN_CONFIRMATION)) {
            if (mPin.equals(getPin())) {
                val intent = Intent()
                intent.putExtra(KEY_DATA_PIN, getPin())
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                getString(R.string.message_pin_confirmation_mismatch).showToast(this)
                mType = KEY_TYPE_RESET_PIN
                resetForm()
            }
        }
    }

    private fun resetForm() {
        if (mType.equals(KEY_TYPE_RESET_PIN)) {
            tvConfQuestion.text = getString(R.string.activation_input_pin_desc)
            tvDesc.text = getString(R.string.text_message_reset_pin)
            tvDesc.gone()
        } else if (mType.equals(KEY_TYPE_RESET_PIN_CONFIRMATION)) {
            tvConfQuestion.text = getString(R.string.text_pin_op_confirmation)
            tvDesc.text = getString(R.string.text_input_6_digit_confirm_op)

        }

        /**
         * from register "OttoCash"
         */
        if (intent.hasExtra(KEY_HEADER_MESSAGE)) {
            tvDesc.text = intent.getStringExtra(KEY_HEADER_MESSAGE)
            DeviceUtil.setStatusBar(this, ContextCompat.getColor(this, R.color.dark_blue_grey))
        }

        et1.setText("")
        et2.setText("")
        et3.setText("")
        et4.setText("")
        et5.setText("")
        et6.setText("")
        setFocus(et1)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL) {
            if (et6.isFocused) {
                setFocus(et5)
            } else if (et5.isFocused) {
                setFocus(et4)
            } else if (et4.isFocused) {
                setFocus(et3)
            } else if (et3.isFocused) {
                setFocus(et2)
            } else if (et2.isFocused) {
                setFocus(et1)
            }
        }
        return super.dispatchKeyEvent(event)
    }
}
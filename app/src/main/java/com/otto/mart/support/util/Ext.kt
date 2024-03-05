package com.otto.mart.support.util

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun Int.px(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
fun <T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)
fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            cb(s.toString())
        }
    })
}

fun TextView.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cb(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isVisible(isVisible: Boolean) {
    if (isVisible) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

fun TextView.setColor(resColor: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextColor(context.getColor(resColor))
    } else {
        this.setTextColor(context.resources.getColor(resColor))
    }
}

fun String.showToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}

fun Any?.notNull(f: () -> Unit) {
    if (this != null) {
        f()
    }
}

inline fun String.plus(prefix: String? = null, value: String, postfix: String? = null): String {
    return prefix ?: "".plus(value).plus(postfix ?: "")
}

inline fun String.plusPostSpace(value: String) = value.plus(" ")
inline fun String.plusPreSpace(value: String) = " ".plus(value)
inline fun String.plusPostAsteriks(value: String) = value.plus("*")
inline fun String.plusPreAsteriks(value: String) = "*".plus(value)

fun String.convertCurrencyToDouble(): Double {

    val numfor = (NumberFormat.getInstance(Locale.GERMANY) as DecimalFormat).apply {
        roundingMode = RoundingMode.HALF_UP
        applyPattern("#,##")
        applyLocalizedPattern("#,##")
    }

    val value = this.replace("[^\\d.,]".toRegex(), "")
    if (value.isEmpty()) return 0.0
//    numfor.parse(value)?.let { return BigDecimal.valueOf(it.toDouble()).setScale(2, RoundingMode.HALF_UP).toDouble() }
    numfor.parse(value)?.let { return it.toDouble() }
    return 0.0
}

fun Number.convertCurrency() {

}
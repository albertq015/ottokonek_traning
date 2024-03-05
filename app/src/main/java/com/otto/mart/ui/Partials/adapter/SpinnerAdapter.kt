package com.otto.mart.ui.Partials.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes

class SpinnerAdapter(context: Context, dataset: MutableList<SpinnerData>, @LayoutRes val resLayout: Int, val placeHolder: SpinnerData) : ArrayAdapter<SpinnerData>(context, 0, dataset) {

    var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: layoutInflater.inflate(resLayout, parent, false)
        getItem(position)?.let { setItem(view, it) }
                ?: setItem(view, placeHolder)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: layoutInflater.inflate(resLayout, parent, false)
        getItem(position)?.let { setItem(view, it) }
                ?: setItem(view, placeHolder)
        return view
    }

    override fun getItem(position: Int): SpinnerData? = if (position == 0) null else super.getItem(position.minus(1))

    override fun getCount(): Int = super.getCount().plus(1)

    override fun isEnabled(position: Int): Boolean = position != 0

    private fun setItem(view: View, data: SpinnerData) {
        view.findViewById<TextView>(android.R.id.text1)?.text = data.name
    }
}

data class SpinnerData(val name: String, val code: String)
package com.otto.mart.support.utils.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView

class CustomRadioGroup @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var state: RadioButton? = null
        set(value) {
            field?.isChecked = false
            field = value
        }
    private var checkedPos: Int? = null

    private var selected: String? = null

    private val child = mutableListOf<RadioButton>()

    var listener: RadioListener? = null

    override fun addView(child: View?) {
        super.addView(child)
        check(child)
    }

    override fun addView(child: View?, index: Int) {
        super.addView(child, index)
        check(child)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        super.addView(child, width, height)
        check(child)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        check(child)
    }

    private fun check(view: View?) {
        view?.run {
//            if (view is ViewGroup) {
//                val viewGroup = this as ViewGroup
//                for (i in 0..(viewGroup).childCount) {
//                    viewGroup.getChildAt(i).run {
//                        if (this is RadioButton) {
//                            viewGroup.setOnClickListener { this.performClick() }
//                            child.add(this)
//                        }
//                    }
//                }
//                if (child.isNotEmpty()) {
//                    for ((index, view) in child.withIndex()) {
//                        view.setOnCheckedChangeListener { _, isChecked ->
//                            if (isChecked) {
//                                checkedPos = index
//                                state = view
//                                selected = saveViewState(viewGroup)
//                            }
//                        }
//                    }
//                }
//            }
//            for (i in 0..((view as ViewGroup).childCount)) {
//                if (view.getChildAt(i) is RadioButton) {
                    val rb = view as RadioButton
                    rb.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            state = rb
                            listener?.onItemSelected(rb.id)
//                            selected = saveViewState(viewGroup)
                        }
//                    }
//                }
            }
        }
    }

    private fun saveViewState(it: ViewGroup?): String {
        val radioButton = it?.getChildAt(0) as RadioButton
        var textView: TextView? = null
        if (it.childCount > 1) {
            textView = it.getChildAt(1) as TextView
        }

        return "${radioButton.text} ${textView?.text}"
    }

    fun getValue() = state?.text.toString()
    fun getSelected() = selected
    fun getChecedPos() = checkedPos
    fun setCheckedItem(position: Int) {
        if (child.size <= position && position >= 0) {
            child[position].isChecked = true
        }
    }

    interface RadioListener {
        fun onItemSelected(viewId: Int)
    }
}
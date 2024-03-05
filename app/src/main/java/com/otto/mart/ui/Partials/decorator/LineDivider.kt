package com.otto.mart.ui.Partials.decorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.otto.mart.R

class LineDivider(val context: Context, val orientation: Int, val margin: Int = 0, val drawable: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    companion object {
        const val VERTICAL = 1
        const val HORIZONTAL = 2
    }

    var isExcludeLastItem = false
    private var lineDrawable: Drawable? = null
    private var transparentLineDrawable: Drawable? = null

    init {
        transparentLineDrawable = ContextCompat.getDrawable(context, R.drawable.line_divider_vertical_transparent)
        lineDrawable = if (orientation == VERTICAL) {
            ContextCompat.getDrawable(context, drawable)
        } else
            ContextCompat.getDrawable(context, R.drawable.line_divider_vertical)
    }

    override fun onDrawOver(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        val left = parent.paddingLeft + margin
        val right = parent.width - parent.paddingRight - margin

        val childCount = parent.childCount

        if (childCount > 1) {
            for (x in 0..childCount) {
                val child = parent.getChildAt(x)
                child?.let {
                    val layoutParams = child.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams

                    val top = child.bottom + layoutParams.bottomMargin
                    val bottom = top.plus(lineDrawable?.intrinsicHeight ?: 0)

                    if (isExcludeLastItem.and(x == childCount-1))
                        drawLine(transparentLineDrawable, left, top, right, bottom, c)
                    else
                        drawLine(lineDrawable, left, top, right, bottom, c)
                }
            }
        }
    }

    private fun drawLine(drawable: Drawable?, left: Int, top: Int, right: Int, bottom: Int, c: Canvas) {
        drawable?.setBounds(left, top, right, bottom)
        drawable?.draw(c)
    }
}
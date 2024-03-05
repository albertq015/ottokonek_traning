package com.otto.mart.ui.Partials.decorator

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class GridTokoGrosir(
        private val space: Int,
        private val span: Int
) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view).plus(1)

        when {
            itemPosition % span == 0 -> setRightItem(outRect)
            itemPosition % span == 1 -> setFirstItem(outRect)
            else -> setMidItem(outRect)
        }

    }

    private fun setMidItem(outRect: Rect) {
        outRect.apply {
            bottom = space / 2
            right = space / 2
            top = space / 2
            left = space / 2
        }
    }

    private fun setRightItem(outRect: Rect) {
        outRect.apply {
            bottom = space / 2
            right = 0
            top = space / 2
            left = space / 2
        }
    }

    private fun setFirstItem(outRect: Rect) {
        outRect.apply {
            bottom = space / 2
            right = space / 2
            top = space / 2
            left = 0
        }
    }
}
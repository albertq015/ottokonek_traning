package com.otto.mart.ui.component

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.request.target.BitmapImageViewTarget

class CustomGlideTarget(view: ImageView, private val roundValue: Int, private val placeHolder: Int) : BitmapImageViewTarget(view) {
    override fun setResource(resource: Bitmap?) {
        if (view is SquareImageByWidth) {
            resource?.let { (view as SquareImageByWidth).setRoundImage(resource, roundValue) }
                    ?: view.setImageResource(placeHolder)
        } else {
            super.setResource(resource)
        }
    }
}
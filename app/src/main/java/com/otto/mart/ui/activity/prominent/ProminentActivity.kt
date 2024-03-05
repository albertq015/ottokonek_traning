package com.otto.mart.ui.activity.prominent

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.Window
import androidx.core.text.HtmlCompat
import app.beelabs.com.codebase.base.BaseActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.otto.mart.R
import kotlinx.android.synthetic.main.activity_prominent.*
import java.util.*


class ProminentActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_prominent)
        btnCancel.setOnClickListener { cancelResponse() }
        btnAgree.setOnClickListener { initAllPermission(::okResponse, ::cancelResponse) }
        val spanlistener = object : ClickableSpan() {
            override fun onClick(p0: View) {
                val i = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://otto-konek.s3-ap-southeast-1.amazonaws.com/tnc/otto_konek_privacy_policy.html")
                }
                startActivity(i)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    ds.color = resources.getColor(R.color.dark_blue_green_focus, null)
                else
                    ds.color = resources.getColor(R.color.dark_blue_green_focus)
            }
        }
//        var labelMessage =
//            //60 - 74
//            "To learn more about h1ow we collect and use data, review our Privacy Policy."
        var labelMessage =
            SpannableString("To learn more about how we collect and use data, review our Privacy Policy.").apply {
                this.setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    60,
                    74,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                this.setSpan(spanlistener, 60, 74, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            }
//        if (this.getString(R.string.locale) != "ingris") {
//            //0858a0
//            labelMessage =
//                SpannableString("Untuk mengetahui bagaimana kami mengumpulkan d1an mengunakan data, tinjau di Polisi Privasi kami.").apply {
//                    this.setSpan(
//                        ForegroundColorSpan(Color.BLUE),
//                        65,
//                        79,
//                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                    )
//                    this.setSpan(spanlistener, 65, 79, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//                }
//        }
        pnc.movementMethod = LinkMovementMethod.getInstance()
        pnc.isClickable = true
        pnc.text = labelMessage

    }

    private fun okResponse() {
        val returnIntent = Intent()
        returnIntent.putExtra("result", "")
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    private fun cancelResponse() {
        val returnIntent = Intent()
        returnIntent.putExtra("result", "")
        setResult(RESULT_CANCELED, returnIntent)
        finish()
    }


    fun initAllPermission(doafter: () -> Unit, doOtherwise: () -> Unit) {
        val permissions = if (Build.VERSION.SDK_INT >= 33) {
            setOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                "android.permission.POST_NOTIFICATIONS"
            )
        } else {
            setOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
    }

        Dexter.withContext(this)
            .withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0?.isAnyPermissionPermanentlyDenied == true || p0?.areAllPermissionsGranted() == false) {
                        doOtherwise()
                    } else
                        doafter()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()
    }

}


//https://otto-konek.s3-ap-southeast-1.amazonaws.com/tnc/otto_konek_privacy_policy.html

//fun initAllPermission(doafter: () -> Unit, doOtherwise: () -> Unit) {
//    val permissions = if (Build.VERSION.SDK_INT >= 33) {
//        setOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.CAMERA,
//            "android.permission.POST_NOTIFICATIONS"
//        )
//    } else {
//        setOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA
//        )
//    }
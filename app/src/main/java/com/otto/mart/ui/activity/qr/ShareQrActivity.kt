package com.otto.mart.ui.activity.qr

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import com.otto.mart.R
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.*
import com.otto.mart.ui.activity.AppActivity
import github.nisrulz.screenshott.ScreenShott
import kotlinx.android.synthetic.main.content_share_qr.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class ShareQrActivity : AppActivity() {

    companion object {
        val KEY_QR_CONTENT = "qr_content"
        val KEY_AMOUNT = "amount"
        val KEY_ACTION_TYPE = "action_type"
    }

    enum class ACTION {
        SHARE,
        DOWNLOAD
    }

    var mQrContent = ""
    var mAmount = ""
    var mAction: ACTION? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_qr)

        if (intent.hasExtra(KEY_QR_CONTENT)) {
            mQrContent = intent?.getStringExtra(KEY_QR_CONTENT)!!
        }

        if (intent.hasExtra(KEY_AMOUNT)) {
            mAmount = intent?.getStringExtra(KEY_AMOUNT)!!
        }

        if (intent.hasExtra(KEY_QR_CONTENT)) {
            mAction = intent.getSerializableExtra(KEY_ACTION_TYPE) as ACTION?
        }

        initView()
        displayMerchantInfo()
        displayQrCode(mQrContent)

        Handler().postDelayed({
            if (mAction == ACTION.SHARE) {
                shareReceipt()
                finish()
            } else {
                downloadQr()
            }
        }, 500)
    }

    private fun initView() {
        val screenWidth = resources.displayMetrics.widthPixels
        val width: Int = (70 * screenWidth) / 100
        UIUtils.resize(imgQr, width, width)
    }

    private fun displayMerchantInfo() {
        tvMerchantName.text = SessionManager.getUserProfile().name
                ?: SessionManager.getUserProfile().merchant_name ?: ""
//        SessionManager.getNMID()?.takeIf { it.isNotEmpty() }?.run { tvNMID.text = "NMID : ${this}" }
//        tvTid.text = SessionManager.getTID()
        tvNmid.text = SessionManager.getPrefLogin().binName +  " QR"
        tvDate.text = getString(R.string.text_print_version) + DateUtil.getSimpleTime("yyyy.MM.dd")
    }

    private fun displayQrCode(qrContent: String?) {
        if (!mAmount.equals("")) {
            tvAmount.text = mAmount
            tvAmount.visible()
        }

        imgQr.setImageBitmap(BitmapUtil.generateBitmap(qrContent, 9, DeviceUtil.dpToPx(240), DeviceUtil.dpToPx(240)))
    }

    private fun shareReceipt() {
        // View with spaces as per constraints
        val bitmapView = ScreenShott.getInstance().takeScreenShotOfView(masterLayout)
        //val bitmapView = ScreenShott.getInstance().takeScreenShotOfRootView(masterLayout)
        //val bitmapView = BitmapUtil.getBitmapFromView(masterLayout, shareLayout.getChildAt(0).height, shareLayout.getChildAt(0).width)

        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val outstream: OutputStream?
        try {
            outstream = contentResolver.openOutputStream(uri!!)
            bitmapView.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
            outstream!!.close()
        } catch (e: Exception) {
            System.err.println(e.toString())
        }

        share.putExtra(Intent.EXTRA_TEXT, "Easy Cashless Payment with Smartphone. Scan This QR with your e-money App. For More information visit www.ottokonek.com")
        share.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(share, getString(R.string.ppob_msg_share_it)))
    }

    private fun downloadQr() {
        val bitmapView = ScreenShott.getInstance().takeScreenShotOfView(masterLayout)
        saveImageToGallery(bitmapView)
    }

    fun saveImageToGallery(qrImages: Bitmap?): Boolean {
        var result = false
        try {
            var currentTimeMiilis = System.currentTimeMillis()
            //Reset date to prevent multiple image
            //currentTimeMiilis = 1530005294

            var directory = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath!! + "/OttoPay"
            } else {
                directory =  Environment.getExternalStorageDirectory().absolutePath + "/OttoPay"
            }
            val f = File(directory)
            if (!f.exists()) {
                f.mkdirs()
            }
            val imageFile = File(directory, String.format("ottopay_qr_%d.jpg", currentTimeMiilis))
            saveBitmapToJPG(qrImages!!, imageFile)
            scanMediaFile(imageFile)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    fun saveBitmapToJPG(bitmap: Bitmap, photo: File?) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream: OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }

    private fun scanMediaFile(imageFile: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(imageFile)
        mediaScanIntent.data = contentUri
        sendBroadcast(mediaScanIntent)

        getString(R.string.share_qr_msg_download_success).showToast(this)
        finish()
    }
}

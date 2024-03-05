package com.otto.mart.ui.fragment.wallet

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.otto.mart.R
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.EmvcoUtil
import com.otto.mart.support.util.pref.Pref
import com.otto.mart.support.util.pref.PrefConstance
import com.otto.mart.ui.activity.bogasari.catalog.CatalogBogasariActivity
import com.otto.mart.ui.activity.qr.payment.QrPaymentActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.activity.transaction.ProcessQRPaymentActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.fragment_pay_qr.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.*

class ScanQRFragment : Fragment(R.layout.fragment_pay_qr) {

    private val RC_PIN_INDOMARET_PURCHASE = 2010
    private val RC_PIN_SHOW_QR = 2011

    private val RESULT_LOAD_IMG = 777

    private var contentInitialized: Boolean = false
    private var paybleAmount: Int = 0
    private var isManualInput: Boolean = false
    private var mQRContent = ""

    private var isfromFile = false
    private var mFlash = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getIntentData()
        initPermissions()
    }

    override fun onResume() {
        super.onResume()
        if (contentInitialized) {
            cam_view?.startCamera()
            cam_view?.setResultHandler(scannerHandler)
        }
    }

    override fun onPause() {
        super.onPause()
        if (contentInitialized) {
            cam_view?.stopCamera()
        }
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        cam_view?.stopCamera()
        cam_view?.setResultHandler(null)
        super.startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RESULT_LOAD_IMG -> {
                    val selectedImage = data?.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        activity?.let {
                            val cursor: Cursor? = it.getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null)

                            if (cursor != null) {
                                cursor.moveToFirst()
                                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                                val picturePath = cursor.getString(columnIndex)
                                val bmp = BitmapFactory.decodeFile(picturePath)
                                val intArray = IntArray(bmp.width * bmp.height)
                                bmp.getPixels(intArray, 0, bmp.width, 0, 0, bmp.width, bmp.height)
                                val source = RGBLuminanceSource(bmp.width, bmp.height, intArray)
                                val bbmp = BinaryBitmap(HybridBinarizer(source))
                                val reader: Reader = MultiFormatReader()
                                try {
                                    val hints = Hashtable<DecodeHintType, Any?>()
                                    hints[DecodeHintType.TRY_HARDER] = true
                                    mQRContent = reader.decode(bbmp, hints).text
                                    gotoProcessQRPayment(mQRContent)
                                } catch (e: NotFoundException) {
                                    isfromFile = false
                                    e.printStackTrace()
                                    val dialog = ErrorDialog(it, activity, false, false, "Pemberitahuan, File Qr yang kamu pilih salah, Pilih file lain", "")
                                    dialog.setOnDismissListener { v: DialogInterface? -> openGallery() }
                                    dialog.show()
                                }
                                cursor.close()
                            }
                        }
                    }

//                    if (data == null || data.data == null) {
//                        Log.e("TAG", "The uri is null, probably the user cancelled the image selection process using the back button.")
//                        return
//                    }
//                    val uri = data.data
//                    try {
//                        uri?.let {
//                            val inputStream: InputStream? = activity?.getContentResolver()?.openInputStream(it)
//                            var bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
//                            if (bitmap == null) {
//                                Log.e("TAG", "uri is not a bitmap," + uri.toString())
//                                return
//                            }
//                            val width: Int = bitmap.getWidth()
//                            val height: Int = bitmap.getHeight()
//                            val pixels = IntArray(width * height)
//                            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
//                            bitmap.recycle()
//                            bitmap = null
//                            val source = RGBLuminanceSource(width, height, pixels)
//                            val bBitmap = BinaryBitmap(HybridBinarizer(source))
//                            val reader = MultiFormatReader()
//                            try {
//                                val result = reader.decode(bBitmap)
//                                mQRContent = result.toString()
//                                gotoProcessQRPayment(mQRContent)
//                            } catch (e: NotFoundException) {
//                                Log.e("TAG", "decode exception", e)
//                                val dialog = activity?.let { ErrorDialog(it, activity, false, false, "Pemberitahuan, File Qr yang kamu pilih salah, Pilih file lain", "") }
//                                dialog?.setOnDismissListener { v: DialogInterface? -> openGallery() }
//                                dialog?.show()
//                            }
//                        }
//                    } catch (e: FileNotFoundException) {
//                        Log.e("TAG", "can not open file" + uri.toString(), e)
//                        val dialog = activity?.let { ErrorDialog(it, activity, false, false, "Gagal membuka File", "") }
//                                    dialog?.setOnDismissListener { v: DialogInterface? -> openGallery() }
//                                    dialog?.show()
//                    }


                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getIntentData() {
        tvBalance?.text = "Saldo yang dapat digunakan : ${DataUtil.convertCurrency(activity?.intent?.getLongExtra("balance",0))}"
    }

    private fun initPermissions() {
        activity?.let { it ->
            Dexter.withContext(it).withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        initContent()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        activity?.finish()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        p1?.continuePermissionRequest()
                    }

                })
        }
    }


    private fun initContent() {
        contentInitialized = true

        cam_view?.setFormats(listOf(BarcodeFormat.QR_CODE))
        cam_view?.setResultHandler(scannerHandler)

        ivQrGallery.setOnClickListener {
            openGallery()
        }

        ivQrFlash.setOnClickListener {
            switchFlashLight()
        }
    }

    private fun switchFlashLight() {
        mFlash = !mFlash
        cam_view.setFlash(mFlash)
    }

    private fun openGallery() {
        isfromFile = true
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
    }

    private var scannerHandler: ZXingScannerView.ResultHandler = object : ZXingScannerView.ResultHandler {
        var isReady = false

        override fun handleResult(result: Result) {
            paybleAmount = 0
            cam_view?.resumeCameraPreview(this)

            when (EmvcoUtil.parseEMVCOtag01(result.toString())) {
                11 -> {
                    showLoading("Mohon tunggu")
                    isManualInput = true
                    isReady = true
                }
                12 -> {
                    showLoading("Mohon tunggu")
                    isManualInput = false
                    isReady = true
                //    paybleAmount = Integer.parseInt(EmvcoUtil.parseEMVCOtag54(result.toString()))
                }
                else -> {
                    showLoading("Mohon tunggu")
                    isReady = true
                    isManualInput = true
                }
            }

            if (isReady) {
                cam_view?.stopCamera()
                activity?.let { ProgressDialogComponent.dismissProgressDialog(it as BaseActivity) }

                val qrContent = EmvcoUtil.parseEMVCOtag62(result.text)
                val qrContentSeparated = Arrays.asList(*qrContent.split("\\s*;;;;\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())

                if(qrContentSeparated.size > 0) {
                    if (qrContentSeparated[0].equals("IDM", ignoreCase = true)) {
                        Pref.getPreference().putString(PrefConstance.STORE_ID, qrContentSeparated[1])
                        Pref.getPreference().putString("t59", EmvcoUtil.parseEMVCOtagByNum(59, result.text))
                        Pref.getPreference().putString("t60", EmvcoUtil.parseEMVCOtagByNum(60, result.text))
                        Pref.getPreference().putString("t62", EmvcoUtil.parseEMVCOtag62(result.text))

                        val jenk = Intent(activity, RegisterPinResetActivity::class.java)
                        jenk.putExtra("confirm", true)
                        startActivityForResult(jenk, RC_PIN_INDOMARET_PURCHASE)
                    } else if (qrContentSeparated.size > 1 && qrContentSeparated[1].contains("BG")) {
                        val intent = Intent(activity, CatalogBogasariActivity::class.java)
                        intent.putExtra("qrContent", result.text)
                        intent.putExtra("merchantId", qrContent.split(";;;;".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
                        startActivity(intent)
                        activity?.finish()
                    } else {
                        mQRContent = result.text
                        gotoProcessQRPayment(result.text)
                    }
                } else {
                    mQRContent = result.text
                    gotoProcessQRPayment(result.text)
                }
            }
        }
    }

    private fun showLoading(message: String) {
        activity?.let { activity ->
            if (!(activity as Activity).isFinishing) {
                ProgressDialogComponent.showProgressDialog(activity, message, false).show()
            }
        }
    }

    private fun gotoProcessQRPayment(result: String) {
        val intent = Intent(activity, QrPaymentActivity::class.java)
        intent.putExtra(ProcessQRPaymentActivity.KEY_QR_CONTENT, result)
        startActivity(intent)
    }
}
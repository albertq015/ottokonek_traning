package com.otto.mart.ui.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otto.mart.BuildConfig
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.localmodel.ppob.PpobMenu
import com.otto.mart.support.util.SystemUtil
import com.otto.mart.ui.Partials.adapter.ppob.PpobMenuAdapter
import com.otto.mart.ui.activity.ppob.setup.PpobMenuSetup
import com.otto.mart.ui.component.dialog.ErrorDialog
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

/**
 * Created by macari on 3/5/18.
 */
open class PpobActivity : AppActivity() {
    private val TAG = this.javaClass.simpleName
    var mEtCustNumber: EditText? = null
    var mEtEmail: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(context))
    }

    /**
     * Avoid user to take screeen capture
     */
    override fun disableScreenshot() {
        if (!BuildConfig.DEBUG) {
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    //region Pick Contact
    protected fun pickContact(etCustNumber: EditText?, etEmail: EditText?) {
        mEtCustNumber = etCustNumber
        mEtEmail = etEmail
//        val pickContactIntent = Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"))
        val pickContactIntent = Intent(Intent.ACTION_PICK, Uri.parse(""))
        pickContactIntent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, AppKeys.KEY_PICK_CONTACT_REQUEST)
    }

    override fun onResume() {
        if (SystemUtil.isRooted()) {
            val errorDialog = ErrorDialog(this, this, false, false, getString(R.string.security_root_message), "")
            errorDialog.setOnDismissListener { finishAffinity() }
            errorDialog.show()
        }
        super.onResume()
    }

    override fun onPause() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        super.onPause()
    }

    private fun validatePhoneNumber(number: String) {
        var validNumber = number.replace("\\+|\\-|\\ ".toRegex(), "")
        if ("0" != validNumber.substring(0, 1)) {
            val tempNumber = validNumber.substring(2)
            validNumber = "0$tempNumber"
        }
        val items = validNumber.split("").toTypedArray()
        mEtCustNumber!!.setText("")
        for (i in items.indices) {
            val latestPhone = mEtCustNumber!!.text.toString() + items[i]
            mEtCustNumber!!.setText(latestPhone)
        }
        mEtCustNumber!!.setSelection(mEtCustNumber!!.text.length)
        mEtEmail!!.setText("")
    }

    //endregion Pick Contact
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // Make sure the request was successful
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) { // Check which request we're responding to
            if (requestCode == AppKeys.KEY_PICK_CONTACT_REQUEST) { // Get the URI that points to the selected contact
                val contactUri = data?.data
                // We only need the NUMBER column, because there will be only one row in the result
                val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

                contactUri?.let {
                    val cursor = contentResolver.query(it, projection, null, null, null)

                    cursor?.moveToFirst()
                    // Retrieve the phone number from the NUMBER column
                    val column = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val number = column?.let { it1 -> cursor?.getString(it1) }
                    // Do something with the phone number...
                    number?.let { it1 -> validatePhoneNumber(it1) }
                }
            }
        }
    }

    protected fun showAllPpobMenu(title: String?) {
        val dialog = Dialog(this, R.style.DialogNoPadding)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_ppob_all_menu)

        val masterLayout = dialog.findViewById<LinearLayout>(R.id.master_layout)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val rvMenu: RecyclerView = dialog.findViewById(R.id.rvMenu)

        tvTitle.text = title
        rvMenu.setHasFixedSize(false)

        val gridLayoutManager = GridLayoutManager(this, 4)
        rvMenu.layoutManager = gridLayoutManager

        masterLayout.setOnClickListener {
                v: View? -> dialog.dismiss()
        }

        val ppobMenuAdapter = PpobMenuAdapter(this, PpobMenuSetup(this).getAllPpobMenu())
        rvMenu.adapter = ppobMenuAdapter

        ppobMenuAdapter.setmOnViewClickListener(object : PpobMenuAdapter.OnViewClickListener {
            override fun onViewClickListener(item: PpobMenu, position: Int) {
                if (item.intent != null) {
                    startActivity(item.intent)
                } else {
                    val dialog = ErrorDialog(this@PpobActivity, this@PpobActivity, false, false, getString(R.string.dialog_msg_coming_soon), "")
                    dialog.show()
                }
            }
        })
        dialog.show()
    }
}

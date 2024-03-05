package com.otto.mart.ui.activity.contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.ContactUsResponse
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.showToast
import com.otto.mart.ui.Partials.ContactUsRepo
import com.otto.mart.ui.activity.AppActivity
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.item_contact_us.view.*
import kotlinx.android.synthetic.main.toolbar.*

class ContactActivity : AppActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        initView()
        initContent()
        initListener()
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.profile_menu_contact_us)

        //Email
        iclEmail.ivIcon.setImageResource(R.drawable.ic_email_outline)
        iclEmail.tvLabel.text = getString(R.string.label_email)

        //Telephone
        iclTelp.ivIcon.setImageResource(R.drawable.ic_telephone_outline)
        iclTelp.tvLabel.text = getString(R.string.text_telephone)

        //Whatsapp
        iclWA.ivIcon.setImageResource(R.drawable.ic_whatsapp_outline)
        iclWA.tvLabel.text = getString(R.string.label_whatsapp)
        iclWA.gone()

        //Website
        iclWeb.ivIcon.setImageResource(R.drawable.ic_website_outline)
        iclWeb.tvLabel.text = getString(R.string.text_website)

        //Jam Operasional
        iclTime.ivIcon.setImageResource(R.drawable.ic_time_outline)
        iclTime.tvLabel.text = getString(R.string.label_working_hour)
        iclTime.ivArrow.gone()
    }

    private fun initContent() {
        ContactUsRepo().getContactUs {
            it?.let { setContactData(it) } ?: getString(R.string.text_error).showToast(this)
        }
    }

    private fun setContactData(data: ContactUsResponse.ContactUsData) {
        iclEmail.tvValue.text = data.email
        iclTelp.tvValue.text = data.telepon
        iclWA.tvValue.text = data.whatsapp
        iclWeb.tvValue.text = data.website
        iclTime.tvValue.text = data.support_hours
    }

    private fun initListener() {
        iclEmail.setOnClickListener(this)
        iclTelp.setOnClickListener(this)
        iclWA.setOnClickListener(this)
        iclWeb.setOnClickListener(this)
        iclTime.setOnClickListener(this)
        btnToolbarBack.setOnClickListener { finish() }
    }

    override fun onClick(v: View?) {
        when (v) {
            iclEmail -> {
                if (iclEmail.tvValue.text != getString(R.string.load)) {
                    startActivity(Intent.createChooser(Intent(Intent.ACTION_SENDTO).apply {
                        type = "text/plain"
                        data = Uri.parse("mailto:${iclEmail.tvValue.text}")
//                    putExtra(Intent.EXTRA_EMAIL, "halo@ottopay.id")
                    }, getString(R.string.label_working_hour)))
                }
            }

            iclTelp -> {
                if (iclTelp.tvValue.text != getString(R.string.load))
                    startActivity(Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:${iclTelp.tvValue.text}") })
            }

            iclWA -> {
                if (iclWA.tvValue.text != getString(R.string.load)) {
                    var waNumber = iclWA.tvValue.text.toString()
                            .replace("-", "").replace(" ", "")
                    waNumber = "+62" + waNumber.substring(1, waNumber.length)
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/${waNumber}?text=Saya%20memerlukan%20bantuan\n")))
                }
            }

            iclWeb -> {
                if (iclWeb.tvValue.text != getString(R.string.load)) {
                    val value = iclWeb.tvValue.text.toString()
                    val url = if (value.contains("https://")) value else "https://$value"
                    startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) })
                }
            }
        }
    }
}

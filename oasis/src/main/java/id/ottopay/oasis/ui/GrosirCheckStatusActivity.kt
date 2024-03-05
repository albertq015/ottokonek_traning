package id.ottopay.oasis.ui

import android.content.Intent
import android.os.Bundle
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import id.ottopay.oasis.R
import id.ottopay.oasis.adapters.HistoryOasisAdapter
import kotlinx.android.synthetic.main.activity_grosir_check_status.*
import kotlinx.android.synthetic.main.oasis_toolbar.*


class GrosirCheckStatusActivity : AppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grosir_check_status)
        tvToolbarTitle.setText(getString(com.otto.mart.R.string.text_order_history))
        btnToolbarBack.setOnClickListener { View ->
            if(intent.getBooleanExtra("fromgrosir",false)){
                val intent = Intent(this, DashboardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }else {
                onBackPressed()
            }
        }

        viewpager.adapter = HistoryOasisAdapter(supportFragmentManager,this)
        viewpagertab.setupWithViewPager(viewpager)

    }


}
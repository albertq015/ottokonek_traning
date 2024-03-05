package com.otto.mart.ui.activity.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.otto.mart.R
import com.otto.mart.ui.activity.AppActivity
import kotlinx.android.synthetic.main.activity_profile_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class ProfileDetailActivity : AppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_detail)
    }
}

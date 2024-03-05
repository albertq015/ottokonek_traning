package id.ottopay.oasis.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.otto.mart.model.APIModel.Response.grosir.DataResponseItemList
import id.ottopay.oasis.R
import kotlinx.android.synthetic.main.activity_grosir_payment_method.*
import kotlinx.android.synthetic.main.oasis_toolbar.*

class GrosirPaymentMethod : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grosir_payment_method)

        tvToolbarTitle.setText("Select Payment Method")

        btnToolbarBack.setOnClickListener { View ->
            onBackPressed()
        }

        var dataProduct = Gson().fromJson(
            intent.extras?.getString("dataProduct"),
            DataResponseItemList::class.java
        )

        name_payment_1.text = dataProduct.payment_method.get(0).name
        desc_1.text = dataProduct.payment_method.get(0).description

        name_payment_2.text = dataProduct.payment_method.get(1).name
        desc_2.text = dataProduct.payment_method.get(1).description

        pao1.setOnClickListener {
            intent.putExtra("code",dataProduct.payment_method.get(0).code)
            intent.putExtra("name",dataProduct.payment_method.get(0).name)
            intent.putExtra("pos", 0)
            setResult(RESULT_OK,intent)
            finish()
        }

        pao2.setOnClickListener {
            intent.putExtra("code",dataProduct.payment_method.get(1).code)
            intent.putExtra("name",dataProduct.payment_method.get(1).name)
            intent.putExtra("pos", 1)
            setResult(RESULT_OK,intent)
            finish()
        }

    }
}
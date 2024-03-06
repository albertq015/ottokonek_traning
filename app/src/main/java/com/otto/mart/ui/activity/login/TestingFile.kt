package com.otto.mart.ui.activity.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.otto.mart.R
import com.otto.mart.api.Client
import com.otto.mart.model.APIModel.Request.SampleRequest
import com.otto.mart.model.APIModel.Response.Response
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TestingFile:Activity() {
    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout._test_activity)


        val button1 = findViewById<Button>(R.id.button_active)
        val button2 = findViewById<Button>(R.id.button_active2)
        val button3 = findViewById<Button>(R.id.button_active3)
        val button4 = findViewById<Button>(R.id.button_active4)
        val text = findViewById<EditText>(R.id.text)
        val text1 = findViewById<EditText>(R.id.text1)
        val text2 = findViewById<EditText>(R.id.text2)
        val text3 = findViewById<EditText>(R.id.text3)
        val text4 = findViewById<EditText>(R.id.text4)
        button1.text = "Click"
        button3.text = "Call API"




        button1.setOnClickListener{
            buttonFunction(button1)

        }
        button2.setOnClickListener{
            val intent = Intent(this@TestingFile, LoginActivity::class.java)
            startActivity(intent)
        }

        button3.setOnClickListener{
            apiCall(text,text1,text2,text3,text4)
        }
        button4.setOnClickListener{

        }
    }
    @SuppressLint("SetTextI18n")
    private fun buttonFunction(button: Button){
        if(button.text == "Reset"){
            Toast.makeText(this@TestingFile, "You clicked the button",Toast.LENGTH_SHORT).show()
            button.text = "Click"
        } else {
            Toast.makeText(this@TestingFile, "You reset the button",Toast.LENGTH_SHORT).show()
            button.text = "Reset"
        }
    }
    fun apiCall(textView: TextView,textView1: TextView, textView2: TextView, textView3: TextView, textView4: TextView){
        val client = OkHttpClient.Builder().addInterceptor(ChuckerInterceptor(this@TestingFile)).build()
        val retrofit = Retrofit.Builder().baseUrl("https://dev2-ottokonek.ottopay.id").addConverterFactory(GsonConverterFactory.create()).client(client).build()
        val apiCall = retrofit.create(Client::class.java)
        val request = SampleRequest()
        request.id = 34
        request.app_id = "com.ottokonek.dev"
        request.version = 34
        apiCall.dataAPI(request)
            .enqueue(object : Callback<Response>{
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    Toast.makeText(this@TestingFile,response.body()?.data?.version, Toast.LENGTH_LONG).show()
                    textView.text = response.body()?.data?.api
                    textView1.text = response.body()?.data?.version
//                    textView2.text = response.body()?.data?.update.toString()
                    textView3.text = response.body()?.data?.id
//                    textView4.text = response.body()?.meta?.message

                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Toast.makeText(this@TestingFile, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }
}
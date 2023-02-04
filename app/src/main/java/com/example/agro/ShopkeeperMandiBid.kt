package com.example.agro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ShopkeeperMandiBid : AppCompatActivity() {
    var title : String? = ""
    var basePrice : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopkeeper_mandi_bid)
        if (intent.hasExtra("popuptitle")) {
            title = intent.getStringExtra("popuptitle").toString()
            findViewById<TextView>(R.id.popup_window_title).text = title
        }
        if (intent.hasExtra("basePrice")) {
            basePrice = intent.getStringExtra("basePrice").toString()
            findViewById<TextView>(R.id.basePrice).text = basePrice
        }
    }
}
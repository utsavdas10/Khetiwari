package com.example.agro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class Name_Email : AppCompatActivity() {
    var phoneNumber : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_email)
        if (intent.hasExtra("phoneNumber")) {
            phoneNumber = intent.getStringExtra("phoneNumber").toString()
        }
        findViewById<Button>(R.id.continueBtn).setOnClickListener {
            val email = findViewById<EditText>(R.id.email).text.toString()
            val name = findViewById<EditText>(R.id.name).text.toString()
            val intent= Intent(this,Home::class.java)
            intent.putExtra("phoneNumber",phoneNumber)
            intent.putExtra("NAME",name)
            intent.putExtra("EMAIL",email)
            startActivity(intent)
        }
    }
}
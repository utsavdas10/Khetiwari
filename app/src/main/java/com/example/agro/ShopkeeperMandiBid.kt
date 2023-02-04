package com.example.agro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ShopkeeperMandiBid : AppCompatActivity() {
    var title: String? = ""
    var currentPrice: String? = ""
    var name: String? = ""
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopkeeper_mandi_bid)
        if (intent.hasExtra("popuptitle")) {
            title = intent.getStringExtra("popuptitle").toString()
            findViewById<TextView>(R.id.popup_window_title).text = title
        }
        if (intent.hasExtra("basePrice")) {
            currentPrice = intent.getStringExtra("basePrice").toString()
        }
        var fdatabase = FirebaseDatabase.getInstance().getReference("Users")
        val rCurrentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        fdatabase.child(rCurrentUserId).get().addOnSuccessListener {
            name = it.child("name").value.toString()
        }
        fdatabase = FirebaseDatabase.getInstance().getReference("Auction")
        fdatabase.child(rCurrentUserId).child(title.toString()).get().addOnSuccessListener {
            if(it.exists()){
                val price = it.child("cp").value.toString()
                if (price.isNotEmpty())
                    currentPrice = price
            }
            findViewById<TextView>(R.id.basePrice).text = "Current price: "+currentPrice.toString()
        }

        findViewById<Button>(R.id.addBtn).setOnClickListener {
            val price = findViewById<EditText>(R.id.editTextBasePrice).text.toString()
            if(price.isNotEmpty()) {
                if (currentPrice.toString().toInt() < price.toInt()) {
                    currentPrice = price
                    Toast.makeText(this, "Bid registered", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ShopkeeperMandi::class.java))
                }
                else
                    Toast.makeText(this, "Bidding price should be greater than current price", Toast.LENGTH_LONG).show()
            }
            else
                Toast.makeText(this, "Bidding price should not be empty", Toast.LENGTH_SHORT).show()
            uploadData()
        }
    }

    private fun uploadData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("Auction")
        val User = Auction("${currentPrice}", "${name}")
        database.child(currentUserId).child(title.toString()).setValue(User)
    }
}
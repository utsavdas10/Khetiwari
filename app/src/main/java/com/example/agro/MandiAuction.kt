package com.example.agro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MandiAuction : AppCompatActivity() {
    var name: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandi_auction)

        val product = intent.getStringExtra("Product")
        val quantity = intent.getStringExtra("Quantity")
        val unit = intent.getStringExtra("Unit1")
        val price = intent.getStringExtra("Price")

        val fdatabase = FirebaseDatabase.getInstance().getReference("Users")
        val rCurrentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        fdatabase.child(rCurrentUserId).get().addOnSuccessListener {
            name = it.child("name").value.toString()
            val arr = name?.split(" ")
            val firstWord = arr?.get(0)
            findViewById<TextView>(R.id.name).text =firstWord
        }


        val user = FirebaseAuth.getInstance().currentUser

        val userId = user?.uid.toString()
        findViewById<TextView>(R.id.quantity).text = "${quantity} ${unit?.lowercase()} -"
        findViewById<TextView>(R.id.item).text =product

        val bidderLinear: LinearLayout = findViewById(R.id.bidderLinear)
        val inflater: LayoutInflater = LayoutInflater.from(this)

        val db = Firebase.firestore.collection("Users").document(userId).collection("Selling")

        for(i in 0..10) {
            val view: View = inflater.inflate(R.layout.activity_bidder_card, bidderLinear, false)

            view.findViewById<Button>(R.id.accept).setOnClickListener {
                TODO()
            }

            bidderLinear.addView(view)
        }
    }
}
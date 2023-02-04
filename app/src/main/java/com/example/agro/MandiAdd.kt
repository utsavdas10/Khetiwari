package com.example.agro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.agro.data_classes.MandiAuctionData
import com.example.agro.data_classes.StorageData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MandiAdd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandi_add)


        val Product = intent.getStringExtra("Product")

        val Quantity = intent.getStringExtra("Quantity")
        val QuantityInt = Integer.parseInt(Quantity)

        val Unit1 = intent.getStringExtra("Unit1")

        findViewById<TextView>(R.id.popup_window_title).text = Product

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid.toString()
        val db = Product?.let {
            Firebase.firestore.collection("Users").document(userId)
                .collection("Storage").document(it)
        }

        findViewById<Button>(R.id.addBtn).setOnClickListener {

            val quantity = findViewById<EditText>(R.id.editTextQuantity).text.toString()
            val quantityInt = Integer.parseInt(quantity)

            val basePrice = findViewById<EditText>(R.id.editTextBasePrice).text.toString()
            val basePriceInt = Integer.parseInt(basePrice)

            val updatedQuantity = QuantityInt - quantityInt
            val updatedData = StorageData(updatedQuantity, Unit1)

            db?.set(updatedData)?.addOnSuccessListener {
                Toast.makeText(applicationContext, "Item Added", Toast.LENGTH_LONG).show()
            }?.addOnFailureListener {
                Toast.makeText(applicationContext, "Error adding Item", Toast.LENGTH_LONG).show()
            }

            val auction = MandiAuctionData(Product, quantityInt ,Unit1 ,basePriceInt, user?.displayName.toString())

            Product?.let {
                Firebase.firestore.collection("Users").document(userId)
                    .collection("Selling").document(it)
            }
                ?.set(auction)
            startActivity(Intent(this, MandiStorage::class.java))
            finish()
        }
    }
}
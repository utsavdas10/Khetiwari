package com.example.agro

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_storage.*

class MandiStorage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandi_storage)

        val intent = Intent(this, MandiAdd::class.java)
        val storageLinear: LinearLayout = findViewById(R.id.mandiStorageLinear)
        val inflater: LayoutInflater = LayoutInflater.from(this)

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid.toString()
        val db = Firebase.firestore.collection("Users").document(userId)
            .collection("Storage")
        db.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val view: View = inflater.inflate(R.layout.activity_mandi_storage_card, storageLinear, false)

                    val product = document.id
                    val quantity = document.data["quantity"]
                    val unit = document.data["unit"]
                    view.findViewById<TextView>(R.id.Name).text = product
                    view.findViewById<TextView>(R.id.amount).text = quantity.toString() + " "+unit.toString().lowercase()

                    storageLinear.addView(view)

                    view.setOnClickListener {
                        intent.putExtra("Product", product)
                        intent.putExtra("Quantity", quantity.toString())
                        intent.putExtra("Unit1", unit.toString())
                        startActivity(intent)
                        finish()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}
package com.example.agro

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_storage.*

class Mandi : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandi)

        findViewById<ImageView>(R.id.iBtn).setOnClickListener{
            startActivity(Intent(this,MandiI::class.java))
        }

        val intent1 = Intent(this, MandiAuction::class.java)

        findViewById<ImageButton>(R.id.imageButton1).setOnClickListener {
            val intent = Intent(this, MandiStorage::class.java)
            startActivity(intent)
        }
        
        val mandiLinear: LinearLayout = findViewById(R.id.mandiLinear)
        val inflater: LayoutInflater = LayoutInflater.from(this)

        val user = FirebaseAuth.getInstance().currentUser

        val userId = user?.uid.toString()
        val db = Firebase.firestore.collection("Users").document(userId).collection("Selling")

        db.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val view: View = inflater.inflate(R.layout.activity_mandi_card, mandiLinear, false)

                    view.findViewById<TextView>(R.id.item).text = document.id
                    view.findViewById<TextView>(R.id.quantity).text = document.data["quantity"].toString() +" "+ document.data["unit"].toString().lowercase()+" -"
                    val fdatabase = FirebaseDatabase.getInstance().getReference("Users")
                    val rCurrentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                    fdatabase.child(rCurrentUserId).get().addOnSuccessListener {
                        val name = it.child("name").value.toString()
                        val arr = name.split(" ")
                        val firstWord = arr[0]
                        view.findViewById<TextView>(R.id.name).text = firstWord
                    }


                    view.setOnClickListener {
                        intent1.putExtra("Product", document.id)
                        intent1.putExtra("Quantity", document.data["quantity"].toString())
                        intent1.putExtra("Unit1", document.data["unit"].toString())
                        intent1.putExtra("Price", document.data["price"].toString())
                        startActivity(intent1)
                    }

                    mandiLinear.addView(view)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}
package com.example.agro

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_storage.*

class ShopkeeperStore : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopkeeper_store)

        val imgbtn = findViewById<ImageButton>(R.id.imageButton)
        imgbtn.setOnClickListener {
            val intent = Intent(this, ShopkeeperShopAdd::class.java)
            intent.putExtra("popuptitle", "Add Items!")
            intent.putExtra("popupbtn", "Done")
            intent.putExtra("darkstatusbar", false)
            startActivity(intent)
        }
        val shopkeeperStoreLinear: LinearLayout = findViewById(R.id.shopkeeperStoreLinear)
        val inflater: LayoutInflater = LayoutInflater.from(this)

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid.toString()
        val db = Firebase.firestore.collection("Users").document(userId)
            .collection("Selling_In_Shop")
        db.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val view: View = inflater.inflate(R.layout.activity_shopkeeper_store_card, shopkeeperStoreLinear, false)

                    view.findViewById<TextView>(R.id.Name).text = document.data["product"].toString()
                    view.findViewById<TextView>(R.id.amount).text = document.data["quantity"].toString()+" " + document.data["unit"].toString()
                    view.findViewById<TextView>(R.id.cost).text = "Rs. "+ document.data["price"].toString()

                    shopkeeperStoreLinear.addView(view)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}
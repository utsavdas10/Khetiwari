package com.example.agro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShopkeeperMandi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopkeeper_mandi)

        val user = FirebaseAuth.getInstance().currentUser
        val userName =  user?.displayName.toString()
        val userId = user?.uid.toString()
        val db = Firebase.firestore.collection("Users")
        //.document(userId).collection("Selling_In_Shop")
        val linear1: LinearLayout = findViewById(R.id.linear1)
        val inflater: LayoutInflater = LayoutInflater.from(this)

        db.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val view: View = inflater.inflate(R.layout.activity_shopkeeper_store_card, linear1, false)
                    val db1 = db.document(document.id).collection("Selling")
                    if(db1.document()!=null) {
                        db1.get()
                            .addOnSuccessListener { documents1 ->
                                for (document1 in documents1) {
                                    if (document1.data["product"] != null)
                                        view.findViewById<TextView>(R.id.Name).text =
                                            document1.data["product"].toString()
////
                                    view.findViewById<TextView>(R.id.amount).text =
                                        document1.data["quantity"].toString() + document1.data["unit"].toString()

                                    view.findViewById<TextView>(R.id.cost).text =
                                        "Rs. " + document1.data["price"].toString()

                                    view.findViewById<TextView>(R.id.seller).text =
                                        "Seller: "+ document1.data["sellerName"].toString()
//
                                    if(view.parent != null) {
                                        (view.parent as? ViewGroup)?.removeView(view)
                                    }
                                    linear1.addView(view)
//
                                    view.findViewById<CardView>(R.id.card).setOnClickListener {
                                        val intent = Intent(this, ShopkeeperMandiBid::class.java)
                                        intent.putExtra("popuptitle", "" + document1.data["product"].toString())
                                        intent.putExtra("basePrice", "" + document1.data["price"].toString() )
                                        startActivity(intent)
                                    }
                                }
                            }
                    }
                }
            }
    }
}
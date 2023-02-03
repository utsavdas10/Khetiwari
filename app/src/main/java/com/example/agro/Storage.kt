package com.example.agro

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_storage.*

class Storage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        val imgbtn = findViewById<ImageButton>(R.id.imageButton)
        imgbtn.setOnClickListener {
            val intent = Intent(this, PopUpWindow::class.java)
            intent.putExtra("popuptitle", "Add Items!")
            intent.putExtra("popupbtn", "Done")
            intent.putExtra("darkstatusbar", false)
            startActivity(intent)
        }
        val linear1: LinearLayout = findViewById(R.id.storageLinear)
        val inflater: LayoutInflater = LayoutInflater.from(this)

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid.toString()
        val db = Firebase.firestore.collection("Users").document(userId)
            .collection("Storage")
        db.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val view: View = inflater.inflate(R.layout.activity_storage_card, storageLinear, false)

                    view.findViewById<TextView>(R.id.Name).text = document.id
                    view.findViewById<TextView>(R.id.amount).text = document.data["quantity"].toString() + document.data["unit"].toString()

                    storageLinear.addView(view)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

    }
}
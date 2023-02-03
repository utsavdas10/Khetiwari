package com.example.agro

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors

class Account : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        readData()

        findViewById<Button>(R.id.edit_profile_button).setOnClickListener {
            val intent = Intent(this,Edit_profile::class.java)
            intent.putExtra("MOBILE_NUMBER",findViewById<TextView>(R.id.mobile_number).text.toString())
            intent.putExtra("DOB",findViewById<TextView>(R.id.dob).text.toString())
            intent.putExtra("ADDRESS",findViewById<TextView>(R.id.address).text.toString())
            intent.putExtra("PIN",findViewById<TextView>(R.id.pin_code).text.toString())
            startActivity(intent)}


        val user = FirebaseAuth.getInstance().currentUser
        findViewById<Button>(R.id.sign_out_button).setOnClickListener{
            Firebase.auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }
        user?.let {

            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())

            // Initializing the image
            var image: Bitmap? = null

            executor.execute {
                val photoUrl = user.photoUrl
                try {
                    val `in` = java.net.URL(photoUrl.toString()).openStream()
                    image = BitmapFactory.decodeStream(`in`)

                    // Only for making changes in UI
                    handler.post {
                        findViewById<ImageView>(R.id.profile_photo).setImageBitmap(image)
                    }
                }

                // If the URL does not point to
                // image or any other kind of failure
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun readData() {
        database = FirebaseDatabase.getInstance().getReference("Users")
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child(currentUserId).get().addOnSuccessListener {
            val mobileNum = it.child("mobile").value
            val address = it.child("address").value
            val dob = it.child("dob").value
            val pin = it.child("pin").value
            val name = it.child("name").value
            val email = it.child("email").value
            findViewById<TextView>(R.id.mobile_number).text = mobileNum.toString()
            findViewById<TextView>(R.id.dob).text = dob.toString()
            findViewById<TextView>(R.id.address).text = address.toString()
            findViewById<TextView>(R.id.pin_code).text = pin.toString()
            findViewById<TextView>(R.id.name).text = name.toString()
            findViewById<TextView>(R.id.email).text = email.toString()
        }
    }


}
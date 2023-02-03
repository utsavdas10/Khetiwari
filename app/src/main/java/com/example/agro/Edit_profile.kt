package com.example.agro

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.concurrent.Executors

class Edit_profile : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    var db_phoneNumber: String? = null
    var db_dob: String? = null
    var db_address: String? = null
    var db_pinCode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val user = FirebaseAuth.getInstance().currentUser
        if (intent.hasExtra("MOBILE_NUMBER")) {
            db_phoneNumber = intent.getStringExtra("MOBILE_NUMBER")
            findViewById<EditText>(R.id.mobile).setText(db_phoneNumber)
        }
        if (intent.hasExtra("DOB")) {
            db_dob = intent.getStringExtra("DOB")
            findViewById<EditText>(R.id.dob).setText(db_dob)
        }
        if (intent.hasExtra("ADDRESS")) {
            db_address = intent.getStringExtra("ADDRESS")
            findViewById<EditText>(R.id.address).setText(db_address)
        }
        if (intent.hasExtra("PIN")) {
            db_pinCode = intent.getStringExtra("PIN")
            findViewById<EditText>(R.id.pin_code).setText(db_pinCode)
        }
        val fdatabase = FirebaseDatabase.getInstance().getReference("Users")
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        fdatabase.child(currentUserId).get().addOnSuccessListener {
            val name = it.child("name").value
            val email = it.child("email").value
            findViewById<TextView>(R.id.name).text = name.toString()
            findViewById<TextView>(R.id.email).text = email.toString()
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

        findViewById<Button>(R.id.saveBtn).setOnClickListener {
            val mobileNum = findViewById<EditText>(R.id.mobile).text.toString()
            val address = findViewById<EditText>(R.id.address).text.toString()
            val dob = findViewById<EditText>(R.id.dob).text.toString()
            val pin = findViewById<EditText>(R.id.pin_code).text.toString()
            updateData(mobileNum, dob, address, pin)
        }

    }

    private fun updateData(mobileNum: String, dob: String, address: String, pin: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")

        val user = mapOf<String, String>(
            "mobile" to mobileNum,
            "dob" to dob,
            "address" to address,
            "pin" to pin
        )
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child(currentUserId).updateChildren(user).addOnSuccessListener {

            Toast.makeText(this, "Successfuly Updated", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Account::class.java)

            startActivity(intent)


        }.addOnFailureListener {

            Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()

        }
    }
}
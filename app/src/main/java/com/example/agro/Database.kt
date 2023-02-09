package com.example.agro

import android.widget.TextView
import com.example.agro.data_classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Database {
    private lateinit var database: DatabaseReference
    var name: String = ""
    var email: String = ""
    var mobile : String = ""
    var dob : String = ""
    var address : String = ""
    var pin : String = ""
    constructor()
    constructor(email:String,name:String,mobile: String, dob: String, address: String, pin: String) {
        this.email = email
        this.name = name
        this.mobile = mobile
        this.dob = dob
        this.address = address
        this.pin = pin
    }

    fun uploadData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("Users")
        val User = User("${email}", "${name}", "${mobile}", "${dob}", "${address}", "${pin}")
        database.child(currentUserId).setValue(User)

        //Firestore database upload
        val db = Firebase.firestore.collection("Users").document(currentUserId)
        db.set(User)
    }

    fun readData() {
        database = FirebaseDatabase.getInstance().getReference("Users")
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child(currentUserId).get().addOnSuccessListener {
            mobile = it.child("mobile").value.toString()
            address = it.child("address").value.toString()
            dob = it.child("dob").value.toString()
            pin = it.child("pin").value.toString()
            name = it.child("name").value.toString()
            email = it.child("email").value.toString()
        }
        return
    }
}
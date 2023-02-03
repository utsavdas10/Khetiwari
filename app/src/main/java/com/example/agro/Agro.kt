package com.example.agro

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Agro : Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)
        val userRef = Firebase.database.getReference("Users")
        userRef.keepSynced(true)
    }
}
package com.example.agro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.agro.data_classes.ShopData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShopkeeperShopAdd : AppCompatActivity() {
    private var unit: String  = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopkeeper_shop_add)

        val user = FirebaseAuth.getInstance().currentUser
        val userName = user?.displayName.toString()
        val userId = user?.uid.toString()

        val Un = resources.getStringArray(R.array.Units)

        val spinnerLanguages = findViewById<Spinner>(R.id.spinner)
        if (spinnerLanguages != null){
            val adapter =
                ArrayAdapter(this,R.layout.drop,Un)
            adapter.setDropDownViewResource(R.layout.drop)
            spinnerLanguages.adapter=adapter
        }
        spinnerLanguages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                unit = Un[position].toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                unit = Un[0].toString()
            }
        }

        findViewById<Button>(R.id.popup_window_button).setOnClickListener {

            val product = findViewById<EditText>(R.id.editTextName).text.toString()
            val quantity = findViewById<EditText>(R.id.editTextQuantity).text.toString()
            val quantityInt = Integer.parseInt(quantity)
            val price = findViewById<EditText>(R.id.editTextPrice).text.toString()
            val priceInt = Integer.parseInt(price)

            val db = Firebase.firestore.collection("Users").document(userId)
                .collection("Selling_In_Shop").document(product)
            val shopData = ShopData(product, quantityInt, unit, priceInt, userName)
            db.set(shopData).addOnSuccessListener {
                Toast.makeText(this, "Item Added", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
            finish()
        }
    }
}
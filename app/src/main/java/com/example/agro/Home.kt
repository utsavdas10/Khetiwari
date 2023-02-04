package com.example.agro

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agro.data_classes.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Attributes.Name
import kotlin.math.roundToInt

class Home : AppCompatActivity() {
    val CITY: String = "Gandhinagar,IN"
    val API: String = "1472fe708df6d7d4cf2c5ff997a78262"
    var name: String? = ""
    private lateinit var database : DatabaseReference
    var userEmail: String? = ""
    var phoneNumber: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(currentUserId).get().addOnSuccessListener {

            if (!(it.exists())){
                uploadData()
            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            name = user.displayName
            userEmail = user.email.toString()
            if (intent.hasExtra("phoneNumber")) {
                phoneNumber = intent.getStringExtra("phoneNumber").toString()
            }
            if (intent.hasExtra("NAME")) {
                name = intent.getStringExtra("NAME").toString().trim()
            }
            if (intent.hasExtra("EMAIL")) {
                userEmail = intent.getStringExtra("EMAIL").toString()
            }
        }

        database.child(currentUserId).get().addOnSuccessListener {
            val name = it.child("name").value.toString()
            val arr = name.split(" ")
            val firstWord = arr[0]
            findViewById<TextView>(R.id.name1).text = "Hello, "+firstWord
        }


        findViewById<Switch>(R.id.switch1).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                startActivity(Intent(this,Home2::class.java ))
        }
        findViewById<Button>(R.id.buttomRightPetal).setOnClickListener {
            startActivity(Intent(this,Account::class.java))
        }
        findViewById<Button>(R.id.topRightPetal).setOnClickListener {
            startActivity(Intent(this,Shop::class.java))
        }
        findViewById<Button>(R.id.buttomLeftPetal).setOnClickListener {
            startActivity(Intent(this, Storage::class.java))
        }
        findViewById<Button>(R.id.topLeftPetal).setOnClickListener {
            startActivity(Intent(this, Mandi::class.java))
        }
        weatherTask().execute()
    }

    private fun uploadData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("Users")
        val User = User("${userEmail}","${name}","${phoneNumber}","","","")
        database.child(currentUserId).setValue(User)

        //Firestore database upload
        val user = FirebaseAuth.getInstance().currentUser
        val fdatabase = FirebaseDatabase.getInstance().getReference("Users")
        val rCurrentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        fdatabase.child(rCurrentUserId).get().addOnSuccessListener {
            val name = it.child("name").value
            val email = it.child("email").value
            val userId: String = user?.uid.toString()
            val users = UserData(name.toString(), email.toString())
            val db = Firebase.firestore.collection("Users").document(userId)
            db.set(users)
        }
    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText =
                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updatedAt * 1000)
                    )
                val temp = main.getString("temp").toDouble().roundToInt().toString() + "°C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")+" mbar"
                val humidity = main.getString("humidity")+"%"
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")+" km/hr"
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name") + ", " + sys.getString("country")
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.pressure).text= pressure
                findViewById<TextView>(R.id.location).text =  "\uD83D\uDCCDGadhinagar, IN"
                findViewById<TextView>(R.id.weather_type).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.humidity).text = humidity
                findViewById<TextView>(R.id.wind_speed).text = windSpeed

            }

            catch (e: Exception) {

            }
        }
    }
}
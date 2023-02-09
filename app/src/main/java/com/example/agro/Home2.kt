package com.example.agro

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.agro.data_classes.User
import com.example.agro.data_classes.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class Home2 : AppCompatActivity() {
    val CITY: String = "Kolkata,IN"
    val API: String = "1472fe708df6d7d4cf2c5ff997a78262"

    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)

        var name: String? = ""
        var userEmail: String? = ""
        var phoneNumber: String? = ""

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

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(currentUserId).get().addOnSuccessListener {

            if (!(it.exists())) {
                val obj = Database("${userEmail}", "${name}", "${phoneNumber}", "", "", "")
                obj.uploadData()
                Handler(Looper.myLooper()!!).postDelayed(Runnable {
                    database.child(currentUserId).get().addOnSuccessListener {
                        val name = it.child("name").value.toString()
                        val arr=name.split(" ")
                        findViewById<TextView>(R.id.name1).text = "Hello, "+arr[0]
                    }

                }, 200)
            }
            else{
                database.child(currentUserId).get().addOnSuccessListener {
                    val name = it.child("name").value.toString()
                    val arr = name.split(" ")
                    findViewById<TextView>(R.id.name1).text = "Hello, " + arr[0]
                }
            }
        }

        val db = Firebase.firestore.collection("Users").document(currentUserId)
        db.get().addOnSuccessListener {
            val name = it.data?.get("name")?.toString()
            val arr = name?.split(" ")
            val firstWord = arr?.get(0)
            findViewById<TextView>(R.id.name1).text = "Hello, " + firstWord
        }

        findViewById<Button>(R.id.topRightPetal).setOnClickListener {
            startActivity(Intent(this,Account::class.java))
        }
        findViewById<Button>(R.id.bottomPetal).setOnClickListener {
            startActivity(Intent(this,ShopkeeperStore::class.java))
        }
        findViewById<Button>(R.id.topLeftPetal).setOnClickListener {
            startActivity(Intent(this, ShopkeeperMandi::class.java))
        }
        weatherTask().execute()
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
                findViewById<TextView>(R.id.location).text =  "\uD83D\uDCCDKolkata, IN"
                findViewById<TextView>(R.id.weather_type).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.humidity).text = humidity
                findViewById<TextView>(R.id.wind_speed).text = windSpeed

            }

            catch (e: Exception) {

            }
        }
    }
}
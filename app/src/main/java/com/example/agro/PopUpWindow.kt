package com.example.agro

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.core.graphics.ColorUtils
import com.example.agro.data_classes.StorageData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_pop_up_window.*

class PopUpWindow : AppCompatActivity() {

    private var popupTitle = ""
    private var popupText1 = ""
    private var popupText2 = ""
    private var popupButton = ""
    private var darkStatusBar = false
    private var unit: String  = ""
    private var storedQuantity: String  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_pop_up_window)

        val Un = resources.getStringArray(R.array.Units)

        val spinnerLanguages = findViewById<Spinner>(R.id.spinner)
        if (spinnerLanguages != null){
        val adapter =
            ArrayAdapter(this,R.layout.drop,Un)
        adapter.setDropDownViewResource(R.layout.drop)
        spinnerLanguages.adapter=adapter
            }

        val bundle = intent.extras
        popupTitle = bundle?.getString("popuptitle", "Title") ?: ""
        popupButton = bundle?.getString("popupbtn", "Button") ?: ""
        darkStatusBar = bundle?.getBoolean("darkstatusbar", false) ?: false

        popup_window_title.text = popupTitle
        addBtn.text = popupButton

        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // If you want dark status bar, set darkStatusBar to true
                if (darkStatusBar) {
                    this.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                this.window.statusBarColor = Color.TRANSPARENT
                setWindowFlag(this, false)
            }
        }

        // Fade animation for the background of Popup Window
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popup_window_background.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()


        // Fade animation for the Popup Window
        popup_window_view_with_border.alpha = 0f
        popup_window_view_with_border.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        spinnerLanguages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                unit = Un[position].toString()
                }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                unit = Un[0].toString()
            }
        }
        // Close the Popup Window when you press the button
        addBtn.setOnClickListener {

            val product = findViewById<EditText>(R.id.editTextName).text.toString()
            val quantity = findViewById<EditText>(R.id.editTextQuantity).text.toString()
            val quantity1 = Integer.parseInt(quantity)

            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid.toString()
            val db = Firebase.firestore.collection("Users").document(userId)
                .collection("Storage").document(product)

//            db.get().addOnSuccessListener { document ->
//                if (document != null) {
//                    storedQuantity = document.data.toString()//{quantity = 2kg}
//                    storedQuantity.replace("{","")
//                    storedQuantity.replace("}","")
//                    storedQuantity.replace("=","")
//                    storedQuantity.replace("quantity","")
//                    storedQuantity.replace(unit,"")
//                    Toast.makeText(this, storedQuantity, Toast.LENGTH_LONG).show()
//                } else {
//                    Log.d(TAG, "No such document")
//                }
//            }

            val storage = StorageData(quantity1, unit)

            db.set(storage).addOnSuccessListener {
                Toast.makeText(this, "Item Added", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Unknown Error", Toast.LENGTH_LONG).show()
                }
            onBackPressed()
        }
    }

    private fun setWindowFlag(activity: Activity, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        win.attributes = winParams
    }


    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popup_window_background.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

        // Fade animation for the Popup Window when you press the back button
        popup_window_view_with_border.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }

}


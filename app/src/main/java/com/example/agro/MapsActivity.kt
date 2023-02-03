package com.example.agro

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.agro.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.random.Random

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var currentLocation : Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocationUser()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    private fun getCurrentLocationUser() {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
        !=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),permissionCode)
            return
        }
        val getLocation = fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            location ->
            if(location!=null){
                currentLocation = location

                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            permissionCode -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocationUser()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        googleMap?.addMarker(markerOptions)?.showInfoWindow()
        for (i in 1..6) {
            val lat = Random.nextDouble(0.0001, 0.01)
            val long = Random.nextDouble(0.0001, 0.01)
            val s = listOf(1,-1)
            val c=Random.nextInt(0,2)
            val c1=Random.nextInt(0,2)
            if(s[c]==1 && s[c1]==1) {
                val latLng1 =
                    LatLng(currentLocation.latitude.plus(lat), currentLocation.longitude.plus(long))
                val markerOptions1 = MarkerOptions().position(latLng1).title("Nearby Shops")
                googleMap?.addMarker(markerOptions1)
            }
            else if(s[c]==1 && s[c1]==-1) {
                val latLng1 =
                    LatLng(currentLocation.latitude.plus(lat), currentLocation.longitude.minus(long))
                val markerOptions1 = MarkerOptions().position(latLng1).title("Nearby Shops")
                googleMap?.addMarker(markerOptions1)
            }
            else if(s[c]==-1 && s[c1]==1) {
                val latLng1 =
                    LatLng(currentLocation.latitude.minus(lat), currentLocation.longitude.plus(long))
                val markerOptions1 = MarkerOptions().position(latLng1).title("Nearby Shops")
                googleMap?.addMarker(markerOptions1)
            }
            else if(s[c]==-1 && s[c1]==-1) {
                val latLng1 =
                    LatLng(currentLocation.latitude.minus(lat), currentLocation.longitude.minus(long))
                val markerOptions1 = MarkerOptions().position(latLng1).title("Nearby Shops")
                googleMap?.addMarker(markerOptions1)
            }
        }
    }
}
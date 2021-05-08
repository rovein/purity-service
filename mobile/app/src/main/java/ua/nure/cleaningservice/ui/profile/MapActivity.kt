package ua.nure.cleaningservice.ui.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.network.JsonPlaceHolderApi
import ua.nure.cleaningservice.network.NetworkService

class MapActivity : AppCompatActivity() {
    private var mMap: GoogleMap? = null
    private var mMapMarkerIcon: MapMarkerIcon? = null
    private var mApi: JsonPlaceHolderApi? = null
    private var mCurrentLocation: Location? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mapView: MapView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mMapMarkerIcon = MapMarkerIcon()
        mApi = NetworkService.getInstance().apiService
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLastLocation()
        mapView = findViewById(R.id.map)
    }

    private fun fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }
        val task = mFusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location: Location? ->
            if (location != null) {
                mCurrentLocation = location
                println(mCurrentLocation)
                mapView!!.getMapAsync { googleMap: GoogleMap? ->
                    mMap = googleMap
                    showLocation(location)
                }
            }
        }
    }

    private fun showLocation(location: Location) {
        val latLng = LatLng(location.latitude,
                location.longitude)
        val icon = mMapMarkerIcon!!.getCurrentLocationIcon(this)
        val markerOptions = MarkerOptions()
                .position(latLng)
                .title(getString(R.string.current_location))
                .icon(icon)
        mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        mMap!!.addMarker(markerOptions)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation()
            }
        }
    }

    private inner class MapMarkerIcon {
        private var currentLocationIcon: BitmapDescriptor? = null
        fun getCurrentLocationIcon(context: Context): BitmapDescriptor? {
            if (currentLocationIcon == null) {
                createCurrentLocationIcon(context)
            }
            return currentLocationIcon
        }

        private fun createCurrentLocationIcon(context: Context) {
            val background = ContextCompat.getDrawable(context, R.drawable.ic_my_location_background)
            background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
            val vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_my_location)
            vectorDrawable!!.setBounds(18, 18, vectorDrawable.intrinsicWidth + 18, vectorDrawable.intrinsicHeight + 18)
            val bitmap = Bitmap.createBitmap(background.intrinsicWidth, background.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            background.draw(canvas)
            vectorDrawable.draw(canvas)
            currentLocationIcon = BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    companion object {
        private const val REQUEST_CODE = 1
    }
}

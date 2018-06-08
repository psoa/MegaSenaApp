package br.com.psoa.megasena.megasenaapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import br.com.psoa.megasena.megasenaapp.R
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.android.synthetic.main.activity_lottery_location_maps.*
import java.text.DateFormat
import java.util.*


class LotteryLocationMapsActivity : AppCompatActivity(), OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private val INTERVAL = (1000 * 10).toLong()
    private val FASTEST_INTERVAL = (1000 * 5).toLong()
    var mCurrentLocation: Location? = null
    var mLastUpdateTime: String? = null
    lateinit var mLocationRequest: LocationRequest
    private lateinit var mMap: GoogleMap
    private lateinit var mGoogleApiClient: GoogleApiClient
    val REQUEST_GPS = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottery_location_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.locationMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        createLocationRequest()
        btCloseMap.setOnClickListener { onCloseClicked() }
    }

    private fun onCloseClicked() {
        val intent = Intent(this@LotteryLocationMapsActivity,
                MenuActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
        finish()
        finish()
    }

    override fun onLocationChanged(location: Location?) {
        mCurrentLocation = location
        mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
        updateUI()
    }

    private fun updateUI() {
        if (null != mCurrentLocation) {
            val lat = mCurrentLocation?.latitude.toString()
            val lng = mCurrentLocation?.longitude.toString()
        }
    }

    override fun onConnected(p0: Bundle?) {
        checkPermission()
        if (mCurrentLocation != null) {
            addMarker(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude, "You are here")
        }
    }

    private fun checkPermission() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                val builder = AlertDialog.Builder(this)

                builder.setMessage("Mega Sena needs to access your location")
                        .setTitle("Permission request")

                builder.setPositiveButton("OK") { dialog, id ->
                    //Log.i(FragmentActivity.TAG, "Clicked")
                    requestPermission()
                }

                val dialog = builder.create()
                dialog.show()

            } else {
                requestPermission()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            REQUEST_GPS -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish()
                }
                return
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_GPS)
    }

    @Synchronized
    private fun callConnection() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API).build()
        mGoogleApiClient.connect()
        mvpLocation ()
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = INTERVAL
        mLocationRequest.fastestInterval = FASTEST_INTERVAL
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun addMarker(latitude: Double, longitude: Double, title: String) {
        val sydney = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f))

    }

    fun mvpLocation () {
        val lastLotteryLocation = LatLng(-26.759707, -53.1729115)
        mMap.addMarker(MarkerOptions().position(lastLotteryLocation).title("Espaço Criança Sorriso"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLotteryLocation, 15.toFloat()))
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        callConnection()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented")
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented")
    }

}
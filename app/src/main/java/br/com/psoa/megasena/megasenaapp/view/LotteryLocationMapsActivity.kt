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

/**
 * Show the location of the last lottery, in this MVP show where lottery 2038 took place
 *
 */
//FIXME: Replace for a fragment. A lot of exceptions and a strong deadline made this activity
class LotteryLocationMapsActivity : AppCompatActivity(), OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private val _interval = (1000 * 10).toLong()
    private val _fastedInterval = (1000 * 5).toLong()
    private var _currentLocation: Location? = null
    private var _lastUpdateTime: String? = null
    private lateinit var _locationRequest: LocationRequest
    private lateinit var _maps: GoogleMap
    private lateinit var _googleApiClient: GoogleApiClient
    private val _requestGPS = 0

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
        _currentLocation = location
        _lastUpdateTime = DateFormat.getTimeInstance().format(Date())
        //updateUI()
    }

//    private fun updateUI() {
//        if (null != _currentLocation) {
//            val lat = _currentLocation?.latitude.toString()
//            val lng = _currentLocation?.longitude.toString()
//        }
//    }

    override fun onConnected(p0: Bundle?) {
        checkPermission()
        if (_currentLocation != null) {
            addMarker(_currentLocation!!.latitude, _currentLocation!!.longitude,
                    getString(R.string.you_are_here))
        }
    }

    private fun checkPermission() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                val builder = AlertDialog.Builder(this)

                builder.setMessage(getString(R.string.access_location_permission))
                        .setTitle(R.string.access_location_permission_title)

                builder.setPositiveButton("OK") { _, _ -> requestPermission()
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
            _requestGPS -> {
                if (grantResults.isEmpty()
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
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
                _requestGPS)
    }

    @Synchronized
    private fun callConnection() {
        _googleApiClient = GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API).build()
        _googleApiClient.connect()
        mvpLocation ()
    }

    private fun createLocationRequest() {
        _locationRequest = LocationRequest()
        _locationRequest.interval = _interval
        _locationRequest.fastestInterval = _fastedInterval
        _locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun addMarker(latitude: Double, longitude: Double, title: String) {
        val sydney = LatLng(latitude, longitude)
        _maps.addMarker(MarkerOptions().position(sydney).title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)))
        _maps.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        _maps.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f))

    }


    /**
     * Just mock a fixed location of the lottery 2038 (but it really happens there :) )
     *
     */
    private fun mvpLocation () {
        val lastLotteryLocation = LatLng(-26.759707, -53.1729115)
        _maps.addMarker(MarkerOptions().position(lastLotteryLocation).title("Espaço Criança Sorriso"))
        _maps.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLotteryLocation, 15.toFloat()))
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        _maps = googleMap
        callConnection()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented")
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented")
    }

}
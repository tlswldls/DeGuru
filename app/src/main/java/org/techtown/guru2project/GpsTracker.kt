package org.techtown.guru2project

import android.app.Service
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager

import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationListener
import java.util.jar.Manifest


class GpsTracker: android.location.LocationListener {
    private var mContext: Context? = null
    var location= null
    var getLatitude: Double = 0.0
    var getLongitude: Double = 0.0

    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
    private val MIN_TIME_BW_UPDATES = 1000 * 60 * 1.toLong()
    protected var locationManager: LocationManager? = null


    fun GpsTracker(context: Context?) {
        mContext = context
        getLocation()
    }


    fun getLocation(): Location? {
        try {
            locationManager =
                mContext.getSystemService(LOCATION_SERVICE)
            val isGPSEnabled =
                locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
                ) {
                } else return null
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this
                    )
                    if (locationManager != null) {
                        location =
                            locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            latitude = location.getLatitude()
                            longitude = location.getLongitude()
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this
                        )
                        if (locationManager != null) {
                            location =
                                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location.getLatitude()
                                longitude = location.getLongitude()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("@@@", "" + e.toString())
        }
        return location
    }

    fun getLatitude(): Double {
        if (location != null) {
            latitude = location.getLatitude()
        }
        return latitude
    }

    fun getLongitude(): Double {
        if (location != null) {
            longitude = location.getLongitude()
        }
        return longitude
    }

    fun onLocationChanged(location: Location?) {}

    fun onProviderDisabled(provider: String?) {}

    fun onProviderEnabled(provider: String?) {}

    fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    fun onBind(arg0: Intent?): IBinder? {
        return null
    }


    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(this@GpsTracker)
        }
    }
}
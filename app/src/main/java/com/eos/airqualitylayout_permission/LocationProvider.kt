package com.eos.airqualitylayout_permission

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.ContextCompat

class LocationProvider (private val context: Context): LocationListener{
    //위치, 위치 매니저
    private var location : Location? = null     //처음에 null일 수 있기 때문에 null로 초기화해주는 것임
    private var locationManager : LocationManager? = null

    init{
        //초기화 시 위치 받아오기
        location = getLocation()
    }

    //creating function
    //@SuppressLint("SuspiciousIndentation")
    private fun getLocation(): Location? {
        try {
            //위치 정보 시스템 서비스를 가져오기
            locationManager = context.getSystemService(
                Context.LOCATION_SERVICE) as LocationManager
            //permission
            var gpsLocation : Location? = null
            var networkLocation: Location? = null

            //provider 두개가 활성화되어 있는지 확인
            val isGPSEnabled: Boolean = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            //!! -> null이 무조건 아니라는 것을 뜻함
            val isNetworkEnabled : Boolean = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if(!isGPSEnabled && !isNetworkEnabled){
                // gps, netword provider are not possible
                return null
            }
            else{
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                val hasCoarseLocationPerimission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)

                if(hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPerimission != PackageManager.PERMISSION_GRANTED){
                    return null
                    }
                if(isNetworkEnabled){
                    networkLocation = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
                //gps 통한 위치 파악 가능할 때
                if(isGPSEnabled){
                    gpsLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
                return if(gpsLocation != null && networkLocation != null){
                        if(gpsLocation.accuracy > networkLocation.accuracy){
                            location = gpsLocation
                            gpsLocation
                        }
                    else{
                        location = gpsLocation
                            gpsLocation
                    }
                }
                else{
                    //gpsLocation ?: networkLocation
                    gpsLocation
                    //gpsLocation ? return gpsLocation : return networkLocation
                }

                }
            }
        catch(e: Exception){
            e.printStackTrace()}
        return location
        }
    //TODO: 함수 만들기
    //위치 정보를 가져오는 함수
    fun getLocationLatitude(): Double{
        return location?.latitude ?: 0.0
    }
    fun getLocationLongitude(): Double {
        return location?.longitude ?: 0.0
    }

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
    }
}
package com.khs.nbbang.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Base64
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kakao.util.maps.helper.Utility
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object KakaoUtils {
    private val TAG_CLASS: String = javaClass.simpleName
    private val LOG_TAG = LogUtil.TAG_DEFAULT

    fun getKeyHash(context: Context?): String? {
        val packageInfo: PackageInfo =
            Utility.getPackageInfo(context, PackageManager.GET_SIGNATURES)
                ?: return null
        for (signature in packageInfo.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {
                LogUtil.eLog(LOG_TAG, TAG_CLASS, "디버그 keyHash$signature")
            }
        }
        return null
    }


    val PERMISSIONS_REQUEST_CODE = 100
    var REQUIRED_PERMISSIONS = arrayOf<String>( Manifest.permission.ACCESS_FINE_LOCATION)

    private fun checkPermission(context: Context, activity: Activity) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            val lm: LocationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {
                val userNowLocation: Location? =
                    lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                val uLatitude = userNowLocation!!.latitude
                val uLongitude = userNowLocation!!.longitude
            } catch (e: NullPointerException) {
                LogUtil.eLog(LOG_TAG, TAG_CLASS, "error : ${e.message}")
            }
        } else {
            Toast.makeText(context, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(
                activity,
                REQUIRED_PERMISSIONS,
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

}
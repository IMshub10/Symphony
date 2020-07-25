package com.example.symphony.Helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object Utils {
    fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    fun checkPermission(
        activity: Activity?,
        permissionString: String,
        permissionCode: Int
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val existingPermissionStatus = ContextCompat.checkSelfPermission(
            activity!!,
            permissionString
        )
        if (existingPermissionStatus == PackageManager.PERMISSION_GRANTED) return
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permissionString),
            permissionCode
        )
    }

    fun checkPermission(
        fragment: Fragment,
        permissionString: String,
        permissionCode: Int
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || fragment.context == null) return
        val existingPermissionStatus = ContextCompat.checkSelfPermission(
            fragment.context!!,
            permissionString
        )
        if (existingPermissionStatus == PackageManager.PERMISSION_GRANTED) return
        fragment.requestPermissions(arrayOf(permissionString), permissionCode)
    }

    fun isReadStorageGranted(context: Context?): Boolean {
        val storagePermissionGranted = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return storagePermissionGranted == PackageManager.PERMISSION_GRANTED
    }

    fun isWriteStorageGranted(context: Context?): Boolean {
        val storagePermissionGranted = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return storagePermissionGranted == PackageManager.PERMISSION_GRANTED
    }

    fun isCameraGranted(context: Context?): Boolean {
        val cameraPermissionGranted = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.CAMERA
        )
        return cameraPermissionGranted == PackageManager.PERMISSION_GRANTED
    }
}
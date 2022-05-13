package com.telemedicine.indihealth.helper

import android.content.Context
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber


object AppPermission {
    var onAllPermissionGranted: (() -> Unit)? = null
    var onAllPermissionPermanentlyDenied: (() -> Unit)? = null
    var onPermissionDenied: (() -> Unit)? = null
    fun permission(context: Context) {
        Dexter.withContext(context)
            .withPermissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.INTERNET
            )
            .withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            onAllPermissionGranted?.invoke()
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            onAllPermissionPermanentlyDenied?.invoke()
                        }
                        if (report.deniedPermissionResponses.size >= 1) {
                            onPermissionDenied?.invoke()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                })
            .withErrorListener { p0 -> Timber.e("Dexter Error = $p0") }.check()
    }



}
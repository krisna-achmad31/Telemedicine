package com.telemedicine.indihealth.utility

import android.app.Activity
import android.app.AlertDialog
import com.telemedicine.indihealth.R
import kotlin.system.exitProcess

fun Activity.checkDeviceRoot(): Boolean {
    return if (RootUtil.isDeviceRooted) {
        AlertDialog.Builder(this)
            .setMessage("Your device is rooted. you can not use this app into rooted device.")
            .setCancelable(false)
            .setPositiveButton(R.string.alert_ok) { _, _ ->
                exitProcess(0)
            }.show()
        true
    } else {
        false
    }
}
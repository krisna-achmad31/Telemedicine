package com.telemedicine.indihealth.helper

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.telemedicine.indihealth.R

object AppSnackbar {

    fun set(string: String, view: View) {
        Snackbar.make(view, string, Snackbar.LENGTH_SHORT).show()
    }

    fun setError(string: String, view: View) {
        Snackbar
            .make(view, string, Snackbar.LENGTH_SHORT).setBackgroundTint(
                ContextCompat.getColor(
                    view.context,
                    R.color.amber_800
                )
            )
            .show()
    }

}
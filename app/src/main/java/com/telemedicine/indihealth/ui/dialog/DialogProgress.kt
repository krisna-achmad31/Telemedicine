package com.telemedicine.indihealth.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.telemedicine.indihealth.R


class DialogProgress{

    var dialog: CustomDialog?= null

    fun show(context: Context): Dialog? {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.dialog_progress_app, null)

        dialog = CustomDialog(context).apply {
            setContentView(view)
            setCancelable(false)
            show()
        }
        return dialog
    }

    class CustomDialog(context: Context) : Dialog(context) {
        init {
            // Set Semi-Transparent Color for Dialog Background
            window?.decorView?.rootView?.setBackgroundResource(R.color.colorTransparent)
        }
    }
}
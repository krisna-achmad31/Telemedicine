package com.telemedicine.indihealth.base

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import com.telemedicine.indihealth.helper.Event
import com.telemedicine.indihealth.ui.dialog.DialogProgress
import timber.log.Timber

abstract class BaseFragment : Fragment() {

    private val dialog: DialogProgress? by lazy {
        DialogProgress()
    }

    fun loadingValidation(it: Event<Boolean>, context: Context) {
        Timber.d("it = ${it.peekContent()} dialog = $dialog")
        it.getContentIfNotHandled()?.apply {
            when (this) {
                true -> {
                    dialog!!.show(context)
                }
                else -> {
                    if (dialog!!.dialog != null) {
                        dialog!!.dialog!!.dismiss()
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (dialog!!.dialog != null) {
                            dialog!!.dialog!!.dismiss()
                        }
                    },1000)
                }
            }
        }
    }


    fun loadingValidation(it: Event<Boolean>, context: Context, view: View) {
        Timber.d("it = ${it.peekContent()} dialog = $dialog")
        it.getContentIfNotHandled()?.apply {
            when (this) {
                true -> {
                    dialog!!.show(context)
                    view.visibility = GONE
                }
                else -> {
                    if (dialog!!.dialog != null) {
                        dialog!!.dialog!!.dismiss()
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (dialog!!.dialog != null) {
                            dialog!!.dialog!!.dismiss()
                        }
                    },1000)

                    view.visibility = VISIBLE
                }
            }
        }
    }


//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (dialog!!.dialog != null) {
//            dialog!!.dialog!!.dismiss()
//        }
//        Handler(Looper.getMainLooper()).postDelayed({
//            if (dialog!!.dialog != null) {
//                dialog!!.dialog!!.dismiss()
//            }
//        },1000)
//    }
}
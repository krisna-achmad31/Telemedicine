package com.telemedicine.indihealth

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp
class TelemedicineApp : Application(){
    override fun onCreate() {
        super.onCreate()
        initTimber()
    }



    private fun initTimber(){
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
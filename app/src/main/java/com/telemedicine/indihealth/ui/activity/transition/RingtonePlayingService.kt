package com.telemedicine.indihealth.ui.activity.transition

import android.app.Service
import android.app.Service.START_NOT_STICKY
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder


class RingtonePlayingService : Service() {
    private var ringtone: Ringtone? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(applicationContext, notification);
        ringtone!!.play()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        ringtone!!.stop()
    }
}
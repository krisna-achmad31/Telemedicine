package com.telemedicine.indihealth.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.facebook.react.bridge.UiThreadUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import com.telemedicine.indihealth.ui.activity.HomeActivity
import com.telemedicine.indihealth.ui.activity.MainActivity
import com.telemedicine.indihealth.ui.activity.doctor.ConsultationCallFragment
import com.telemedicine.indihealth.ui.activity.transition.RingtonePlayingService
import com.telemedicine.indihealth.ui.activity.transition.TransitionActivity
import com.telemedicine.indihealth.ui.fragment.doctor.consultation.schedule.ConsultationScheduleDoctorFragment
import org.json.JSONObject
import timber.log.Timber


class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        const val CALL_NOTIF = "CALL_NOTIF"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val body = remoteMessage.data["body"]
        Timber.d("NOTIF = ${remoteMessage.notification}")
        Timber.d("data =${remoteMessage.data}")
        Timber.d("getUser = ${getUser()}")
        Timber.d("from = ${remoteMessage.from}")
        if (remoteMessage.notification != null ){
            Timber.d("notif = ${remoteMessage.notification}")
        } else {
            Timber.d("GAK ADA APAPA")
        }
        body?.let {
            val jsonObject = JSONObject(it)
            try {
                when (jsonObject.getString("name")) {
                    "panggilan_konsultasi_pasien" -> {
                        Timber.d("jsonGetString iduser = ${jsonObject.get("id_user")} with [\"${getUser()?.id}\"]")
                        if ("[\"${getUser()?.id}\"]" == jsonObject.get("id_user")) {
                            Timber.d("isExist? ${getSharedPreference()?.getBoolean(AppVar.IS_CALL_NOTIFICATION_EXIST)}")
                            if (!getSharedPreference()?.getBoolean(AppVar.IS_CALL_NOTIFICATION_EXIST)!!) {
                                onDoctorCalled(jsonObject)
                                showCallNotification(jsonObject)
                                Timber.d("SEKARANG LAGI DISINI")
                            } else {
//                                getSharedPreference()?.clearCallNotif()
                            }
                        } else {
                            Timber.d(getUser().toString())
                        }
                    }
                    "pengiriman" -> {
                        onShipmenSent(jsonObject)
                    }
                    "universal" -> {
                        universalNotif(jsonObject)
                    }
                    "vp" -> {
                        showPaymentVerificationNotification(jsonObject)
                    }
                    "reject_by_dokter" -> {
                        val notificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.cancelAll()

                        onCallReject(jsonObject)

                        val keterangan = jsonObject.getString("keterangan")
                        val i = Intent(CALL_NOTIF)
                        i.putExtra(CALL_NOTIF, jsonObject.getString("name"))
                        i.putExtra("keterangan", keterangan)
                        LocalBroadcastManager.getInstance(this).sendBroadcast(i)
                    }
                    "panggilan_farmasi_pasien", "panggilan_farmasi_dokter" -> {
                        Timber.d("jsonGetString iduser = ${jsonObject.get("id_user")} with [\"${getUser()?.id}\"]")
                        if ("[\"${getUser()?.id}\"]" == jsonObject.get("id_user")) {
                            Timber.d("isExist? ${getSharedPreference()?.getBoolean(AppVar.IS_CALL_NOTIFICATION_EXIST)}")
                            if (!getSharedPreference()?.getBoolean(AppVar.IS_CALL_NOTIFICATION_EXIST)!!) {
                                onPharmacyCalled(jsonObject)
                            } else {

                            }
                        } else {
                            Timber.d(getUser().toString())
                        }
                    }
                    "akhiri_panggilan_farmasi_pasien" -> {

                    }
                    else -> {
                        val keterangan = jsonObject.getString("keterangan")
                        val i = Intent(CALL_NOTIF)
                        i.putExtra(CALL_NOTIF, jsonObject.getString("name"))
                        i.putExtra("keterangan", keterangan)
                        LocalBroadcastManager.getInstance(this).sendBroadcast(i)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun onDoctorCalledEnd() {
        if (SharedPreferenceApp(
                this.getSharedPreferences("telemedicine", 0),
                Gson()
            ).getBoolean(AppVar.IS_ROOM_CREATED)!!
        ) {
            UiThreadUtil.runOnUiThread {
                ConsultationCallFragment().onBackPressed()
                Toast.makeText(
                    this,
                    "Konsultasi telah diakhiri oleh dokter",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun onPharmacyCalled(jsonObject: JSONObject) {
        val roomName = jsonObject.getString("room_name")
        val keterangan = jsonObject.getString(TransitionActivity.MESSAGE)
        val idFarmasi = jsonObject.getString(TransitionActivity.ID_PHARMACY)

        val intent = Intent(this, TransitionActivity::class.java).apply {
            putExtra("roomName", roomName)
            putExtra("keterangan", keterangan)
            putExtra("id_farmasi", idFarmasi)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun onDoctorCalled(jsonObject: JSONObject) {
        val roomName = jsonObject.getString(TransitionActivity.ROOM_NAME)
        val keterangan = jsonObject.getString(TransitionActivity.MESSAGE)
        val idDokter = jsonObject.getString(TransitionActivity.ID_DOCTOR)
        val idKonsultasi = jsonObject.getString(TransitionActivity.ID_JADWAL_KONSULTASI)

        val intent = Intent(this, TransitionActivity::class.java).apply {
            putExtra("roomName", roomName)
            putExtra("keterangan", keterangan)
            putExtra("id_dokter", idDokter)
            putExtra("id_jadwal_konsultasi", idKonsultasi)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun showCallNotification(jsonObject: JSONObject) {
        Timber.d("LAGI DISNI")
        val roomName = jsonObject.getString(TransitionActivity.ROOM_NAME)
        val keterangan = jsonObject.getString(TransitionActivity.MESSAGE)
        val idDokter = jsonObject.getString(TransitionActivity.ID_DOCTOR)
        val idKonsultasi = jsonObject.getString(TransitionActivity.ID_JADWAL_KONSULTASI)

        val intent = Intent(this, TransitionActivity::class.java).apply {
            putExtra("roomName", roomName)
            putExtra("keterangan", keterangan)
            putExtra("id_dokter", idDokter)
            putExtra("id_jadwal_konsultasi", idKonsultasi)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            //addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

//        val pendingIntent = PendingIntent.getActivity(
//            this, 0 /* Request code */, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )

        val channelId = getString(R.string.call_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_call_in)
            .setContentTitle("Panggilan Telekonsultasi Masuk")
            .setContentText(keterangan)
            .setSound(defaultSoundUri)
            .setAutoCancel(false)
            .setPriority(2)

        //notificationBuilder.setFullScreenIntent(pendingIntent, true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Panggilan masuk",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun showPaymentVerificationNotification(jsonObject: JSONObject) {
        val title = "Pembayaran diverifikasi"
        val message = jsonObject.getString("keterangan")
        val intent = Intent(this, MainActivity::class.java)

        intent.putExtra("toOpen", "assesment")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder : NotificationCompat.Builder
        when (getUser()?.role) {
            "pasien" -> {
                notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    //.setContentIntent(ConsultationAssessmentFragment.pendingIntent(baseContext))
                    .setContentIntent(pendingIntent)
            }
            "dokter" -> {

                notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(ConsultationScheduleDoctorFragment.pendingIntent(baseContext))
            }
            else -> {
                notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
            }
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notifikasi",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun onCallReject(jsonObject: JSONObject){
        val title = "Panggilan Telekonsultasi Tak Terjawab"
        val message = jsonObject.getString("keterangan")
        val intent = Intent(this, MainActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder : NotificationCompat.Builder
        when (getUser()?.role) {
            "pasien" -> {
                notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_round_call_missed_24)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    //.setContentIntent(ConsultationAssessmentFragment.pendingIntent(baseContext))
                    .setContentIntent(pendingIntent)
            }
            else -> {
                notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_round_call_missed_24)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
            }
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notifikasi",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun onShipmenSent(jsonObject: JSONObject) {
        val title = "Paket Dikirim"
        val message = jsonObject.getString("keterangan")
        val intent = Intent(this, MainActivity::class.java)

        intent.putExtra("toOpen", "assesment")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder : NotificationCompat.Builder
        when (getUser()?.role) {
            "pasien" -> {
                notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_round_local_shipping_24)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    //.setContentIntent(ConsultationAssessmentFragment.pendingIntent(baseContext))
                    .setContentIntent(pendingIntent)
            }
            else -> {
                notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_round_local_shipping_24)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
            }
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notifikasi",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun universalNotif(jsonObject: JSONObject) {
        val title = jsonObject.getString("judul")
        val message = jsonObject.getString("keterangan")
        val intent = Intent(this, MainActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder : NotificationCompat.Builder
        when (getUser()?.role) {
            "pasien" -> {
                notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    //.setContentIntent(ConsultationAssessmentFragment.pendingIntent(baseContext))
                    .setContentIntent(pendingIntent)
            }
            else -> {
                notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
            }
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notifikasi",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        uploadToken(token)
        Timber.d("token $token")
    }

    private fun uploadToken(token: String) {
        Timber.d("User = ${getUser()}")
        try {
            Timber.d(token)
            val id = getUser()?.id
            val lg = AppVar.BASE_URL
            AndroidNetworking.post(lg + "User/updateToken")
                .addBodyParameter("token", token)
                .addBodyParameter("id_user", id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Timber.d("response = $response")
                    }

                    override fun onError(anError: ANError?) {
                        anError?.printStackTrace()
                        Timber.d("error = $anError")
                    }
                })
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getUser(): User? {
        return SharedPreferenceApp(
            this.getSharedPreferences("telemedicine", 0),
            Gson()
        ).getUserValue()
    }

    private fun getSharedPreference(): SharedPreferenceApp? {
        return SharedPreferenceApp(
            this.getSharedPreferences("telemedicine", 0),
            Gson()
        )
    }


}
package com.telemedicine.indihealth.ui.activity.transition

//import com.telemedicine.indihealth.databinding.LayoutTransitionBinding

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.service.MyFirebaseMessagingService
import com.telemedicine.indihealth.ui.activity.HomeActivity
import com.telemedicine.indihealth.ui.activity.patient.ConsultationCallPatientFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_transition.*


@AndroidEntryPoint
class TransitionActivity : AppCompatActivity() {

    companion object {
        const val ROOM_NAME = "roomName"
        const val MESSAGE = "keterangan"
        const val ID_DOCTOR = "id_dokter"
        const val ID_JADWAL_KONSULTASI = "id_jadwal_konsultasi"
        const val NAME_DOCTOR = "doctor_name"
        const val ID_PHARMACY = "id_farmasi"
    }

    //private val binding: LayoutTransitionBinding by binding(R.layout.layout_transition)
    private val viewModel: TransitionViewModel by viewModels()
    fun Intent(context: TransitionViewModel, java: Class<HomeActivity>): Intent? {
        TODO("Not yet implemented")
    }

    private var ringtone: Ringtone? = null
    private var v :Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_transition)

        viewModel.roomName = ROOM_NAME.getExtra()
        viewModel.message = MESSAGE.getExtra()
        viewModel.idDokter = ID_DOCTOR.getExtra()
        viewModel.idKonsultasi = ID_JADWAL_KONSULTASI.getExtra()
        viewModel.idFarmasi = ID_PHARMACY.getExtra()
        //var message: String? = ""

        setOnClickListener()
        setObservableValue()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver, IntentFilter(
                MyFirebaseMessagingService.CALL_NOTIF
            )
        )

        val tv_message: TextView = findViewById(R.id.transition_tv_message)
        tv_message.text = viewModel.message

        window.addFlags(//WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }

//        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
//        val mp: MediaPlayer = MediaPlayer.create(applicationContext, alarmSound)
//        mp.start()

        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(applicationContext, notification);
        ringtone!!.play()

        v = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(0, 1000, 1000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v!!.vibrate(VibrationEffect.createWaveform(pattern, 0))
        } else {
            //deprecated in API 26
            //v!!.vibrate(pattern, 0)
        }
    }


    override fun onResume() {
        super.onResume()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        viewModel.setRoomIsCalled()
    }

    private fun setOnClickListener() {
        transition_iv_cancel.setOnClickListener {
            viewModel.postRejectCall()
            viewModel.setRoomIsDestroyed()
            ringtone!!.stop()
            v!!.cancel()
        }
        transition_iv_call.setOnClickListener {
            viewModel.postAnswerCall()
            ringtone!!.stop()
            v!!.cancel()
        }
    }

    private fun setObservableValue() {
        viewModel.apply {
            rejectCallStatus.observe(this@TransitionActivity, {
                it.getContentIfNotHandled()?.apply {
//                    when (this) {
//                        true -> finish()
//                        false -> {
//                            "Terjadi kesalahan, coba beberapa saat lagi".toast()
//                            finish()
//                        }
//                    }
                    ringtone!!.stop()
                    v!!.cancel()
                    finish()
                }
            })
            answerCallStatus.observe(this@TransitionActivity, {
                it.getContentIfNotHandled()?.apply {
                    if (viewModel.idFarmasi != null) {
                        ConsultationCallPatientFragment.startActivityPharmacy(
                            this@TransitionActivity,
                            viewModel.getUserValue()?.name,
                            viewModel.roomName,
                            viewModel.idFarmasi,
                            viewModel.getUserValue()?.id
                        )
                    } else {
                        ConsultationCallPatientFragment.startActivity(
                            this@TransitionActivity,
                            viewModel.getUserValue()?.name,
                            viewModel.roomName,
                            viewModel.idDokter,
                            viewModel.getUserValue()?.id,
                            viewModel.message?.substringAfter("Dokter"),
                            viewModel.idKonsultasi
                        )
                    }

                    ringtone!!.stop()
                    v!!.cancel()
                    finish()
//                    when (this) {
//                        true -> ConsultationCallPatientFragment.startActivity(
//                            this@TransitionActivity,
//                            viewModel.user?.name,
//                            viewModel.roomName
//                        )
//                        false -> {
//                            "Terjadi kesalahan, coba beberapa saat lagi".toast()
//                        }
//                    }
                }
            })
        }
    }

    private fun String.getExtra(): String? {
        return intent.getStringExtra(this)
    }

    private fun String.toast() {
        Toast.makeText(this@TransitionActivity, this, Toast.LENGTH_SHORT).show()
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, i: Intent) {
            val filter: String = MyFirebaseMessagingService.CALL_NOTIF
            if (i.action == filter) {
                val name = i.getStringExtra(filter)
                val keterangan = i.getStringExtra("keterangan")
                if (name == "reject_by_dokter") {
                    keterangan?.toast()
                    ringtone!!.stop()
                    finish()
                    v!!.cancel()
//                    val intent = Intent(this@TransitionActivity, MainActivity::class.java).apply {
//                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    }
//                    startActivity(intent)

                    viewModel.setRoomIsDestroyed()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setRoomIsDestroyed()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
        ringtone!!.stop()
        v!!.cancel()
    }


}
package com.telemedicine.indihealth

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.telemedicine.indihealth.service.MyFirebaseMessagingService
import com.telemedicine.indihealth.ui.activity.transition.TransitionActivity
import com.telemedicine.indihealth.ui.activity.transition.TransitionViewModel
import kotlinx.android.synthetic.main.layout_transition.*

class CallActivity : AppCompatActivity() {

    companion object {
        const val ROOM_NAME = "roomName"
        const val MESSAGE = "keterangan"
        const val ID_DOCTOR = "id_dokter"
        const val ID_JADWAL_KONSULTASI = "id_jadwal_konsultasi"
        const val NAME_DOCTOR = "doctor_name"
    }

    private val viewModel: TransitionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)


    }

}
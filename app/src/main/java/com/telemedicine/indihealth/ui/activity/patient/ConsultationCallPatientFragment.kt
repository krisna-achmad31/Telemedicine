package com.telemedicine.indihealth.ui.activity.patient

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.facebook.react.modules.core.PermissionListener
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragmentActivity
import com.telemedicine.indihealth.databinding.LayoutConsultationCallVideoPatientBinding
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.service.MyFirebaseMessagingService
import com.telemedicine.indihealth.ui.activity.MainActivity
import com.telemedicine.indihealth.ui.activity.transition.TransitionViewModel
import com.telemedicine.indihealth.ui.bottomsheet.ConsultationAssessmentBottomSheet
import com.telemedicine.indihealth.ui.bottomsheet.chat.ConsultationChatBottomSheet
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_consultation_call_video.*
import kotlinx.android.synthetic.main.layout_consultation_call_video_patient.*
import kotlinx.android.synthetic.main.layout_consultation_call_video_patient.view.*
import org.jitsi.meet.sdk.*
import timber.log.Timber
import java.net.URL

@AndroidEntryPoint
class ConsultationCallPatientFragment : BaseFragmentActivity(), JitsiMeetActivityInterface{

    companion object {
        private const val EXTRA = "EXTRA"
        const val USER_INFO = "USER_INFO"
        const val ROOM_NAME = "ROOM_NAME"
        const val ID_DOCTOR = "ID_DOCTOR"
        const val ID_USER = "ID_USER"
        const val NAME_DOCTOR = "NAME_DOCTOR"
        const val ID_JADWAL_KONSULTASI = "ID_JADWAL_KONSULTASI"
        const val ID_PHARMACY = "ID_PHARMACY"

        fun startActivity(context: Context, userInfo: String?, roomName: String?) {
            val intent = Intent(context, ConsultationCallPatientFragment::class.java)
            intent.putExtra(USER_INFO, userInfo)
            intent.putExtra(ROOM_NAME, roomName)
            context.startActivity(intent)
        }

        fun startActivity(
            context: Context,
            userInfo: String?,
            roomName: String?,
            idDokter: String?,
            idPasien: String?
        ) {
            val intent = Intent(context, ConsultationCallPatientFragment::class.java)
            intent.putExtra(USER_INFO, userInfo)
            intent.putExtra(ROOM_NAME, roomName)
            intent.putExtra(ID_DOCTOR, idDokter)
            intent.putExtra(ID_USER, idPasien)
            context.startActivity(intent)
        }

        fun startActivity(
            context: Context,
            userInfo: String?,
            roomName: String?,
            idDokter: String?,
            idPasien: String?,
            nameDoctor: String?,
            idKonsultasi: String?
        ) {
            val intent = Intent(context, ConsultationCallPatientFragment::class.java)
            intent.putExtra(USER_INFO, userInfo)
            intent.putExtra(ROOM_NAME, roomName)
            intent.putExtra(ID_DOCTOR, idDokter)
            intent.putExtra(ID_USER, idPasien)
            intent.putExtra(NAME_DOCTOR, nameDoctor)
            intent.putExtra(ID_JADWAL_KONSULTASI, idKonsultasi)
            context.startActivity(intent)
        }

        fun startActivityPharmacy(
            context: Context,
            userInfo: String?,
            roomName: String?,
            idPharmacy: String?,
            idPasien: String?
        ) {
            val intent = Intent(context, ConsultationCallPatientFragment::class.java)
            intent.putExtra(USER_INFO, userInfo)
            intent.putExtra(ROOM_NAME, roomName)
            intent.putExtra(ID_PHARMACY, idPharmacy)
            intent.putExtra(ID_USER, idPasien)
            context.startActivity(intent)
        }

    }


    private lateinit var jitsiView: JitsiMeetView

    private val REQUEST_CODE_ASK_PERMISSIONS by lazy { 11 }

    private val binding: LayoutConsultationCallVideoPatientBinding by binding(R.layout.layout_consultation_call_video_patient)

    private val userInfo: JitsiMeetUserInfo by lazy {
        JitsiMeetUserInfo().apply {
            displayName = viewModel.user?.name

        }
    }

    private val viewModel: TransitionViewModel by viewModels()

    private val roomName: String? by lazy {
        intent.getStringExtra(ROOM_NAME)
    }

    private val isPharmacy: Boolean by lazy {
        intent.getStringExtra(ID_PHARMACY) != null
    }

    private val scheduleDoctorConsultation: ScheduleDoctorConsultation by lazy {
        Timber.d("name_doctor = ${intent.getStringExtra(NAME_DOCTOR)}")
        ScheduleDoctorConsultation(
            id_dokter = intent.getStringExtra(ID_DOCTOR), id_pasien = intent.getStringExtra(
                ID_USER
            ), nama_dokter = intent.getStringExtra(NAME_DOCTOR), id = intent.getStringExtra(
                ID_JADWAL_KONSULTASI)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
        if (isPharmacy) {
            Timber.d("pharmacyCall")
            binding.consultationCallVideoPatientBtnEnd.visibility = View.GONE
            binding.consultationCallVideoPatientBtnChat.setOnClickListener {
                val schedule = ScheduleDoctorConsultation(
                    id_dokter = intent.getStringExtra(ID_PHARMACY),
                    id_pasien = intent.getStringExtra(ID_USER),
                    nama_dokter = "Farmasi",
                    id = ""
                )
                val modalbottomSheetFragment = ConsultationChatBottomSheet(schedule, "Farmasi").apply {
                    onNavigationClicked = {
                        this.dismiss()
                    }
                }
                modalbottomSheetFragment.show(supportFragmentManager, modalbottomSheetFragment.tag)
            }
        } else {
            Timber.d("scheduleDoctorConsultation : $scheduleDoctorConsultation")
            viewModel.idKonsultasi = intent.getStringExtra(ID_JADWAL_KONSULTASI)
            viewModel.getConsultation()
            setOnClickListener(scheduleDoctorConsultation)
            viewModel.assessment.observe(this) {
                it.getContentIfNotHandled()?.let { status ->
                    if (!status) {
                        showAssessment(scheduleDoctorConsultation)
                    }
                }
            }
        }

        startMeet()
        //register broadcast receiver
        val intentFilter = IntentFilter()
        intentFilter.addAction(MyFirebaseMessagingService.CALL_NOTIF)
        intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_JOINED.action)
        intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.action)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
    }

    override fun onResume() {
        super.onResume()
        viewModel.setRoomIsCalled()
        JitsiMeetActivityDelegate.onHostResume(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.setRoomIsDestroyed()
        Timber.d("onDestroyed")
        if (::jitsiView.isInitialized) {
            jitsiView.leave()
            jitsiView.dispose()
            jitsiView.listener = null
            Timber.d("jitsiView.isInitialized = $jitsiView")
        }
        if (isPharmacy) {
            // TODO: end call by patient
        } else {
            viewModel.postEndCallByPasien(scheduleDoctorConsultation.id_dokter)
        }

        JitsiMeetActivityDelegate.onHostDestroy(this)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat
                .requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                    REQUEST_CODE_ASK_PERMISSIONS
                )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        JitsiMeetActivityDelegate.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        JitsiMeetActivityDelegate.onActivityResult(
            this, requestCode, resultCode, data
        )
    }


//    override fun onConferenceJoined(p0: MutableMap<String, Any>?) {
//    }
//
//    override fun onConferenceTerminated(p0: MutableMap<String, Any>?) {
//        finish()
//    }
//
//    override fun onConferenceWillJoin(p0: MutableMap<String, Any>?) {
//    }

    private fun startMeet() {
        val baseUrl = if(isPharmacy) {
            AppVar.BASE_CALL_URL
        } else {
            AppVar.BASE_VIDEO_URL
        }
        val options = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL(baseUrl))
            .setRoom(roomName)
            .setUserInfo(userInfo)
            .setFeatureFlag("chat.enabled", false)
            .setFeatureFlag("add-people.enabled", false)
            .setFeatureFlag("live-streaming.enabled", false)
            .setFeatureFlag("video-share.enabled", false)
            .setFeatureFlag("invite.enabled", false)
            .setFeatureFlag("overflow-menu.enabled", false)
            .setFeatureFlag("tile-view.enabled", false)
            .setFeatureFlag("raise-hand.enabled", false)
            .setFeatureFlag("meeting-password.enabled", false)
        if (isPharmacy) {
            options.setAudioOnly(true)
        }

        jitsiView = JitsiMeetView(this).apply {
//            listener = this@ConsultationCallPatientFragment
            join(options.build())
            binding.consultationCallVideoPatientViewVideo.addView(this)
        }
    }

    private fun setOnClickListener(schedule: ScheduleDoctorConsultation?) {
        binding.apply {
            consultation_call_video_patient_btn_chat.setOnClickListener {
                val title =
                    if (schedule!!.nama_pasien.isNullOrBlank()) schedule.nama_dokter else schedule.nama_pasien
                val modalbottomSheetFragment = ConsultationChatBottomSheet(schedule, title ?: "Chat").apply {
                    onNavigationClicked = {
                        this.dismiss()
                    }
                }
                modalbottomSheetFragment.show(supportFragmentManager, modalbottomSheetFragment.tag)
            }
            consultationCallVideoPatientBtnEnd.setOnClickListener {
                finish()
            }
        }
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, i: Intent) {
            val filter: String = MyFirebaseMessagingService.CALL_NOTIF
            if (i.action == filter) {
                val name = i.getStringExtra(filter)
                val keterangan = i.getStringExtra("keterangan")
                if (name == "panggilan_konsultasi_berakhir_pasien") {
                    keterangan?.toast()
                    finish()
                    val intent = Intent(this@ConsultationCallPatientFragment, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                    startActivity(intent)
                } else if (name == "reject_by_dokter"){
                    keterangan?.toast()
                    finish()
                    val intent = Intent(this@ConsultationCallPatientFragment, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                    startActivity(intent)
                }
            } else {
                when(i.action){
                    BroadcastEvent.Type.CONFERENCE_JOINED.action -> {
                    }
                    BroadcastEvent.Type.CONFERENCE_TERMINATED.action -> {
                        finish()
                    }
                    BroadcastEvent.Type.CONFERENCE_WILL_JOIN.action -> {

                    }
                }
            }
        }
    }

    private fun String.toast() {
        Toast.makeText(this@ConsultationCallPatientFragment, this, Toast.LENGTH_SHORT).show()
    }

    private fun showAssessment(schedule: ScheduleDoctorConsultation?){
        val modalbottomSheetFragment = ConsultationAssessmentBottomSheet(schedule).apply {
            onNavigationClicked = {
                this.dismiss()
            }
        }
        modalbottomSheetFragment.show(supportFragmentManager, modalbottomSheetFragment.tag)
    }

}
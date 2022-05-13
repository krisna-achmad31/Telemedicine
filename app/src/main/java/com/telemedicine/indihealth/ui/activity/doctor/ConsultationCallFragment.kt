package com.telemedicine.indihealth.ui.activity.doctor

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.facebook.react.modules.core.PermissionListener
import com.telemedicine.indihealth.R
import com.telemedicine.indihealth.base.BaseFragmentActivity
import com.telemedicine.indihealth.databinding.LayoutConsultationCallVideoBinding
import com.telemedicine.indihealth.databinding.LayoutProfilePatientShowBinding
import com.telemedicine.indihealth.helper.AppToast
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.service.MyFirebaseMessagingService
import com.telemedicine.indihealth.service.MyFirebaseMessagingService.Companion.CALL_NOTIF
import com.telemedicine.indihealth.ui.activity.additional.AdditionalActivity
import com.telemedicine.indihealth.ui.activity.diagnosis.DiagnosisActivity
import com.telemedicine.indihealth.ui.activity.prescription.ConsultationPrescriptionActivity
import com.telemedicine.indihealth.ui.bottomsheet.ConsultationAssessmentBottomSheet
import com.telemedicine.indihealth.ui.bottomsheet.chat.ConsultationChatBottomSheet
import com.telemedicine.indihealth.ui.bottomsheet.diagnosis.ConsultationDiagnosisBottomSheet
import com.telemedicine.indihealth.ui.bottomsheet.prescription.ConsultationPrescriptionBottomSheet
import com.telemedicine.indihealth.ui.dialog.DialogNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_consultation_call_video.*
import org.jitsi.meet.sdk.*
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class ConsultationCallFragment : BaseFragmentActivity(), JitsiMeetActivityInterface {
    companion object {
        private const val EXTRA = "EXTRA"
        private const val DIAGNOSIS = 12
        private const val PRESCRIPTION = 13
        private const val ADDITIONAL = 14
        private const val TAG = "MainActivity"
        fun startActivity(context: Context, schedule: ScheduleDoctorConsultation?) {
            if (context is Activity) {
                val intent = Intent(context, ConsultationCallFragment::class.java)
                intent.putExtra(EXTRA, schedule)
                context.startActivity(intent)
            }
        }

    }

    private var countdown: TextView? = null
    private var btn60menit: Button?= null
    private var btn30menit: Button?= null
    private var startstop_countdown: Button? = null
    private var isStarted = false
    private var isStarted2 = false
    private var JOINED = false
    private lateinit var dialog: DialogNotification
    private lateinit var jitsiView: JitsiMeetView

    private val REQUEST_CODE_ASK_PERMISSIONS by lazy { 11 }

    private val viewModel: ConsultationCallViewModel by viewModels()

    private val binding: LayoutConsultationCallVideoBinding by binding(R.layout.layout_consultation_call_video)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scheduleDoctorConsultation =
            intent?.getParcelableExtra<ScheduleDoctorConsultation>(EXTRA)
        viewModel.scheduleDoctorConsultation = scheduleDoctorConsultation
        binding.vm = viewModel
        setObservableValue()
        setOnClickListener(scheduleDoctorConsultation)
        viewModel.setRoomFinal()
        startMeet()
        //register broadcast receiver
        val intentFilter = IntentFilter()
        intentFilter.addAction(CALL_NOTIF)
        intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_JOINED.action)
        intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.action)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
        //setup toolbar
        binding.toolbar.menu.getItem(0)?.icon?.setTint(Color.WHITE)
        binding.toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                R.id.menu_screenshot -> {
                    binding.root.let { view ->
                        try {
                            val bitmap = view.drawToBitmap()
                            saveScreenshot(bitmap, this)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }

        countdown = findViewById(R.id.countdown)
    }



    private fun setTimer() {
    if (viewModel.durasi.value?.durasi.equals("3600"))
    {
        startTimer2()
    }else{
        startTimer()
    }
}

    private var countDownTimer = object : CountDownTimer(10000 * 180, 1000){
        override fun onTick(p0: Long) {
            Log.d(TAG, "onTick: ${p0/1000f}")
            countdown?.text = getString(R.string.formatted_time,
            TimeUnit.MILLISECONDS.toMinutes(p0)%60,
            TimeUnit.MILLISECONDS.toSeconds(p0)%60)
        }

        override fun onFinish() {
            Log.d(TAG, "onFinish: called")
            isStarted = false
//            startstop_countdown?.text = "START"
            Toast.makeText(applicationContext, "Waktu konsultasi sudah 30 menit", Toast.LENGTH_LONG).show()
            onBackPressed()
        }
    }

    private fun startTimer(){
        countDownTimer.start()
        isStarted = true
        //btn30menit?.text = "STOP"
    }

//    private fun stopTimer(){
//        countDownTimer.cancel()
//        isStarted = false
//        btn30menit?.text = "30 Menit"
//        countdown?.text = "00.00"
//        binding.btn60menit.visibility = View.VISIBLE
//    }

    private var countDownTimer2 = object : CountDownTimer(10000 * 360 , 1000){
        override fun onTick(p0: Long) {
            Log.d(TAG, "onTick: ${p0/1000f}")
            countdown?.text = getString(R.string.formatted_time,
                TimeUnit.MILLISECONDS.toMinutes(p0)%60,
                TimeUnit.MILLISECONDS.toSeconds(p0)%60)
        }

        override fun onFinish() {
            Log.d(TAG, "onFinish: called")
            isStarted2 = false
//            startstop_countdown?.text = "START"
            Toast.makeText(applicationContext, "Waktu konsultasi sudah 60 menit", Toast.LENGTH_LONG).show()
            onBackPressed()
        }
    }

    private fun startTimer2(){
        countDownTimer2.start()
        isStarted2 = true
        //btn60menit?.text = "STOP"
    }

//    private fun stopTimer2(){
//        countDownTimer2.cancel()
//        isStarted2 = false
//        btn60menit?.text = "60 Menit"
//        countdown?.text = "00.00"
//        binding.btn30menit.visibility = View.VISIBLE
//    }



    private fun saveScreenshot(bitmap: Bitmap, context: Context) {
        val path = Environment.getExternalStorageDirectory().toString() + "/Pictures/"
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(path, "${System.currentTimeMillis()}.jpg")
        try {
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
            fOut.close()
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath,
                file.name,
                file.name
            )
            Toast.makeText(this, "Screenshot tersimpan di ${file.absolutePath}", Toast.LENGTH_SHORT)
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        JitsiMeetActivityDelegate.onHostResume(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        Timber.d("onDestroyed onConsultationCallFragent")
        if (::jitsiView.isInitialized) {
            jitsiView.leave()
            jitsiView.dispose()
            Timber.d("jitsiView.isInitialized = $jitsiView")
        }
        viewModel.postEndCallByDokter()
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
        if (requestCode == DIAGNOSIS) {
            if (resultCode == RESULT_OK) {
                viewModel.diagnosis = data?.getStringExtra("diagnosis")
            }
        } else if (requestCode == PRESCRIPTION) {
            if (resultCode == RESULT_OK) {
                viewModel.prescription = data?.getParcelableArrayListExtra("prescription")
                data?.getStringExtra("isPrescriptionSend")?.let {
                    if (it == "send") viewModel.isPrescriptionSend = true
                }
            }
        } else if (requestCode == ADDITIONAL) {

        } else {
            JitsiMeetActivityDelegate.onActivityResult(
                this, requestCode, resultCode, data
            )
        }
    }


//    override fun onConferenceJoined(p0: MutableMap<String, Any>?) {
//        viewModel.postCallVideo()
//        dialog = DialogNotification.newInstance(
//            "call",
//            "Memanggil pasien",
//            "Silahkan menunggu pasien untuk mengangkat panggilan Anda."
//        )!!
//        dialog.isConfirmButtonVisible = false
//        dialog.isCancelButtonVisible = true
//        dialog.cancelButtonText = "Tutup"
//        dialog.isIconVisible = true
//        dialog.apply {
//            onCancelClicked = {
//            dismiss()
//            onConferenceTerminated(mutableMapOf())
//            }
//        }
//        dialog.show(supportFragmentManager, "")
//    }

//    override fun onConferenceTerminated(p0: MutableMap<String, Any>?) {
//        viewModel.postEndCallByDokter()
//        finish()
//    }

    fun onConferenceTerminated() {
        jitsiView.leave()
//        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
//        LocalBroadcastManager.getInstance(this).sendBroadcast(hangupBroadcastIntent)
    }

//    override fun onConferenceWillJoin(p0: MutableMap<String, Any>?) {
//    }

    override fun onBackPressed() {
        JitsiMeetActivityDelegate.onBackPressed()
    }

    private fun startMeet() {
        val options = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL(AppVar.BASE_VIDEO_URL))
            .setRoom(viewModel.roomNameFinal)
            .setFeatureFlag("chat.enabled", false)
            .setFeatureFlag("add-people.enabled", false)
            .setFeatureFlag("live-streaming.enabled", false)
            .setFeatureFlag("video-share.enabled", false)
            .setFeatureFlag("invite.enabled", false)
            .setFeatureFlag("overflow-menu.enabled", false)
            .setFeatureFlag("tile-view.enabled", false)
            .setFeatureFlag("raise-hand.enabled", false)
            .setFeatureFlag("meeting-password.enabled", false)
            .setFeatureFlag("meeting-password.enabled", false)
            .setFeatureFlag("call-integration.enabled", false)
            .setFeatureFlag("server-url-change.enabled", false)

//            .setFeatureFlag("toolbox.enabled", false)

//        sharedvideo
            .build()
        jitsiView = JitsiMeetView(this).apply {
//            listener = this@ConsultationCallFragment
            join(options)
            consultation_call_video_view_video.addView(this)
//            startTimer()
            //setTimer()
        }

    }

    private fun setOnClickListener(schedule: ScheduleDoctorConsultation?) {
        binding.mcvIcon1.setOnClickListener {
            //chat
            val title =
                if (schedule!!.nama_pasien.isNullOrBlank()) schedule.nama_dokter else schedule.nama_pasien
            val modalbottomSheetFragment = ConsultationChatBottomSheet(schedule, title ?: "Chat").apply {
                onNavigationClicked = {
                    this.dismiss()
                }
            }
            modalbottomSheetFragment.show(supportFragmentManager, modalbottomSheetFragment.tag)
        }

        binding.mcvIcon2.setOnClickListener {
            //add assessment
            val modalbottomSheetFragment = ConsultationAssessmentBottomSheet(schedule).apply {
                onNavigationClicked = {
                    this.dismiss()
                }
            }
            modalbottomSheetFragment.show(supportFragmentManager, modalbottomSheetFragment.tag)
        }

        binding.mcvIcon3.setOnClickListener {
            //add diagnosis
            val intent = Intent(this@ConsultationCallFragment, DiagnosisActivity::class.java)
            intent.putExtra("schedule", schedule)
            intent.putExtra("diagnosis", viewModel.diagnosis)
            startActivityForResult(intent, DIAGNOSIS)
        }

        binding.mcvIcon4.setOnClickListener {
            //add recipe
            val intent =
                Intent(this@ConsultationCallFragment, ConsultationPrescriptionActivity::class.java)
            intent.putExtra("schedule", schedule)
            intent.putExtra("prescription", viewModel.prescription)
            startActivityForResult(intent, PRESCRIPTION)
        }

        binding.mcvIcon5.setOnClickListener {
            //finish consultation
            if (viewModel.diagnosis.isNullOrEmpty() && viewModel.showDiagnosis.value == true) {
                "Diagnosis tidak boleh kosong".toast()
            } else if (viewModel.prescription.isNullOrEmpty()) {
                "Resep tidak boleh kosong".toast()
            } else if (!viewModel.isPrescriptionSend) {
                "Resep obat belum dikirim".toast()
            } else {
                viewModel.checkBeforeEnded()
            }
        }

        binding.mcvIcon6.setOnClickListener {
            //add additional info
            val intent =
                Intent(this@ConsultationCallFragment, AdditionalActivity::class.java)
            intent.putExtra("schedule", schedule)
            startActivityForResult(intent, PRESCRIPTION)
        }

        binding.apply {
            binding.toolbar.title = schedule?.nama_pasien
            /*consultation_call_video_mcv_assessment.setOnClickListener {
                val modalbottomSheetFragment = ConsultationAssessmentBottomSheet(schedule).apply {
                    onNavigationClicked = {
                        this.dismiss()
                    }
                }
                modalbottomSheetFragment.show(supportFragmentManager, modalbottomSheetFragment.tag)
            }*/
            /*consultation_call_video_mcv_chat.setOnClickListener {
                val modalbottomSheetFragment = ConsultationChatBottomSheet(schedule).apply {
                    onNavigationClicked = {
                        this.dismiss()
                    }
                }
                modalbottomSheetFragment.show(supportFragmentManager, modalbottomSheetFragment.tag)
            }*/

            /*consultation_call_video_mcv_prescription.setOnClickListener {
                val intent = Intent(this@ConsultationCallFragment, ConsultationPrescriptionActivity::class.java)
                intent.putExtra("schedule", schedule)
                intent.putExtra("prescription", viewModel.prescription)
                startActivityForResult(intent, PRESCRIPTION)
//                val modalbottomSheetFragment = ConsultationPrescriptionBottomSheet(schedule).apply {
//                    onNavigationClicked = {
//                        this.dismiss()
//                    }
//                }
//                modalbottomSheetFragment.isCancelable = false
//                modalbottomSheetFragment.show(supportFragmentManager, modalbottomSheetFragment.tag)
            }*/

            /*consultation_call_video_mcv_diagnosis.setOnClickListener {
                val intent = Intent(this@ConsultationCallFragment, DiagnosisActivity::class.java)
                intent.putExtra("schedule", schedule)
                intent.putExtra("diagnosis", viewModel.diagnosis)
                startActivityForResult(intent, DIAGNOSIS)
//                startActivity()
//                val modalbottomSheetFragment = ConsultationDiagnosisBottomSheet(schedule).apply {
//                    onNavigationClicked = {
//                        this.dismiss()
//                    }
//                }
//                modalbottomSheetFragment.show(supportFragmentManager, modalbottomSheetFragment.tag)
            }*/
//            consultation_call_video_btn_call_end.setOnClickListener {
//                if(viewModel.diagnosis.isNullOrEmpty()){
//                    "Diagnosis tidak boleh kosong".toast()
//                }else if(viewModel.prescription.isNullOrEmpty()) {
//                    "Resep tidak boleh kosong".toast()
//                }else if (!viewModel.isPrescriptionSend){
//                    "Resep obat belum dikirim".toast()
//                }else {
//                    viewModel.checkBeforeEnded()
//                }
//            }

            consultation_call_video_btn_call.setOnClickListener {
                viewModel.postCallVideo()
                dialog = DialogNotification.newInstance(
                    "call",
                    "Memanggil pasien",
                    "Silahkan menunggu pasien untuk mengangkat panggilan Anda."
                )!!
                dialog.isConfirmButtonVisible = false
                dialog.isCancelButtonVisible = true
                dialog.cancelButtonText = "Tutup"
                dialog.isIconVisible = true
                dialog.apply {
                    onCancelClicked = {
                        dismiss()
                    }
                    if (JOINED) {
                        dismiss()
                    }
                }
                dialog.show(supportFragmentManager, "")
            }

            binding.toolbar.setNavigationOnClickListener {
                onConferenceTerminated()
            }
        }
    }

    private fun setObservableValue() {
        viewModel.apply {
            responseStatus.observe(this@ConsultationCallFragment, {
                it.getContentIfNotHandled()?.apply {
                    when (this.getValue("type")) {
                        "endCall" -> {
                            when (this.getValue("status")) {
                                "success" -> {
                                    dialogEndCall()?.show(supportFragmentManager, "")
                                }
                                "failed" -> {
                                    dialogEndCall()?.show(supportFragmentManager, "")
                                }
                            }
                        }
                    }
                }
            })
            conditionBeforeEnded.observe(this@ConsultationCallFragment, {
                it.getContentIfNotHandled()?.apply {
                    when (this) {
                        AppVar.IS_CALL_DIAGNOSIS_EXIST -> {
                            AppToast.set(
                                this@ConsultationCallFragment,
                                "Silahkan mengisi diagnosis pasien terlebih dahulu"
                            )
                            showDiagnosis()
                        }
                        AppVar.IS_CALL_RECIPE_EXIST -> {
                            dialogConfirmPrescription()?.show(supportFragmentManager, "")
                        }
                        else -> {
                            viewModel.onCallEnded()
                            viewModel.clearChat()
                            AppToast.set(this@ConsultationCallFragment, "Konsultasi Selesai")
                        }
                    }
                }
            })
            showDiagnosis.observe(this@ConsultationCallFragment) {
                binding.mcvIcon3.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, i: Intent) {
            val filter: String = MyFirebaseMessagingService.CALL_NOTIF
            if (i.action == filter) {
                val name = i.getStringExtra(filter)
                val keterangan = i.getStringExtra("keterangan")
                if (name == "reject_konsultasi") {
                    Timber.d("isRejected")
                    if (::dialog.isInitialized) {
                        dialog.dismiss()
                        consultation_call_video_btn_call.isEnabled = true
                    }
                    "Panggilan Anda Dibatalkan".toast()
                } else if (name == "panggilan_konsultasi_dokter") {
                    if (::dialog.isInitialized) {
                        dialog.dismiss()
                        consultation_call_video_btn_call.isEnabled = false
                    }
                    "Panggilan Anda Diterima".toast()
                    setTimer()
                } else if (name == "reject_by_pasien") {
                    consultation_call_video_btn_call.isEnabled = true
                    "Konsultasi Video Telah Dihentikan Oleh Pasien".toast()
                }
            } else {
                when (i.action) {
                    BroadcastEvent.Type.CONFERENCE_JOINED.action -> {
                        viewModel.postCallVideo()
                        dialog = DialogNotification.newInstance(
                            "call",
                            "Memanggil pasien",
                            "Silahkan menunggu pasien untuk mengangkat panggilan Anda."
                        )!!
                        dialog.isConfirmButtonVisible = false
                        dialog.isCancelButtonVisible = true
                        dialog.cancelButtonText = "Batalkan Panggilan"
                        dialog.isIconVisible = true
                        dialog.apply {
                            onCancelClicked = {
                                dismiss()
                                onConferenceTerminated()
                            }

                        }
                        dialog.show(supportFragmentManager, "")
                    }
                    BroadcastEvent.Type.CONFERENCE_TERMINATED.action -> {
                        viewModel.postEndCallByDokter()
                        finish()
                    }
                    BroadcastEvent.Type.PARTICIPANT_JOINED.action -> {
                        JOINED = true
                    }

                }
            }
        }
    }

    private fun String.toast() {
        Toast.makeText(this@ConsultationCallFragment, this, Toast.LENGTH_SHORT).show()
    }

    private fun dialogEndCall(): DialogNotification? {
        val dialog = DialogNotification.newInstance(
            "success",
            "Konsultasi Selesai",
            "Konsultasi dengan pasien telah berakhir "
        )!!
        dialog.isConfirmButtonVisible = false
        dialog.isCancelButtonVisible = true
        dialog.cancelButtonText = "Tutup"
        dialog.apply {
            onCancelClicked = {
                dismiss()
                finish()
            }
        }
        return dialog
    }

    private fun dialogConfirmPrescription(): DialogNotification? {
        val dialog = DialogNotification.newInstance(
            "failed",
            "Keluar tanpa memberikan resep obat",
            "Apakah Anda yakin untuk menyelesaikan konsultasi video tanpa memberikan resep obat?"
        )!!
        dialog.cancelButtonText = "Tidak, masukkan resep obat "
        dialog.confirmButtonText = "Iya, selesaikan tanpa resep obat"
        dialog.isConfirmButtonVisible = true
        dialog.isCancelButtonVisible = true
        dialog.apply {
            onCancelClicked = {
                showPrescription()
                dismiss()
            }
            onConfirmClicked = {
                viewModel.onDialogPrescriptionClicked()
            }
        }
        return dialog
    }

    private fun showDiagnosis() {
        Timber.d("schedule = ${viewModel.scheduleDoctorConsultation}")
        val modalbottomSheetFragment =
            ConsultationDiagnosisBottomSheet(viewModel.scheduleDoctorConsultation).apply {
                onNavigationClicked = {
                    this.dismiss()
                }
            }
        modalbottomSheetFragment.show(supportFragmentManager, modalbottomSheetFragment.tag)
    }

    private fun showPrescription() {
        val modalbottomSheetFragment =
            ConsultationPrescriptionBottomSheet(viewModel.scheduleDoctorConsultation).apply {
                onNavigationClicked = {
                    this.dismiss()
                }
            }
        modalbottomSheetFragment.show(supportFragmentManager, modalbottomSheetFragment.tag)
    }

}


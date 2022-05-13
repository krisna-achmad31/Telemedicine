package com.telemedicine.indihealth.ui.bottomsheet.chat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.telemedicine.indihealth.base.BaseViewModel
import com.telemedicine.indihealth.helper.AppVar
import com.telemedicine.indihealth.model.Chat
import com.telemedicine.indihealth.model.ScheduleDoctorConsultation
import com.telemedicine.indihealth.model.User
import com.telemedicine.indihealth.network.repo.ConsultationDoctorRepository
import com.telemedicine.indihealth.persistence.SharedPreferenceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File

class ConsultationChatViewModel @ViewModelInject constructor(
    private val repo: ConsultationDoctorRepository,
    private val sharedPreference: SharedPreferenceApp
) : BaseViewModel() {

    private val user: User? by lazy {
        sharedPreference.getUserValue()
    }

    var scheduleDoctorConsultation: ScheduleDoctorConsultation? = null
    var fileToUpload: MultipartBody.Part? = null
    var fileName: String? = null
    var param: HashMap<String?, RequestBody?>? = null
    var child = AppVar.PATH_CHATS + "/"

    val dbReference : DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    val parser: SnapshotParser<Chat?> by lazy {
        SnapshotParser<Chat?> { dataSnapshot ->
            val friendlyMessage: Chat? =
                dataSnapshot.getValue(Chat::class.java)
            friendlyMessage?.id = dataSnapshot.key
            friendlyMessage!!
        }
    }
    val chatText : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun setPhoto(file: File) {
        // Parsing any Media type file
        val requestBody: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        fileToUpload =
            MultipartBody.Part.createFormData("file", file.name, requestBody)
        Timber.d("requestBody = $fileToUpload")
    }

    fun postChatImage() {
        setLoading(true)
        val chatId =
            "${scheduleDoctorConsultation?.id_dokter}_${scheduleDoctorConsultation?.id_pasien}"
        param = hashMapOf(
            "chat_id" to chatId.toRequestBody("text/plain".toMediaTypeOrNull())
        )
        viewModelScope.launch(Dispatchers.IO) {
            repo.postChatImage(
                param!!,
                fileToUpload,
                onSuccess = {
                    setLoading(false)
                    sendToFirebaseDatabase()
                },
                onError = {
                    setLoading(false)
                    setStatus(
                        hashMapOf(
                            "status" to "failed",
                            "msg" to it
                        )
                    )
                }
            )
        }
    }

    fun sendToFirebaseDatabase(){
        val text = chatText.value?.trim()
        if(fileToUpload != null || !text.isNullOrEmpty()){
            val messageData = Chat(
                text = text, type = user?.role, file = fileToUpload?.let { fileName }
            )
            dbReference.child(child)
                .push().setValue(messageData).addOnSuccessListener {
                    chatText.value = ""
                    fileToUpload = null
                }
        }
    }

    fun getUserRole(): String? {
        return sharedPreference.getUserValue()?.role
    }
}
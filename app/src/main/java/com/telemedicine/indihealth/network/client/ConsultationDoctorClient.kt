package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.model.AdditionalInfo
import com.telemedicine.indihealth.model.GlobalResponse
import com.telemedicine.indihealth.network.service.doctor.ConsultationDoctorService
import com.telemedicine.indihealth.network.service.doctor.consultation.AdditionalInfoResponse
import com.telemedicine.indihealth.network.service.doctor.consultation.ScheduleDoctorConsultationResponse
import com.telemedicine.indihealth.network.service.doctor.diagnosis.DiagnosisResponse
import com.telemedicine.indihealth.network.service.doctor.prescription.MedicineResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ConsultationDoctorClient @Inject constructor(
    private val service: ConsultationDoctorService
) {
    suspend fun getScheduleDoctor(
        param: HashMap<String?,Any?>?,
        onResult: (response: ApiResponse<ScheduleDoctorConsultationResponse>) -> Unit
    ) {
        service
            .getScheduleDoctor(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postCallVideo(
        param: HashMap<String,String?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .postCallVideo(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postChatImage(
        param: HashMap<String?, RequestBody?>,
        file: MultipartBody.Part?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .postChatImage(param,file)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getMedicine(
        param: HashMap<String?,Any?>?,
        onResult: (response: ApiResponse<MedicineResponse>) -> Unit
    ) {
        service
            .getMedicine(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun clearPrescription(
//        param: String?,
        param: HashMap<String,String?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .clearPrescription(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postPrescription(
        param: HashMap<String,String?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .postPrescription(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getDiagnosis(
        param: HashMap<String?,Any?>?,
        onResult: (response: ApiResponse<DiagnosisResponse>) -> Unit
    ) {
        service
            .getDiagnosis(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postDiagnosis(
        param: HashMap<String,String?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .postDiagnosis(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }


    suspend fun postRejectCall(
        param: HashMap<String,String?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .postRejectCall(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postAnswerCall(
        param: HashMap<String,String?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .postAnswerCall(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postEndCall(
        param: HashMap<String,String?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .postEndCall(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postEndCallByPasien(
        param: HashMap<String,String?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .postEndCallByPasien(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postEndCallByDokter(
        param: HashMap<String,String?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .postEndCallByDokter(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postAdditional(
        param: HashMap<String, String>,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .postAdditionalInfo(param)
            .toResponseDataSource()
            .request(onResult)
    }

    suspend fun getAdditional(
        param: HashMap<String,String?>?,
        onResult: (response: ApiResponse<AdditionalInfoResponse>) -> Unit
    ) {
        service
            .getAdditionalInfo(param)
            .toResponseDataSource()
            .request(onResult)
    }
}
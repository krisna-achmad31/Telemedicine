package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.model.GlobalResponse
import com.telemedicine.indihealth.network.service.consultation.ConsultationService
import com.telemedicine.indihealth.network.service.consultation.asessment.AssessmentListResponse
import com.telemedicine.indihealth.network.service.consultation.asessment.AssessmentResponse
import com.telemedicine.indihealth.network.service.consultation.payment.*
import com.telemedicine.indihealth.network.service.consultation.registration.ConsultationDoctorResponse
import com.telemedicine.indihealth.network.service.consultation.schedule.ScheduleResponse
import com.telemedicine.indihealth.network.service.profile.doctor.DoctorResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ConsultationClient @Inject constructor(
    private val mService: ConsultationService
) {

    suspend fun getConsultationDoctor(
        hashMap: HashMap<String,Any>,
        onResult: (response: ApiResponse<ConsultationDoctorResponse>) -> Unit
    ) {
        mService
            .getConsultationDoctor(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postRegistrationDoctor(
        hashMap: HashMap<String,Any?>,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        mService
            .postRegistrationDoctor(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getPaymentList(
        hashMap: HashMap<String?,String?>,
        onResult: (response: ApiResponse<PaymentResponse>) -> Unit
    ) {
        mService
            .getPaymentList(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getBankTransferList(
        onResult: (response: ApiResponse<PaymentListResponse>) -> Unit
    ) {
        mService
            .getBankTransferList()
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postPayment(
        param: HashMap<String?,RequestBody?>,
        foto: MultipartBody.Part?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        mService
            .postPayment(param,foto)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun updateCanceledOnProcessPayment(
        param: HashMap<String?,Any?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        mService
            .updateCanceledOnProcessPayment(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun deletePayment(
        param: HashMap<String?,Any?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        mService
            .deletePayment(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getAssessment(
        param: HashMap<String?,Any?>?,
        onResult: (response: ApiResponse<AssessmentResponse>) -> Unit
    ) {
        mService
            .getAssessment(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postAssessment(
        param: HashMap<String?,RequestBody?>?,
        foto: List<MultipartBody.Part>,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        mService
            .postAssessment(param, foto)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun updateAssessment(
        param: HashMap<String?, RequestBody?>?,
        foto: List<MultipartBody.Part>,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        mService
            .updateAssessment(param, foto)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getSchedule(
        param: HashMap<String?,Any?>?,
        onResult: (response: ApiResponse<ScheduleResponse>) -> Unit
    ) {
        mService
            .getSchedule(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }




    suspend fun postOwlexaGenerateOtp(
        param: HashMap<String?,String?>,
        onResult: (response: ApiResponse<OwlexaGenerateOtpResponse>) -> Unit
    ) {
        mService
            .postOwlexaGenerateotp(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postOwlexaVerification(
        param: HashMap<String?,String?>,
        onResult: (response: ApiResponse<OwlexaVerificationResponse>) -> Unit
    ) {
        mService
            .postOwlexaVerification(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getTocOwlexa(
        onResult: (response: ApiResponse<OwlexaGetTocResponse>) -> Unit
    ){
        mService
            .getTocOwlexa()
            .toResponseDataSource()
            .request(onResult)
    }

    suspend fun getAssessmentList(
        patientId: String,
        onResult: (response: ApiResponse<AssessmentListResponse>) -> Unit
    ){
        mService
            .getAssessmentList(patientId)
            .toResponseDataSource()
            .request(onResult)
    }

    suspend fun getVaList(
        onResult: (response: ApiResponse<PaymentListResponse>) -> Unit
    ){
        mService
            .getVaList()
            .toResponseDataSource()
            .request(onResult)
    }

    suspend fun getProfileDoctor(
        hashMap: HashMap<String, Any>,
        onResult: (response: ApiResponse<DoctorResponse>) -> Unit
    ) {
        mService
            .getProfileDoctor(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }
}
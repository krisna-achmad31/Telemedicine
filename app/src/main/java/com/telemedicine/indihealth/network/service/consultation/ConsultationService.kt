package com.telemedicine.indihealth.network.service.consultation

import com.skydoves.sandwich.DataSource
import com.telemedicine.indihealth.model.GlobalResponse
import com.telemedicine.indihealth.network.service.consultation.asessment.AssessmentListResponse
import com.telemedicine.indihealth.network.service.consultation.asessment.AssessmentResponse
import com.telemedicine.indihealth.network.service.consultation.payment.*
import com.telemedicine.indihealth.network.service.consultation.registration.ConsultationDoctorResponse
import com.telemedicine.indihealth.network.service.consultation.schedule.ScheduleResponse
import com.telemedicine.indihealth.network.service.profile.doctor.DoctorResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ConsultationService {
    @GET("pasien/pendaftaran/tampildokter")
    suspend fun getConsultationDoctor(
        @QueryMap condition: HashMap<String, Any>
    ): DataSource<ConsultationDoctorResponse>

    @FormUrlEncoded
    @POST("pasien/pendaftaran")
    suspend fun postRegistrationDoctor(
        @FieldMap condition: HashMap<String, Any?>
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @POST("owlexa/Api/generateOtp")
    suspend fun postOwlexaGenerateotp(
        @FieldMap condition: HashMap<String?, String?>
    ): DataSource<OwlexaGenerateOtpResponse>

    @FormUrlEncoded
    @POST("owlexa/Api/verification")
    suspend fun postOwlexaVerification(
        @FieldMap condition: HashMap<String?, String?>
    ): DataSource<OwlexaVerificationResponse>


    @GET("pasien/Pembayaran/list")
    suspend fun getPaymentList(
        @QueryMap condition: HashMap<String?, String?>
    ): DataSource<PaymentResponse>

    @GET("pasien/pembayaran/bank")
    suspend fun getBankTransferList() : DataSource<PaymentListResponse>


//    @GET("pasien/Pembayaran/list")
//    suspend fun PostPaymentDetail(
//        @QueryMap condition: HashMap<String?, RequestBody?>
//    ): DataSource<PaymentResponse>


    @Multipart
    @POST("pasien/pembayaran/bukti")
    suspend fun postPayment(
        @PartMap param: HashMap<String?, RequestBody?>,
        @Part foto: MultipartBody.Part?
    ): DataSource<GlobalResponse>


    @FormUrlEncoded
    @POST("pasien/pembayaran/setPembayaranStatus/belum-bayar")
    suspend fun updateCanceledOnProcessPayment(
        @FieldMap condition: HashMap<String?, Any?>?
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @POST("pasien/pembayaran/batalkan")
    suspend fun deletePayment(
        @FieldMap condition: HashMap<String?, Any?>?
    ): DataSource<GlobalResponse>

    @GET("pasien/assesment/detail")
    suspend fun getAssessment(
        @QueryMap condition: HashMap<String?, Any?>?
    ): DataSource<AssessmentResponse>

    @Multipart
    @POST("pasien/assesment/add")
    suspend fun postAssessment(
        @PartMap condition: HashMap<String?, RequestBody?>?,
        @Part foto: List<MultipartBody.Part>
    ): DataSource<GlobalResponse>

    @Multipart
    @POST("pasien/assesment/update")
    suspend fun updateAssessment(
        @PartMap condition: HashMap<String?, RequestBody?>?,
        @Part foto: List<MultipartBody.Part>
    ): DataSource<GlobalResponse>

    @GET("pasien/konsultasi/jadwal")
    suspend fun getSchedule(
        @QueryMap condition: HashMap<String?, Any?>?
    ): DataSource<ScheduleResponse>

    @GET("dokter/konsultasi/jadwal")
    suspend fun getScheduleDoctor(
        @QueryMap condition: HashMap<String?, Any?>?
    ): DataSource<ScheduleResponse>

    @GET("owlexa/Api/termsOwlexa")
    suspend fun getTocOwlexa():DataSource<OwlexaGetTocResponse>

    @GET("pasien/assesment/list")
    suspend fun getAssessmentList(
        @Query("id_pasien") patientId: String
    ):DataSource<AssessmentListResponse>

    @GET("dokter/profile")
    suspend fun getProfileDoctor(
        @QueryMap condition: HashMap<String, Any>
    ): DataSource<DoctorResponse>

    @GET("pasien/pembayaran/paymentMethod")
    suspend fun getVaList():DataSource<PaymentListResponse>
}
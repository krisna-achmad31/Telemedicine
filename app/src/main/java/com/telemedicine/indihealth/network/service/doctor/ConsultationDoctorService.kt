package com.telemedicine.indihealth.network.service.doctor

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.skydoves.sandwich.DataSource
import com.telemedicine.indihealth.model.AdditionalInfo
import com.telemedicine.indihealth.model.GlobalResponse
import com.telemedicine.indihealth.network.service.doctor.consultation.AdditionalInfoResponse
import com.telemedicine.indihealth.network.service.doctor.consultation.ScheduleDoctorConsultationResponse
import com.telemedicine.indihealth.network.service.doctor.diagnosis.DiagnosisResponse
import com.telemedicine.indihealth.network.service.doctor.prescription.MedicineResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ConsultationDoctorService {
    @GET("dokter/konsultasi/jadwal")
    suspend fun getScheduleDoctor(
        @QueryMap condition: HashMap<String?, Any?>?
    ): DataSource<ScheduleDoctorConsultationResponse>


    @FormUrlEncoded
    @POST("conference/call")
    suspend fun postCallVideo(
        @FieldMap condition: HashMap<String, String?>?
    ): DataSource<GlobalResponse>

    @Multipart
    @POST("all/chat/attachment")
    suspend fun postChatImage(
        @PartMap param: HashMap<String?, RequestBody?>,
        @Part file: MultipartBody.Part?
    ): DataSource<GlobalResponse>

    @GET("dokter/obat/list")
    suspend fun getMedicine(
        @QueryMap condition: HashMap<String?, Any?>?
    ): DataSource<MedicineResponse>

    @FormUrlEncoded
    @POST("dokter/resep/insert")
    suspend fun postPrescription(
        @FieldMap condition: HashMap<String, String?>?
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @GET("dokter/resep/hapus")
    suspend fun clearPrescription(
//        @FieldMap condition: String?
        @QueryMap condition: HashMap<String, String?>?
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @POST("RekamMedik/diagnosisList")
    suspend fun getDiagnosis(
        @FieldMap condition: HashMap<String?, Any?>?
    ): DataSource<DiagnosisResponse>

    @FormUrlEncoded
    @POST("dokter/diagnosis")
    suspend fun postDiagnosis(
        @FieldMap condition: HashMap<String, String?>?
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @POST("conference/reject")
    suspend fun postRejectCall(
        @FieldMap condition: HashMap<String, String?>?
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @POST("conference/jawab")
    suspend fun postAnswerCall(
        @FieldMap condition: HashMap<String, String?>?
    ): DataSource<GlobalResponse>


    @FormUrlEncoded
    @POST("conference/end_call")
    suspend fun postEndCall(
        @FieldMap condition: HashMap<String, String?>?
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @POST("conference/end_call_by_pasien")
    suspend fun postEndCallByPasien(
        @FieldMap condition: HashMap<String, String?>?
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @POST("conference/end_call_by_dokter")
    suspend fun postEndCallByDokter(
        @FieldMap condition: HashMap<String, String?>?
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @POST("dokter/resep/insertinfo")
    suspend fun postAdditionalInfo(
        @FieldMap condition: HashMap<String, String>
    ): DataSource<GlobalResponse>

    @GET("dokter/Resep/infoHasil")
    suspend fun getAdditionalInfo(
        @QueryMap condition: HashMap<String, String?>?
    ): DataSource<AdditionalInfoResponse>
}
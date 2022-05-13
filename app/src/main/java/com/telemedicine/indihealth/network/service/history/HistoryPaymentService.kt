package com.telemedicine.indihealth.network.service.history

import com.skydoves.sandwich.DataSource
import com.telemedicine.indihealth.network.service.history.payment.HistoryConsultationResponse
import com.telemedicine.indihealth.network.service.history.payment.HistoryDrugResponse
import com.telemedicine.indihealth.network.service.history.profile.ProfileDoctorResponse
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HistoryPaymentService {
    @FormUrlEncoded
    @POST("Profile/user")
    suspend fun getDoctorProfile(
        @FieldMap condition: HashMap<String?, String?>
    ): DataSource<ProfileDoctorResponse>

    @FormUrlEncoded
    @POST("pasien/pembayaran/history")
    suspend fun getHistoryPayment(
        @Field("id_pasien") id_pasien: String?
    ): DataSource<HistoryConsultationResponse>

    @FormUrlEncoded
    @POST("pasien/pembayaran/historyObat")
    suspend fun getHistoryPaymentDrug(
        @Field("id_pasien") id_pasien: String?
    ): DataSource<HistoryDrugResponse>
}
package com.telemedicine.indihealth.network.service.records

import com.skydoves.sandwich.DataSource
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MedicalRecordsService {
    @FormUrlEncoded
    @POST("RekamMedik/rekam")
    suspend fun getMedicalRecords(
        @Field("id_dokter") id_dokter: String?,
        @Field("id_pasien") id_pasien: String?,

    ): DataSource<MedicalRecordsResponse>

    @FormUrlEncoded
    @POST("RekamMedik/detail")
    suspend fun getMedicalRecordsDetail(
        @Field("id_jadwal_konsultasi") id_jadwal_konsultasi: String?,
        @Field("id_pasien") id_pasien: String?
    ): DataSource<MedicalRecordsDetailResponse>
}
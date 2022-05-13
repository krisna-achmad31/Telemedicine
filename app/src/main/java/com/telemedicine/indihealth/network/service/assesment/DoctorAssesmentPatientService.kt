package com.telemedicine.indihealth.network.service.assesment

import com.skydoves.sandwich.DataSource
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DoctorAssesmentPatientService {
    @FormUrlEncoded
    @POST("dokter/assesment/list")
    suspend fun getAssesmentPatient(
        @Field("id_dokter") id_dokter: String?
    ): DataSource<DoctorAssesmentPatientResponse>
}
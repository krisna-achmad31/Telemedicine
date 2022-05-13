package com.telemedicine.indihealth.network.service.schedule

import com.skydoves.sandwich.DataSource
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DoctorScheduleService {
    @FormUrlEncoded
    @POST("dokter/jadwal/detail")
    suspend fun getDoctorSchedule(
        @Field("id_dokter") id_dokter: String?
    ): DataSource<DoctorScheduleResponse>
}
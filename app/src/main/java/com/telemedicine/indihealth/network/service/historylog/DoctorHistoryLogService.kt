package com.telemedicine.indihealth.network.service.historylog

import com.skydoves.sandwich.DataSource
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DoctorHistoryLogService {
    @FormUrlEncoded
    @POST("dokter/history/historylog")
    suspend fun getHistoryLog(
        @Field("id_dokter") id_dokter: String?
    ): DataSource<DoctorHistoryLogResponse>
}
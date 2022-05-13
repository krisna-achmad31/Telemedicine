package com.telemedicine.indihealth.network.service.queue

import com.skydoves.sandwich.DataSource
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface QueueService {
    @FormUrlEncoded
    @POST("Antrian/cekAntrian")
    suspend fun getQueue(
        @Field("id_pasien") id_pasien: String?,
        @Field("status") status: String?
    ): DataSource<QueueResponse>
}
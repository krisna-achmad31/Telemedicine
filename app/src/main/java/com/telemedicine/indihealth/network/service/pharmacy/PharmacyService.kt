package com.telemedicine.indihealth.network.service.pharmacy

import com.skydoves.sandwich.DataSource
import com.telemedicine.indihealth.model.GlobalResponse
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Surya Mahadi on 11/23/2021
 */
interface PharmacyService {
    @FormUrlEncoded
    @POST("Conference/jawabFarmasi")
    suspend fun postAnswerCall(
        @FieldMap condition: HashMap<String, String?>?
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @POST("Conference/tolakFarmasi")
    suspend fun postRejectCall(
        @FieldMap condition: HashMap<String, String?>?
    ): DataSource<GlobalResponse>
}
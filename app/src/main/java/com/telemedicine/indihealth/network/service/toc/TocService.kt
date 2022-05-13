package com.telemedicine.indihealth.network.service.toc

import com.skydoves.sandwich.DataSource
import com.telemedicine.indihealth.network.service.login.LoginResponse
import retrofit2.http.*

interface TocService {
    @GET("User/updateTerms")
    suspend fun postUpdateToc(
        @Query("id_pasien") userId: String?
    ): DataSource<UpdateTocResponse>

    @FormUrlEncoded
    @POST("Login")
    suspend fun postLogin(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): DataSource<LoginResponse>

    @GET("User/termsOfCondition")
    suspend fun getToc(
        @Query("pasien_id") userId: String
    ): DataSource<TocResponse>
}
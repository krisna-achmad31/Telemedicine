package com.telemedicine.indihealth.network.service.login

import com.skydoves.sandwich.DataSource
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    //API Login
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("Login")
    suspend fun getLogin(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): DataSource<LoginResponse>
}
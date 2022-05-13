package com.telemedicine.indihealth.network.service.forgotpassword

import com.skydoves.sandwich.DataSource
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ForgotPasswordService {
    @FormUrlEncoded
    @POST("Profile/forgot_password")
    suspend fun forgot(
        @Field("email") email: String?
    ): DataSource<ForgotPasswordResponse>
}
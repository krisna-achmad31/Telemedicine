package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.network.service.forgotpassword.ForgotPasswordResponse
import com.telemedicine.indihealth.network.service.forgotpassword.ForgotPasswordService
import javax.inject.Inject

class ForgotPasswordClient @Inject constructor(
    private val forgotPasswordService: ForgotPasswordService
) {
    suspend fun forgotPassword(
        email: String,
        onResult: (response: ApiResponse<ForgotPasswordResponse>) -> Unit
    ) {
        forgotPasswordService.forgot(
            email
        ).toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }
}
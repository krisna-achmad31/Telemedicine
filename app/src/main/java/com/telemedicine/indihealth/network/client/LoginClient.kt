package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.network.service.login.LoginResponse
import com.telemedicine.indihealth.network.service.login.LoginService
import javax.inject.Inject

class LoginClient @Inject constructor(
    private val loginService: LoginService
) {
    suspend fun getLogin(
        email: String,
        password: String,
        onResult: (response: ApiResponse<LoginResponse>) -> Unit
    ) {
        loginService.getLogin(
            email, password
        ).toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }
}
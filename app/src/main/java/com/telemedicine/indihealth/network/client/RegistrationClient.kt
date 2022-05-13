package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.network.service.registration.RegistrationResponse
import com.telemedicine.indihealth.network.service.registration.RegistrationService
import javax.inject.Inject

class RegistrationClient @Inject constructor(
    private val registrationService: RegistrationService
) {
    suspend fun postRegistration(
        password: String,
        email: String,
        name: String,
        lahir_tanggal: String,
        id_user_kategori: String,
        aktif: String,
        username: String,
        lahir_tempat: String,
        jenis_kelamin: String,
        telephone: String,
        onResult: (response: ApiResponse<RegistrationResponse>) -> Unit
    ) {
        registrationService.postRegistration(
            password, email, name, lahir_tanggal, id_user_kategori, aktif, "", username, lahir_tempat, jenis_kelamin, "", "", "", telephone, ""
        ).toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }
}
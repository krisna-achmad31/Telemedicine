package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.network.service.login.LoginResponse
import com.telemedicine.indihealth.network.service.toc.TocResponse
import com.telemedicine.indihealth.network.service.toc.TocService
import com.telemedicine.indihealth.network.service.toc.UpdateTocResponse
import javax.inject.Inject

class TocClient @Inject constructor(
    private val mService: TocService
) {
    suspend fun postUpdateToc(
        patientId: String?,
        onResult: (response: ApiResponse<UpdateTocResponse>) -> Unit
    ){
        mService.postUpdateToc(patientId)
            .toResponseDataSource()
            .request(onResult)
    }

    suspend fun getPatientProfile(
        userId: String?,
        stringPassword: String?,
        onResult: (response: ApiResponse<LoginResponse>) -> Unit
    ) {
        mService
            .postLogin(userId,stringPassword)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getToc(
        userId: String,
        onResult: (response: ApiResponse<TocResponse>) -> Unit
    ) {
        mService
            .getToc(userId)
            .toResponseDataSource()
            .request(onResult)
    }
}
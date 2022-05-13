package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.model.GlobalResponse
import com.telemedicine.indihealth.network.service.doctor.consultation.ScheduleDoctorConsultationResponse
import com.telemedicine.indihealth.network.service.pharmacy.PharmacyService
import javax.inject.Inject

/**
 * Created by Surya Mahadi on 11/23/2021
 */
class PharmacyClient @Inject constructor(
    private val service: PharmacyService
) {

    suspend fun postAnswerCall(
        param: HashMap<String, String?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .postAnswerCall(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postRejectCall(
        param: HashMap<String, String?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        service
            .postRejectCall(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

}
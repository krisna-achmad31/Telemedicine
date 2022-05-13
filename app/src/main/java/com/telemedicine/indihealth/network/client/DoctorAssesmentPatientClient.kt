package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.network.service.assesment.DoctorAssesmentPatientResponse
import com.telemedicine.indihealth.network.service.assesment.DoctorAssesmentPatientService
import javax.inject.Inject

class DoctorAssesmentPatientClient @Inject constructor(
    private val mService: DoctorAssesmentPatientService
) {
    suspend fun getAssesmentPatient(
        id_dokter: String?,
        onResult: (response: ApiResponse<DoctorAssesmentPatientResponse>) -> Unit
    ) {
        mService
            .getAssesmentPatient(id_dokter)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

}
package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.network.service.schedule.DoctorScheduleResponse
import com.telemedicine.indihealth.network.service.schedule.DoctorScheduleService
import javax.inject.Inject

class DoctorScheduleClient @Inject constructor(
    private val mService: DoctorScheduleService
) {
    suspend fun getDoctorSchedule(
        id_dokter: String?,
        onResult: (response: ApiResponse<DoctorScheduleResponse>) -> Unit
    ) {
        mService
            .getDoctorSchedule(id_dokter)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

}
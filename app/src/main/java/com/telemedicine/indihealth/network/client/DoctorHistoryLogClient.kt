package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.network.service.historylog.DoctorHistoryLogResponse
import com.telemedicine.indihealth.network.service.historylog.DoctorHistoryLogService
import javax.inject.Inject

class DoctorHistoryLogClient @Inject constructor(
    private val mService: DoctorHistoryLogService
) {

    suspend fun getHistoryLog(
        id_dokter: String?,
        onResult: (queueResponse: ApiResponse<DoctorHistoryLogResponse>) -> Unit
    ) {
        mService
            .getHistoryLog(id_dokter)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

}
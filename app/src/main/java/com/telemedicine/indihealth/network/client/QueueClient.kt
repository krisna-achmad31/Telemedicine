package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.network.service.queue.QueueResponse
import com.telemedicine.indihealth.network.service.queue.QueueService
import javax.inject.Inject

class QueueClient @Inject constructor(
    private val mService: QueueService
) {

    suspend fun getQueue(
        id_pasien: String?,
        status: String,
        onResult: (queueResponse: ApiResponse<QueueResponse>) -> Unit
    ) {
        mService
            .getQueue(id_pasien, status)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

}
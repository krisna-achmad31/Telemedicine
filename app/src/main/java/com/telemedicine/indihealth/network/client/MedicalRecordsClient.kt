package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.network.service.records.MedicalRecordsDetailResponse
import com.telemedicine.indihealth.network.service.records.MedicalRecordsResponse
import com.telemedicine.indihealth.network.service.records.MedicalRecordsService
import javax.inject.Inject

class MedicalRecordsClient @Inject constructor(
    private val mService: MedicalRecordsService
) {
    suspend fun getMedicalRecords(
        id_dokter: String?,
        id_pasien: String?,

        onResult: (queueResponse: ApiResponse<MedicalRecordsResponse>) -> Unit
    ) {
        mService
            .getMedicalRecords(id_dokter, id_pasien)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getMedicalRecordsDetail(
        id_jadwal_konsultasi: String?,
        id_pasien: String?,
        onResult: (queueResponse: ApiResponse<MedicalRecordsDetailResponse>) -> Unit
    ) {
        mService
            .getMedicalRecordsDetail(id_jadwal_konsultasi, id_pasien)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

}
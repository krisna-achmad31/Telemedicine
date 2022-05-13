package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.network.service.history.HistoryPaymentService
import com.telemedicine.indihealth.network.service.history.payment.HistoryConsultationResponse
import com.telemedicine.indihealth.network.service.history.payment.HistoryDrugResponse
import com.telemedicine.indihealth.network.service.history.profile.ProfileDoctorResponse
import javax.inject.Inject

class HistoryPaymentClient @Inject constructor(
    private val mService: HistoryPaymentService
) {

    suspend fun getDoctorProfile(
        hashMap: HashMap<String?, String?>,
        onResult: (doctorResponse: ApiResponse<ProfileDoctorResponse>) -> Unit
    ) {
        mService
            .getDoctorProfile(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getHistoryPayment(
        id_pasien: String?,
        onResult: (response: ApiResponse<HistoryConsultationResponse>) -> Unit
    ) {
        mService
            .getHistoryPayment(id_pasien)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getHistoryPaymentDrug(
        id_pasien: String?,
        onResult: (response: ApiResponse<HistoryDrugResponse>) -> Unit
    ) {
        mService
            .getHistoryPaymentDrug(id_pasien)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

}
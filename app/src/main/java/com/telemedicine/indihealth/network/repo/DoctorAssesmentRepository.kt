package com.telemedicine.indihealth.network.repo

import com.skydoves.sandwich.*
import com.telemedicine.indihealth.model.DoctorAssesment
import com.telemedicine.indihealth.network.client.DoctorAssesmentPatientClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DoctorAssesmentRepository @Inject constructor(
    private val mClient: DoctorAssesmentPatientClient
) {
    suspend fun getAssesmentPatient(
        id_dokter: String?,
        onSuccess: (List<DoctorAssesment>?) -> Unit,
        onError: (String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        mClient.getAssesmentPatient(id_dokter) {
            Timber.d("response .... $it")
            it.onSuccess {
                if (data != null) {
                    if (data?.status!!) {
                        onSuccess(data?.data)
                    } else {
                        Timber.d("is error = ${data?.msg}")
                        onError(data?.msg)
                    }
                }
            }
                .onError {
                    Timber.d("onError")
                    onError(message())
                }
                .onException {
                    Timber.d("onException")
                    onError(message())
                }
                .onFailure {
                    Timber.d("onFailure")
                    onError("error")
                }
        }
    }

}


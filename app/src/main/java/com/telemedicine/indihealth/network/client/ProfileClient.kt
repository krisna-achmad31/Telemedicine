package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.network.service.profile.ProfileService
import com.telemedicine.indihealth.network.service.profile.UpdatePasswordResponse
import com.telemedicine.indihealth.network.service.profile.UpdateUsernameResponse
import com.telemedicine.indihealth.network.service.profile.doctor.DoctorResponse
import com.telemedicine.indihealth.network.service.profile.doctor.ProfileDoctorResponse
import com.telemedicine.indihealth.network.service.profile.patient.GlobalItemResponse
import com.telemedicine.indihealth.network.service.profile.patient.ProfilePatientResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileClient @Inject constructor(
    private val mService: ProfileService
) {

    suspend fun getPatientProfile(
        hashMap: HashMap<String?, String?>,
        onResult: (patientResponse: ApiResponse<ProfilePatientResponse>) -> Unit
    ) {
        mService
            .getPatientProfile(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postEditProfile(
        hashMap: HashMap<String, RequestBody?>,
        foto: MultipartBody.Part?,
        onResult: (response: ApiResponse<ProfilePatientResponse>) -> Unit
    ) {
        mService.postEditProfile(
            hashMap,
            foto
        ).toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getProfileDoctor(
        hashMap: HashMap<String, Any>,
        onResult: (response: ApiResponse<ProfileDoctorResponse>) -> Unit
    ) {
        mService
            .getProfileDoctor(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getDoctorProfile(
        hashMap: HashMap<String?, String?>,
        onResult: (response: ApiResponse<DoctorResponse>) -> Unit
    ) {
        mService
            .getDoctorProfile(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getProvinsi(
        hashMap: HashMap<String, String?>,
        onResult: (response: ApiResponse<GlobalItemResponse>) -> Unit
    ) {
        mService
            .getProvinsi(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getKota(
        hashMap: HashMap<String, String?>,
        onResult: (response: ApiResponse<GlobalItemResponse>) -> Unit
    ) {
        mService
            .getKota(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getKecamatan(
        hashMap: HashMap<String, String?>,
        onResult: (response: ApiResponse<GlobalItemResponse>) -> Unit
    ) {
        mService
            .getKecamatan(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getKelurahan(
        hashMap: HashMap<String, String?>,
        onResult: (response: ApiResponse<GlobalItemResponse>) -> Unit
    ) {
        mService
            .getKelurahan(hashMap)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postUpdateUsername(
        userId: String?,
        username: String?,
        password: String?,
        onResult: (response: ApiResponse<UpdateUsernameResponse>) -> Unit
    ) {
        mService
            .postUpdateUsername(userId,username,password)
            .toResponseDataSource()
            .request(onResult)
    }

    suspend fun postUpdatePassword(
        userId: String?,
        newPassword: String?,
        oldPassword: String?,
        onResult: (response: ApiResponse<UpdatePasswordResponse>) -> Unit
    ){
        mService.postUpdatePassword(newPassword,oldPassword,userId)
            .toResponseDataSource()
            .request(onResult)
    }

}
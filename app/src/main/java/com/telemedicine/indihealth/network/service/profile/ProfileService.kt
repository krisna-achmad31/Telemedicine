package com.telemedicine.indihealth.network.service.profile

import com.skydoves.sandwich.DataSource
import com.telemedicine.indihealth.network.service.profile.doctor.DoctorResponse
import com.telemedicine.indihealth.network.service.profile.doctor.ProfileDoctorResponse
import com.telemedicine.indihealth.network.service.profile.patient.GlobalItemResponse
import com.telemedicine.indihealth.network.service.profile.patient.ProfilePatientResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ProfileService {
    @GET("pasien/profile")
    suspend fun getPatientProfile(
        @QueryMap condition: HashMap<String?, String?>
    ): DataSource<ProfilePatientResponse>

    @Multipart
    @POST("User/manageUser")
    suspend fun postEditProfile(
        @PartMap param: HashMap<String, RequestBody?>,
        @Part foto: MultipartBody.Part?
    ): DataSource<ProfilePatientResponse>

    @GET("pasien/dokter/list")
    suspend fun getProfileDoctor(
        @QueryMap condition: HashMap<String, Any>
    ): DataSource<ProfileDoctorResponse>

    @GET("dokter/profile")
    suspend fun getDoctorProfile(
        @QueryMap condition: HashMap<String?, String?>
    ): DataSource<DoctorResponse>

//    Alamat/getProvinsiList

    @FormUrlEncoded
    @POST("Alamat/getProvinsiList")
    suspend fun getProvinsi(
        @FieldMap condition: HashMap<String, String?>
    ): DataSource<GlobalItemResponse>

    @FormUrlEncoded
    @POST("Alamat/getKotKabList")
    suspend fun getKota(
        @FieldMap condition: HashMap<String, String?>
    ): DataSource<GlobalItemResponse>

    @FormUrlEncoded
    @POST("Alamat/getKecamatanList")
    suspend fun getKecamatan(
        @FieldMap condition: HashMap<String, String?>
    ): DataSource<GlobalItemResponse>

    @FormUrlEncoded
    @POST("Alamat/getKelurahanList")
    suspend fun getKelurahan(
        @FieldMap condition: HashMap<String, String?>
    ): DataSource<GlobalItemResponse>

    @FormUrlEncoded
    @POST("User/updateUsername")
    suspend fun postUpdateUsername(
        @Field("user_id") userId: String?,
        @Field("username") username: String?,
        @Field("password") password: String?
    ): DataSource<UpdateUsernameResponse>

    @FormUrlEncoded
    @POST("User/updatePassword")
    suspend fun postUpdatePassword(
        @Field("new_password") newPassword: String?,
        @Field("old_password") oldPassword: String?,
        @Field("user_id") userId: String?
    ): DataSource<UpdatePasswordResponse>
}
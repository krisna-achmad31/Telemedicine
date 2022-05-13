package com.telemedicine.indihealth.network.service.registration

import com.skydoves.sandwich.DataSource
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegistrationService {
    //API Registration
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("User/registrasi")
    suspend fun postRegistration(
        @Field("password") password: String?,
        @Field("email") email: String?,
        @Field("name") name: String?,
        @Field("lahir_tanggal") birth_date: String?,
        @Field("id_user_kategori") category: String?,
        @Field("aktif") active: String?,
        @Field("foto") photo: String?,
        @Field("username") username: String?,
        @Field("lahir_tempat") birth_place: String?,
        @Field("jenis_kelamin") gender: String?,
        @Field("alamat_jalan") jalan: String?,
        @Field("alamat_kota") kota: String?,
        @Field("alamat_provinsi") provinsi: String?,
        @Field("telp") telp: String?,
        @Field("id_fasyankes") id_fasyankes: String?
    ): DataSource<RegistrationResponse>

}
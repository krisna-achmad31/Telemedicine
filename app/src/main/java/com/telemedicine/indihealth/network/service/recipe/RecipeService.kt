package com.telemedicine.indihealth.network.service.recipe

import com.skydoves.sandwich.DataSource
import com.telemedicine.indihealth.model.GlobalResponse
import com.telemedicine.indihealth.network.service.consultation.payment.OwlexaGenerateOtpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RecipeService {
    @FormUrlEncoded
    @POST("pasien/resep/list")
    suspend fun getRecipe(
        @Field("id_pasien") id_pasien: String?
    ): DataSource<RecipeResponse>

    @FormUrlEncoded
    @POST("pasien/resep/detail")
    suspend fun getRecipeDetail(
        @Field("id_jadwal_konsultasi") id_jadwal_konsultasi: String?
    ): DataSource<RecipeDetailResponse>

    @Multipart
    @POST("pasien/resep/bukti")
    suspend fun postPayment(
        @PartMap param: HashMap<String?, RequestBody?>,
        @Part foto: MultipartBody.Part?
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @POST("pasien/resep/hapusBukti")
    suspend fun deletePayment(
        @FieldMap condition: HashMap<String?, Any?>?
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @POST("pasien/resep/batalkanResep")
    suspend fun cancelRecipe(
        @FieldMap condition: HashMap<String?, Any?>?
    ): DataSource<GlobalResponse>

    @FormUrlEncoded
    @POST("owlexa/Api/generateOtp")
    suspend fun postOwlexaGenerateotp(
        @FieldMap condition: HashMap<String?, String?>
    ): DataSource<OwlexaGenerateOtpResponse>

    @FormUrlEncoded
    @POST("owlexa/Api/verification_obat")
    suspend fun postOwlexaVerificationObat(
        @FieldMap condition: HashMap<String?, String?>
    ): DataSource<OwlexaVerificationObatResponse>

}
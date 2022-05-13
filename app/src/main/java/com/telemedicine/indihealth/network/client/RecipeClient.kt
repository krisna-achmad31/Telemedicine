package com.telemedicine.indihealth.network.client

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import com.telemedicine.indihealth.model.GlobalResponse
import com.telemedicine.indihealth.network.service.consultation.payment.OwlexaGenerateOtpResponse
import com.telemedicine.indihealth.network.service.recipe.OwlexaVerificationObatResponse
import com.telemedicine.indihealth.network.service.recipe.RecipeDetailResponse
import com.telemedicine.indihealth.network.service.recipe.RecipeResponse
import com.telemedicine.indihealth.network.service.recipe.RecipeService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RecipeClient @Inject constructor(
    private val mService: RecipeService
) {
    suspend fun getRecipe(
        id_pasien: String?,
        onResult: (response: ApiResponse<RecipeResponse>) -> Unit
    ) {
        mService
            .getRecipe(id_pasien)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun getRecipeDetail(
        id_jadwal_konsultasi: String?,
        onResult: (response: ApiResponse<RecipeDetailResponse>) -> Unit
    ) {
        mService
            .getRecipeDetail(id_jadwal_konsultasi)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postPayment(
        param: HashMap<String?, RequestBody?>,
        foto: MultipartBody.Part?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        mService
            .postPayment(param,foto)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun deletePayment(
        param: HashMap<String?,Any?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        mService
            .deletePayment(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun cancelRecipe(
        param: HashMap<String?,Any?>?,
        onResult: (response: ApiResponse<GlobalResponse>) -> Unit
    ) {
        mService
            .cancelRecipe(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postOwlexaGenerateOtp(
        param: HashMap<String?,String?>,
        onResult: (response: ApiResponse<OwlexaGenerateOtpResponse>) -> Unit
    ) {
        mService
            .postOwlexaGenerateotp(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }

    suspend fun postOwlexaVerificationObat(
        param: HashMap<String?,String?>,
        onResult: (response: ApiResponse<OwlexaVerificationObatResponse>) -> Unit
    ) {
        mService
            .postOwlexaVerificationObat(param)
            .toResponseDataSource()
            // request API network call asynchronously.
            .request(onResult)
    }


}
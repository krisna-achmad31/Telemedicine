package com.telemedicine.indihealth.network.service.recipe

import com.telemedicine.indihealth.model.RecipeDetail

data class RecipeDetailResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:List<RecipeDetail>
)
package com.telemedicine.indihealth.network.service.recipe

import com.telemedicine.indihealth.model.Recipe

data class RecipeResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:List<Recipe>
)
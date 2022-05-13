package com.telemedicine.indihealth.network.service.main

import com.telemedicine.indihealth.model.News

data class NewsResponse(
    val status:Boolean=false,
    val message:String="",
    val data:List<News>
)
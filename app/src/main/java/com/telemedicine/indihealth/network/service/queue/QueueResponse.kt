package com.telemedicine.indihealth.network.service.queue

import com.telemedicine.indihealth.model.Queue

data class QueueResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:List<Queue>
)
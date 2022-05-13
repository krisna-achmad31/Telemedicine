package com.telemedicine.indihealth.network.service.history.payment

import com.telemedicine.indihealth.model.HistoryDrug

data class HistoryDrugResponse(
    val status:Boolean? =false,
    val msg:String?="",
    val data:List<HistoryDrug>
)
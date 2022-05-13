package com.telemedicine.indihealth.model

/**
 * Created by Surya Mahadi on 11/28/2021
 */
data class AdditionalInfo(
    val id: String? = "",
    val id_jadwal_konsultasi: String? = "",
    val planning: String? = "",
    val laboratorium: String? = "",
    val radiologi: String? = "",
    val pemeriksaan: String? = "",
    val kesimpulan: String? = ""
)

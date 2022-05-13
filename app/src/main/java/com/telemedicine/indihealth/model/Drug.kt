package com.telemedicine.indihealth.model

import java.text.NumberFormat
import java.util.*

data class Drug (

	val id : String?,
	val id_jadwal_konsultasi : String?,
	val id_pasien : String?,
	val id_dokter : String?,
	val id_obat : String?,
	val jumlah_obat : String?,
	val keterangan : String?,
	val dibatalkan : String?,
	val created_at : String?,
    val dirilis:String?,
    val diverifikasi:String?,
    val order_status:String?,
	val updated_at : String?,
	val nama_obat : String?,
	val detail_obat : String?,
	val harga_per_n_unit : Int = 0,
	val harga : Int = 0,
    val unit : String?

) {
    val getStatusDibatalkan: String
        get() {
            return when (dibatalkan) {
                "0" -> "Tidak Dibatalkan"
                else -> "Dibatalkan"
            }
        }


    val getStatusVerifikasi: String
        get() {
            return when (diverifikasi) {
                "1" -> "SUDAH VERIFIKASI"
                else -> "BELUM VERIFIKASI"
            }
        }


    val getStatusObat: String
        get() {
            var status = ""
            status += if(diverifikasi.equals("1")){
                "SUDAH VERIFIKASI"
            }else {
                "BELUM VERIFIKASI"
            }

            if(diverifikasi.equals("1")){
                status += if(dirilis.equals("1")){
                    " & SUDAH RILIS"
                } else{
                    " & BELUM RILIS"
                }
            }

            if(dibatalkan.equals("1")){
                status = "DIBATALKAN"
            }
            return status
        }

    val getStatusRilis: String
        get() {
            return when (dirilis) {
                "1" -> "SUDAH RILIS"
                else -> "BELUM RILIS"
            }
        }


    val getStatusOrder: String
        get() {
            return when (diverifikasi) {
                "1" -> "DELIVERID"
                else -> "PENDING"
            }
        }
    val getPrice: String
        get() {
            val localeID = Locale("in", "ID")
            val format = NumberFormat.getCurrencyInstance(localeID)
            val hasil1 = format.format(harga)
            return (hasil1 + " / " + harga_per_n_unit)
        }
    val getTotalDrug: String
        get() = jumlah_obat + " " + unit
}
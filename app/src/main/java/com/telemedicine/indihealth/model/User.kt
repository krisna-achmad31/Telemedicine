package com.telemedicine.indihealth.model

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class User(
    val id: String? = "",
    val str: String? = "",
    val email: String? = "",
    var username: String? = "",
    val password: String? = "",
    val web_token: String? = "",
    val mobile_token: String? = "",
    val reg_id: String? = "",
    val nik: String = "",
    val name: String = "",
    val nama_ibu: String = "",
    val nama_ayah: String = "",
    val nama_pj: String = "",
    val telp_pj: String = "",
    val hubungan_pj: String = "",
    val alamat_jalan_pj: String = "",
    val alamat_kelurahan_pj: String = "",
    val alamat_kecamatan_pj: String = "",
    val alamat_kota_pj: String = "",
    val alamat_provinsi_pj: String = "",
    val kode_pos_pj: String = "",
    val lahir_tempat: String? = "",
    val lahir_tanggal: String? = "",
    val jenis_kelamin: String? = "",
    val alamat_jalan: String? = "",
    val alamat_kelurahan: String? = "",
    val alamat_kecamatan: String? = "",
    val alamat_kota: String? = "",
    val alamat_provinsi: String? = "",
    val kode_pos: String? = "",
    val telp: String? = "",
    val id_user_kategori: String? = "",
    val id_user_level: String? = "",
    val id_fasyankes: String? = "",
    val foto: String? = "",
    val aktif: String? = "",
    val created_at: String? = "",
    val updated_at: String? = "",
    val updated_by:String? = "",
    val pengalaman_kerja: String?="",
    val poli: String?="",
    val role: String? = "",
    val no_medrec: String? = "",
    val fasyankes: Fasyankes? = null,
    val provinsi: GlobalItem? = null,
    val kota: GlobalItem? = null,
    val kecamatan: GlobalItem? = null,
    val kelurahan: GlobalItem? = null,
    val provinsi_pj: GlobalItem? = null,
    val kota_pj: GlobalItem? = null,
    val kecamatan_pj: GlobalItem? = null,
    val kelurahan_pj: GlobalItem? = null,
    val toc: String? = null,
    val durasi: String? = "",
    val use_diagnosa: String? = "0"
) {
    val getName: String
        get() = name.capitalize(Locale("in", "ID"))
    val getPengalaman: String
        get() = pengalaman_kerja + " Tahun"
    val getTempatTanggalLahir: String
        get() {
//            var dateText = lahir_tanggal
//            try {
//                val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
//                val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
//                val inputDateStr = lahir_tanggal
//                val date: Date? = inputFormat.parse(inputDateStr)
//                dateText = outputFormat.format(date)
//            }catch (e : Exception){
//
//            }
            return "$lahir_tempat, $lahir_tanggal"
        }
    val getAlamat: String
        get() {
            if (kelurahan?.name == null){
                return ""
            } else {
                return alamat_jalan + ", " + kelurahan.name + ", " + kecamatan?.name + ", " + kota?.name + ", " + provinsi?.name + ", " + kode_pos
            }
        }


//    val getAlamatPj: String
//        get() = alamat_jalan_pj + ", " + alamat_kelurahan_pj + ", " + alamat_kecamatan_pj + ", " + alamat_kota_pj + ", " + alamat_provinsi_pj + ", " + kode_pos_pj

    val getAlamatPj: String
        get() = "$alamat_jalan_pj, $alamat_kelurahan_pj, $alamat_kecamatan_pj, $alamat_kota_pj, $alamat_provinsi_pj, $kode_pos_pj"

    val getTanggalLahir: String?
        get() {
            var dateText = lahir_tanggal
            try {
                val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                val inputDateStr = lahir_tanggal
                val date: Date? = inputFormat.parse(inputDateStr)
                dateText = outputFormat.format(date)
            }catch (e : Exception){

            }
            return dateText
        }
}
package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Doctor(
    val id: String? = "",
    val str: String? = "",
    val email: String? = "",
    val username: String? = "",
    val password: String? = "",
    val web_token: String? = "",
    val mobile_token: String? = "",
    val reg_id: String? = "",
    val name: String = "",
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
    val pengalaman_kerja: String? = "",
    val poli: String? = "",
    val provinsi: String? = "",
    val kota: String? = "",
    val kecamatan: String? = "",
    val kelurahan: String? = ""
) : Parcelable {
    val getName: String
        get() = name.capitalize(Locale("in", "ID"))
    val getPengalaman: String
        get() = pengalaman_kerja + " Tahun"
    val getTempatTanggalLahir: String
        get() = lahir_tempat + "/" + lahir_tanggal
    val getAlamat: String
        get() = alamat_jalan + ", " + kelurahan + ", " + kecamatan + ", " + kota + ", " + provinsi + ", " + kode_pos
}
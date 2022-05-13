package com.telemedicine.indihealth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ProfileDoctor(
    val id: String? = "",
    val str: String? = "",
    val email: String? = "",
    val username: String? = "",
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
    val id_fasyankes: String? = "",
    val foto: String? = "",
    val aktif: String? = "",
    val created_at: String? = "",
    val pengalaman_kerja: String? = "",
    val poli: String? = "",
    val durasi: String? = "",
    val provinsi: GlobalItem? = null,
    val kota: GlobalItem? = null,
    val kecamatan: GlobalItem? = null,
    val kelurahan: GlobalItem? = null
) : Parcelable {
    val getName: String
        get() = name.capitalize(Locale("in", "ID"))
    val getPengalaman: String
        get() = pengalaman_kerja + " Tahun"
    val getTempatTanggalLahir: String
        get() = lahir_tempat + "/" + lahir_tanggal
    val getAlamat: String
        get() = alamat_jalan + ", " + kelurahan?.name + ", " + kecamatan?.name + ", " + kota?.name + ", " + provinsi?.name + ", " + kode_pos
}
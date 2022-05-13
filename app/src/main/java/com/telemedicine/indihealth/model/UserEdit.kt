package com.telemedicine.indihealth.model

data class UserEdit(
    var password: String? = "",
    var email: String? = "",
    var name: String? = "",
    var lahir_tanggal: String? = "",
    var id_user_kategori: String? = "",
    var aktif: String? = "",
    var foto: String? = "",
    var username: String? = "",
    var lahir_tempat: String? = "",
    var jenis_kelamin: String? = "",
    var alamat_jalan: String? = "",
    var alamat_kelurahan: String? = "",
    var alamat_kecamatan: String? = "",
    var alamat_kota: String? = "",
    var alamat_provinsi: String? = "",
    var telp: String? = "",
    var id_fasyankes: String? = ""
)
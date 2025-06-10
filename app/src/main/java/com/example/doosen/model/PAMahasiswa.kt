package com.example.doosen.model

import com.example.doosen.model.InfoSetoranMahasiswa
import com.google.gson.annotations.SerializedName


data class UserData(
    val nip: String,
    val nama: String,
    val email: String,
    val info_mahasiswa_pa: MahasiswaPAInfo
)

data class MahasiswaPAInfo(
    val ringkasan: List<RingkasanAngkatan>,
    val daftar_mahasiswa: List<MahasiswaPA>
)

data class RingkasanAngkatan(
    val tahun: String,
    val total: Int
)

data class MahasiswaPA(
    val email: String,
    val nim: String,
    val nama: String,
    val angkatan: String,
    val semester: Int,
    val info_setoran: InfoSetoran
)



data class UserInfo(
    val data: UserData,
    val nip: String?,
    val nama: String?,
    val email: String?,
    @SerializedName("info_mahasiswa_pa")
    val infoMahasiswaPa: InfoMahasiswaPa?
)

data class InfoMahasiswaPa(
    val ringkasan: List<Ringkasan>?,
    @SerializedName("daftar_mahasiswa")
    val daftarMahasiswa: List<Mahasiswa>?
)

data class Ringkasan(
    val tahun: String,
    val total: Int
)

data class Mahasiswa(
    val email: String,
    val nim: String,
    val nama: String,
    val angkatan: String,
    val semester: Int,
    @SerializedName("info_setoran")
    val info_setoran: InfoSetoranMahasiswa?
)




data class InfoSetoran(
    @SerializedName("total_wajib_setor")
    val totalWajibSetor: Int?,
    @SerializedName("total_sudah_setor")
    val totalSudahSetor: Int?,
    @SerializedName("total_belum_setor")
    val totalBelumSetor: Int?,
    @SerializedName("persentase_progres_setor")
    val persentaseProgresSetor: Double?,
    @SerializedName("tgl_terakhir_setor")
    val tglTerakhirSetor: String?,
    @SerializedName("terakhir_setor")
    val terakhirSetor: String?
)

data class UserInfoResponse(
    val response: Boolean,
    val message: String,
    val data: UserInfo?
)
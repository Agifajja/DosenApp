package com.example.doosen.model


data class DetailMahasiswaResponse(
    val response: Boolean,
    val message: String,
    val data: DetailMahasiswaData
)

data class DetailMahasiswaData(
    val info: MahasiswaInfo,
    val setoran: SetoranDetail
)

data class MahasiswaInfo(
    val nama: String,
    val nim: String,
    val email: String,
    val angkatan: String,
    val semester: Int,
    val dosen_pa: DosenPA
)


data class DosenPA(
    val nip: String,
    val nama: String,
    val email: String
)

data class SetoranDetail(
    val info_dasar: InfoSetoran,
    val ringkasan: List<RingkasanKategori>,
    val detail: List<DetailKomponenSetoran>,
    val log: List<LogSetoran>
)

data class RingkasanKategori(
    val label: String,
    val total_wajib_setor: Int,
    val total_sudah_setor: Int,
    val total_belum_setor: Int,
    val persentase_progres_setor: Double
)

data class DetailKomponenSetoran(
    val id: String,
    val nama: String,
    val label: String,
    val sudah_setor: Boolean,
    val info_setoran: InfoSetoranDetail? = null
)

data class InfoSetoranDetail(
    val id: String,
    val tgl_setoran: String,
    val tgl_validasi: String,
    val dosen_yang_mengesahkan: DosenPA
)

data class LogSetoran(
    val id: Int,
    val keterangan: String,
    val aksi: String,
    val ip: String,
    val user_agent: String,
    val timestamp: String,
    val nim: String,
    val dosen_yang_mengesahkan: DosenPA
)
data class InfoSetoranMahasiswa(
    val total_wajib_setor: Int,
    val total_sudah_setor: Int,
    val total_belum_setor: Int,
    val persentase_progres_setor: Double,
    val tgl_terakhir_setor: String,
    val terakhir_setor: String
)


data class SimpanSetoranRequest(
    val data_setoran: List<KomponenSetoranRequest>,
    val tgl_setoran: String? = null
)

data class KomponenSetoranRequest(
    val nama_komponen_setoran: String,
    val id_komponen_setoran: String
)

data class SimpanSetoranResponse(
    val response: Boolean,
    val message: String
)

data class HapusSetoranRequest(
    val data_setoran: List<HapusKomponen>
)


data class HapusKomponen(
    val id: String,
    val id_komponen_setoran: String,
    val nama_komponen_setoran: String
)


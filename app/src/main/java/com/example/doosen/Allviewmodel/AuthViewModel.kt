package com.example.doosen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doosen.network.DataStoreManager
import com.example.doosen.model.*
import com.example.doosen.network.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

enum class LoadingStatus {
    LOADING, SUCCESS, ERROR
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    var token by mutableStateOf("")

    private val _nama = MutableStateFlow("Nama Tidak Diketahui")
    val nama: StateFlow<String> = _nama.asStateFlow()

    private val _email = MutableStateFlow("Email Tidak Diketahui")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _nip = MutableStateFlow("NIP Tidak Diketahui")
    val nip: StateFlow<String> = _nip.asStateFlow()

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error.asStateFlow()

    private val _status = MutableStateFlow(LoadingStatus.SUCCESS)
    val status: StateFlow<LoadingStatus> = _status.asStateFlow()

    private val _ringkasan = MutableStateFlow<List<Ringkasan>>(emptyList())
    val ringkasan: StateFlow<List<Ringkasan>> = _ringkasan.asStateFlow()

    private val _daftarMahasiswa = MutableStateFlow<List<Mahasiswa>>(emptyList())
    val daftarMahasiswa: StateFlow<List<Mahasiswa>> = _daftarMahasiswa.asStateFlow()


    private val _detailMahasiswa = MutableStateFlow<DetailMahasiswaResponse?>(null)
    val detailMahasiswa: StateFlow<DetailMahasiswaResponse?> = _detailMahasiswa.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.token.collect { savedToken ->
                token = savedToken ?: ""
                if (token.isNotEmpty()) {
                    try {
                        fetchUserInfo(token)
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Error during init: ${e.message}")
                        _error.value = "Gagal memuat data: ${e.message}"
                    }
                }
            }
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            if (pass.isBlank()) {
                _error.value = "Password tidak boleh kosong"
                return@launch
            }
            updateStatus(LoadingStatus.LOADING)
            try {
                val tokenResult = ApiClient.authService.loginDosen(
                    grantType = "password",
                    clientId = "setoran-mobile-dev",
                    clientSecret = "aqJp3xnXKudgC7RMOshEQP7ZoVKWzoSl",
                    username = email,
                    password = pass,
                    scope = "openid profile email"
                )

                val accessToken = tokenResult.accessToken
                token = accessToken
                dataStoreManager.saveToken(accessToken)
                dataStoreManager.saveRefreshToken(tokenResult.refreshToken)

                _error.value = ""
                fetchUserInfo(accessToken)
                updateStatus(LoadingStatus.SUCCESS)
            } catch (e: HttpException) {
                _error.value = "Login gagal: ${e.response()?.errorBody()?.string() ?: e.message()}"
                updateStatus(LoadingStatus.ERROR)
            } catch (e: Exception) {
                _error.value = "Login gagal: ${e.message}"
                updateStatus(LoadingStatus.ERROR)
            }
        }
    }

    fun fetchUserInfo(token: String) {
        viewModelScope.launch {
            updateStatus(LoadingStatus.LOADING)
            try {
                val response = ApiClient.apiService.getUserProfile("Bearer $token")
                if (response.isSuccessful) {
                    val profile = response.body()?.data
                    _nama.value = profile?.nama ?: "Nama Tidak Diketahui"
                    _email.value = profile?.email ?: "Email Tidak Diketahui"
                    _nip.value = profile?.nip ?: "NIP Tidak Diketahui"

                    profile?.infoMahasiswaPa?.let {
                        _ringkasan.value = it.ringkasan ?: emptyList()
                        _daftarMahasiswa.value = it.daftarMahasiswa ?: emptyList()
                    }

                    updateStatus(LoadingStatus.SUCCESS)
                } else {
                    _error.value = "Gagal mengambil profil: ${response.message()}"
                    updateStatus(LoadingStatus.ERROR)
                }
            } catch (e: Exception) {
                _error.value = "Terjadi kesalahan: ${e.message}"
                updateStatus(LoadingStatus.ERROR)
            }
        }
    }


    fun fetchDetailMahasiswa(nim: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getDetailMahasiswa("Bearer $token", nim)
                if (response.isSuccessful) {
                    _detailMahasiswa.value = response.body()
                } else {
                    _error.value = "Gagal ambil detail mahasiswa: ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Gagal ambil detail mahasiswa: ${e.message}")
                _error.value = "Gagal ambil detail mahasiswa: ${e.message}"
            }
        }
    }

    fun validasiKomponenSetoran(id: String, nama: String, nim: String) {
        viewModelScope.launch {
            try {
                val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    .format(java.util.Date())

                val body = SimpanSetoranRequest(
                    data_setoran = listOf(
                        KomponenSetoranRequest(
                            nama_komponen_setoran = nama,
                            id_komponen_setoran = id
                        )
                    ),
                    tgl_setoran = currentDate
                )

                val response = ApiClient.apiService.simpanSetoran("Bearer $token", nim, body)

                _error.value = response.body()?.message ?: "Gagal menyimpan setoran"

                if (response.isSuccessful && response.body()?.response == true) {
                    fetchDetailMahasiswa(nim)
                }

            } catch (e: Exception) {
                Log.e("AuthViewModel", "Gagal validasi: ${e.message}")
                _error.value = "Gagal validasi: ${e.message}"
            }
        }
    }

    fun hapusKomponenSetoran(nim: String, komponen: HapusKomponen) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.hapusSetoran(
                    token = "Bearer $token",
                    nim = nim,
                    body = HapusSetoranRequest(listOf(komponen))
                )

                _error.value = response.body()?.message ?: "Gagal hapus setoran"
                if (response.isSuccessful && response.body()?.response == true) {
                    fetchDetailMahasiswa(nim)
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Gagal hapus: ${e.message}")
                _error.value = "Gagal hapus: ${e.message}"
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            try {
                dataStoreManager.clear()
                token = ""
                _nama.value = "Nama Tidak Diketahui"
                _email.value = "Email Tidak Diketahui"
                _nip.value = "NIP Tidak Diketahui"
                _ringkasan.value = emptyList()
                _daftarMahasiswa.value = emptyList()
                _error.value = ""
                _status.value = LoadingStatus.SUCCESS
                onLoggedOut()
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Gagal logout: ${e.message}")
                _error.value = "Logout gagal: ${e.message}"
            }
        }
    }

    private fun updateStatus(newStatus: LoadingStatus) {
        _status.value = newStatus
    }

    fun setError(message: String) {
        _error.value = message
    }

    fun clearError() {
        _error.value = ""
    }
}

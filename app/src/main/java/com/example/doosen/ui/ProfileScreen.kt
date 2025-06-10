package com.example.doosen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.doosen.AuthViewModel
import com.example.doosen.LoadingStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilScreen(nav: NavHostController, vm: AuthViewModel = hiltViewModel()) {
    val nama by vm.nama.collectAsState()
    val email by vm.email.collectAsState()
    val nip by vm.nip.collectAsState()
    val ringkasan by vm.ringkasan.collectAsState()
    val status by vm.status.collectAsState()
    val error by vm.error.collectAsState()

    val cardColor = MaterialTheme.colorScheme.surfaceVariant
    val emerald = Color(0xFF00695C)

    Scaffold(
        containerColor = Color(0xFFF9FDFD)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (status) {
                LoadingStatus.LOADING -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(Modifier.weight(1f))
                        CircularProgressIndicator()
                        Text("Memuat data profil...", modifier = Modifier.padding(top = 8.dp))
                        Spacer(Modifier.weight(1f))
                    }
                }

                LoadingStatus.ERROR -> {
                    Text("Terjadi kesalahan: $error", color = MaterialTheme.colorScheme.error)
                }

                else -> {
                    // ✅ Card: Informasi Dosen + Ringkasan Mahasiswa
                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 🧑‍🏫 Ikon Profil
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Foto Dosen",
                                modifier = Modifier
                                    .size(80.dp),
                                tint = emerald
                            )

                            Spacer(Modifier.height(12.dp))
                            Text(nama, fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                            Text("NIP: $nip", style = MaterialTheme.typography.bodySmall)
                            Text(email, style = MaterialTheme.typography.bodySmall)

                            Spacer(Modifier.height(20.dp))

                            Text("📚 Ringkasan Mahasiswa PA", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 16.sp)
                            Spacer(Modifier.height(8.dp))

                            if (ringkasan.isEmpty()) {
                                Text("Belum ada data ringkasan mahasiswa.", style = MaterialTheme.typography.bodyMedium)
                            } else {
                                ringkasan.forEach {
                                    Text("• Angkatan ${it.tahun}: ${it.total} mahasiswa", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // 🔴 Tombol Logout
                    Button(
                        onClick = {
                            vm.logout {
                                nav.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Keluar", fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
                    }
                }
            }
        }
    }
}

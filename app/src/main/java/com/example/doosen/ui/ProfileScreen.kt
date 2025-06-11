package com.example.doosen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.doosen.AuthViewModel
import com.example.doosen.LoadingStatus


@Composable
fun ProfilScreen(nav: NavHostController, vm: AuthViewModel = hiltViewModel()) {
    val nama by vm.nama.collectAsState()
    val email by vm.email.collectAsState()
    val nip by vm.nip.collectAsState()
    val ringkasan by vm.ringkasan.collectAsState()
    val status by vm.status.collectAsState()
    val error by vm.error.collectAsState()

    val emerald = Color(0xFF00695C)

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2C786C), Color(0xFFA9C9C3))
    )

    Scaffold(containerColor = Color.Transparent) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundGradient)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                when (status) {
                    LoadingStatus.LOADING -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Memuat data profil...")
                        }
                    }

                    LoadingStatus.ERROR -> {
                        Text("Terjadi kesalahan: $error", color = MaterialTheme.colorScheme.error)
                    }

                    else -> {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(90.dp)
                                            .background(Color.White, shape = CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = "Foto Dosen",
                                            tint = emerald,
                                            modifier = Modifier.size(80.dp)
                                        )
                                    }

                                    Spacer(Modifier.height(12.dp))
                                    Text(nama, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    Text("NIP: $nip", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    Text(email, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                                    Spacer(Modifier.height(20.dp))

                                    Text(
                                        "📚 Ringkasan Mahasiswa PA",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    if (ringkasan.isEmpty()) {
                                        Text(
                                            "Belum ada data ringkasan mahasiswa.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray
                                        )
                                    } else {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            ringkasan.forEach {
                                                Text(
                                                    "• Angkatan ${it.tahun}: ${it.total} mahasiswa",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Tombol Logout di bawah
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
                                .padding(top = 16.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD32F2F),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Keluar", fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}
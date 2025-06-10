package com.example.doosen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.doosen.model.DetailKomponenSetoran
import com.example.doosen.model.DetailMahasiswaData
import com.example.doosen.model.HapusKomponen
import com.example.doosen.model.LogSetoran
import com.example.doosen.AuthViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMahasiswaScreen(nim: String, vm: AuthViewModel = hiltViewModel()) {
    val detail = vm.detailMahasiswa.collectAsState().value
    val errorMessage = vm.error.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTabIndex by remember { mutableStateOf(0) }

    val emerald = Color(0xFF00695C)

    val tabs = listOf(
        TabItem("Profil", Icons.Default.AccountCircle),
        TabItem("Setoran", Icons.Default.ListAlt)
    )

    LaunchedEffect(nim) { vm.fetchDetailMahasiswa(nim) }
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotBlank()) {
            snackbarHostState.showSnackbar(errorMessage)
            vm.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Detail Mahasiswa", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = emerald)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            detail?.data?.let { data ->
                Box(modifier = Modifier.weight(1f)) {
                    when (selectedTabIndex) {
                        0 -> ProfilMahasiswaTab(data)
                        1 -> SetoranMahasiswaTab(
                            detailList = data.setoran.detail,
                            nim = data.info.nim,
                            onValidasiClicked = { id, nama, nim ->
                                vm.validasiKomponenSetoran(id, nama, nim)
                            },
                            onHapusClicked = { id, nama, nim, infoId ->
                                if (infoId != null) {
                                    vm.hapusKomponenSetoran(
                                        nim = nim,
                                        komponen = HapusKomponen(
                                            id = infoId,
                                            id_komponen_setoran = id,
                                            nama_komponen_setoran = nama
                                        )
                                    )
                                } else {
                                    vm.setError("ID info setoran tidak ditemukan.")
                                }
                            }
                        )
                    }
                }

                // 🔻 Bottom Button Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val isSelected = selectedTabIndex == index
                        Button(
                            onClick = { selectedTabIndex = index },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) emerald else Color.LightGray,
                                contentColor = if (isSelected) Color.White else Color.Black
                            ),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .height(46.dp)
                        ) {
                            Icon(tab.icon, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(tab.title, fontSize = 14.sp)
                        }
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

data class TabItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun ProfilMahasiswaTab(data: DetailMahasiswaData) {
    val info = data.info
    val setoran = data.setoran.info_dasar
    val ringkasan = data.setoran.ringkasan
    val total = setoran.totalWajibSetor ?: 0
    val sudah = setoran.totalSudahSetor ?: 0
    val progress = if (total > 0) sudah.toFloat() / total else 0f

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        // 🎉 Card Sambutan
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF00695C)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("PROGRES SETORAN ,", color = Color.White)
                Text(info.nama, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                Text("NIM : ${info.nim}", color = Color.White.copy(alpha = 0.9f))
                Text("EMAIL : ${info.email}", color = Color.White.copy(alpha = 0.9f))
                Text("ANGKATAN :  ${info.angkatan}" , color = Color.White.copy(alpha = 0.9f))
                Text("SEMESTER :  ${info.semester}" , color = Color.White.copy(alpha = 0.9f))
            }
        }

        // 🔄 Progress Setoran Total
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Progress Setoran", fontWeight = FontWeight.SemiBold)
                    Text("$sudah dari $total sudah disetor")
                }

                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(58.dp),
                    strokeWidth = 5.dp,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.LightGray
                )
            }
        }

        // 📊 Ringkasan Per Komponen
        Text("Progres", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        if (ringkasan.isEmpty()) {
            Text("Belum ada data progres", style = MaterialTheme.typography.bodyLarge)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ringkasan.forEach { item ->
                    val value = if (item.total_wajib_setor > 0)
                        item.total_sudah_setor.toFloat() / item.total_wajib_setor
                    else 0f
                    val percent = (value * 100).toInt()

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.label, fontWeight = FontWeight.Bold)
                                Text("~ $percent%")
                            }
                            Text("Wajib: ${item.total_wajib_setor}, Sudah: ${item.total_sudah_setor}, Belum: ${item.total_belum_setor}")

                            LinearProgressIndicator(
                                progress = { value },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .height(8.dp),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = Color.LightGray
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    "${item.total_sudah_setor}/${item.total_wajib_setor}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SetoranMahasiswaTab(
    detailList: List<DetailKomponenSetoran>,
    nim: String,
    onValidasiClicked: (id: String, nama: String, nim: String) -> Unit,
    onHapusClicked: (id: String, nama: String, nim: String, infoId: String?) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(detailList) { item ->
            val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
            val statusColor = if (item.sudah_setor) Color(0xFF4CAF50) else Color(0xFFF44336)
            val statusText = if (item.sudah_setor) "✅ Sudah disetor" else "❌ Belum disetor"

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${item.nama} (${item.label})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )


                    Spacer(modifier = Modifier.height(8.dp))

                    Spacer(Modifier.height(4.dp))
                    Text(statusText, color = statusColor)

                    item.info_setoran?.let {
                        Spacer(Modifier.height(8.dp))
                        Text("📅 Tgl Setor: ${it.tgl_setoran}")
                        Text("👨‍🏫 Dosen: ${it.dosen_yang_mengesahkan.nama}")
                    }

                    Spacer(Modifier.height(12.dp))

                    if (!item.sudah_setor) {
                        Button(
                            onClick = { onValidasiClicked(item.id, item.nama, nim) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Setorkan")
                        }
                    } else {
                        Button(
                            onClick = {
                                onHapusClicked(item.id, item.nama, nim, item.info_setoran?.id)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Batalkan Setoran")
                        }
                    }
                }
            }
        }
    }
}


package com.example.doosen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ListAlt
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
import com.example.doosen.AuthViewModel
import com.example.doosen.model.DetailKomponenSetoran
import com.example.doosen.model.DetailMahasiswaData
import com.example.doosen.model.HapusKomponen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMahasiswaScreen(nim: String, vm: AuthViewModel = hiltViewModel()) {
    val detail = vm.detailMahasiswa.collectAsState().value
    val errorMessage = vm.error.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTabIndex by remember { mutableStateOf(0) }

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
    val emerald = Color(0xFF00695C)
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(emerald, Color(0xFFB2DFDB))
    )


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Detail Mahasiswa", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF00695C), // emerald
                    titleContentColor = Color.White
                )


            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(gradientBrush) // <- tambahkan ini
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

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val isSelected = selectedTabIndex == index
                        Button(
                            onClick = { selectedTabIndex = index },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(16.dp)
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

    val emeraldDark = Color(0xFF065F46)
    val emeraldPrimary = Color(0xFF10B981)
    val emeraldLight = Color(0xFF34D399)
    val backgroundGradient = Brush.verticalGradient(listOf(emeraldPrimary, emeraldLight))
    val textDark = Color(0xFF1B1B1B)
    val progressTrack = Color(0xFFE5E7EB)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF00695C), Color(0xFF4DB6AC))
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text("PROGRES SETORAN", color = Color.White.copy(alpha = 0.85f))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(info.nama, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        InfoItem(label = "NIM", value = info.nim)
                        InfoItem(label = "Email", value = info.email)
                        InfoItem(label = "Angkatan", value = info.angkatan)
                        InfoItem(label = "Semester", value = info.semester.toString())
                    }
                }
            }
        }


        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Progress Setoran", fontWeight = FontWeight.SemiBold, color = textDark)
                    Text("$sudah dari $total sudah disetor", color = textDark)
                }

                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(58.dp),
                    strokeWidth = 6.dp,
                    color = emeraldLight,
                    trackColor = progressTrack
                )
            }
        }

        Text("RINGKASAN PROGRES", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = emeraldDark)

        if (ringkasan.isEmpty()) {
            Text("Belum ada data progres", style = MaterialTheme.typography.bodyLarge, color = textDark)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ringkasan.forEach { item ->
                    val value = if (item.total_wajib_setor > 0)
                        item.total_sudah_setor.toFloat() / item.total_wajib_setor
                    else 0f
                    val percent = (value * 100).toInt()

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.label, fontWeight = FontWeight.Bold, color = textDark)
                                Text("~ $percent%", color = textDark)
                            }
                            Text(
                                "Wajib: ${item.total_wajib_setor}, Sudah: ${item.total_sudah_setor}, Belum: ${item.total_belum_setor}",
                                fontSize = 14.sp,
                                color = textDark
                            )

                            LinearProgressIndicator(
                                progress = { value },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .height(8.dp),
                                color = emeraldLight,
                                trackColor = progressTrack
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    "${item.total_sudah_setor}/${item.total_wajib_setor}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(detailList) { item ->
            val statusColor = if (item.sudah_setor) Color(0xFF388E3C) else Color(0xFFD32F2F)
            val statusText = if (item.sudah_setor) "✅ Sudah disetor" else "❌ Belum disetor"

            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${item.nama} (${item.label})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(statusText, color = statusColor)

                    item.info_setoran?.let {
                        Spacer(Modifier.height(8.dp))
                        Text("📅 Tgl Setor: ${it.tgl_setoran}", color = Color.Black)
                        Text("👨‍🏫 Dosen: ${it.dosen_yang_mengesahkan.nama}", color = Color.Black)
                    }

                    Spacer(Modifier.height(12.dp))

                    val buttonColor = if (!item.sudah_setor) Color(0xFF00695C) else Color(0xFFD32F2F)
                    val buttonText = if (!item.sudah_setor) "Setorkan" else "Batalkan Setoran"
                    val onClick = {
                        if (!item.sudah_setor) {
                            onValidasiClicked(item.id, item.nama, nim)
                        } else {
                            onHapusClicked(item.id, item.nama, nim, item.info_setoran?.id)
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(
                            onClick = onClick,
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(buttonText, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun InfoItem(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$label:", fontWeight = FontWeight.SemiBold, color = Color.White)
        Text(value, color = Color.White.copy(alpha = 0.9f))
    }
}
package com.example.doosen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.doosen.R
import com.example.doosen.AuthViewModel
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxHeight

// Import tambahan yang diperlukan untuk alpha
import androidx.compose.ui.draw.alpha

// Import tambahan yang diperlukan
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon


@Composable
fun BerandaScreen(
    nav: NavHostController,
    vm: AuthViewModel = hiltViewModel(),
    onMahasiswaClick: (String) -> Unit
) {
    val daftarMahasiswa by vm.daftarMahasiswa.collectAsState()
    val ringkasan by vm.ringkasan.collectAsState()

    val angkatanOptions = listOf("Semua") + ringkasan.map { it.tahun }
    var selectedAngkatan by remember { mutableStateOf("Semua") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = daftarMahasiswa.filter {
        (selectedAngkatan == "Semua" || it.angkatan == selectedAngkatan) &&
                it.nama.contains(searchQuery, ignoreCase = true)
    }

    val emerald = Color(0xFF00695C)
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(emerald, Color(0xFFB2DFDB))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            // 📌 FILTER SECTION
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // 🔍 Search
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Cari nama") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = emerald,
                        unfocusedBorderColor = Color.White,
                        cursorColor = emerald,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = emerald,
                        unfocusedLabelColor = Color.White
                    )
                )

                // 🎚️ Dropdown
                AngkatanDropdown(
                    items = angkatanOptions,
                    selected = selectedAngkatan,
                    onSelected = { selectedAngkatan = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Daftar Mahasiswa:",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList) { mhs ->
                    MahasiswaCard(
                        nama = mhs.nama,
                        nim = mhs.nim,
                        onClick = { onMahasiswaClick(mhs.nim) }
                    )
                }
            }
        }
    }
}

@Composable
fun MahasiswaCard(nama: String, nim: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = nama,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "NIM: $nim",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun AngkatanDropdown(
    items: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                Text(selected)
            }
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

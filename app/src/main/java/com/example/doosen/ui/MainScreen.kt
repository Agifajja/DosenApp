package com.example.doosen.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.doosen.AuthViewModel
import com.example.doosen.R

sealed class Screen(val route: String, val title: String = "") {
    object Login : Screen("login")
    object Beranda : Screen("beranda", "Dashboard")
    object Profil : Screen("profil", "Profil Dosen")
    object DetailMahasiswa : Screen("detail_mahasiswa/{nim}", "Detail Mahasiswa") {
        fun createRoute(nim: String) = "detail_mahasiswa/$nim"
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, vm: AuthViewModel) {
    val currentRoute = currentRoute(navController)
    Log.d("MainScreen", "Navigated to route: $currentRoute")

    when (currentRoute) {
        Screen.Beranda.route -> {
            BerandaScreen(
                nav = navController,
                vm = vm,
                onMahasiswaClick = { nim ->
                    navController.navigate(Screen.DetailMahasiswa.createRoute(nim))
                }
            )
        }
        Screen.Profil.route -> {
            ProfilScreen(nav = navController, vm = vm)
        }
    }
}


@Composable
fun BottomNavBar(navController: NavHostController) {
    val emerald = Color(0xFF00695C)
    val currentRoute = currentRoute(navController)

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 4.dp,
    ) {
        NavigationBarItem(
            selected = currentRoute == Screen.Beranda.route,
            onClick = {
                navController.navigate(Screen.Beranda.route) {
                    popUpTo(Screen.Beranda.route) { inclusive = true }
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Beranda",
                    tint = if (currentRoute == Screen.Beranda.route) emerald else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "Beranda",
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    color = if (currentRoute == Screen.Beranda.route) emerald else Color.Gray
                )
            }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Profil.route,
            onClick = {
                navController.navigate(Screen.Profil.route) {
                    popUpTo(Screen.Beranda.route)
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.profil),
                    contentDescription = "Profil",
                    tint = if (currentRoute == Screen.Profil.route) emerald else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "Profil",
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    color = if (currentRoute == Screen.Profil.route) emerald else Color.Gray
                )
            }
        )
    }
}


@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}